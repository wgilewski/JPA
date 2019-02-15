package com.app.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionInfo {
    private String exceptionMessage;
    private LocalDateTime exceptionDateTime;

    public ExceptionInfo(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
        this.exceptionDateTime = LocalDateTime.now();
    }
}
