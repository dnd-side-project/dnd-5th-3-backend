package com.dnd5th3.dnd5th3backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN("C000","오류가 발생했습니다."),
    INVALID_REQUEST("C001","요청에 오류가 있습니다."),
    SERVER_ERROR("C002","서버에서 오류가 발생했습니다."),
    ACCESS_DENIED("C003","권한이 없습니다.");

    private final String code;
    private final String message;
}

