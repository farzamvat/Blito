package com.blito.rest.viewmodels.payments;

import com.blito.enums.BankGateway;

public class ZarinpalPayRequestResponseViewModel extends PaymentRequestViewModel {
	private String zarinpalWebGatewayURL;

	public ZarinpalPayRequestResponseViewModel() {
	}

	public ZarinpalPayRequestResponseViewModel(String zarinpalWebGatewayURL) {
		this.zarinpalWebGatewayURL = zarinpalWebGatewayURL;
		setGateway(BankGateway.ZARINPAL);
	}

	public String getZarinpalWebGatewayURL() {
		return zarinpalWebGatewayURL;
	}

	public void setZarinpalWebGatewayURL(String zarinpalWebGatewayURL) {
		this.zarinpalWebGatewayURL = zarinpalWebGatewayURL;
	}
	
}
