package com.codesvenue.counterclinic.controller;

import org.springframework.validation.FieldError;

import java.util.List;

public class InputValidationException extends RuntimeException {
    private final List<FieldError> fieldErrors;

    public InputValidationException(String msg, List<FieldError> fieldErrors) {
        super(msg);
        this.fieldErrors = fieldErrors;
    }
}
