package iuh.fit.ecommerce.exceptions.custom;

import iuh.fit.ecommerce.exceptions.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidParamException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidParamException(String message) {
        super(message);
        this.errorCode = ErrorCode.INVALID_PARAMETER;
    }

    public InvalidParamException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public InvalidParamException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}
