package com.blito.services;

import com.blito.rest.viewmodels.google.shortener.UrlShortenerResponseViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

@Service
public class SmsService {

	@Autowired
	private UrlShortenerService urlShortenerService;
	@Value("${kaveh.negar.api.key}")
	private String apiKey;
	@Value("${serverAddress}")
	private String serverAddress;
	@Value("${api.base.url}")
	private String baseUrl;

	RestTemplate rest = new RestTemplate();

	private final Logger log = LoggerFactory.getLogger(SmsService.class);

	public void sendBlitRecieptSms(String receptor, String token) {

		String blitPdfUrl = String.valueOf(new StringBuilder(serverAddress).append(baseUrl).append("/public/blits/").append(token).append("/blit.pdf"));
		UrlShortenerResponseViewModel urlShortenerResponseViewModel = null;
		try {
			urlShortenerResponseViewModel = urlShortenerService.generateBitlyShortenedUrl(blitPdfUrl);
			log.debug("url shortening by bitly response '{}'",urlShortenerResponseViewModel.toString());
			System.out.println(urlShortenerResponseViewModel.toString());
			ResponseEntity<String> smsResponse = rest.getForEntity(
					"https://api.kavenegar.com/v1/" + apiKey + "/verify/lookup.json?receptor=" + receptor + "&token="
							+ urlShortenerResponseViewModel.getData().getUrl() + "&template=" + "BlitoReceipt",
					String.class);
			log.debug("sending sms to kavenegar response '{}'",smsResponse.getBody());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void sendOperatorStatusSms(String receptor, String message) {
//	    String messageEncoded = new String(message.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
	    ResponseEntity<String> smsResponse = rest.getForEntity(
                "https://api.kavenegar.com/v1/" + apiKey +"/sms/send.json?receptor=" + receptor + "&message=" +  message, String.class);
        log.debug("sending sms to kavenegar response '{}'",smsResponse.getBody());

    }
}
