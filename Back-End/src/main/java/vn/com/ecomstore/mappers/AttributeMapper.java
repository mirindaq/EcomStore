package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.attribute.AttributeResponse;
import vn.com.ecomstore.entities.Attribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttributeMapper {
    @Mapping(source = "category.name", target = "categoryName")
    AttributeResponse toResponse(Attribute attribute);

//    @Named("mapActiveAttributes")
//    default List<AttributeResponse> mapActiveAttributes(List<Attribute> attributes) {
//        if (attributes == null) return null;
//        return attributes.stream()
//                .filter(Attribute::isStatus) // chỉ lấy status = true
//                .map(this::toResponse)
//                .toList();
//    }
}