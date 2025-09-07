import { useState, useEffect, useCallback } from 'react'
import { Button } from '@/components/ui/button'
import { Checkbox } from '@/components/ui/checkbox'
import { Input } from '@/components/ui/input'
import { ScrollArea } from '@/components/ui/scroll-area'
import { Separator } from '@/components/ui/separator'
import { Loader2, Search } from 'lucide-react'
import { brandService } from '@/services/brand.service'
import { categoryBrandService } from '@/services/categoryBrand.service'
import type { Brand } from '@/types/brand.type'
import type { CategoryWithBrandCount, CategoryBrandAddRequest } from '@/types/categoryBrand.type'

interface CategoryBrandFormProps {
  category?: CategoryWithBrandCount | null
  onSubmit: (data: CategoryBrandAddRequest) => void
  onCancel: () => void
  isLoading?: boolean
}

export default function CategoryBrandForm({
  category,
  onSubmit,
  onCancel,
  isLoading = false
}: CategoryBrandFormProps) {
  const [brands, setBrands] = useState<Brand[]>([])
  const [selectedBrandIds, setSelectedBrandIds] = useState<number[]>([])
  const [searchTerm, setSearchTerm] = useState('')
  const [loading, setLoading] = useState(false)
  const [loadingBrands, setLoadingBrands] = useState(false)

  // Lấy danh sách tất cả brands
  const fetchBrands = useCallback(async () => {
    setLoadingBrands(true)
    try {
      const response = await brandService.getBrands(1, 999, searchTerm)
      setBrands(response.data?.data || [])
    } catch (error) {
      console.error('Lỗi khi lấy danh sách brands:', error)
    } finally {
      setLoadingBrands(false)
    }
  }, [searchTerm])

  // Lấy danh sách brand IDs đã được gán cho category
  const fetchAssignedBrands = useCallback(async () => {
    if (!category?.categoryId) return
    
    setLoading(true)
    try {
      const response = await categoryBrandService.getBrandIdsByCategoryId(category.categoryId)
      setSelectedBrandIds(response.data || [])
    } catch (error) {
      console.error('Lỗi khi lấy danh sách brands đã gán:', error)
    } finally {
      setLoading(false)
    }
  }, [category?.categoryId])

  // Load dữ liệu ban đầu
  useEffect(() => {
    fetchBrands()
  }, [])

  // Load danh sách brands đã được gán cho category
  useEffect(() => {
    if (category?.categoryId) {
      fetchAssignedBrands()
    }
  }, [fetchAssignedBrands])

  // Xử lý chọn/bỏ chọn brand
  const handleBrandToggle = useCallback((brandId: number) => {
    setSelectedBrandIds(prev => {
      if (prev.includes(brandId)) {
        return prev.filter(id => id !== brandId)
      } else {
        return [...prev, brandId]
      }
    })
  }, [])

  // Xử lý chọn tất cả
  const handleSelectAll = () => {
    const allBrandIds = brands.map(brand => brand.id)
    setSelectedBrandIds(allBrandIds)
  }

  // Xử lý bỏ chọn tất cả
  const handleDeselectAll = () => {
    setSelectedBrandIds([])
  }

  // Xử lý tìm kiếm
  const handleSearch = () => {
    fetchBrands()
  }

  // Xử lý submit form
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!category?.categoryId) return

    const request: CategoryBrandAddRequest = {
      brands: selectedBrandIds,
      categoryId: category.categoryId
    }

    onSubmit(request)
  }

  // Sử dụng trực tiếp danh sách brands từ API (đã được lọc)
  const filteredBrands = brands

  return (
    <form onSubmit={handleSubmit} className="flex flex-col h-full">
      {/* Header với thông tin category */}
      <div className="flex-shrink-0 mb-4">
        <div className="bg-blue-50 p-4 rounded-lg border">
          <h3 className="font-medium text-blue-900">
            Danh mục: {category?.categoryName || 'Chưa chọn danh mục'}
          </h3>
          <p className="text-sm text-blue-700 mt-1">
            Hiện tại có {selectedBrandIds.length} thương hiệu được gán
          </p>
        </div>
      </div>

      {/* Thanh tìm kiếm */}
      <div className="flex-shrink-0 mb-4">
        <div className="flex gap-2">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 h-4 w-4" />
            <Input
              placeholder="Tìm kiếm thương hiệu..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10"
              onKeyDown={(e) => {
                if (e.key === 'Enter') {
                  e.preventDefault()
                  handleSearch()
                }
              }}
            />
          </div>
          <Button
            type="button"
            variant="outline"
            onClick={handleSearch}
            disabled={loadingBrands}
            className="px-4"
          >
            {loadingBrands ? (
              <Loader2 className="h-4 w-4 animate-spin" />
            ) : (
              <Search className="h-4 w-4" />
            )}
          </Button>
        </div>
      </div>

      {/* Nút chọn tất cả/bỏ chọn tất cả */}
      <div className="flex-shrink-0 mb-3 flex gap-2">
        <Button
          type="button"
          variant="outline"
          size="sm"
          onClick={handleSelectAll}
          disabled={loadingBrands}
        >
          Chọn tất cả
        </Button>
        <Button
          type="button"
          variant="outline"
          size="sm"
          onClick={handleDeselectAll}
          disabled={loadingBrands}
        >
          Bỏ chọn tất cả
        </Button>
      </div>

      {/* Danh sách brands */}
      <div className="flex-1 overflow-hidden">
        <ScrollArea className="h-full">
          {loadingBrands ? (
            <div className="flex items-center justify-center py-8">
              <Loader2 className="h-6 w-6 animate-spin" />
              <span className="ml-2">Đang tải danh sách thương hiệu...</span>
            </div>
          ) : filteredBrands.length === 0 ? (
            <div className="text-center py-8 text-gray-500">
              {searchTerm ? 'Không tìm thấy thương hiệu nào' : 'Không có thương hiệu nào'}
            </div>
          ) : (
            <div className="grid grid-cols-5 gap-2 ">
              {filteredBrands.map((brand) => (
                <div
                  key={brand.id}
                  className={`relative group rounded-xl p-4 transition-all duration-300 ease-out border-2 border-gray-200 ${
                    selectedBrandIds.includes(brand.id)
                      ? 'bg-gradient-to-br from-green-50 to-green-100 shadow-lg shadow-green-200/50'
                      : 'bg-white hover:bg-gray-50 hover:shadow-md hover:shadow-gray-200/50'
                  }`}
                >
                  {/* Checkbox */}
                  <div className="absolute top-3 right-3 z-20">
                    <Checkbox
                      id={`brand-${brand.id}`}
                      checked={selectedBrandIds.includes(brand.id)}
                      onCheckedChange={() => handleBrandToggle(brand.id)}
                      disabled={loading}
                      className="data-[state=checked]:bg-green-500 data-[state=checked]:border-green-500 data-[state=checked]:shadow-md data-[state=checked]:shadow-green-200/50 border-gray-300 hover:border-green-400 transition-all duration-200"
                    />
                  </div>

                  {/* Clickable area - tách biệt với checkbox */}
                  <div 
                    className="cursor-pointer relative z-10"
                    onClick={(e) => {
                      e.stopPropagation()
                      handleBrandToggle(brand.id)
                    }}
                  >
                    {/* Brand Logo */}
                    <div className="flex justify-center mb-3">
                      <div className="w-16 h-16 rounded-xl overflow-hidden bg-white flex items-center justify-center shadow-sm group-hover:shadow-md transition-all duration-200">
                        {brand.image ? (
                          <img
                            src={brand.image}
                            alt={brand.name}
                            className="w-full h-full object-cover"
                            onError={(e) => {
                              const target = e.target as HTMLImageElement;
                              target.style.display = 'none';
                              target.nextElementSibling?.classList.remove('hidden');
                            }}
                          />
                        ) : null}
                        <div className={`w-full h-full flex items-center justify-center text-gray-400 ${brand.image ? 'hidden' : ''}`}>
                          <svg
                            className="w-8 h-8"
                            fill="none"
                            stroke="currentColor"
                            viewBox="0 0 24 24"
                          >
                            <path
                              strokeLinecap="round"
                              strokeLinejoin="round"
                              strokeWidth={2}
                              d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"
                            />
                          </svg>
                        </div>
                      </div>
                    </div>

                    {/* Brand Info */}
                    <div className="text-center">
                      <div className="font-bold text-gray-900 block mb-2 line-clamp-2 group-hover:text-gray-800 transition-colors duration-200 text-sm leading-tight">
                        {brand.name}
                      </div>
                      
                      <div className="text-xs text-gray-500 mb-3">
                        <span className="inline-block bg-slate-100 px-3 py-1.5 rounded-full text-slate-600 group-hover:bg-slate-200 group-hover:text-slate-700 transition-all duration-200">
                          {brand.origin}
                        </span>
                      </div>

                      {/* Status Badge */}
                      <div className="flex justify-center">
                        <span className={`px-3 py-1.5 text-xs rounded-full font-medium transition-all duration-200 ${
                          brand.status 
                            ? 'bg-emerald-50 text-emerald-700 group-hover:bg-emerald-100' 
                            : 'bg-rose-50 text-rose-700 group-hover:bg-rose-100'
                        }`}>
                          {brand.status ? 'Hoạt động' : 'Tạm dừng'}
                        </span>
                      </div>
                    </div>
                  </div>

                  {/* Hover Effect */}
                  <div className={`absolute inset-0 rounded-xl transition-all duration-300 pointer-events-none ${
                    selectedBrandIds.includes(brand.id)
                      ? 'bg-gradient-to-br from-green-500/10 to-green-600/5'
                      : 'group-hover:bg-gradient-to-br group-hover:from-gray-500/8 group-hover:to-gray-600/4'
                  }`} />
                </div>
              ))}
            </div>
          )}
        </ScrollArea>
      </div>

      <Separator className="my-4" />

      {/* Nút hành động */}
      <div className="flex-shrink-0 flex justify-end gap-3">
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
          disabled={isLoading || loading}
        >
          {isLoading ? (
            <>
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              Đang lưu...
            </>
          ) : (
            `Lưu (${selectedBrandIds.length} thương hiệu)`
          )}
        </Button>
      </div>
    </form>
  )
}
