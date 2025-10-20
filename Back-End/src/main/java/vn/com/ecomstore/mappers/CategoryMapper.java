package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.category.CategoryResponse;
import vn.com.ecomstore.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {AttributeMapper.class})
public interface CategoryMapper {
//    @Mapping(target = "attributes", source = "attributes", qualifiedByName = "mapActiveAttributes")
    CategoryResponse toResponse(Category category);

    @Named("toResponseWithoutAttributes")
    @Mapping(target = "attributes", ignore = true)
    CategoryResponse toResponseWithoutAttributes(Category category);
}