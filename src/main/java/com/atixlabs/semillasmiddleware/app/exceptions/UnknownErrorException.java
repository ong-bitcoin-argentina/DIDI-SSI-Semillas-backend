package com.atixlabs.semillasmiddleware.app.exceptions;

public class UnknownErrorException extends RuntimeException{

    public UnknownErrorException(String msg){ super(msg);}
    public UnknownErrorException(String msg, Throwable t){ super(msg, t);}
}
