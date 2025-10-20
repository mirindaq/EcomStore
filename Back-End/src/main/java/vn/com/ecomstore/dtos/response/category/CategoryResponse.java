package vn.com.ecomstore.dtos.response.category;

import vn.com.ecomstore.dtos.response.attribute.AttributeResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<AttributeResponse> attributes;

}