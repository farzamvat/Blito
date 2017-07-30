package blito.test.integration;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class AuthorizationTest {
	@Autowired
	private TestRestTemplate rest;
	
	@Test
	public void authorizationFailTest() throws URISyntaxException
	{
		RequestEntity<String> request = 
				new RequestEntity<>(null,null,HttpMethod.GET,new URI("/api/blito/v1.0/account/user-info"));
		ResponseEntity<String> response = 
				rest.exchange("/api/blito/v1.0/account/user-info",HttpMethod.GET,request, String.class);
		assertEquals(401,response.getStatusCodeValue());
	}
}
