package com.codesvenue.counterclinic.user.model;

public class ActionNotAllowedException extends RuntimeException {
    public ActionNotAllowedException(String msg) {
        super(msg);
    }
}
