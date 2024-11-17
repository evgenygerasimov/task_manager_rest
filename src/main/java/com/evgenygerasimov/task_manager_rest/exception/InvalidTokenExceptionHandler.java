package com.evgenygerasimov.task_manager_rest.exception;

public class InvalidTokenExceptionHandler extends RuntimeException {
    public InvalidTokenExceptionHandler(String message) {
        super(message);
    }
}
