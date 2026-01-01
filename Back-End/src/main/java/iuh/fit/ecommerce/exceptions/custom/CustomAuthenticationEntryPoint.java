package iuh.fit.ecommerce.exceptions.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.ecommerce.dtos.response.base.ResponseError;
import iuh.fit.ecommerce.exceptions.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorCode errorCode = ErrorCode.TOKEN_MISSING;
        ResponseError error = ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .path(request.getRequestURI())
                .message(errorCode.getMessage())
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String jsonError = new ObjectMapper().writeValueAsString(error);
        response.getWriter().write(jsonError);
    }
}
