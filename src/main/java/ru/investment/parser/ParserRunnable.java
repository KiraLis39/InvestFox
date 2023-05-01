package ru.investment.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import ru.investment.interfaces.IParserTask;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ParserRunnable implements IParserTask {
    private final StateMachineFactory<ParserStates, ParserEvents> stateMachineFactory;
    private UUID uuid;
    private ParserRunService parserRunService;
    private StateMachine<ParserStates, ParserEvents> stateMachine;
    private Thread parserThread;

    @Override
    public void run() {
        stateMachine = stateMachineFactory.getStateMachine("ZakupkiGov_PurchaseList");
        log.debug("Initial stateMachine.getState().getId(): {}", stateMachine.getState().getId());
        stateMachine.getExtendedState().getVariables().put("uuid", uuid);
        try {
            stateMachine.start();
            stateMachine.sendEvent(ParserEvents.ON_SETUP_STARTED);
        } catch (Exception e) {
            log.error("\n\nPURCHASE LIST PARSER ERROR:\n" + e.getMessage(), e);
            throw e;
        } finally {
            log.info(
                    "StateMachine finished, isComplete(): {}, final state: {}",
                    stateMachine.isComplete(),
                    stateMachine.getState().getIds()
            );

            parserRunService.stop(uuid);
        }
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void setParserThread(Thread parserThread) {
        this.parserThread = parserThread;
    }

    @Override
    public void stop() {
        log.debug("StateMachine {} stops", this.uuid);
        getStateMachine().sendEvent(ParserEvents.ON_STOP_BY_USER);
    }

    public StateMachine<ParserStates, ParserEvents> getStateMachine() {
        return this.stateMachine;
    }
}
