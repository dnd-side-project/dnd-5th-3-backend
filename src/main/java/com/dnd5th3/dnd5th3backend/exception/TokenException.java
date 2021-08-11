package com.dnd5th3.dnd5th3backend.exception;

public class TokenException extends RuntimeException {
    public TokenException() {
        super("만료 되었거나 유효하지 않는 토큰입니다.");
    }
}
