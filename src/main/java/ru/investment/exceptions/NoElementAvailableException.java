package ru.investment.exceptions;

import ru.investment.exceptions.root.ParsingException;

public class NoElementAvailableException extends ParsingException {

    public NoElementAvailableException(String errorMsg) {
        super(errorMsg);
    }

    public NoElementAvailableException(String errorMsg, String errorData) {
        super(errorMsg, errorData);
    }

    public NoElementAvailableException(String errorMsg, String errorData, Throwable e) {
        super(errorMsg, errorData, e);
    }
}
