package com.blito.rest.viewmodels.payments;

import com.blito.enums.PayResult;

/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/

public class BlitoPaymentResult {
    private String token;
    private PayResult result;
    private String message;

    public BlitoPaymentResult(String token, PayResult result, String message) {
        this.token = token;
        this.result = result;
        this.message = message;
    }

    public static BlitoPaymentResult transformZarinpal(String authority, String status) {
        return new BlitoPaymentResult(authority, status.equals("OK") ? PayResult.SUCCESS : PayResult.FAILURE );
    }

    public static BlitoPaymentResult transformPayDotIr(Integer status,Integer transId,String message) {
        return new BlitoPaymentResult(String.valueOf(transId), status == 1 ? PayResult.SUCCESS : PayResult.FAILURE , message);
    }

    public BlitoPaymentResult(String token, PayResult result) {
        this.token = token;
        this.result = result;
    }

    public BlitoPaymentResult(String token, PayResult result, String message, String refNum) {
        this.token = token;
        this.result = result;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public PayResult getResult() {
        return result;
    }

    public void setResult(PayResult result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
