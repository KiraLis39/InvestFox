package ru.investment.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

import java.text.SimpleDateFormat;
import java.util.EnumMap;
import java.util.TimeZone;
import java.util.UUID;

@Slf4j
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Component
@Scope("prototype")
public class ParserThread extends Thread {
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");
    private final EnumMap<ENVIRONMENTS, Object> data = new EnumMap<>(ENVIRONMENTS.class);
    private UUID uuid;
    private StateMachine<ParserStates, ParserEvents> stateMachine;
    private String threadState;
    private boolean isFinished = false;

    public ParserThread(CardsTaskRunnable cardsTaskRunnable) {
        super(cardsTaskRunnable, cardsTaskRunnable.getName());
        cardsTaskRunnable.setParserThread(this);
    }

    public void setup() {
        setThreadState(State.RUNNABLE.name());

        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        log.debug("Initial stateMachine.getState().getId(): {}", stateMachine.getState().getId());

        log.info("*** Parsing the exists purchases from table... ***");
        this.stateMachine.sendEvent(ParserEvents.ON_SETUP_COMPLETED);
    }

    public void store(ENVIRONMENTS key, Object value) {
        data.put(key, value);
    }

    public Object get(ENVIRONMENTS key) {
        return data.getOrDefault(key, "-=NA=-");
    }

    public void close() {
//        log.info("*** Total work time is " + sdf.format(getWorkTime()) + " ***\n");
        setThreadState(ParserThread.ThreadState.SHUTTING_DOWN.name());
        stateMachine.sendEvent(ParserEvents.ON_STOP_BY_USER);
        isFinished = true;
    }

    public enum ThreadState {
        RUNNABLE,
        TERMINATED,
        DEAD,
        SHUTTING_DOWN,
    }

    public enum ENVIRONMENTS {
        TASK,
        PURCHASE_PAGE,
    }
}
