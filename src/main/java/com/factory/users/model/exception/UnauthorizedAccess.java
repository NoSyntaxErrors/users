package com.factory.users.model.exception;


public class UnauthorizedAccess extends RuntimeException {

    private static final long serialVersionUID = 1700749936682546043L;

    public UnauthorizedAccess() {
        super("Not authorized", new Throwable());
    }

}
