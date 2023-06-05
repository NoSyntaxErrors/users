package com.factory.users.model.exception;


public class UnauthorizedAccessException extends RuntimeException {

    private static final long serialVersionUID = 1700749936682546043L;

    public UnauthorizedAccessException(String message) {
        super(message, new Throwable());
    }

}
