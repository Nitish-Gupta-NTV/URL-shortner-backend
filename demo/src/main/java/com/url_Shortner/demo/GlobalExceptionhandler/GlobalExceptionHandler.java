package com.url_Shortner.demo.GlobalExceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernotexception(UsernameNotFoundException e)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("usernotfoundreach the globalexceptionhandler "+e.getMessage());
    }

}

