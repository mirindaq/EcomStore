package vn.com.ecomstore.mappers;

import org.mapstruct.Mapper;
import vn.com.ecomstore.dtos.response.category.CategoryResponse;
import vn.com.ecomstore.entities.Category;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class})
public interface CategoryMapper {
//    @Mapping(source = "modifiedAt", target = "updatedAt")
    CategoryResponse toResponse(Category category);
}