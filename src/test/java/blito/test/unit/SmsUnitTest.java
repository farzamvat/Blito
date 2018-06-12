package blito.test.unit;

import com.blito.Application;
import com.blito.services.SmsService;
import com.blito.services.UrlShortenerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SmsUnitTest {
	
	@Value("${kaveh.negar.api.key}")
	String apiKey;
	@Autowired
	private SmsService smsService;
	@Autowired
	private UrlShortenerService urlShortenerService;
	
	TestRestTemplate rest = new TestRestTemplate();
	
	@Test
	public void sendSmsKaveNegar() throws InterruptedException, UnsupportedEncodingException {
//
//		ResponseEntity<String> response = rest.getForEntity("https://api.kavenegar.com/v1/"+ apiKey + "/verify/lookup.json?receptor=09127976837&token=852596&template=Blito", String.class);
//		System.out.println("******************************");
//		System.out.println(response.getBody());
//		System.out.println("******************************");
//		
		System.out.println(urlShortenerService.generateBitlyShortenedUrl("http://blito.ir//api/blito/v1.0/public/blits/17593015/blit.pdf").toString());
	}


}
