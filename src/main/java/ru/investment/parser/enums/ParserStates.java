package ru.investment.parser.enums;

public enum ParserStates {
    IN_INIT,
    IN_RUNNING,

    IN_SETTING_UP,
    IN_LOADING_DATA,
    IN_PARSING,
    IN_ERROR_STATE,
    IN_STOPPED_BY_ERRORS,
    IN_SLEEPING,
    IN_TECHNICAL_WORK_SLEEPING,
    IN_STOPPED_BY_USER,
    IN_DEAD,
    IN_CUSTOM_END
}
