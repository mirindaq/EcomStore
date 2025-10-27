package vn.com.ecomstore.dtos.response.productQuestion;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductQuestionAnswerResponse {
    private Long id;
    private String userName;
    private String content;
    private Boolean status;
    private LocalDateTime createdAt;
    private Boolean admin;
}
