package iuh.fit.ecommerce.exceptions.custom;

import iuh.fit.ecommerce.exceptions.ErrorCode;
import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
    private final ErrorCode errorCode;

    public ConflictException(String message) {
        super(message);
        this.errorCode = ErrorCode.CONFLICT;
    }

    public ConflictException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ConflictException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
