package ru.investment.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.investment.utils.ExceptionsUtil;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {
    private static final String HANDLER_EXCEPTION_PREFIX = "Events service Exception: {}";

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {
        String errorMessage = ExceptionsUtil.getFullExceptionMessage(ex) != null
                ? ": " + ExceptionsUtil.getFullExceptionMessage(ex) : "";
        log.warn(HANDLER_EXCEPTION_PREFIX, errorMessage.concat(". Вызвано при: ")
                .concat(request != null ? request.getContextPath() : "NA"));
        return new ResponseEntity<>(
                new FrontendErrorMessage(new Date(), "E000", errorMessage),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {GlobalServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(GlobalServiceException ex, WebRequest request) {
        log.warn(HANDLER_EXCEPTION_PREFIX, ExceptionsUtil.getFullExceptionMessage(ex).concat(". Вызвано при: ")
                .concat(request != null ? request.getContextPath() : "NA"));
        return new ResponseEntity<>(
                new FrontendErrorMessage(new Date(), ex.getErrorCode(), ExceptionsUtil.getFullExceptionMessage(ex)),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }
}
