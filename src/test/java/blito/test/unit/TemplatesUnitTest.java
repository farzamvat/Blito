package blito.test.unit;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import com.blito.services.util.HtmlRenderer;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class TemplatesUnitTest {

	@Autowired
	private HtmlRenderer htmlRenderer;
	@Autowired
	private UserRepository userRepository;
	@Value("${serverAddress}")
	private String serverAddress;
	@Value("${api.base.url}")
	private String baseUrl;
	
	@Test
	public void accountVerificationTemplateTest()
	{
		User user = new User();
		user.setFirstname("farzamn");
		user.setLastname("Vatanzadeh");
		user.setEmail("farzam.vat@gmail.com");
		user.setMobile("09124337522");
		user = userRepository.save(user);
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);
        map.put("baseUrl", baseUrl);
        map.put("serverAddress",serverAddress);
		try {
			String content = htmlRenderer.renderHtml("accountVerification", map);
			assertTrue(true);
		} catch(Exception exception)
		{
			assertTrue(false);
		}
	}
	
	@Test
	public void forgetPasswordTemplateTest()
	{
//		Map<String, Object> map = new HashMap<String, Object>();
//        map.put("user", user);
//        map.put("baseUrl", baseUrl);
//        map.put("serverAddress",serverAddress);
//        String content = htmlRenderer.renderHtml("forgetPassword", map);
	}
}
