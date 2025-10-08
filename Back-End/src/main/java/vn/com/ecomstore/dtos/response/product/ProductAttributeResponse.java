package vn.com.ecomstore.dtos.response.product;

import vn.com.ecomstore.dtos.response.attribute.AttributeResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductAttributeResponse {
    private Long id;
    private String value;
    private AttributeResponse attribute;
}
