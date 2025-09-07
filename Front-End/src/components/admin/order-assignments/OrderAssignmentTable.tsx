import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { Search, Edit, Trash2, Eye, Loader2, Truck } from "lucide-react"
import type { OrderAssignment } from "@/types/order-assignment.type"

interface OrderAssignmentTableProps {
  orderAssignments: OrderAssignment[]
  onEdit: (assignment: OrderAssignment) => void
  onDelete: (id: number) => void
  onView: (assignment: OrderAssignment) => void
  isLoading?: boolean
  onSearch: (searchTerm: string) => void
  currentPage?: number
  pageSize?: number
}

export default function OrderAssignmentTable({
  orderAssignments,
  onEdit,
  onDelete,
  onView,
  onSearch,
  isLoading,
  currentPage = 1,
  pageSize = 7
}: OrderAssignmentTableProps) {
  const [searchTerm, setSearchTerm] = useState("")

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('vi-VN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(amount)
  }

  const getStatusBadge = (status: string) => {
    const statusConfig = {
      'PENDING': { label: 'Chờ xử lý', className: 'bg-yellow-100 text-yellow-800 border-yellow-200' },
      'ACCEPTED': { label: 'Đã nhận', className: 'bg-blue-100 text-blue-800 border-blue-200' },
      'IN_PROGRESS': { label: 'Đang giao', className: 'bg-orange-100 text-orange-800 border-orange-200' },
      'DELIVERED': { label: 'Đã giao', className: 'bg-green-100 text-green-800 border-green-200' },
      'CANCELLED': { label: 'Đã hủy', className: 'bg-red-100 text-red-800 border-red-200' }
    }
    
    const config = statusConfig[status as keyof typeof statusConfig] || statusConfig['PENDING']
    
    return (
      <Badge variant="outline" className={config.className}>
        {config.label}
      </Badge>
    )
  }

  const handleSearch = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.preventDefault()
      onSearch(searchTerm)
    }
  }

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between py-4">
        <div className="flex items-center gap-3">
          <div className="relative flex-1 max-w-sm">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
            <Input
              placeholder="Tìm kiếm theo mã đơn hàng, tên khách hàng..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10 border-gray-200 focus:border-blue-500 focus:ring-blue-500"
              disabled={isLoading}
              onKeyDown={handleSearch}
            />
          </div>
          <Button
            onClick={() => onSearch(searchTerm)}
            disabled={isLoading}
            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2"
          >
            <Search className="h-4 w-4 mr-2" />
            Tìm kiếm
          </Button>
        </div>

        <div className="text-sm text-gray-600">
          Tổng cộng: <span className="font-semibold text-gray-900">{orderAssignments.length}</span> phân công
        </div>
      </div>

      <div className="rounded-lg border border-gray-200 overflow-hidden">
        <Table>
          <TableHeader>
            <TableRow className="bg-gray-50 hover:bg-gray-50">
              <TableHead className="font-semibold text-gray-700">STT</TableHead>
              <TableHead className="font-semibold text-gray-700">Mã đơn hàng</TableHead>
              <TableHead className="font-semibold text-gray-700">Khách hàng</TableHead>
              <TableHead className="font-semibold text-gray-700">Shipper</TableHead>
              <TableHead className="font-semibold text-gray-700">Tổng tiền</TableHead>
              <TableHead className="font-semibold text-gray-700">Trạng thái</TableHead>
              <TableHead className="font-semibold text-gray-700">Ngày phân công</TableHead>
              <TableHead className="font-semibold text-gray-700">Thao tác</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow>
                <TableCell colSpan={8} className="text-center py-12">
                  <div className="flex flex-col items-center space-y-3">
                    <Loader2 className="h-8 w-8 animate-spin text-blue-500" />
                    <p className="text-gray-500 font-medium">Đang tải dữ liệu...</p>
                  </div>
                </TableCell>
              </TableRow>
            ) : orderAssignments.length === 0 ? (
              <TableRow>
                <TableCell colSpan={8} className="text-center py-24 text-gray-500">
                  <div className="flex flex-col items-center space-y-4">
                    <div className="w-16 h-16 rounded-full bg-gray-100 flex items-center justify-center">
                      <Truck className="h-8 w-8 text-gray-400" />
                    </div>
                    <div className="space-y-2">
                      <p className="text-lg font-medium text-gray-600">
                        {searchTerm ? "Không tìm thấy phân công nào" : "Chưa có phân công nào"}
                      </p>
                      <p className="text-sm text-gray-400">
                        {searchTerm ? "Thử tìm kiếm với từ khóa khác" : "Hãy tạo phân công đầu tiên"}
                      </p>
                    </div>
                  </div>
                </TableCell>
              </TableRow>
            ) : (
              orderAssignments.map((assignment, index) => (
                <TableRow key={assignment.id} className="hover:bg-gray-50 transition-colors duration-200">
                  <TableCell className="text-center font-medium text-gray-600">
                    {(currentPage - 1) * pageSize + index + 1}
                  </TableCell>
                  <TableCell className="font-semibold text-gray-900">
                    {assignment.order.orderCode}
                  </TableCell>
                  <TableCell>
                    <div className="space-y-1">
                      <p className="font-medium text-gray-900">{assignment.order.customerName}</p>
                      <p className="text-sm text-gray-500">{assignment.order.customerPhone}</p>
                    </div>
                  </TableCell>
                  <TableCell>
                    <div className="space-y-1">
                      <p className="font-medium text-gray-900">{assignment.shipper.name}</p>
                      <p className="text-sm text-gray-500">{assignment.shipper.phone}</p>
                    </div>
                  </TableCell>
                  <TableCell className="font-semibold text-green-600">
                    {formatCurrency(assignment.order.totalAmount)}
                  </TableCell>
                  <TableCell>
                    {getStatusBadge(assignment.status)}
                  </TableCell>
                  <TableCell className="text-gray-600">
                    {formatDate(assignment.assignedAt)}
                  </TableCell>
                  <TableCell>
                    <div className="flex space-x-2">
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => onView(assignment)}
                        className="border-blue-200 text-blue-600 hover:bg-blue-50 hover:border-blue-300"
                        disabled={isLoading}
                      >
                        <Eye className="h-4 w-4" />
                      </Button>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => onEdit(assignment)}
                        className="border-green-200 text-green-600 hover:bg-green-50 hover:border-green-300"
                        disabled={isLoading}
                      >
                        <Edit className="h-4 w-4" />
                      </Button>
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => onDelete(assignment.id)}
                        className="border-red-200 text-red-600 hover:bg-red-50 hover:border-red-300"
                        disabled={isLoading}
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>
    </div>
  )
}
