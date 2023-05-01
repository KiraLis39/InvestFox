package ru.investment.parser.handlers.markers;

import ru.investment.exceptions.root.ParsingException;

public interface IParseRoot {
    void parseItem(String sourceUrl) throws ParsingException;
}
