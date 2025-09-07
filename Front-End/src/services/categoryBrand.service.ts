import axiosClient from '@/configurations/axios.config';
import type { 
  CategoryWithBrandCountResponse,
  CategoryBrandAddRequest
} from '@/types/categoryBrand.type';

export const categoryBrandService = {
  getCategoriesWithBrandCount: async (page: number = 1, size: number = 7) => {
    const response = await axiosClient.get<CategoryWithBrandCountResponse>(`/category-brands?page=${page}&size=${size}`);
    return response.data;
  },

  getBrandIdsByCategoryId: async (categoryId: number) => {
    const response = await axiosClient.get<{ data: number[] }>(`/category-brands/${categoryId}/brands`);
    return response.data;
  },

  assignBrandsToCategory: async (request: CategoryBrandAddRequest) => {
    const response = await axiosClient.post('/category-brands', request);
    return response.data;
  }
};
