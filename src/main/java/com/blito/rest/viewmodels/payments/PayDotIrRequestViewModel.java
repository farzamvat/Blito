package com.blito.rest.viewmodels.payments;

import com.blito.enums.BankGateway;

/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/

public class PayDotIrRequestViewModel extends PaymentRequestViewModel {
    private String payDotIrWebGatewayURL;

    public PayDotIrRequestViewModel() {
    }

    public PayDotIrRequestViewModel(String payDotIrWebGatewayURL) {
        this.payDotIrWebGatewayURL = payDotIrWebGatewayURL;
        setGateway(BankGateway.PAYDOTIR);
    }

    public String getPayDotIrWebGatewayURL() {
        return payDotIrWebGatewayURL;
    }

    public void setPayDotIrWebGatewayURL(String payDotIrWebGatewayURL) {
        this.payDotIrWebGatewayURL = payDotIrWebGatewayURL;
    }
}
