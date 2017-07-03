package com.blito.services;


import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.blito.payments.zarinpal.PaymentRequest;
import com.blito.payments.zarinpal.PaymentRequestResponse;


public class ZarinpalClient extends WebServiceGatewaySupport {
	
	private String zarinpalURL;
	
	public ZarinpalClient(String zarinpalURL)
	{
		this.zarinpalURL = zarinpalURL;
	}
	
	public PaymentRequestResponse getPaymentRequest(String merchantID,int amount,String email,String mobile,String callbackURL)
	{
		PaymentRequest request = new PaymentRequest();
		request.setMerchantID(merchantID);
		request.setCallbackURL(callbackURL);
		request.setAmount(amount);
		request.setCallbackURL(callbackURL);
		
		PaymentRequestResponse response = 
				(PaymentRequestResponse) getWebServiceTemplate().marshalSendAndReceive(zarinpalURL,request);
		return response;
	}
	
}
