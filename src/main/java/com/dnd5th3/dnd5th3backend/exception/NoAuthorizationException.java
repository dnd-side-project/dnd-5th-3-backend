package com.dnd5th3.dnd5th3backend.exception;

public class NoAuthorizationException extends RuntimeException{
    public NoAuthorizationException(String message) {
        super(message);
    }
}
