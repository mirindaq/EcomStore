package vn.com.ecomstore.dtos.request.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {
    private String accessToken;
}