package com.blito.payments.zarinpal.client;


import io.vavr.control.Try;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.blito.exceptions.ZarinpalException;
import com.blito.payments.zarinpal.PaymentRequest;
import com.blito.payments.zarinpal.PaymentRequestResponse;
import com.blito.payments.zarinpal.PaymentVerification;
import com.blito.payments.zarinpal.PaymentVerificationResponse;


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
				(PaymentRequestResponse) getWebServiceTemplate().marshalSendAndReceive(request);
		if(response.getStatus() != 100)
			throw new ZarinpalException(ZarinpalException.generateMessage(response.getStatus()));
		return response.getAuthority();
	}
	
	public Try<PaymentVerificationResponse> getPaymentVerificationResponse(int amount,String authority)
	{
		return Try.of(() -> {
			PaymentVerification paymentVerification = new PaymentVerification();
			paymentVerification.setAmount(amount);
			paymentVerification.setAuthority(authority);
			paymentVerification.setMerchantID(this.zarinpalMerchantId);
			PaymentVerificationResponse verificationResponse =
					(PaymentVerificationResponse) getWebServiceTemplate().marshalSendAndReceive(paymentVerification);
			if(verificationResponse.getStatus() != 100)
				throw new ZarinpalException(ZarinpalException.generateMessage(verificationResponse.getStatus()));
			return verificationResponse;
		});
	}
	
}
