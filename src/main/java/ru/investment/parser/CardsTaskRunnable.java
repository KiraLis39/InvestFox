package ru.investment.parser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CardsTaskRunnable implements Runnable {

    private String name;
    private ParserThread parserThread;

    @Override
    public void run() {
        try {
            parserThread.setup();
        } finally {
            log.warn("Прерывание работы потока парсера. Если было не умышленно - следует обратить внимание.");
            parserThread.setFinished(true);
        }
    }

    public void setParserThread(ParserThread parserThread) {
        this.parserThread = parserThread;
    }

    public CardsTaskRunnable name(String name) {
        this.name = name;
        return this;
    }
}
