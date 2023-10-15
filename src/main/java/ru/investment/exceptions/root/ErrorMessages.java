package ru.investment.exceptions.root;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {
    UNIVERSAL_ERROR_MESSAGE_TEMPLATE("E0XX", "Ошибка: "),
    PARTICIPANT_IS_ABSENT_ERROR("E001", "Пользователь не найден: "),
    NOT_ENOUGH_DATA("E002", "Не достаточно данных: "),
    SYNC_ERROR("E003", "Ошибка синхронизации между сервисами"),
    BROWSER_CLOSED("E004", "Браузер закрыт"),
    TOKEN_PARSE_ERROR("E005", "Ошибка парсинга токена");

    private final String errorCode;
    private final String errorCause;
}
