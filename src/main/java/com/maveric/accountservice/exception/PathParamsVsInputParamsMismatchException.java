package com.maveric.accountservice.exception;

public class PathParamsVsInputParamsMismatchException extends RuntimeException{
    public PathParamsVsInputParamsMismatchException(String message) {
        super(message);
    }
}
