package com.blito.exceptions;

/**
 * @author Farzam Vatanzadeh
 * 12/3/17
 * Mailto : farzam.vat@gmail.com
 **/

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
