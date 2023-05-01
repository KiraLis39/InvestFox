package ru.investment.parser.handlers.markers;

import ru.investment.exceptions.root.ParsingException;

public interface IParseHandler {
    void parseFrom(String sourceUrl) throws ParsingException;
}
