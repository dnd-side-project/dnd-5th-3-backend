package com.dnd5th3.dnd5th3backend.exception;

public class DuplicateVoteException extends RuntimeException{
    public DuplicateVoteException(String message) {
        super(message);
    }
}
