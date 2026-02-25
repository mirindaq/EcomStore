package iuh.fit.ecommerce.controllers;

import iuh.fit.ecommerce.dtos.request.authentication.LoginRequest;
import iuh.fit.ecommerce.dtos.request.authentication.RegisterRequest;
import iuh.fit.ecommerce.dtos.response.authentication.AuthLoginResult;
import iuh.fit.ecommerce.dtos.response.authentication.LoginResponse;
import iuh.fit.ecommerce.dtos.response.authentication.RefreshTokenResponse;
import iuh.fit.ecommerce.dtos.response.base.ResponseSuccess;
import iuh.fit.ecommerce.dtos.response.user.UserProfileResponse;
import iuh.fit.ecommerce.services.AuthenticationService;
import iuh.fit.ecommerce.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/admin/login")
    public ResponseEntity<ResponseSuccess<LoginResponse>> loginStaff(@Valid @RequestBody LoginRequest loginRequest) {
        AuthLoginResult result = authenticationService.staffLogin(loginRequest);
        ResponseCookie cookie = CookieUtil.buildRefreshTokenCookie(result.getRefreshTokenForCookie());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ResponseSuccess<>(HttpStatus.OK, "Login Success", result.getLoginResponse()));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseSuccess<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthLoginResult result = authenticationService.userLogin(loginRequest);
        ResponseCookie cookie = CookieUtil.buildRefreshTokenCookie(result.getRefreshTokenForCookie());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ResponseSuccess<>(HttpStatus.OK, "Login Success", result.getLoginResponse()));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseSuccess<Void>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authenticationService.register(registerRequest);
        return ResponseEntity.ok(new ResponseSuccess<>(
                HttpStatus.OK,
                "Login Success",
                null
        ));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseSuccess<RefreshTokenResponse>> refresh(HttpServletRequest request) {
        RefreshTokenResponse body = authenticationService.refreshToken(request);
        return ResponseEntity.ok(new ResponseSuccess<>(HttpStatus.OK, "Token refreshed", body));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseSuccess<?>> logout(HttpServletRequest request) {
        authenticationService.logout(request);
        ResponseCookie clearCookie = CookieUtil.clearRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .body(new ResponseSuccess<>(HttpStatus.OK, "Logout Success", null));
    }

    @GetMapping("/social-login")
    public ResponseEntity<ResponseSuccess<String>> socialLogin(
            @RequestParam("login_type") String loginType,
            @RequestParam(value = "redirect_uri", required = false) String redirectUri) {
        return ResponseEntity.ok().body(
                new ResponseSuccess<>(HttpStatus.OK,
                        "Social login successfully",
                        authenticationService.generateAuthUrl(loginType, redirectUri)
                )
        );
    }

    @GetMapping("/social-login/callback")
    public ResponseEntity<ResponseSuccess<LoginResponse>> socialLoginCallback(
            @RequestParam("login_type") String loginType,
            @RequestParam String code,
            @RequestParam(value = "redirect_uri", required = false) String redirectUri) throws IOException {

        AuthLoginResult result = authenticationService.socialLoginCallback(loginType, code, redirectUri);
        ResponseCookie cookie = CookieUtil.buildRefreshTokenCookie(result.getRefreshTokenForCookie());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ResponseSuccess<>(HttpStatus.OK, "Social login callback successfully", result.getLoginResponse()));
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseSuccess<UserProfileResponse>> getProfile() {
        return ResponseEntity.ok().body(
                new ResponseSuccess<>(HttpStatus.OK,
                        "Social login callback successfully",
                        authenticationService.getProfile()
                ));
    }
}
