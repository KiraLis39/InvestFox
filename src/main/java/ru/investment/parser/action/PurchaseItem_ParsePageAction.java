package ru.investment.parser.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;
import ru.investment.parser.handlers.OpenAndParseHandler;

@Slf4j
@Component
@Scope("prototype")
@RequiredArgsConstructor
public class PurchaseItem_ParsePageAction implements Action<ParserStates, ParserEvents> {

    private final ApplicationContext appCtx;

    @Override
    public void execute(final StateContext<ParserStates, ParserEvents> contx) {
        appCtx.getBean(OpenAndParseHandler.class).toParse();
    }
}
