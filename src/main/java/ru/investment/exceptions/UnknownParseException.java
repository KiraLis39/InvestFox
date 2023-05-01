package ru.investment.exceptions;

import ru.investment.exceptions.root.ParsingException;

public class UnknownParseException extends ParsingException {

    public UnknownParseException(Exception e) {
        super(e);
    }

    public UnknownParseException(String message) {
        super(message);
    }

    public UnknownParseException(String message, String errorData) {
        super(message, errorData);
    }

    public UnknownParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
