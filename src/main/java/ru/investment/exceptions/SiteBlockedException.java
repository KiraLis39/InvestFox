package ru.investment.exceptions;

import ru.investment.exceptions.root.ParsingException;

public class SiteBlockedException extends ParsingException {

    public SiteBlockedException(Exception e) {
        super(e);
    }

    public SiteBlockedException(String message) {
        super(message);
    }

    public SiteBlockedException(String message, String errorData) {
        super(message, errorData);
    }

    public SiteBlockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
