package blito.test.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SmsUnitTest {
	
	@Value("${kaveh.negar.api.key}")
	String apiKey;
	
	TestRestTemplate rest = new TestRestTemplate();
	
	@Test
	public void sendSmsKaveNegar()
	{

		ResponseEntity<String> response = rest.getForEntity("https://api.kavenegar.com/v1/"+ apiKey + "/verify/lookup.json?receptor=09127976837&token=852596&template=Blito", String.class);
		System.out.println("******************************");
		System.out.println(response.getBody());
		System.out.println("******************************");

		
	}
}
