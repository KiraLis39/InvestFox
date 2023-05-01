package ru.investment.interfaces;

import java.util.UUID;

public interface IParserTask extends Runnable {
    UUID getUuid();

    void setUuid(UUID uuid);

    void setParserThread(Thread parserThread);

    void stop();
}
