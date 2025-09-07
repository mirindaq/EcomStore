import type { ResponseApi, ResponseApiWithPagination } from "./responseApi.type";

export type CategoryWithBrandCount = {
  categoryId: number;
  categoryName: string;
  brandCount: number;
};

export type CategoryBrandRequest = {
  categoryId: number;
  brandId: number;
};

export type CategoryBrandAddRequest = {
  brands: number[];
  categoryId: number;
};

export type CategoryWithBrandCountResponse = ResponseApiWithPagination<CategoryWithBrandCount[]>;
