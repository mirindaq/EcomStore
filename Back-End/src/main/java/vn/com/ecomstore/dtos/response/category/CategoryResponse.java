package vn.com.ecomstore.dtos.response.category;

import lombok.*;
import vn.com.ecomstore.entities.Attribute;
import vn.com.ecomstore.entities.Category;

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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeResponse {
        private Long id;
        private String name;

    }
}
