package com.url_Shortner.demo.GlobalExceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernotexception(UsernameNotFoundException e)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("usernotfoundreach the globalexceptionhandler "+e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        String error = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");
        return ResponseEntity.badRequest()
                .body(Map.of("error", error));
    }


        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }



