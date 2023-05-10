package com.example.anonymousboard.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final BindingResult bindingResult) {
        String defaultMessage = bindingResult.getFieldErrors()
                .get(0)
                .getDefaultMessage();
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .errorCode(CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value())
                .message(defaultMessage)
                .build());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .message(e.getMessage())
                .build()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.builder()
                .message("서버에서 예상치 못한 오류가 발생했습니다.")
                .errorCode(CommonErrorCode.RUNTIME.value())
                .build());
    }
}
