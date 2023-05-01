package ru.investment.exceptions;

import ru.investment.exceptions.root.ParsingException;

public class NotEnoughDataException extends ParsingException {

    public NotEnoughDataException(String message) {
        super(message);
    }

    public NotEnoughDataException(String message, String errorData) {
        super(message, errorData);
    }

    public NotEnoughDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughDataException(String message, String errorData, Throwable cause) {
        super(message, errorData, cause);
    }
}
