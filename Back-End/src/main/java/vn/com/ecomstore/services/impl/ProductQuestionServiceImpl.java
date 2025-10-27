package vn.com.ecomstore.services.impl;

import vn.com.ecomstore.dtos.request.productQuestion.ProductQuestionAddRequest;
import vn.com.ecomstore.dtos.response.base.ResponseWithPagination;
import vn.com.ecomstore.dtos.response.productQuestion.ProductQuestionResponse;
import vn.com.ecomstore.entities.Product;
import vn.com.ecomstore.entities.ProductQuestion;
import vn.com.ecomstore.entities.User;
import vn.com.ecomstore.mappers.ProductQuestionMapper;
import vn.com.ecomstore.repositories.ProductQuestionRepository;
import vn.com.ecomstore.services.ProductQuestionService;
import vn.com.ecomstore.services.ProductService;
import vn.com.ecomstore.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQuestionServiceImpl implements ProductQuestionService {

    private final ProductQuestionRepository productQuestionRepository;
    private final ProductService productService;
    private final ProductQuestionMapper productQuestionMapper;
    private final SecurityUtil securityUtil;

    @Override
    public ProductQuestionResponse createProductQuestion(ProductQuestionAddRequest request) {
        User user = securityUtil.getCurrentUser();

        Product product = productService.getProductEntityById(request.getProductId());

        ProductQuestion productQuestion = ProductQuestion.builder()
                .product(product)
                .user(user)
                .content(request.getContent())
                .build();

        return productQuestionMapper.toResponse(productQuestionRepository.save(productQuestion));
    }

    @Override
    public ResponseWithPagination<List<ProductQuestionResponse>> getProductQuestionsByProductSlug(String slug, int page, int size) {
        page = Math.max(0, page - 1);
        Pageable pageable = PageRequest.of(page, size);

        Product product = productService.getProductEntityBySlug(slug);

        Page<ProductQuestion> productQuestionsPage = productQuestionRepository.findByProduct(product, pageable);

        return ResponseWithPagination.fromPage(productQuestionsPage, productQuestionMapper::toResponse);
    }

}
