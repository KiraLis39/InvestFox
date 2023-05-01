package ru.investment.exceptions;

import ru.investment.exceptions.root.ParsingException;

public class DateParseException extends ParsingException {

    public DateParseException(Exception e) {
        super(e);
    }

    public DateParseException(String message) {
        super(message);
    }

    public DateParseException(String message, String errorData) {
        super(message, errorData);
    }

    public DateParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
