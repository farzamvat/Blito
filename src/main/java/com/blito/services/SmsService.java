package com.blito.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService {

	@Value("${kaveh.negar.api.key}")
	String apiKey;

	RestTemplate rest = new RestTemplate();

	private final Logger log = LoggerFactory.getLogger(SmsService.class);
	
	public void sendBlitRecieptSms(String receptor, String token, String token2, String token3) {
		ResponseEntity<String> response = rest.getForEntity("https://api.kavenegar.com/v1/" + apiKey
				+ "/verify/lookup.json?receptor=" + receptor + "&token=" + token + "6&template=" + "BlitoTrackCode",
				String.class);
		
		log.debug(response.getBody());

	}
}
