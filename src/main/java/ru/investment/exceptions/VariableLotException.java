package ru.investment.exceptions;

import ru.investment.exceptions.root.ParsingException;

public class VariableLotException extends ParsingException {

    public VariableLotException(Exception e) {
        super(e);
    }

    public VariableLotException(String message) {
        super(message);
    }

    public VariableLotException(String message, String errorData) {
        super(message, errorData);
    }

    public VariableLotException(String message, Throwable cause) {
        super(message, cause);
    }
}
