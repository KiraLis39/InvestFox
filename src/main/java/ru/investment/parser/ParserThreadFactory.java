package ru.investment.parser;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ThreadFactory;

@Slf4j
public class ParserThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(@NotNull Runnable r) {
        return new Thread(r);
    }
}
