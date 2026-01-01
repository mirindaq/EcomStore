package iuh.fit.ecommerce.exceptions.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.ecommerce.dtos.response.base.ResponseError;
import iuh.fit.ecommerce.exceptions.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        ResponseError error = ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .path(request.getRequestURI())
                .message(errorCode.getMessage())
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        new ObjectMapper().writeValue(response.getWriter(), error);
    }
}