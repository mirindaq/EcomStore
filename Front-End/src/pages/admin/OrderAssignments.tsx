import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Plus } from "lucide-react"
import { toast } from "sonner"
import { OrderAssignmentDialog, OrderAssignmentTable } from "@/components/admin/order-assignments"
import Pagination from "@/components/ui/pagination"
import { useQuery, useMutation } from "@/hooks"
import { orderAssignmentService } from "@/services/order-assignment.service"
import type { 
  OrderAssignment, 
  OrderAssignmentListResponse, 
  CreateOrderAssignmentRequest,
  UpdateOrderAssignmentRequest
} from "@/types/order-assignment.type"

export default function OrderAssignments() {
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [editingAssignment, setEditingAssignment] = useState<OrderAssignment | null>(null)
  const [currentPage, setCurrentPage] = useState(1)
  const [pageSize, _] = useState(7)
  const [searchTerm, setSearchTerm] = useState("")

  const {
    data: assignmentsData,
    isLoading: isLoadingAssignments,
    refetch: refetchAssignments
  } = useQuery<OrderAssignmentListResponse>(
    () => orderAssignmentService.getOrderAssignments(currentPage, pageSize, searchTerm),
    {
      queryKey: ['order-assignments', currentPage.toString(), pageSize.toString(), searchTerm],
    }
  )

  const pagination = assignmentsData?.data;
  const assignments = assignmentsData?.data?.data || [];

  // Create order assignment
  const createAssignmentMutation = useMutation(
    (data: CreateOrderAssignmentRequest) => orderAssignmentService.createOrderAssignment(data),
    {
      onSuccess: () => {
        toast.success('Phân công giao hàng thành công')
        refetchAssignments()
        setIsDialogOpen(false)
        setEditingAssignment(null)
      },
      onError: (error) => {
        console.error('Error creating order assignment:', error)
        toast.error('Không thể phân công giao hàng')
      }
    }
  )

  // Update order assignment
  const updateAssignmentMutation = useMutation(
    ({ id, data }: { id: number; data: UpdateOrderAssignmentRequest }) =>
      orderAssignmentService.updateOrderAssignment(id, data),
    {
      onSuccess: () => {
        toast.success('Cập nhật phân công thành công')
        refetchAssignments()
        setIsDialogOpen(false)
        setEditingAssignment(null)
      },
      onError: (error) => {
        console.error('Error updating order assignment:', error)
        toast.error('Không thể cập nhật phân công')
      }
    }
  )

  // Delete order assignment
  const deleteAssignmentMutation = useMutation(
    (id: number) => orderAssignmentService.deleteOrderAssignment(id),
    {
      onSuccess: () => {
        toast.success('Xóa phân công thành công')
        refetchAssignments()
      },
      onError: (error) => {
        console.error('Error deleting order assignment:', error)
        toast.error('Không thể xóa phân công')
      }
    }
  )

  const handleOpenAddDialog = () => {
    setEditingAssignment(null)
    setIsDialogOpen(true)
  }

  const handleOpenEditDialog = (assignment: OrderAssignment) => {
    setEditingAssignment(assignment)
    setIsDialogOpen(true)
  }

  const handleCloseDialog = () => {
    setIsDialogOpen(false)
    setEditingAssignment(null)
  }

  const handleFormSubmit = (data: CreateOrderAssignmentRequest | UpdateOrderAssignmentRequest) => {
    if (editingAssignment) {
      updateAssignmentMutation.mutate({ id: editingAssignment.id, data: data as UpdateOrderAssignmentRequest })
    } else {
      createAssignmentMutation.mutate(data as CreateOrderAssignmentRequest)
    }
  }

  const handleDelete = async (id: number) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa phân công này?')) {
      deleteAssignmentMutation.mutate(id)
    }
  }

  const handleView = (assignment: OrderAssignment) => {
    // TODO: Implement view details modal
    toast.info(`Xem chi tiết phân công: ${assignment.order.orderCode}`)
  }

  const handlePageChange = (page: number) => {
    setCurrentPage(page)
  }

  const handleSearch = (searchTerm: string) => {
    setSearchTerm(searchTerm)
  }

  return (
    <div className="space-y-3 p-2">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div className="space-y-1">
          <h1 className="text-2xl font-bold tracking-tight text-gray-900">Quản lý phân công giao hàng</h1>
          <p className="text-lg text-gray-600">
            Phân công và theo dõi việc giao hàng cho shipper
          </p>
        </div>
        <Button
          onClick={handleOpenAddDialog}
          className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 shadow-lg hover:shadow-xl transition-all duration-300"
        >
          <Plus className="mr-2 h-4 w-4" />
          Phân công giao hàng
        </Button>
      </div>

      <OrderAssignmentTable
        orderAssignments={assignments}
        onEdit={handleOpenEditDialog}
        onDelete={handleDelete}
        onView={handleView}
        isLoading={isLoadingAssignments}
        onSearch={handleSearch}
        currentPage={currentPage}
        pageSize={pageSize}
      />

      {/* Pagination */}
      {pagination && pagination.totalPage > 1 && (
        <div className="flex justify-center">
          <Pagination
            currentPage={currentPage}
            totalPages={pagination.totalPage}
            onPageChange={handlePageChange}
          />
        </div>
      )}

      {/* Dialog */}
      <OrderAssignmentDialog
        open={isDialogOpen}
        onOpenChange={setIsDialogOpen}
        assignment={editingAssignment}
        onSubmit={handleFormSubmit}
        isLoading={createAssignmentMutation.isLoading || updateAssignmentMutation.isLoading}
      />
    </div>
  )
}
