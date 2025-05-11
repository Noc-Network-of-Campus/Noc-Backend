package com.mycompany.myapp.web.controller.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리를 담당하는 공통 컨트롤러
 * - 유효성 검증 오류, 일반 예외 등 일관된 포맷으로 응답
 */
@RestControllerAdvice
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @Valid @ModelAttribute 검증 실패 처리
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity handleBindException(BindException e) {
        return handleValidationErrors(e.getFieldErrors());
    }

    /**
     * @Valid @RequestBody 검증 실패 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return handleValidationErrors(e.getBindingResult().getFieldErrors());
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        return handleApiException(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 공통 예외 응답 포맷
     */
    protected ResponseEntity handleApiException(Exception e, HttpStatus status) {
        Map<String, String> res = new HashMap<>();
        res.put("statusCode", "error");
        res.put("responseMessage", e.getMessage());
        logger.warn("Unhandled Exception: {}", e.getMessage(), e);
        return new ResponseEntity<>(res, status);
    }

    /**
     * 유효성 검증 오류 공통 응답 포맷
     */
    private ResponseEntity handleValidationErrors(java.util.List<FieldError> fieldErrors) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> res = new HashMap<>();
        res.put("statusCode", "error");
        res.put("responseMessage", "유효성 검사 실패");
        res.put("errors", errors);

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
