package com.elasticsearch.springdata.controller.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.elasticsearch.springdata.service.exception.BookNotFoundException;
import com.elasticsearch.springdata.service.exception.DuplicateIsbnException;


@RestControllerAdvice
public class BookControllerExceptionHandler {

    @ExceptionHandler(value = {BookNotFoundException.class, DuplicateIsbnException.class})
    public ResponseEntity<Body> doHandleBookExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Body(ex.getMessage()));
    }

    public static class Body {

        private String message;

        public Body(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}