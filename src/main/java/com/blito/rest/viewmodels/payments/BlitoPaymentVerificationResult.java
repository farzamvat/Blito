package com.blito.rest.viewmodels.payments;

import com.blito.enums.PayResult;
import com.blito.payments.payir.viewmodel.response.PayDotIrVerificationResponse;
import com.blito.payments.zarinpal.PaymentVerificationResponse;

/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/

public class BlitoPaymentVerificationResult {
    private PayResult result;
    private String refNum;
    private Integer amount;

    public BlitoPaymentVerificationResult(PayResult payResult, Integer amount) {
        this.result = payResult;
        this.amount = amount;
    }

    public BlitoPaymentVerificationResult(PayResult payResult, long refID) {
        this.result = payResult;
        this.refNum = String.valueOf(refID);
    }


    public static BlitoPaymentVerificationResult transformPayDotIrVerificationResponse(PayDotIrVerificationResponse response) {
        return new BlitoPaymentVerificationResult(
                response.getStatus() == 1 ?
                        PayResult.SUCCESS :
                        PayResult.FAILURE,
                response.getAmount());
    }

    public static BlitoPaymentVerificationResult transformZarinpalVerificationResponse(PaymentVerificationResponse response) {
        return new BlitoPaymentVerificationResult(
                response.getStatus() == 100 ?
                        PayResult.SUCCESS :
                        PayResult.FAILURE,
                response.getRefID());
    }

    public PayResult getResult() {
        return result;
    }

    public void setResult(PayResult result) {
        this.result = result;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
