package vn.com.ecomstore.mappers;

import org.mapstruct.Mapper;
import vn.com.ecomstore.dtos.response.category.CategoryResponse.AttributeResponse;
import vn.com.ecomstore.entities.Attribute;

@Mapper(componentModel = "spring")
public interface AttributeMapper {
    AttributeResponse toResponse(Attribute attribute);
}