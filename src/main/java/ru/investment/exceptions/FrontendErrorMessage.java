package ru.investment.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FrontendErrorMessage {
    private Date timestamp;
    private String errorCode;
    private String errorCause;
}
