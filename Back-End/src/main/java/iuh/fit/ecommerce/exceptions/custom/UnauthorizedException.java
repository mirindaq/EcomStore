package iuh.fit.ecommerce.exceptions.custom;

import iuh.fit.ecommerce.exceptions.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    private final ErrorCode errorCode;

    public UnauthorizedException(String message) {
        super(message);
        this.errorCode = ErrorCode.UNAUTHORIZED;
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UnauthorizedException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
