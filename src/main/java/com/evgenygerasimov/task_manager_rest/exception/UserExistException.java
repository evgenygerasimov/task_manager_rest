package com.evgenygerasimov.task_manager_rest.exception;

public class UserExistException extends  RuntimeException {
    public UserExistException(String message) {
        super(message);
    }
}
