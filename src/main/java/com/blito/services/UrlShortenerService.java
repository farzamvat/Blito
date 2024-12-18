package com.blito.services;

import com.blito.rest.viewmodels.google.shortener.UrlShortenerRequestViewModel;
import com.blito.rest.viewmodels.google.shortener.UrlShortenerResponseViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class UrlShortenerService {
	@Value("${google.url.shortener.api.key}")
	private String googleUrlShortenerApiKey;
	@Value("${bit.ly.url.shortener.access.token}")
	private String bitlyUrlShortenerApiKey;
	private RestTemplate rest = new RestTemplate();
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public UrlShortenerResponseViewModel generateShortenUrl(String url)
	{
		UrlShortenerRequestViewModel requestViewModel = new UrlShortenerRequestViewModel(url);
		ResponseEntity<UrlShortenerResponseViewModel> response =
			rest.postForEntity("https://www.googleapis.com/urlshortener/v1/url?key="+googleUrlShortenerApiKey, requestViewModel , UrlShortenerResponseViewModel.class);
		log.info("Response from google url shortener '{}'",response);
		return response.getBody();
	}

	public UrlShortenerResponseViewModel generateBitlyShortenedUrl(String url) throws UnsupportedEncodingException {
		ResponseEntity<UrlShortenerResponseViewModel> response =
				rest.getForEntity("https://api-ssl.bitly.com/v3/shorten?access_token=" + bitlyUrlShortenerApiKey + "&longUrl=" + url,UrlShortenerResponseViewModel.class);
		return response.getBody();
	}
}
