package com.blito.endpoints;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.blito.payments.saman.webservice.producer.FinalizePaymentRequest;
import com.blito.payments.saman.webservice.producer.FinalizePaymentResponse;


@Endpoint
public class SamanEndpoint {
	
	private static final String NAMESPACE_URI = "http://localhost:8085/services";
	
	@PayloadRoot(namespace=NAMESPACE_URI,localPart="finalizePaymentRequest")
	@ResponsePayload
	public FinalizePaymentResponse fromSamanBank(@RequestPayload FinalizePaymentRequest request)
	{
		FinalizePaymentResponse response = new FinalizePaymentResponse();
		response.setName("Naaaame");
		return response;
	}
}
