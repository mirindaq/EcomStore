import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Badge } from "@/components/ui/badge"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Loader2, Package, User, MapPin, Phone, Mail } from "lucide-react"
import { useQuery } from "@/hooks"
import { orderAssignmentService } from "@/services/order-assignment.service"
import type { 
  OrderAssignment, 
  CreateOrderAssignmentRequest, 
  UpdateOrderAssignmentRequest,
  Order,
  Shipper
} from "@/types/order-assignment.type"

interface OrderAssignmentFormProps {
  assignment?: OrderAssignment | null
  onSubmit: (data: CreateOrderAssignmentRequest | UpdateOrderAssignmentRequest) => void
  onCancel: () => void
  isLoading?: boolean
}

export default function OrderAssignmentForm({ 
  assignment, 
  onSubmit, 
  onCancel, 
  isLoading 
}: OrderAssignmentFormProps) {
  const [formData, setFormData] = useState<CreateOrderAssignmentRequest>({
    orderId: 0,
    shipperId: 0,
    notes: ""
  })
  const [updateData, setUpdateData] = useState<UpdateOrderAssignmentRequest>({
    status: 'PENDING',
    notes: ""
  })

  // Lấy danh sách đơn hàng chưa được phân công
  const { data: unassignedOrdersData, isLoading: isLoadingOrders } = useQuery(
    () => orderAssignmentService.getUnassignedOrders(1, 100),
    { queryKey: ['unassigned-orders'] }
  )

  // Lấy danh sách shipper
  const { data: shippersData, isLoading: isLoadingShippers } = useQuery(
    () => orderAssignmentService.getShippers(1, 100),
    { queryKey: ['shippers'] }
  )

  const unassignedOrders = unassignedOrdersData?.data?.data || []
  const shippers = shippersData?.data?.data || []

  useEffect(() => {
    if (assignment) {
      setUpdateData({
        status: assignment.status,
        notes: assignment.notes || ""
      })
    } else {
      setFormData({
        orderId: 0,
        shipperId: 0,
        notes: ""
      })
    }
  }, [assignment])

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (assignment) {
      onSubmit(updateData)
    } else {
      if (formData.orderId && formData.shipperId) {
        onSubmit(formData)
      }
    }
  }

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(amount)
  }

  const getStatusOptions = () => [
    { value: 'PENDING', label: 'Chờ xử lý' },
    { value: 'ACCEPTED', label: 'Đã nhận' },
    { value: 'IN_PROGRESS', label: 'Đang giao' },
    { value: 'DELIVERED', label: 'Đã giao' },
    { value: 'CANCELLED', label: 'Đã hủy' }
  ]

  const selectedOrder = unassignedOrders.find(order => order.id === formData.orderId)
  const selectedShipper = shippers.find(shipper => shipper.id === formData.shipperId)

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {assignment ? (
        // Form cập nhật
        <>
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="status" className="text-right font-medium text-gray-700">
              Trạng thái <span className="text-red-500">*</span>
            </Label>
            <Select
              value={updateData.status}
              onValueChange={(value: any) => setUpdateData(prev => ({ ...prev, status: value }))}
              disabled={isLoading}
            >
              <SelectTrigger className="col-span-3">
                <SelectValue placeholder="Chọn trạng thái" />
              </SelectTrigger>
              <SelectContent>
                {getStatusOptions().map(option => (
                  <SelectItem key={option.value} value={option.value}>
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="grid grid-cols-4 items-start gap-4">
            <Label htmlFor="notes" className="text-right font-medium text-gray-700 pt-2">
              Ghi chú
            </Label>
            <Textarea
              id="notes"
              value={updateData.notes}
              onChange={(e) => setUpdateData(prev => ({ ...prev, notes: e.target.value }))}
              className="col-span-3 border-gray-200 focus:border-blue-500 focus:ring-blue-500"
              rows={3}
              placeholder="Nhập ghi chú về việc giao hàng..."
              disabled={isLoading}
            />
          </div>
        </>
      ) : (
        // Form tạo mới
        <>
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="orderId" className="text-right font-medium text-gray-700">
              Đơn hàng <span className="text-red-500">*</span>
            </Label>
            <Select
              value={formData.orderId.toString()}
              onValueChange={(value) => setFormData(prev => ({ ...prev, orderId: parseInt(value) }))}
              disabled={isLoading || isLoadingOrders}
            >
              <SelectTrigger className="col-span-3">
                <SelectValue placeholder="Chọn đơn hàng" />
              </SelectTrigger>
              <SelectContent>
                {isLoadingOrders ? (
                  <SelectItem value="loading" disabled>
                    <div className="flex items-center space-x-2">
                      <Loader2 className="h-4 w-4 animate-spin" />
                      <span>Đang tải...</span>
                    </div>
                  </SelectItem>
                ) : unassignedOrders.length === 0 ? (
                  <SelectItem value="empty" disabled>
                    Không có đơn hàng nào
                  </SelectItem>
                ) : (
                  unassignedOrders.map((order) => (
                    <SelectItem key={order.id} value={order.id.toString()}>
                      {order.orderCode} - {order.customerName} - {formatCurrency(order.totalAmount)}
                    </SelectItem>
                  ))
                )}
              </SelectContent>
            </Select>
          </div>

          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="shipperId" className="text-right font-medium text-gray-700">
              Shipper <span className="text-red-500">*</span>
            </Label>
            <Select
              value={formData.shipperId.toString()}
              onValueChange={(value) => setFormData(prev => ({ ...prev, shipperId: parseInt(value) }))}
              disabled={isLoading || isLoadingShippers}
            >
              <SelectTrigger className="col-span-3">
                <SelectValue placeholder="Chọn shipper" />
              </SelectTrigger>
              <SelectContent>
                {isLoadingShippers ? (
                  <SelectItem value="loading" disabled>
                    <div className="flex items-center space-x-2">
                      <Loader2 className="h-4 w-4 animate-spin" />
                      <span>Đang tải...</span>
                    </div>
                  </SelectItem>
                ) : shippers.length === 0 ? (
                  <SelectItem value="empty" disabled>
                    Không có shipper nào
                  </SelectItem>
                ) : (
                  shippers.map((shipper) => (
                    <SelectItem key={shipper.id} value={shipper.id.toString()}>
                      {shipper.name} - {shipper.phone}
                    </SelectItem>
                  ))
                )}
              </SelectContent>
            </Select>
          </div>

          <div className="grid grid-cols-4 items-start gap-4">
            <Label htmlFor="notes" className="text-right font-medium text-gray-700 pt-2">
              Ghi chú
            </Label>
            <Textarea
              id="notes"
              value={formData.notes}
              onChange={(e) => setFormData(prev => ({ ...prev, notes: e.target.value }))}
              className="col-span-3 border-gray-200 focus:border-blue-500 focus:ring-blue-500"
              rows={3}
              placeholder="Nhập ghi chú về việc phân công giao hàng..."
              disabled={isLoading}
            />
          </div>

          {/* Hiển thị thông tin chi tiết */}
          {selectedOrder && (
            <Card className="border-blue-200 bg-blue-50">
              <CardHeader className="pb-3">
                <CardTitle className="text-lg flex items-center space-x-2">
                  <Package className="h-5 w-5 text-blue-600" />
                  <span>Thông tin đơn hàng</span>
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <Label className="text-sm font-medium text-gray-600">Mã đơn hàng</Label>
                    <p className="font-semibold text-gray-900">{selectedOrder.orderCode}</p>
                  </div>
                  <div>
                    <Label className="text-sm font-medium text-gray-600">Tổng tiền</Label>
                    <p className="font-semibold text-green-600">{formatCurrency(selectedOrder.totalAmount)}</p>
                  </div>
                </div>
                <div>
                  <Label className="text-sm font-medium text-gray-600">Khách hàng</Label>
                  <div className="flex items-center space-x-2 mt-1">
                    <User className="h-4 w-4 text-gray-400" />
                    <span className="font-medium">{selectedOrder.customerName}</span>
                  </div>
                </div>
                <div>
                  <Label className="text-sm font-medium text-gray-600">Số điện thoại</Label>
                  <div className="flex items-center space-x-2 mt-1">
                    <Phone className="h-4 w-4 text-gray-400" />
                    <span>{selectedOrder.customerPhone}</span>
                  </div>
                </div>
                <div>
                  <Label className="text-sm font-medium text-gray-600">Địa chỉ giao hàng</Label>
                  <div className="flex items-start space-x-2 mt-1">
                    <MapPin className="h-4 w-4 text-gray-400 mt-0.5" />
                    <span className="text-sm">{selectedOrder.customerAddress}</span>
                  </div>
                </div>
              </CardContent>
            </Card>
          )}

          {selectedShipper && (
            <Card className="border-green-200 bg-green-50">
              <CardHeader className="pb-3">
                <CardTitle className="text-lg flex items-center space-x-2">
                  <User className="h-5 w-5 text-green-600" />
                  <span>Thông tin shipper</span>
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <Label className="text-sm font-medium text-gray-600">Tên shipper</Label>
                  <div className="flex items-center space-x-2 mt-1">
                    <User className="h-4 w-4 text-gray-400" />
                    <span className="font-medium">{selectedShipper.name}</span>
                  </div>
                </div>
                <div>
                  <Label className="text-sm font-medium text-gray-600">Số điện thoại</Label>
                  <div className="flex items-center space-x-2 mt-1">
                    <Phone className="h-4 w-4 text-gray-400" />
                    <span>{selectedShipper.phone}</span>
                  </div>
                </div>
                <div>
                  <Label className="text-sm font-medium text-gray-600">Email</Label>
                  <div className="flex items-center space-x-2 mt-1">
                    <Mail className="h-4 w-4 text-gray-400" />
                    <span>{selectedShipper.email}</span>
                  </div>
                </div>
                <div>
                  <Label className="text-sm font-medium text-gray-600">Trạng thái</Label>
                  <div className="mt-1">
                    <Badge variant={selectedShipper.status ? "default" : "secondary"} className={
                      selectedShipper.status
                        ? "bg-green-100 text-green-800 border-green-200"
                        : "bg-gray-100 text-gray-800 border-gray-200"
                    }>
                      {selectedShipper.status ? "Hoạt động" : "Không hoạt động"}
                    </Badge>
                  </div>
                </div>
              </CardContent>
            </Card>
          )}
        </>
      )}

      <div className="flex justify-end space-x-3 pt-4">
        <Button
          type="button"
          variant="outline"
          onClick={onCancel}
          disabled={isLoading}
        >
          Hủy
        </Button>
        <Button
          type="submit"
          disabled={isLoading || (!assignment && (!formData.orderId || !formData.shipperId))}
          className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700"
        >
          {isLoading ? "Đang xử lý..." : (assignment ? "Cập nhật" : "Phân công")}
        </Button>
      </div>
    </form>
  )
}
