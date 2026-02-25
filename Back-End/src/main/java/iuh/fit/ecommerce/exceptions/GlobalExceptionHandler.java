package iuh.fit.ecommerce.exceptions;

import io.jsonwebtoken.JwtException;
import iuh.fit.ecommerce.dtos.response.base.ResponseError;
import iuh.fit.ecommerce.exceptions.custom.ConflictException;
import iuh.fit.ecommerce.exceptions.custom.InvalidParamException;
import iuh.fit.ecommerce.exceptions.custom.ResourceNotFoundException;
import iuh.fit.ecommerce.exceptions.custom.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.authentication.BadCredentialsException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        String errorMessage = String.join(", ", errorMessages);
        log.warn("Validation error at {}: {}", request.getDescription(false), errorMessage);

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        return ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .message(errorMessage)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(CONFLICT)
    public ResponseError handleConflictException(ConflictException e, WebRequest request) {
        log.warn("Conflict error at {}: {}", request.getDescription(false), e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseError handleDataNotFoundException(ResourceNotFoundException e, WebRequest request) {
        if (e.getErrorCode() == ErrorCode.REFRESH_TOKEN_NOT_FOUND) {
            log.warn("Auth failed: {}", e.getMessage());
        } else {
            log.warn("Resource not found at {}: {}", request.getDescription(false), e.getMessage());
        }
        ErrorCode errorCode = e.getErrorCode();
        return ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleInvalidParamException(InvalidParamException e, WebRequest request) {
        log.warn("Invalid parameter at {}: {}", request.getDescription(false), e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        log.warn("Illegal argument at {}: {}", request.getDescription(false), e.getMessage());
        ErrorCode errorCode = ErrorCode.ILLEGAL_ARGUMENT;
        return ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        log.warn("Access denied at {}: {}", request.getDescription(false), e.getMessage());
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        return ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(errorCode.getMessage())
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        log.warn("Authentication error at {}: {}", request.getDescription(false), ex.getMessage());
        ErrorCode errorCode = ErrorCode.TOKEN_INVALID;
        return ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(errorCode.getMessage())
                .build();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleIllegalStateExceptionException(IllegalStateException ex, WebRequest request) {
        log.error("Illegal state error at {}: {}", request.getDescription(false), ex.getMessage(), ex);
        ErrorCode errorCode = ErrorCode.ILLEGAL_STATE;
        return ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(ex.getMessage())
                .build();
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleException(Exception ex, WebRequest request) {
        log.error("Unhandled exception at {}: {}", request.getDescription(false), ex.getMessage(), ex);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(errorCode.getMessage())
                .build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        log.warn("Auth failed: {}", ex.getMessage());
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseError.builder()
                .timestamp(new Date())
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        log.warn("Auth failed: {}", ex.getMessage());
        return ResponseError.builder()
                .timestamp(new Date())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError handleJwtException(JwtException ex, WebRequest request) {
        log.warn("Auth failed: {}", ex.getMessage());
        return ResponseError.builder()
                .timestamp(new Date())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .message(ex.getMessage())
                .build();
    }



}
