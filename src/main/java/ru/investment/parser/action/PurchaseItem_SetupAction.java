package ru.investment.parser.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

@Slf4j
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class PurchaseItem_SetupAction implements Action<ParserStates, ParserEvents> {

    @Override
    public void execute(final StateContext<ParserStates, ParserEvents> context) {
//        ParserThread parserThread = ((ParserThread) Thread.currentThread());
//        parserThread.getStateMachine().sendEvent(ParserEvents.SETUP_COMPLETED);
//        parserStateService.updateTaskState(context.getEvent().toString(), parserThread);
    }
}
