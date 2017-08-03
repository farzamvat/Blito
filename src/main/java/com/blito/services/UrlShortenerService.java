package com.blito.services;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.blito.rest.viewmodels.google.shortener.UrlShortenerRequestViewModel;
import com.blito.rest.viewmodels.google.shortener.UrlShortenerResponseViewModel;

@Service
public class UrlShortenerService {
	@Value("${google.url.shortener.api.key}")
	private String googleUrlShortenerApiKey;
	private RestTemplate rest = new RestTemplate();
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public CompletableFuture<UrlShortenerResponseViewModel> generateShortenUrl(String url)
	{
		return CompletableFuture.supplyAsync(() -> {
			UrlShortenerRequestViewModel requestViewModel = new UrlShortenerRequestViewModel(url);
			return rest.postForEntity("https://www.googleapis.com/urlshortener/v1/url?key="+googleUrlShortenerApiKey, requestViewModel , UrlShortenerResponseViewModel.class);
		}).handle((res,throwable) -> {
			if(throwable != null)
				log.error("Error in shortening blit pdf url '{}'",throwable);
			UrlShortenerResponseViewModel response = res.getBody();
			log.info("Response from google url shortener '{}'",response);
			return response;
		});
	}

}
