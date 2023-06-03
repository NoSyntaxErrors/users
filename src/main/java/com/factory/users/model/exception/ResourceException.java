package com.factory.users.model.exception;


public class ResourceException extends RuntimeException {

    private static final long serialVersionUID = -2152255233356750751L;

    public ResourceException() {
        super("User(email) is already registered", new Throwable());
    }
}
