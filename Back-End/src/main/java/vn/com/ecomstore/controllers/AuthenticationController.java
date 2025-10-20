package vn.com.ecomstore.controllers;

import vn.com.ecomstore.dtos.request.authentication.LoginRequest;
import vn.com.ecomstore.dtos.request.authentication.RegisterRequest;
import vn.com.ecomstore.dtos.response.authentication.LoginResponse;
import vn.com.ecomstore.dtos.response.authentication.RefreshTokenResponse;
import vn.com.ecomstore.dtos.response.base.ResponseSuccess;
import vn.com.ecomstore.dtos.response.user.UserProfileResponse;
import vn.com.ecomstore.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/admin/login")
    public ResponseEntity<ResponseSuccess<LoginResponse>> loginStaff(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new ResponseSuccess<>(
                HttpStatus.OK,
                "Login Success",
                authenticationService.staffLogin(loginRequest)
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseSuccess<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new ResponseSuccess<>(
                HttpStatus.OK,
                "Login Success",
                authenticationService.userLogin(loginRequest)
        ));
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
        return ResponseEntity.ok(new ResponseSuccess<>(
                HttpStatus.OK,
                "Login Success",
                authenticationService.refreshToken(request)
        ));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseSuccess<?>> logout(HttpServletRequest request) {
        authenticationService.logout(request);
        return ResponseEntity.ok(new ResponseSuccess<>(
                HttpStatus.OK,
                "Logout Success",
                null));
    }

    @GetMapping("/social-login")
    public ResponseEntity<ResponseSuccess<String>> socialLogin(@RequestParam("login_type") String loginType) {
        return ResponseEntity.ok().body(
                new ResponseSuccess<>(HttpStatus.OK,
                        "Social login successfully",
                        authenticationService.generateAuthUrl(loginType)
                )
        );
    }

    @GetMapping("/social-login/callback")
    public ResponseEntity<ResponseSuccess<LoginResponse>> socialLoginCallback(
            @RequestParam("login_type") String loginType,
            @RequestParam String code) throws IOException {

        return ResponseEntity.ok().body(
                new ResponseSuccess<>(HttpStatus.OK,
                        "Social login callback successfully",
                        authenticationService.socialLoginCallback(loginType, code)
                )
        );
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
