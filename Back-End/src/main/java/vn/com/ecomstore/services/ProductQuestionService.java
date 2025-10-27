package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.productQuestion.ProductQuestionAddRequest;
import vn.com.ecomstore.dtos.request.productQuestion.ProductQuestionAnswerAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.productQuestion.ProductQuestionResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface ProductQuestionService {
    ProductQuestionResponse createProductQuestion(@Valid ProductQuestionAddRequest request);

    ResponseWithPagination<List<ProductQuestionResponse>> getProductQuestionsByProductSlug(String slug, int page, int size);

    ProductQuestionResponse createProductQuestionAnswer(@Valid ProductQuestionAnswerAddRequest request);
}
