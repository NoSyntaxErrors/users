package com.factory.users.model.exception;


public class ResourceException extends RuntimeException {

    private static final long serialVersionUID = -2152255233356750751L;

    public ResourceException(String message) {
        super(message, new Throwable());
    }
}
