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
public class PurchaseItem_StopByErrorsAction implements Action<ParserStates, ParserEvents> {

    private final ApplicationProperties props;

    @Override
    public void execute(final StateContext<ParserStates, ParserEvents> context) {
//        log.error("Parser stopped due to exceeding error count ({})", props.getMaxErrorCount());
//        ParserThread parserThread = (ParserThread) Thread.currentThread();
//        parserStateService.updateTaskState(context.getEvent().toString(), parserThread);
//        parserThread.close();
    }
}
