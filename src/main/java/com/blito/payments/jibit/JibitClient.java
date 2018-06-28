package com.blito.payments.jibit;

import com.blito.enums.Response;
import com.blito.exceptions.JibitException;
import com.blito.resourceUtil.ResourceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class JibitClient {
    private Logger log = LoggerFactory.getLogger(getClass());
    private String merchantId;
    private String paymentUrl;
    private String paymentVerificationUrl;
    private ObjectMapper objectMapper;
    private RestTemplate restTemplate;
    private String callbackUrl;

    public JibitClient(String merchantId, String paymentUrl, String paymentVerificationUrl,String callbackUrl,ObjectMapper objectMapper) {
        this.merchantId = merchantId;
        this.paymentUrl = paymentUrl;
        this.paymentVerificationUrl = paymentVerificationUrl;
        this.callbackUrl = callbackUrl;
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    public Try<JibitPaymentResponse> createPaymentRequest(Long amount,String mobile) {
        JibitPaymentRequest request =
                new JibitPaymentRequest(amount,callbackUrl,Integer.valueOf(merchantId),mobile);
        return Try.of(() -> restTemplate.postForEntity(new URI(paymentUrl),request,JibitPaymentResponse.class))
                .map(ResponseEntity::getBody)
                .onFailure(throwable -> {
                    log.error("jibit error '{}'", throwable);
                    throw new JibitException(ResourceUtil.getMessage(Response.JIBIT_ERROR));
                });
    }

    public Try<String> verifyPaymentRequest(String orderId) {
        return Try.of(() -> restTemplate.postForEntity(paymentVerificationUrl+orderId,"",String.class))
                .map(ResponseEntity::getBody)
                .onFailure(throwable -> {
                    log.error("jibit error in payment verification '{}'",throwable);
                    throw new JibitException(ResourceUtil.getMessage(Response.JIBIT_ERROR));
                });
    }



}
