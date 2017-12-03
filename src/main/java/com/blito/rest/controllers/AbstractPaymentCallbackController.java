package com.blito.rest.controllers;

import com.blito.enums.Response;
import com.blito.exceptions.ResourceNotFoundException;
import com.blito.repositories.BlitRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.payments.BlitoPaymentResult;
import com.blito.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.RedirectView;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

/**
 * @author Farzam Vatanzadeh
 * 12/3/17
 * Mailto : farzam.vat@gmail.com
 **/

public class AbstractPaymentCallbackController {
    @Value("${serverAddress}")
    private String serverAddress;
    @Value("${api.base.url}")
    private String baseUrl;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    BlitRepository blitRepository;

    private Logger log = LoggerFactory.getLogger(getClass());
    public CompletionStage<RedirectView> completePayment(String token,Supplier<BlitoPaymentResult> supplier) {
        return CompletableFuture.supplyAsync(() -> paymentService.finalizingPayment(supplier.get()))
                .handle((blit,throwable) -> {
                if(throwable != null) {
                    log.error("******* ERROR IN PAYMENT FLOW '{}'",throwable.getCause());
                    return blitRepository.findByToken(token)
                            .map(b -> {
                                return new RedirectView(String.valueOf(new StringBuilder(serverAddress).append("/payment/").append(b.getTrackCode())));
                            }).orElseThrow(() -> new ResourceNotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
                }
                return new RedirectView(String.valueOf(new StringBuilder(serverAddress).append("/payment/").append(blit.getTrackCode())));
        });
    }
}
