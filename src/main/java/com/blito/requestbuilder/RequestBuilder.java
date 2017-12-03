package com.blito.requestbuilder;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/

public class RequestBuilder<T,U> extends AbstractRequestBuilder<T,U> {
    public RequestBuilder() {
        super();
    }

    @Override
    public ResponseEntity<U> send() {
        return restTemplate.exchange(new RequestEntity<T>(body, headers, method, uri), responseType);
    }
}
