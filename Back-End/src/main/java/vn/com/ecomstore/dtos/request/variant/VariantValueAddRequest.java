package vn.com.ecomstore.dtos.request.variant;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class VariantValueAddRequest {
    @NotBlank(message = "Variant value is required")
    private String value;
}