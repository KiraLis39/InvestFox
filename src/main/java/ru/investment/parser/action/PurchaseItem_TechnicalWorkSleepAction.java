package ru.investment.parser.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.investment.config.ApplicationProperties;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

@Slf4j
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class PurchaseItem_TechnicalWorkSleepAction implements Action<ParserStates, ParserEvents> {

    private final ApplicationProperties props;

    @Override
    public void execute(StateContext<ParserStates, ParserEvents> context) {
//        ParserThread parserThread = (ParserThread) Thread.currentThread();
//        try {
//            parserStateService.updateTaskState(context.getEvent().toString(), parserThread);
//            Thread.sleep(props.getTechnicalWorkSleepMs());
//            parserThread.getStateMachine().sendEvent(ParserEvents.WAKE_UP);
//        } catch (InterruptedException e) {
//            // This may occur when user pressed the "Wake Up" button
//            parserThread.getStateMachine().sendEvent(ParserEvents.WAKE_UP);
//        }
    }
}
