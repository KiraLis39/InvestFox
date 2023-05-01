package ru.investment.exceptions.root;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParsingException extends Exception {
    private final String cause;

    public ParsingException(String message) {
        super(message);
        this.cause = null;
        log.error(message);
    }

    public ParsingException(String message, String errorData) {
        super(message.replace("{}", errorData));
        this.cause = null;
        log.error(message + " (" + errorData + ")");
    }

    public ParsingException(String message, Throwable ex) {
        super(message, ex);
        this.cause = ex == null || ex.getCause() == null ? null : ex.getCause().getMessage();
        log.error(message + " (" + cause + ")");
    }

    public ParsingException(String message, String errorData, Throwable ex) {
        super(errorData == null ? message : message.contains("{}") ? message.replace("{}", errorData) : message + ": " + errorData, ex);
        this.cause = ex == null || ex.getCause() == null ? null : ex.getCause().getMessage();
        log.error(errorData == null ? message : message.replace("{}", errorData) + " (" + cause + ")");
    }

    public ParsingException(Exception ex) {
        super(ex);
        this.cause = ex == null || ex.getCause() == null ? null : ex.getCause().getMessage();
    }

    @Override
    public String toString() {
        return "VariableLotException{" +
                "message='" + getMessage() + '\'' +
                "cause='" + cause + '\'' +
                '}';
    }
}
