import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Link } from "lucide-react"
import { toast } from "sonner"
import CategoryBrandDialog from "@/components/admin/category-brands/CategoryBrandDialog"
import CategoryBrandTable from "@/components/admin/category-brands/CategoryBrandTable"
import Pagination from "@/components/ui/pagination"
import { useQuery, useMutation } from "@/hooks"
import { categoryBrandService } from "@/services/categoryBrand.service"
import type { CategoryWithBrandCount, CategoryWithBrandCountResponse, CategoryBrandAddRequest } from "@/types/categoryBrand.type"

export default function CategoryBrands() {
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [selectedCategory, setSelectedCategory] = useState<CategoryWithBrandCount | null>(null)
  const [currentPage, setCurrentPage] = useState(1)
  const [pageSize, _] = useState(7)
  const [searchTerm, setSearchTerm] = useState("")

  const {
    data: categoriesData,
    isLoading: isLoadingCategories,
    refetch: refetchCategories
  } = useQuery<CategoryWithBrandCountResponse>(
    () => categoryBrandService.getCategoriesWithBrandCount(currentPage, pageSize),
    {
      queryKey: ['categories-with-brand-count', currentPage.toString(), pageSize.toString()],
    }
  )

  const pagination = categoriesData?.data;
  const categories = categoriesData?.data?.data || [];

  // Assign brands to category
  const assignBrandsMutation = useMutation(
    (data: CategoryBrandAddRequest) => categoryBrandService.assignBrandsToCategory(data),
    {
      onSuccess: () => {
        toast.success('Gán thương hiệu cho danh mục thành công')
        refetchCategories()
        setIsDialogOpen(false)
        setSelectedCategory(null)
      },
      onError: (error) => {
        console.error('Error assigning brands to category:', error)
        toast.error('Không thể gán thương hiệu cho danh mục')
      }
    }
  )

  const handleOpenAssignDialog = (category: CategoryWithBrandCount) => {
    setSelectedCategory(category)
    setIsDialogOpen(true)
  }


  const handleFormSubmit = (data: CategoryBrandAddRequest) => {
    assignBrandsMutation.mutate(data)
  }

  const handlePageChange = (page: number) => {
    setCurrentPage(page)
  }

  const handleSearch = (searchTerm: string) => {
    setSearchTerm(searchTerm)
    // Note: API doesn't support search yet, so we'll filter client-side
    // In a real implementation, you'd want to add search to the API
  }

  // Filter categories based on search term
  const filteredCategories = searchTerm 
    ? categories.filter(category => 
        category.categoryName.toLowerCase().includes(searchTerm.toLowerCase())
      )
    : categories

  return (
    <div className="space-y-3 p-2">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div className="space-y-1">
          <h1 className="text-2xl font-bold tracking-tight text-gray-900">Quản lý gán thương hiệu</h1>
          <p className="text-lg text-gray-600">
            Gán thương hiệu cho các danh mục sản phẩm
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button
            variant="outline"
            className="border-blue-200 text-blue-600 hover:bg-blue-50"
          >
            <Link className="mr-2 h-4 w-4" />
            Xem báo cáo
          </Button>
        </div>
      </div>

      <CategoryBrandTable
        categories={filteredCategories}
        onAssign={handleOpenAssignDialog}
        isLoading={isLoadingCategories}
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
      <CategoryBrandDialog
        open={isDialogOpen}
        onOpenChange={setIsDialogOpen}
        category={selectedCategory}
        onSubmit={handleFormSubmit}
        isLoading={assignBrandsMutation.isLoading}
      />
    </div>
  )
}
