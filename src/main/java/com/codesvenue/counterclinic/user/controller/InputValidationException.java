package com.codesvenue.counterclinic.user.controller;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
public class InputValidationException extends RuntimeException {
    private final List<FieldError> fieldErrors;

    public InputValidationException(String msg, List<FieldError> fieldErrors) {
        super(msg);
        this.fieldErrors = fieldErrors;
    }
}
