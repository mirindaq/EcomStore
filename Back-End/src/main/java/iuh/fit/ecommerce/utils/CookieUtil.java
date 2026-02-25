package iuh.fit.ecommerce.utils;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtil {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final int REFRESH_TOKEN_MAX_AGE_DAYS = 7;
    private static final String COOKIE_PATH = "/api/v1/auth";

    private CookieUtil() {
    }

    public static ResponseCookie buildRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path(COOKIE_PATH)
                .maxAge(Duration.ofDays(REFRESH_TOKEN_MAX_AGE_DAYS))
                .sameSite("Strict")
                .build();
    }

    public static ResponseCookie clearRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .path(COOKIE_PATH)
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }
}
