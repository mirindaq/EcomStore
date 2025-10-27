package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.productQuestion.ProductQuestionAnswerResponse;
import vn.com.ecomstore.entities.ProductQuestionAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductQuestionAnswerMapper {

    @Mapping(source = "user.fullName", target = "userName")
    ProductQuestionAnswerResponse toResponse(ProductQuestionAnswer productQuestionAnswer);

}
