package com.blito.exceptions;

/**
 * @author Farzam Vatanzadeh
 * 12/12/17
 * Mailto : farzam.vat@gmail.com
 **/

public class AlreadyPaidException extends RuntimeException {
    public AlreadyPaidException(String message) {
        super(message);
    }
}
