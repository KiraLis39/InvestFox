package ru.investment.exceptions;

import ru.investment.exceptions.root.ParsingException;

public class ParserEntitySaveException extends ParsingException {

    public ParserEntitySaveException(String message) {
        super(message);
    }

    public ParserEntitySaveException(String message, String errorData) {
        super(message, errorData);
    }

    public ParserEntitySaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserEntitySaveException(String message, String cause, Throwable t) {
        super(message, cause, t);
    }
}
