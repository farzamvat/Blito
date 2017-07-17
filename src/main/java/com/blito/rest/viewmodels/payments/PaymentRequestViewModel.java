package com.blito.rest.viewmodels.payments;

import com.blito.enums.BankGateway;

public class PaymentRequestViewModel {
	private BankGateway gateway;

	public BankGateway getGateway() {
		return gateway;
	}

	public void setGateway(BankGateway gateway) {
		this.gateway = gateway;
	}
	
}
