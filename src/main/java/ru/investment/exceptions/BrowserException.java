package ru.investment.exceptions;

import ru.investment.exceptions.root.ParsingException;

public class BrowserException extends ParsingException {

    public BrowserException(String errorMsg) {
        super(errorMsg);
    }

    public BrowserException(String errorMsg, String errorData) {
        super(errorMsg, errorData);
    }

    public BrowserException(String errorMsg, String errorData, Throwable e) {
        super(errorMsg, errorData, e);
    }
}
