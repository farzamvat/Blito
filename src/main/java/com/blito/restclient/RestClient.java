package com.blito.restclient;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.blito.enums.Response;
import com.blito.exceptions.InternalServerException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.UnauthorizedException;
import com.blito.resourceUtil.ResourceUtil;

public class RestClient<T, U> extends AbstractRestClient<T, U> {

	public RestClient() {
		super.rest = new RestTemplate();
		super.rest.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		super.headers = new HttpHeaders();
	}

	@Override
	public ResponseEntity<U> exchange() {
		try {
			return rest.exchange(new RequestEntity<T>(super.body.getBody(), super.headers, super.method, super.uri),
					super.responseType);
		} catch (HttpClientErrorException exception) {
			System.out.println(exception.getResponseBodyAsString());
			switch (exception.getStatusCode()) {
			case UNAUTHORIZED:
				throw new UnauthorizedException(ResourceUtil.getMessage(Response.ACCESS_DENIED));
			case NOT_FOUND:
				throw new NotFoundException("Data not found");
			case BAD_REQUEST:
				throw new RuntimeException("bad request");
			default:
				throw new RuntimeException("Internal Server Error");
			}
		} catch (HttpServerErrorException exception) {
			throw new InternalServerException(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR));
		}

	}

}
