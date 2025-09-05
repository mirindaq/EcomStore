package vn.com.ecomstore.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.ecomstore.dtos.response.category.CategoryResponse;
import vn.com.ecomstore.entities.Category;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class})
public interface CategoryMapper {
//    @Mapping(target = "attributes", source = "attributes", qualifiedByName = "mapActiveAttributes")
    CategoryResponse toResponse(Category category);
}