package com.blito.exceptions;
/*
    @author Farzam Vatanzadeh
*/

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super(message);
    }
}
