package com.blito.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.blito.payments._76._143._201._138._80.services.FinalizePaymentRequest;
import com.blito.payments._76._143._201._138._80.services.FinalizePaymentResponse;
import com.blito.services.PaymentService;


@Endpoint
public class SamanEndpoint {
	
	private static final String NAMESPACE_URI = "http://localhost:8085/services";
	
	@Autowired
	private PaymentService paymentService;
	
	@PayloadRoot(namespace=NAMESPACE_URI,localPart="finalizePaymentRequest")
	@ResponsePayload
	public FinalizePaymentResponse fromSamanBank(@RequestPayload FinalizePaymentRequest request)
	{
		FinalizePaymentResponse response = new FinalizePaymentResponse();
//		paymentService.samanPaymentCallback(request);
		response.setName("Naaaame");
		return response;
	}
	
	
}
