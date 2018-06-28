package com.blito.configs;

import com.blito.payments.jibit.JibitClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JibitConfiguration {
    @Value("${jibit.merchant.id}")
    private String merchantId;
    @Value("${jibit.payment.url}")
    private String paymentUrl;
    @Value("${jibit.payment.verification.url}")
    private String paymentVerificationUrl;
    @Value("${serverAddress}" + "${api.base.url}" + "/jibit-payment-callback")
    private String redirectUrl;

    @Autowired
    @Bean
    public JibitClient jibitClient(ObjectMapper objectMapper) {
        return new JibitClient(merchantId,paymentUrl,paymentVerificationUrl,redirectUrl,objectMapper);
    }
}
