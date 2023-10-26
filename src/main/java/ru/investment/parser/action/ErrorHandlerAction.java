package ru.investment.parser.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.investment.parser.ParserThread;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorHandlerAction implements Action<ParserStates, ParserEvents> {

    @Override
    public void execute(final StateContext<ParserStates, ParserEvents> context) {
        log.error("State Machine Error Handler occurred: " + context.getTarget().getId());
        ParserThread parserThread = ((ParserThread) Thread.currentThread());
        saveParserError(context, parserThread);
    }

    public void saveParserError(StateContext<?, ParserEvents> context, ParserThread parserThread) {
//        ParserTask task = (ParserTask) parserThread.get(ParserThread.ENVIRONMENTS.TASK);
//        task.setState(context.getStateMachine().getState().getIds().toString());
//        task.setLastEvent(context.getEvent().name());
//
//        task = parserTaskService.save(task);
//        parserThread.store(ParserThread.ENVIRONMENTS.TASK, task);
//
//        parserStateService.saveErrorInfo(new UnknownParseException(context.getException().toString()), context.getException().toString());
    }
}
