package com.blito.restclient;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public abstract class AbstractRestClient <T,U> {
	protected RestTemplate rest;
	protected HttpHeaders headers;
	protected HttpEntity<T> body;
	protected HttpMethod method;
	protected URI uri;
	protected Class<U> responseType;
	
	public AbstractRestClient<T,U> addHeader(String key,String value)
	{
		this.headers.add(key, value);
		return this;
	}
	
	public AbstractRestClient<T,U> setBody(T body)
	{
		this.body = new HttpEntity<T>(body);
		return this;
	}
	
	public AbstractRestClient<T,U> setUrl(String url)
	{
		try {
			this.uri = new URI(url);
			return this;
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	public AbstractRestClient<T,U> setMethod(HttpMethod method)
	{
		this.method = method;
		return this;
	}
	
	public AbstractRestClient<T,U> setResponseType(Class<U> type)
	{
		this.responseType = type;
		return this;
	}
	
	public AbstractRestClient<T,U> removeHeader(String key)
	{
		this.headers.remove(key);
		return this;
	}
	public abstract ResponseEntity<U> exchange();
}
