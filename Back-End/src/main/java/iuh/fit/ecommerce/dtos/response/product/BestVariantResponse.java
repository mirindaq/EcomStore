package iuh.fit.ecommerce.dtos.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BestVariantResponse {
    private Long id;
    private Double price;   
    private Double oldPrice;  
    private Double discount; 
    private String sku;
    private Integer stock;
}
