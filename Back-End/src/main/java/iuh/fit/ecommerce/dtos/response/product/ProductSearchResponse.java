package iuh.fit.ecommerce.dtos.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class ProductSearchResponse {
    private Long id;
    private String name;
    private String slug;
    private String thumbnail;
    private Boolean status;
    private Double rating;
    private String spu;
    private Long brandId;
    private Long categoryId;
    private List<String> productImages;

    private Double originalPrice;
    private Double displayPrice;
    private Double discountPercent;
    private BestVariantResponse bestVariant;
}
