import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import type { CategoryWithBrandCount, CategoryBrandAddRequest } from "@/types/categoryBrand.type"
import CategoryBrandForm from "./CategoryBrandForm"

interface CategoryBrandDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  category?: CategoryWithBrandCount | null
  onSubmit: (data: CategoryBrandAddRequest) => void
  isLoading?: boolean
}

export default function CategoryBrandDialog({
  open,
  onOpenChange,
  category,
  onSubmit,
  isLoading
}: CategoryBrandDialogProps) {
  const handleSubmit = (data: CategoryBrandAddRequest) => {
    onSubmit(data)
  }

  const handleCancel = () => {
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[900px] max-h-[85vh] flex flex-col">
        <DialogHeader className="flex-shrink-0">
          <DialogTitle className="text-xl font-semibold text-gray-900">
            Gán thương hiệu cho danh mục
          </DialogTitle>
          <DialogDescription className="text-gray-600">
            {category ? `Chọn thương hiệu để gán cho danh mục "${category.categoryName}"` : "Chọn thương hiệu để gán cho danh mục"}
          </DialogDescription>
        </DialogHeader>
        
        <div className="flex-1 overflow-hidden">
          <CategoryBrandForm
            category={category}
            onSubmit={handleSubmit}
            onCancel={handleCancel}
            isLoading={isLoading}
          />
        </div>
      </DialogContent>
    </Dialog>
  )
}
