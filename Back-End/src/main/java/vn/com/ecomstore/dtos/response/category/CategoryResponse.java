package vn.com.ecomstore.dtos.response.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.com.ecomstore.entities.Attribute;
import vn.com.ecomstore.entities.Category;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AttributeResponse> attributes;

    public static CategoryResponse convertFromEntity(Category entity) {
        return CategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .image(entity.getImage())
                .status(entity.isStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getModifiedAt())
                .attributes(entity.getAttributes()
                        .stream()
                        .map(AttributeResponse::fromEntity)
                        .toList())
                .build();
    }

    @Getter
    @Setter
    @Builder
    public static class AttributeResponse {
        private Long id;
        private String name;

        public static AttributeResponse fromEntity(Attribute a) {
            return AttributeResponse.builder()
                    .id(a.getId())
                    .name(a.getName())
                    .build();
        }
    }
}
