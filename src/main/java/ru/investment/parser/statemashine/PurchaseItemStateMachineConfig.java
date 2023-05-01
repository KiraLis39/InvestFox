package ru.investment.parser.statemashine;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import ru.investment.parser.action.*;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

import static ru.investment.parser.enums.ParserEvents.*;
import static ru.investment.parser.enums.ParserStates.*;

@Configuration
@AllArgsConstructor
@EnableStateMachineFactory(name = "ZakupkiGov_PurchaseItem")
public class PurchaseItemStateMachineConfig extends EnumStateMachineConfigurerAdapter<ParserStates, ParserEvents> {

    private final ApplicationContext beanCtx;

    @Override
    public void configure(final StateMachineConfigurationConfigurer<ParserStates, ParserEvents> config) throws Exception {
        config.withConfiguration().autoStartup(true).listener(new PurchaseItemStateMachineApplicationListener());
    }

    @Override
    public void configure(final StateMachineStateConfigurer<ParserStates, ParserEvents> states) throws Exception {
        states
                .withStates()
                .initial(IN_RUNNING)
                .stateEntry(
                        IN_STOPPED_BY_USER,
                        beanCtx.getBean(PurchaseItem_StopByUserAction.class),
                        beanCtx.getBean(PurchaseItem_ErrorHandlerAction.class)
                )
                .stateEntry(
                        IN_STOPPED_BY_ERRORS,
                        beanCtx.getBean(PurchaseItem_StopByErrorsAction.class),
                        beanCtx.getBean(PurchaseItem_ErrorHandlerAction.class)
                )
                .end(IN_CUSTOM_END)
                .and()
                .withStates()
                .parent(IN_RUNNING)
                .initial(IN_INIT)
                .stateEntry(
                        IN_SETTING_UP,
                        beanCtx.getBean(PurchaseItem_SetupAction.class),
                        beanCtx.getBean(PurchaseItem_ErrorHandlerAction.class))
                .stateEntry(
                        IN_ERROR_STATE,
                        beanCtx.getBean(PurchaseItem_ErrorAction.class),
                        beanCtx.getBean(PurchaseItem_ErrorHandlerAction.class))
                .stateEntry(
                        IN_LOADING_DATA,
                        beanCtx.getBean(PurchaseItem_FindPageToParseAction.class),
                        beanCtx.getBean(PurchaseItem_ErrorHandlerAction.class))
                .stateEntry(
                        IN_PARSING,
                        beanCtx.getBean(PurchaseItem_ParsePageAction.class),
                        beanCtx.getBean(PurchaseItem_ErrorHandlerAction.class))
                .stateEntry(
                        IN_SLEEPING,
                        beanCtx.getBean(PurchaseItem_SleepAction.class),
                        beanCtx.getBean(PurchaseItem_ErrorHandlerAction.class))
                .stateEntry(
                        IN_TECHNICAL_WORK_SLEEPING,
                        beanCtx.getBean(PurchaseItem_TechnicalWorkSleepAction.class),
                        beanCtx.getBean(PurchaseItem_ErrorHandlerAction.class));
    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<ParserStates, ParserEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(IN_RUNNING)
                .event(ON_STOP_BY_USER)
                .target(IN_STOPPED_BY_USER)

                .and()
                .withExternal()
                .source(IN_RUNNING)
                .event(ON_ERROR_OCCURRED)
                .target(IN_ERROR_STATE)

                .and()
                .withExternal()
                .source(IN_INIT)
                .event(ON_SETUP_STARTED)
                .target(IN_SETTING_UP)

                .and()
                .withExternal()
                .source(IN_SETTING_UP)
                .event(ON_ERROR_OCCURRED)
                .target(IN_ERROR_STATE)

                .and()
                .withExternal()
                .source(IN_SETTING_UP)
                .event(ON_SETUP_COMPLETED)
                .target(IN_LOADING_DATA)

                .and()
                .withExternal()
                .source(IN_LOADING_DATA)
                .event(ON_ERROR_OCCURRED)
                .target(IN_ERROR_STATE)

                .and()
                .withExternal()
                .source(IN_LOADING_DATA)
                .event(ON_STOP_BY_USER)
                .target(IN_STOPPED_BY_USER)

                .and()
                .withExternal()
                .source(IN_LOADING_DATA)
                .event(ON_ACCOMPLISHED)
                .target(IN_CUSTOM_END)

                .and()
                .withExternal()
                .source(IN_LOADING_DATA)
                .event(ON_DATA_NOT_FOUND)
                .target(IN_SLEEPING)

                .and()
                .withExternal()
                .source(IN_LOADING_DATA)
                .event(ON_BROKEN_PAGE_FOUND)
                .target(IN_SETTING_UP)

                .and()
                .withExternal()
                .source(IN_LOADING_DATA)
                .event(ON_TECHNICAL_WORK)
                .target(IN_TECHNICAL_WORK_SLEEPING)

                .and()
                .withExternal()
                .source(IN_LOADING_DATA)
                .event(ON_NEW_DATA_FOUND)
                .target(IN_PARSING)

                .and()
                .withExternal()
                .source(IN_LOADING_DATA)
                .event(ON_STOP_BY_USER)
                .target(IN_STOPPED_BY_USER)

                .and()
                .withExternal()
                .source(IN_PARSING)
                .event(ON_STOP_BY_USER)
                .target(IN_STOPPED_BY_USER)

                .and()
                .withExternal()
                .source(IN_PARSING)
                .event(ON_ACCOMPLISHED)
                .target(IN_CUSTOM_END)

                .and()
                .withExternal()
                .source(IN_LOADING_DATA)
                .event(ON_BROKEN_PAGE_FOUND)
                .target(IN_SETTING_UP)

                .and()
                .withExternal()
                .source(IN_PARSING)
                .event(ON_BROKEN_PAGE_FOUND)
                .target(IN_SETTING_UP)

                .and()
                .withExternal()
                .source(IN_LOADING_DATA)
                .event(ON_ERROR_OCCURRED)
                .target(IN_ERROR_STATE)

                .and()
                .withExternal()
                .source(IN_PARSING)
                .event(ON_ERROR_OCCURRED)
                .target(IN_ERROR_STATE)

                .and()
                .withExternal()
                .source(IN_ERROR_STATE)
                .event(ON_ERROR_COUNT_EXCEEDED)
                .target(IN_STOPPED_BY_ERRORS)

                .and()
                .withExternal()
                .source(IN_ERROR_STATE)
                .event(ON_ERROR_COUNT_NOT_EXCEEDED)
                .target(IN_SLEEPING);
    }
}
