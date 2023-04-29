package ru.investment.sites.exceptions;

import java.io.IOException;

public class VariableLotException extends IOException {
    private final String cause;

    public VariableLotException(String message, Exception ex) {
        super(message, ex);
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
