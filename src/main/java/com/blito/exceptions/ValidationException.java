package com.blito.exceptions;

/**
 * @author Farzam Vatanzadeh
 * 11/19/17
 * Mailto : farzam.vat@gmail.com
 **/

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
