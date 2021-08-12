package com.dnd5th3.dnd5th3backend.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class,TokenException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto serverError(Exception e){
        log.error("{}",e.getClass(),e);
        return ErrorResponseDto.of(ErrorCode.INVALID_REQUEST,e);
    }

    @Builder
    @Getter
    private static class ErrorResponseDto{
        private String message;
        private String code;
        private String reason;

        public static ErrorResponseDto of(ErrorCode errorCode,Exception e){
            return ErrorResponseDto
                    .builder()
                    .message(errorCode.getMessage())
                    .code(errorCode.getCode())
                    .reason(e.getMessage())
                    .build();
        }
    }
}
