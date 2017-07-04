package com.blito.payments.zarinpal.client;


import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.blito.exceptions.ZarinpalException;
import com.blito.payments.zarinpal.PaymentRequest;
import com.blito.payments.zarinpal.PaymentRequestResponse;


public class ZarinpalClient extends WebServiceGatewaySupport {
	
	private String zarinpalCallbackURL;
	private String zarinpalMerchantId;
	
	public ZarinpalClient(String zarinpalCallbackURL,String zarinpalMerchantId)
	{
		this.zarinpalCallbackURL = zarinpalCallbackURL;
		this.zarinpalMerchantId = zarinpalMerchantId;
	}
	
	public String getPaymentRequest(int amount,String email,String mobile,String description)
	{
		PaymentRequest request = new PaymentRequest();
		request.setMerchantID(this.zarinpalMerchantId);
		request.setCallbackURL(this.zarinpalCallbackURL);
		request.setAmount(amount);
		request.setDescription(description);
		PaymentRequestResponse response = 
				(PaymentRequestResponse) getWebServiceTemplate().marshalSendAndReceive(zarinpalCallbackURL,request);
		if(response.getStatus() != 100)
			throw new ZarinpalException(ZarinpalException.generateMessage(response.getStatus()));
		return response.getAuthority();
	}
	
	
	
}
