package ru.investment.parser.statemashine;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import ru.investment.parser.action.ErrorAction;
import ru.investment.parser.action.ErrorHandlerAction;
import ru.investment.parser.action.FindPageToParseAction;
import ru.investment.parser.action.ParsePageAction;
import ru.investment.parser.action.SetupAction;
import ru.investment.parser.action.SleepAction;
import ru.investment.parser.action.StopByErrorsAction;
import ru.investment.parser.action.StopByUserAction;
import ru.investment.parser.action.TechnicalWorkSleepAction;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

import static ru.investment.parser.enums.ParserEvents.ON_ACCOMPLISHED;
import static ru.investment.parser.enums.ParserEvents.ON_BROKEN_PAGE_FOUND;
import static ru.investment.parser.enums.ParserEvents.ON_DATA_NOT_FOUND;
import static ru.investment.parser.enums.ParserEvents.ON_ERROR_COUNT_EXCEEDED;
import static ru.investment.parser.enums.ParserEvents.ON_ERROR_COUNT_NOT_EXCEEDED;
import static ru.investment.parser.enums.ParserEvents.ON_ERROR_OCCURRED;
import static ru.investment.parser.enums.ParserEvents.ON_NEW_DATA_FOUND;
import static ru.investment.parser.enums.ParserEvents.ON_SETUP_COMPLETED;
import static ru.investment.parser.enums.ParserEvents.ON_SETUP_STARTED;
import static ru.investment.parser.enums.ParserEvents.ON_STOP_BY_USER;
import static ru.investment.parser.enums.ParserEvents.ON_TECHNICAL_WORK;
import static ru.investment.parser.enums.ParserStates.IN_CUSTOM_END;
import static ru.investment.parser.enums.ParserStates.IN_ERROR_STATE;
import static ru.investment.parser.enums.ParserStates.IN_INIT;
import static ru.investment.parser.enums.ParserStates.IN_LOADING_DATA;
import static ru.investment.parser.enums.ParserStates.IN_PARSING;
import static ru.investment.parser.enums.ParserStates.IN_RUNNING;
import static ru.investment.parser.enums.ParserStates.IN_SETTING_UP;
import static ru.investment.parser.enums.ParserStates.IN_SLEEPING;
import static ru.investment.parser.enums.ParserStates.IN_STOPPED_BY_ERRORS;
import static ru.investment.parser.enums.ParserStates.IN_STOPPED_BY_USER;
import static ru.investment.parser.enums.ParserStates.IN_TECHNICAL_WORK_SLEEPING;

@Configuration
@AllArgsConstructor
@EnableStateMachineFactory(name = "Investment_Example")
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
                        beanCtx.getBean(StopByUserAction.class),
                        beanCtx.getBean(ErrorHandlerAction.class)
                )
                .stateEntry(
                        IN_STOPPED_BY_ERRORS,
                        beanCtx.getBean(StopByErrorsAction.class),
                        beanCtx.getBean(ErrorHandlerAction.class)
                )
                .end(IN_CUSTOM_END)
                .and()
                .withStates()
                .parent(IN_RUNNING)
                .initial(IN_INIT)
                .stateEntry(
                        IN_SETTING_UP,
                        beanCtx.getBean(SetupAction.class),
                        beanCtx.getBean(ErrorHandlerAction.class))
                .stateEntry(
                        IN_ERROR_STATE,
                        beanCtx.getBean(ErrorAction.class),
                        beanCtx.getBean(ErrorHandlerAction.class))
                .stateEntry(
                        IN_LOADING_DATA,
                        beanCtx.getBean(FindPageToParseAction.class),
                        beanCtx.getBean(ErrorHandlerAction.class))
                .stateEntry(
                        IN_PARSING,
                        beanCtx.getBean(ParsePageAction.class),
                        beanCtx.getBean(ErrorHandlerAction.class))
                .stateEntry(
                        IN_SLEEPING,
                        beanCtx.getBean(SleepAction.class),
                        beanCtx.getBean(ErrorHandlerAction.class))
                .stateEntry(
                        IN_TECHNICAL_WORK_SLEEPING,
                        beanCtx.getBean(TechnicalWorkSleepAction.class),
                        beanCtx.getBean(ErrorHandlerAction.class));
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
