package ru.investment.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetConnectException extends RuntimeException {

    public NetConnectException(String message, Throwable cause) {
        super(message, cause);
        log.error(message);
    }
}
