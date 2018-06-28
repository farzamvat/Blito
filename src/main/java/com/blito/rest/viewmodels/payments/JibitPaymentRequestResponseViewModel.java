package com.blito.rest.viewmodels.payments;

import com.blito.enums.BankGateway;

public class JibitPaymentRequestResponseViewModel extends PaymentRequestViewModel {
    private String jibitWebGatewayURL;

    public JibitPaymentRequestResponseViewModel() {
    }

    public JibitPaymentRequestResponseViewModel(String jibitWebGatewayURL) {
        this.jibitWebGatewayURL = jibitWebGatewayURL;
        setGateway(BankGateway.JIBIT);
    }

    public String getJibitWebGatewayURL() {
        return jibitWebGatewayURL;
    }

    public void setJibitWebGatewayURL(String jibitWebGatewayURL) {
        this.jibitWebGatewayURL = jibitWebGatewayURL;
    }
}
