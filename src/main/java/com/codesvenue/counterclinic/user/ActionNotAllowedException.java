package com.codesvenue.counterclinic.user;

public class ActionNotAllowedException extends RuntimeException {
    public ActionNotAllowedException(String msg) {
        super(msg);
    }
}
