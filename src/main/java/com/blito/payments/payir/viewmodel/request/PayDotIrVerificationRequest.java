package com.blito.payments.payir.viewmodel.request;

/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/

public class PayDotIrVerificationRequest extends AbstractPayDotIrRequest {
    private Integer transId;

    public PayDotIrVerificationRequest(Integer transId,String api) {
        this.transId = transId;
        this.setApi(api);
    }

    public Integer getTransId() {
        return transId;
    }

    public void setTransId(Integer transId) {
        this.transId = transId;
    }
}
