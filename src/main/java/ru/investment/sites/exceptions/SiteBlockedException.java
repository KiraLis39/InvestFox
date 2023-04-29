package ru.investment.sites.exceptions;

import java.io.IOException;

public class SiteBlockedException extends IOException {
    private final String cause;

    public SiteBlockedException(String message, Exception ex) {
        super(message, ex);
        this.cause = ex == null || ex.getCause() == null ? null : ex.getCause().getMessage();
    }

    @Override
    public String toString() {
        return "SiteBlockedException{" +
                "message='" + getMessage() + '\'' +
                "cause='" + cause + '\'' +
                '}';
    }
}
