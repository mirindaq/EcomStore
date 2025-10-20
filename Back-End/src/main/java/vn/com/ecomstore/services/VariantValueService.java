package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.variant.VariantValueAddRequest;
import vn.com.ecomstore.dtos.response.variant.VariantValueResponse;
import vn.com.ecomstore.entities.Variant;
import vn.com.ecomstore.entities.VariantValue;

import java.util.List;

public interface VariantValueService {
    List<VariantValueResponse> getVariantValueByVariantId(Long id);

    void changeStatusVariantValue(Long id);

    List<VariantValue> createValues(List<VariantValueAddRequest> variantValues, Variant variant);

    List<VariantValue> updateValue(List<VariantValueAddRequest> variantValues, Variant variant);
    VariantValue getVariantValueEntityById(Long id);
}
