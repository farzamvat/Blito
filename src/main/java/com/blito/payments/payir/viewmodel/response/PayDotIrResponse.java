package com.blito.payments.payir.viewmodel.response;

/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/

public class PayDotIrResponse extends AbstractPayDotIrResponse {
    private Integer transId;

    public Integer getTransId() {
        return transId;
    }

    public void setTransId(Integer transId) {
        this.transId = transId;
    }

    @Override
    public String toString() {
        return "PayDotIrResponse{" +
                "transId=" + transId +
                ", status=" + status +
                '}';
    }
}
