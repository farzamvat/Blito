package com.blito.requestbuilder;


import com.blito.enums.Response;
import com.blito.resourceUtil.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/

public abstract class AbstractRequestBuilder<T, U> {

    protected RestTemplate restTemplate;
    protected HttpHeaders headers;
    protected T body;
    protected HttpMethod method;
    protected URI uri;
    protected Class<U> responseType;
    private Logger log = LoggerFactory.getLogger(getClass());

    public AbstractRequestBuilder() {
        restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        restTemplate.getMessageConverters().add(0, stringHttpMessageConverter);
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }


    public AbstractRequestBuilder<T, U> addHeader(String key, String value) {
        this.headers.add(key, value);
        return this;
    }

    public AbstractRequestBuilder<T, U> setBody(T body) {
        this.body = body;
        return this;
    }



    public AbstractRequestBuilder<T, U> setUrl(String url)  {
        try{
            uri = new URI(url);
            return this;
        }catch (Exception e) {
            log.error("error in url of the service : {}",e);
            throw new RuntimeException(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR));
        }
    }

    public AbstractRequestBuilder<T, U> setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public AbstractRequestBuilder<T, U> setResponseType(Class<U> type) {
        responseType = type;
        return this;
    }

    public AbstractRequestBuilder<T, U> removeHeader(String key) {
        this.headers.remove(key);
        return this;
    }

    public abstract ResponseEntity<U> send() ;

}

