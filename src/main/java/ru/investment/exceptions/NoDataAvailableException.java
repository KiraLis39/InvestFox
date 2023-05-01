package ru.investment.exceptions;

import ru.investment.exceptions.root.ParsingException;

public class NoDataAvailableException extends ParsingException {

    public NoDataAvailableException(String errorMsg) {
        super(errorMsg);
    }

    public NoDataAvailableException(String errorMsg, String errorData) {
        super(errorMsg, errorData);
    }

    public NoDataAvailableException(String errorMsg, String errorData, Throwable e) {
        super(errorMsg, errorData, e);
    }
}
