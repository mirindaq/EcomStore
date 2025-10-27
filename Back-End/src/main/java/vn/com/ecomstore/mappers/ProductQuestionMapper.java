package vn.com.ecomstore.mappers;

import vn.com.ecomstore.dtos.response.productQuestion.ProductQuestionResponse;
import vn.com.ecomstore.entities.ProductQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {ProductQuestionAnswerMapper.class}
)
public interface ProductQuestionMapper {

    @Mapping(source = "user.fullName", target = "userName")
    ProductQuestionResponse toResponse(ProductQuestion productQuestion);
}
