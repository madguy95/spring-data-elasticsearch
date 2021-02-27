package com.elasticsearch.springdata.service.exception;

public class DuplicateIsbnException extends Exception {

    public DuplicateIsbnException(String message) {
        super(message);
    }
}
