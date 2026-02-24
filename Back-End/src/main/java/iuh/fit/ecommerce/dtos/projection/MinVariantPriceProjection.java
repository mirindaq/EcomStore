package iuh.fit.ecommerce.dtos.projection;

public interface MinVariantPriceProjection {
    Long getProductId();
    Long getVariantId();
    Double getPrice();
    Long getBrandId();
    Long getCategoryId();
    String getSku();
    Integer getStock();
}
