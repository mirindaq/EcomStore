import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import type { OrderAssignment, CreateOrderAssignmentRequest, UpdateOrderAssignmentRequest } from "@/types/order-assignment.type"
import OrderAssignmentForm from "./OrderAssignmentForm"

interface OrderAssignmentDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  assignment?: OrderAssignment | null
  onSubmit: (data: CreateOrderAssignmentRequest | UpdateOrderAssignmentRequest) => void
  isLoading?: boolean
}

export default function OrderAssignmentDialog({
  open,
  onOpenChange,
  assignment,
  onSubmit,
  isLoading
}: OrderAssignmentDialogProps) {
  const handleSubmit = (data: CreateOrderAssignmentRequest | UpdateOrderAssignmentRequest) => {
    onSubmit(data)
  }

  const handleCancel = () => {
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[700px] max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold text-gray-900">
            {assignment ? "Cập nhật phân công giao hàng" : "Phân công giao hàng mới"}
          </DialogTitle>
          <DialogDescription className="text-gray-600">
            {assignment ? "Cập nhật thông tin phân công giao hàng" : "Chọn đơn hàng và shipper để phân công giao hàng"}
          </DialogDescription>
        </DialogHeader>
        
        <OrderAssignmentForm
          assignment={assignment}
          onSubmit={handleSubmit}
          onCancel={handleCancel}
          isLoading={isLoading}
        />
      </DialogContent>
    </Dialog>
  )
}
