package com.codesvenue.counterclinic.user;

public class FileUploadFailedException extends RuntimeException {
    public FileUploadFailedException(String errorMessage) {
        super(errorMessage);
    }
}
