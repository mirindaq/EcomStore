import type { ResponseApi, ResponseApiWithPagination } from "./responseApi.type";

export type Shipper = {
  id: number;
  name: string;
  phone: string;
  email: string;
  status: boolean;
  createdAt: string;
  modifiedAt: string;
};

export type Order = {
  id: number;
  orderCode: string;
  customerName: string;
  customerPhone: string;
  customerAddress: string;
  totalAmount: number;
  status: string;
  createdAt: string;
  modifiedAt: string;
};

export type OrderAssignment = {
  id: number;
  order: Order;
  shipper: Shipper;
  assignedAt: string;
  status: 'PENDING' | 'ACCEPTED' | 'IN_PROGRESS' | 'DELIVERED' | 'CANCELLED';
  notes?: string;
  createdAt: string;
  modifiedAt: string;
};

export type CreateOrderAssignmentRequest = {
  orderId: number;
  shipperId: number;
  notes?: string;
};

export type UpdateOrderAssignmentRequest = {
  status?: 'PENDING' | 'ACCEPTED' | 'IN_PROGRESS' | 'DELIVERED' | 'CANCELLED';
  notes?: string;
};

export type OrderAssignmentResponse = ResponseApi<OrderAssignment>;

export type OrderAssignmentListResponse = ResponseApiWithPagination<OrderAssignment[]>;

export type ShipperListResponse = ResponseApiWithPagination<Shipper[]>;

export type OrderListResponse = ResponseApiWithPagination<Order[]>;
