package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.variant.VariantAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.variant.VariantResponse;

import java.util.List;

public interface VariantService {
    VariantResponse createVariant(VariantAddRequest request);

    ResponseWithPagination<List<VariantResponse>> getVariants(int page, int size, String variantName);

    VariantResponse getVariantById(Long id);

    VariantResponse updateVariant(Long id, VariantAddRequest request);

    void changeStatusVariant(Long id);
}
