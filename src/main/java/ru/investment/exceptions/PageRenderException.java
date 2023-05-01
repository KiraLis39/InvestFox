package ru.investment.exceptions;

import ru.investment.exceptions.root.ParsingException;

public class PageRenderException extends ParsingException {

    public PageRenderException(String errorMsg) {
        super(errorMsg);
    }

    public PageRenderException(String errorMsg, String errorData) {
        super(errorMsg, errorData);
    }

    public PageRenderException(String errorMsg, String errorData, Throwable e) {
        super(errorMsg, errorData, e);
    }
}
