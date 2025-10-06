package vn.com.ecomstore.services;

import vn.com.ecomstore.dtos.request.authentication.LoginRequest;
import vn.com.ecomstore.dtos.request.authentication.RefreshTokenRequest;
import vn.com.ecomstore.dtos.response.authentication.LoginResponse;
import vn.com.ecomstore.dtos.response.authentication.RefreshTokenResponse;
import vn.com.ecomstore.dtos.response.user.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface AuthenticationService {
    LoginResponse staffLogin(LoginRequest loginRequest);

    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void logout(HttpServletRequest request);

    String generateAuthUrl(String loginType);

    LoginResponse socialLoginCallback(String loginType, String code) throws IOException;

    LoginResponse userLogin(LoginRequest loginRequest);

    UserProfileResponse getProfile();
}
