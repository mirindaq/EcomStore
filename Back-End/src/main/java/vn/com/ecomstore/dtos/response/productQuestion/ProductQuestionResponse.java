package vn.com.ecomstore.dtos.response.productQuestion;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProductQuestionResponse  {
    private Long id;
    private String content;
    private Boolean status;
    private String userName;
    private LocalDateTime createdAt;
    private List<ProductQuestionAnswerResponse> answers;
}
