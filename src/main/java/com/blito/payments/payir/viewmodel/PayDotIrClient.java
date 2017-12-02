package com.blito.payments.payir.viewmodel;

import com.blito.enums.Response;
import com.blito.exceptions.PayDotIrException;
import com.blito.payments.payir.viewmodel.request.PayDotIrPurchaseRequest;
import com.blito.payments.payir.viewmodel.request.PayDotIrVerificationRequest;
import com.blito.payments.payir.viewmodel.response.PayDotIrResponse;
import com.blito.requestbuilder.RequestBuilder;
import com.blito.resourceUtil.ResourceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/

public class PayDotIrClient {
    private Logger log = LoggerFactory.getLogger(getClass());
    private final String apiKey;
    private final String paymentRequestUrl;
    private final String paymentVerificationUrl;
    private final RestTemplate restTemplate;
    private final String redirectUrl;
    private final ObjectMapper objectMapper;

    public PayDotIrClient() {
        apiKey = null;
        paymentVerificationUrl = null;
        paymentRequestUrl = null;
        restTemplate = null;
        redirectUrl = null;
        objectMapper = null;
    }

    public PayDotIrClient(ObjectMapper objectMapper,String apiKey,String paymentRequestUrl,String paymentVerificationUrl,String redirectUrl) {
        this.apiKey = apiKey;
        this.paymentRequestUrl = paymentRequestUrl;
        this.paymentVerificationUrl = paymentVerificationUrl;
        this.restTemplate = new RestTemplate();
        this.redirectUrl = redirectUrl;
        this.objectMapper = objectMapper;
    }

    public Try<PayDotIrResponse> createPaymentRequest(Integer amount,String mobile,String factorNumber) {
        PayDotIrPurchaseRequest purchaseRequest = new PayDotIrPurchaseRequest(amount,redirectUrl,factorNumber,mobile,apiKey);
        return responseExtractor(Try.ofSupplier(() ->
                restTemplate.postForEntity(paymentRequestUrl,purchaseRequest,String.class)
            ));
    }

    private Try<PayDotIrResponse> responseExtractor(Try<ResponseEntity<String>> httpResponse) {
        return httpResponse.mapTry(response -> {
            if(JsonPath.parse(response.getBody()).read("$.status").equals(1)) {
                return objectMapper.readValue(response.getBody(), PayDotIrResponse.class);
            }
            throw new PayDotIrException(ResourceUtil.getMessage(Response.PAY_DOT_IR_ERROR));
        }).onFailure(throwable -> {
            log.debug("pay.ir error '{}'", throwable.getMessage());
            throw new PayDotIrException(ResourceUtil.getMessage(Response.PAY_DOT_IR_ERROR));
        });
    }

    public Try<PayDotIrResponse> verifyPaymentRequest(Integer transId) {
        PayDotIrVerificationRequest verificationRequest = new PayDotIrVerificationRequest(transId,apiKey);
        return responseExtractor(Try.ofSupplier(() ->
                new RequestBuilder<PayDotIrVerificationRequest,String>()
                        .setMethod(HttpMethod.POST)
                        .addHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .setUrl(paymentVerificationUrl)
                        .setBody(verificationRequest)
                        .setResponseType(String.class)
                        .send()
        ));
    }
}
