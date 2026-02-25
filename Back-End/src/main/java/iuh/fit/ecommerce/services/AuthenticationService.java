package iuh.fit.ecommerce.services;

import iuh.fit.ecommerce.dtos.request.authentication.LoginRequest;
import iuh.fit.ecommerce.dtos.request.authentication.RegisterRequest;
import iuh.fit.ecommerce.dtos.response.authentication.AuthLoginResult;
import iuh.fit.ecommerce.dtos.response.authentication.RefreshTokenResponse;
import iuh.fit.ecommerce.dtos.response.user.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.io.IOException;

public interface AuthenticationService {
    AuthLoginResult staffLogin(LoginRequest loginRequest);

    RefreshTokenResponse refreshToken(HttpServletRequest request);

    void logout(HttpServletRequest request);

    String generateAuthUrl(String loginType, String redirectUri);

    AuthLoginResult socialLoginCallback(String loginType, String code, String redirectUri) throws IOException;

    AuthLoginResult userLogin(LoginRequest loginRequest);

    UserProfileResponse getProfile();

    void register(@Valid RegisterRequest registerRequest);
}
