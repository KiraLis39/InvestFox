package ru.investment.parser.statemashine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import ru.investment.parser.enums.ParserEvents;
import ru.investment.parser.enums.ParserStates;

@Slf4j
public class PurchaseItemStateMachineApplicationListener implements StateMachineListener<ParserStates, ParserEvents> {

    @Override
    public void stateEntered(final State<ParserStates, ParserEvents> state) {
        if (state != null) {
            log.debug("stateEntered: " + state.getId());
        } else {
            log.debug("State is null");
        }
    }

    @Override
    public void stateExited(final State<ParserStates, ParserEvents> state) {
        log.trace("stateExited " + state.getId());
    }

    @Override
    public void eventNotAccepted(final Message<ParserEvents> event) {
        log.warn("Евент не принят " + event);
    }

    @Override
    public void transition(final Transition<ParserStates, ParserEvents> transition) {
    }

    @Override
    public void transitionStarted(final Transition<ParserStates, ParserEvents> transition) {
        log.debug("Machine transition started: " + transition);
    }

    @Override
    public void transitionEnded(final Transition<ParserStates, ParserEvents> transition) {
        log.debug("Machine transition ended: " + transition);
    }

    @Override
    public void stateMachineStarted(final StateMachine<ParserStates, ParserEvents> stateMachine) {
        log.debug("Machine started");
    }

    @Override
    public void stateMachineStopped(final StateMachine<ParserStates, ParserEvents> stateMachine) {
        log.debug("Machine stopped");
    }

    @Override
    public void stateMachineError(final StateMachine<ParserStates, ParserEvents> stateMachine, Exception exception) {
        log.debug("Machine error: " + exception.getMessage(), exception);
    }

    @Override
    public void stateChanged(final State<ParserStates, ParserEvents> from, final State<ParserStates, ParserEvents> to) {
        if (from != null && from.getId() != null) {
            log.debug("Переход из статуса " + from.getId() + " в статус " + to.getId());
        }
    }

    @Override
    public void extendedStateChanged(final Object key, final Object value) {
        log.debug("Machine change state to " + key + ":" + value);
    }

    @Override
    public void stateContext(final StateContext<ParserStates, ParserEvents> stateContext) {
        log.trace("Stage: {}", stateContext.getStage());
    }
}
