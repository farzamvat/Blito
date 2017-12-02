package blito.test.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.services.SmsService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SmsUnitTest {
	
	@Value("${kaveh.negar.api.key}")
	String apiKey;
	@Autowired
	private SmsService smsService;
	
	TestRestTemplate rest = new TestRestTemplate();
	
	@Test
	public void sendSmsKaveNegar() throws InterruptedException
	{
//
//		ResponseEntity<String> response = rest.getForEntity("https://api.kavenegar.com/v1/"+ apiKey + "/verify/lookup.json?receptor=09127976837&token=852596&template=Blito", String.class);
//		System.out.println("******************************");
//		System.out.println(response.getBody());
//		System.out.println("******************************");
//		
		
		smsService.sendBlitRecieptSms("09127976837", "32816213");
		Thread.sleep(5000);
	}

}
