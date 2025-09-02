package vn.com.ecomstore.dtos.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AttributeAddRequest {
    @NotBlank(message = "Attribute name cannot be blank")
    private String name;
}