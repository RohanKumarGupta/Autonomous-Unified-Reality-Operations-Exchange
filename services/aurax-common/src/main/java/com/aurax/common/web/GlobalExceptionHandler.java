package com.aurax.common.web;

import com.aurax.common.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getStatus().value(),
                Collections.emptyList(),
                request.getRequestURI(),
                Instant.now(),
                getCorrelationId()
        );
        return new ResponseEntity<>(ApiResponse.error(apiError), ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> details = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            details.add(fieldName + ": " + errorMessage);
        });

        ApiError apiError = new ApiError(
                "VALIDATION_FAILED",
                "Validation failed for one or more fields",
                HttpStatus.BAD_REQUEST.value(),
                details,
                request.getRequestURI(),
                Instant.now(),
                getCorrelationId()
        );
        return new ResponseEntity<>(ApiResponse.error(apiError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        List<String> details = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();

        ApiError apiError = new ApiError(
                "CONSTRAINT_VIOLATION",
                "Constraint violation",
                HttpStatus.BAD_REQUEST.value(),
                details,
                request.getRequestURI(),
                Instant.now(),
                getCorrelationId()
        );
        return new ResponseEntity<>(ApiResponse.error(apiError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                "MALFORMED_JSON_REQUEST",
                "Malformed JSON request",
                HttpStatus.BAD_REQUEST.value(),
                Collections.emptyList(),
                request.getRequestURI(),
                Instant.now(),
                getCorrelationId()
        );
        return new ResponseEntity<>(ApiResponse.error(apiError), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                "ACCESS_DENIED",
                "Access is denied",
                HttpStatus.FORBIDDEN.value(),
                Collections.emptyList(),
                request.getRequestURI(),
                Instant.now(),
                getCorrelationId()
        );
        return new ResponseEntity<>(ApiResponse.error(apiError), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Collections.singletonList(ex.getMessage()),
                request.getRequestURI(),
                Instant.now(),
                getCorrelationId()
        );
        return new ResponseEntity<>(ApiResponse.error(apiError), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getCorrelationId() {
        String correlationId = MDC.get("correlationId");
        return correlationId != null ? correlationId : UUID.randomUUID().toString();
    }
}
