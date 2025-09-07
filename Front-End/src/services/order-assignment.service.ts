import axiosClient from '@/configurations/axios.config';
import type { 
  CreateOrderAssignmentRequest,
  UpdateOrderAssignmentRequest,
  OrderAssignmentResponse, 
  OrderAssignmentListResponse,
  ShipperListResponse,
  OrderListResponse
} from '@/types/order-assignment.type';

export const orderAssignmentService = {
  getOrderAssignments: async (page: number = 1, size: number = 10, search: string = "") => {
    const response = await axiosClient.get<OrderAssignmentListResponse>(
      `/order-assignments?page=${page}&size=${size}&search=${search}`
    );
    return response.data;
  },

  getOrderAssignmentById: async (id: number) => {
    const response = await axiosClient.get<OrderAssignmentResponse>(`/order-assignments/${id}`);
    return response.data;
  },

  createOrderAssignment: async (request: CreateOrderAssignmentRequest) => {
    const response = await axiosClient.post<OrderAssignmentResponse>('/order-assignments', request);
    return response.data;
  },

  updateOrderAssignment: async (id: number, request: UpdateOrderAssignmentRequest) => {
    const response = await axiosClient.put<OrderAssignmentResponse>(`/order-assignments/${id}`, request);
    return response.data;
  },

  deleteOrderAssignment: async (id: number) => {
    await axiosClient.delete(`/order-assignments/${id}`);
  },

  // Lấy danh sách shipper
  getShippers: async (page: number = 1, size: number = 100) => {
    const response = await axiosClient.get<ShipperListResponse>(
      `/shippers?page=${page}&size=${size}&status=true`
    );
    return response.data;
  },

  // Lấy danh sách đơn hàng chưa được phân công
  getUnassignedOrders: async (page: number = 1, size: number = 100) => {
    const response = await axiosClient.get<OrderListResponse>(
      `/orders/unassigned?page=${page}&size=${size}`
    );
    return response.data;
  }
};
