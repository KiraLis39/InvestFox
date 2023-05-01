package ru.investment.parser.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

@Slf4j
@Component
@RequiredArgsConstructor
public class PurchaseItem_FindPageToParseAction implements Action<ParserStates, ParserEvents> {

    private final ApplicationContext appCtx;

    @Override
    public void execute(final StateContext<ParserStates, ParserEvents> context) {
//        try {
//            ParserThread parserThread = (ParserThread) Thread.currentThread();
//            parserThread.resetCardParseTimeStart(); // начинаем отсчёт времени парсинга очередной карточки.
//
//            Optional<PurchasePage> page = purchasePageService.findNextPageForParsing();
//            if (page.isPresent()) {
//                parserThread.store(
//                    ParserThread.ENVIRONMENTS.PURCHASE_PAGE,
//                    purchasePageService.saveAndFlush(page.get().parserGuid(parserThread.getGuid()).status(ParserStates.OPENING.name()))
//                );
//                parserStateService.updateTaskState(context.getEvent().toString(), parserThread);
//                parserThread.getStateMachine().sendEvent(ParserEvents.NEW_CARD_FOUND);
//            } else {
//                log.info("Доступных для парсинга карточек закупок не обнаружено.");
//                parserThread.getStateMachine().sendEvent(ParserEvents.NEW_CARD_NOT_FOUND);
//            }
//        } catch (Throwable t) {
//            parserStateService.saveErrorInfo(t, "Неудачное создание или сохранение ParserTask");
//            ((ParserThread) Thread.currentThread()).getStateMachine().sendEvent(ParserEvents.ERROR_OCCURRED);
//        }
    }
}
