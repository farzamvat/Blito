package com.blito.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

	@Value("${kaveh.negar.api.key}")
	String apiKey;

	TestRestTemplate rest = new TestRestTemplate();

	public void sendValidationSms(String receptor, String token, String token2, String token3, String template) {
		ResponseEntity<String> response = rest.getForEntity("https://api.kavenegar.com/v1/" + apiKey
				+ "/verify/lookup.json?receptor=" + receptor + "&token=" + token + "6&template=" + template,
				String.class);
		

	}
}
