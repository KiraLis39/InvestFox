package ru.investment.exceptions;

import ru.investment.exceptions.root.ErrorMessages;

/**
 * Исключение, которое используется повсеместно в сервисе.
 * В будущем, возможно, стоит несколько разделить события по разным типам исключений вместо одного.
 */
public class GlobalServiceException extends Exception {
    private final String errorCode;

    public GlobalServiceException(ErrorMessages error) {
        super(error.getErrorCause());
        this.errorCode = error.getErrorCode();
    }

    public GlobalServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public GlobalServiceException(ErrorMessages error, Object data) {
        super(error.getErrorCause() + data);
        this.errorCode = error.getErrorCode();
    }

    public GlobalServiceException(ErrorMessages error, String data) {
        super(error.getErrorCause().concat(": ").concat(data));
        this.errorCode = error.getErrorCode();
    }

    public GlobalServiceException(ErrorMessages error, Exception e) {
        super(error.getErrorCause() + (e.getCause() == null ? e.getMessage()
                : e.getCause().getCause() == null ? e.getCause().getMessage()
                : e.getCause().getCause().getMessage()));
        this.errorCode = error.getErrorCode();
    }

    public String getErrorCode() {
        return errorCode;
    }
}
