import axios from "axios";
import { toast } from "sonner";
import { authService } from "@/services/auth.service";
import AuthStorageUtil from "@/utils/authStorage.util";

const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
  // withCredentials
});

axiosClient.interceptors.request.use(
  (config) => {
    if ((config as any)._skipAuthInterceptor) return config;
    const token = AuthStorageUtil.getAccessToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

let isRefreshing = false;
let isClearingAuth = false;
let failedQueue: Array<{
  resolve: (value?: any) => void;
  reject: (error?: any) => void;
}> = [];

const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach(({ resolve, reject }) => {
    if (error) {
      reject(error);
    } else {
      resolve(token);
    }
  });

  failedQueue = [];
};

/** Xóa auth và redirect về trang login (chỉ chạy 1 lần, tránh loop) */
const clearAuthAndRedirect = () => {
  if (isClearingAuth) return;
  isClearingAuth = true;
  const loginPath = AuthStorageUtil.getLoginPath();
  AuthStorageUtil.clearAll();
  window.location.href = loginPath;
};

axiosClient.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    if (originalRequest._skipAuthInterceptor) {
      return Promise.reject(error);
    }

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (originalRequest._isRefreshRequest) {
        processQueue(error, null);
        clearAuthAndRedirect();
        return Promise.reject(error);
      }

      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        }).then(token => {
          originalRequest.headers.Authorization = `Bearer ${token}`;
          return axiosClient(originalRequest);
        }).catch(err => {
          return Promise.reject(err);
        });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const response = await authService.refreshToken();
        const { accessToken } = (response.data as any).data;
        if (!accessToken) throw new Error('No access token in refresh response');

        AuthStorageUtil.setAccessToken(accessToken);

        processQueue(null, accessToken);

        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return axiosClient(originalRequest);
      } catch (refreshError) {
        processQueue(refreshError, null);
        clearAuthAndRedirect();
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    } else if (error.response?.status === 401) {
      if (!isClearingAuth) clearAuthAndRedirect();
      return Promise.reject(error);
    }

    if (error.response) {
      if (error.response.status === 403) {
        toast.error("Không có quyền truy cập");
      } else if (error.response.status === 500) {
        toast.error("Lỗi server, vui lòng thử lại sau");
      }
      throw error.response.data;
    }
    throw error;
  }
);

export default axiosClient;
