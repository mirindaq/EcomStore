/**
 * Utility để quản lý việc lưu trữ authentication data.
 * Access token = localStorage + Authorization header.
 * Refresh token = httpOnly cookie (chỉ dùng ở endpoint refresh, do server set/clear).
 */

import LocalStorageUtil from './localStorage.util';
import type { UserProfile } from '@/types/auth.type';

export interface TokenData {
  accessToken: string;
  refreshToken?: string; // không lưu phía client; chỉ qua httpOnly cookie
}

class AuthStorageUtil {
  /**
   * Lưu access token vào localStorage
   */
  static setAccessToken(accessToken: string): void {
    LocalStorageUtil.setAccessToken(accessToken);
  }

  /**
   * Lấy access token từ localStorage
   */
  static getAccessToken(): string | null {
    return LocalStorageUtil.getAccessToken();
  }

  /**
   * Lưu thông tin user vào localStorage
   */
  static setUserData(userData: UserProfile): void {
    LocalStorageUtil.setUserData(userData);
  }

  /**
   * Lấy thông tin user từ localStorage
   */
  static getUserData(): UserProfile | null {
    return LocalStorageUtil.getUserData();
  }

  /**
   * Lưu access token và user data (refresh token do server set qua httpOnly cookie)
   */
  static setTokensAndData(tokens: TokenData, userData: UserProfile): void {
    this.setAccessToken(tokens.accessToken);
    this.setUserData(userData);
  }

  /**
   * Lưu access token (refresh token chỉ ở httpOnly cookie, không lưu client)
   */
  static setTokens(tokens: TokenData): void {
    this.setAccessToken(tokens.accessToken);
  }

  /**
   * Lấy access token (refresh token không đọc được từ client vì httpOnly)
   */
  static getTokens(): TokenData | null {
    const accessToken = this.getAccessToken();
    if (!accessToken) return null;
    return { accessToken };
  }

  /**
   * Cập nhật access token (sau khi refresh)
   */
  static updateTokens(tokens: Pick<TokenData, 'accessToken'>): void {
    this.setAccessToken(tokens.accessToken);
  }

  /**
   * Xóa tất cả dữ liệu authentication (cookie refresh do server clear khi logout/refresh fail)
   */
  static clearAll(): void {
    LocalStorageUtil.clearAll();
  }

  /**
   * Kiểm tra có access token không (refresh nằm trong httpOnly cookie)
   */
  static hasTokens(): boolean {
    return this.getAccessToken() !== null;
  }

  /**
   * Kiểm tra có access token không
   */
  static hasAccessToken(): boolean {
    return LocalStorageUtil.hasAccessToken();
  }

  /**
   * Kiểm tra có user data không
   */
  static hasUserData(): boolean {
    return LocalStorageUtil.hasUserData();
  }

  /**
   * Kiểm tra user đã đăng nhập chưa (có access token và user data)
   */
  static isAuthenticated(): boolean {
    return this.hasAccessToken() && this.hasUserData();
  }

  /**
   * Lấy login path dựa trên role của user
   */
  static getLoginPathByRole(role?: string): string {
    const normalizedRole = role?.toUpperCase() || 'CUSTOMER';
    
    if (normalizedRole === 'ADMIN') {
      return '/admin/login';
    } else if (normalizedRole === 'STAFF') {
      return '/staff/login';
    } else if (normalizedRole === 'SHIPPER') {
      return '/shipper/login';
    }
    
    return '/login';
  }

  /**
   * Lấy login path từ user data hiện tại
   */
  static getLoginPath(): string {
    const userData = this.getUserData();
    const role = userData?.roles?.[0];
    return this.getLoginPathByRole(role);
  }
}

export default AuthStorageUtil;

