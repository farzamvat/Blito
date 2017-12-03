package com.blito.configs;

import com.blito.payments.payir.viewmodel.PayDotIrClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/
@Configuration
public class PayDotIrConfiguration {
    @Value("${pay.ir.payment.url}")
    private String paymentUrl;
    @Value("${pay.ir.payment.verification.url}")
    private String paymentVerificationUrl;
    @Value("${pay.ir.api.key}")
    private String apiKey;
    @Value("${serverAddress}" + "${api.base.url}" + "/pay-payment-callback")
    private String redirectUrl;

    @Profile({"dev","live","local"})
    @Autowired
    @Bean
    public PayDotIrClient payDotIrClient(ObjectMapper objectMapper) {
        return new PayDotIrClient(objectMapper,apiKey,paymentUrl,paymentVerificationUrl,redirectUrl);
    }

    @Profile("test")
    @Autowired
    @Bean
    public PayDotIrClient payDotIrClientTest(ObjectMapper objectMapper) {
        return new PayDotIrClient(objectMapper,apiKey,paymentUrl,paymentVerificationUrl,"www.blito.ir");
    }
}
