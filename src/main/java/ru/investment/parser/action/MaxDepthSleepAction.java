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
public class MaxDepthSleepAction implements Action<ParserStates, ParserEvents> {

    private final ApplicationProperties props;

    @Override
    public void execute(StateContext<ParserStates, ParserEvents> context) {
//        try {
//            parserStateService.saveCurrentStateAndTaskByContext(context);
//
//            PurchaseList_SetupAndLoadAction.resetPage();
//            ParserTaskDTO parserTaskDTO = parserTaskService
//                .findOneByGuid(context.getExtendedState().get("guid", String.class))
//                .orElseThrow();
//            parserTaskDTO.setParsedPagesCount(0L);
//            parserTaskService.saveAndFlush(parserTaskDTO);
//
//            Thread.sleep(props.getPurchasesMaxDepthSleepMs());
//            context.getStateMachine().sendEvent(ParserEvents.REPARSE_ALL);
//        } catch (InterruptedException e) {
//            // This may occur when user pressed the "Wake Up" button
//            context.getStateMachine().sendEvent(ParserEvents.REPARSE_ALL);
//        }
    }
}
