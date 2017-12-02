package com.blito.rest.controllers;

import com.blito.payments.payir.viewmodel.request.PayDotIrCallbackRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletionStage;

/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/
@RestController
@RequestMapping("${api.base.url}")
public class PayDotIrController {
    private Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/pay-ir/callback")
    public CompletionStage<ResponseEntity<?>> payDotIrCallback(@RequestBody PayDotIrCallbackRequest payload) {
        return null;
    }
}
