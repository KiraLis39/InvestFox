package ru.investment.exceptions;

import ru.investment.exceptions.root.ParsingException;

public class BadDataException extends ParsingException {

    public BadDataException(Exception e) {
        super(e);
    }

    public BadDataException(String message) {
        super(message);
    }

    public BadDataException(String message, String errorData) {
        super(message, errorData);
    }

    public BadDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
