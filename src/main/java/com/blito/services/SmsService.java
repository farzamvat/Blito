package com.blito.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService {

	@Autowired
	private UrlShortenerService urlShortenerService;
	@Value("${kaveh.negar.api.key}")
	private String apiKey;
	@Value("${serverAddress}")
	private String serverAddress;

	RestTemplate rest = new RestTemplate();

	private final Logger log = LoggerFactory.getLogger(SmsService.class);

	public void sendBlitRecieptSms(String receptor, String token) {

		String blitPdfUrl = String.valueOf(new StringBuilder(serverAddress).append("/payment/").append(token));
		
		urlShortenerService.generateShortenUrl(blitPdfUrl)
			.thenApply(urlShortenerResponse -> {
				return rest.getForEntity(
						"https://api.kavenegar.com/v1/" + apiKey + "/verify/lookup.json?receptor=" + receptor + "&token="
								+ urlShortenerResponse.getId() + "&template=" + "BlitoReceipt",
						String.class);
			}).handle((response,throwable) -> {
				if(throwable != null)
					log.error("Error in sending sms to kavenegar '{}'",throwable);
				log.debug("sending sms to kavenegar response '{}'",response.getBody());
				return "";
			});
	}
}
