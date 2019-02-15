package com.app.exceptions;

public class MyException extends RuntimeException {
    public ExceptionInfo exceptionInfo;

    public MyException(String exceptionMessage) {
        this.exceptionInfo = new ExceptionInfo(exceptionMessage);
    }

    public ExceptionInfo getExceptionInfo() {
        return exceptionInfo;
    }
}
