package ru.investment.parser.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.investment.config.ApplicationProperties;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

@Slf4j
@Component
@RequiredArgsConstructor
public class PurchaseItem_ErrorAction implements Action<ParserStates, ParserEvents> {

    private final ApplicationProperties props;

    @Override
    public void execute(final StateContext<ParserStates, ParserEvents> context) {
//        log.error("State Machine Error occurred: " + context.getTarget().getId());
//        ParserThread parserThread = ((ParserThread) Thread.currentThread());
//        ParserTask task = (ParserTask) parserThread.get(ParserThread.ENVIRONMENTS.TASK);
//        task.setErrorCount(task.getErrorCount() + 1);
//        if (task.getErrorCount() > props.getMaxErrorCount()) {
//            task.setThreadState(ParserThread.THREAD_STATE.TERMINATED.name());
//            parserThread.getStateMachine().sendEvent(ParserEvents.ERROR_COUNT_EXCEEDED);
//        } else {
//            parserThread.getStateMachine().sendEvent(ParserEvents.ERROR_COUNT_NOT_EXCEEDED);
//        }
    }
}
