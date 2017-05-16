package blito.test.integration;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.blito.Application;
import com.blito.enums.EventType;
import com.blito.enums.HostType;
import com.blito.models.EventHost;
import com.blito.models.User;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.security.SecurityContextHolder;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class EventControllerTest {
	
	User user= null;
	private static boolean isInit = false;
	@Autowired
	UserRepository userRepository;
	@Autowired
	EventHostRepository eventHostRepository;
	@Autowired
	TestRestTemplate rest;
	@Value("${api.base.url}")
	String baseUrl;
	
	EventViewModel eventViewModel = null;
	
	@Transactional
	@Before
	public void init()
	{
		if(!isInit) {
			isInit = true;
			user = new User();
			user.setFirstname("Farzam");
			user.setLastname("Vatanzadeh");
			user.setEmail("farzam.vat@gmail.com");
			user.setMobile("09124337522");
			user = userRepository.save(user);
			SecurityContextHolder.setCurrentUser(user);
			
			EventHost eventHost = new EventHost();
			eventHost.setHostName("hostname12");
			eventHost.setHostType(HostType.THEATER);
			eventHost.setTelephone("02188002116");
			eventHost.setUser(user);
			user.getEventHosts().add(eventHost);
			eventHost = eventHostRepository.save(eventHost);
			
			eventViewModel = new EventViewModel();
			eventViewModel.setAddress("Address");
			eventViewModel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(5).toInstant()));
			eventViewModel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));
			eventViewModel.setEventHostName(eventHost.getHostName());
			eventViewModel.setEventHostId(eventHost.getEventHostId());
			eventViewModel.setDescription("description");
			eventViewModel.setEventType(EventType.CONCERT);
		}
	}
	
	@Test
	public void createEvent()
	{
		RequestEntity<EventViewModel> request = new RequestEntity<EventViewModel>(eventViewModel,null,HttpMethod.POST,null);
		ResponseEntity<EventViewModel> response = rest.exchange(baseUrl+"/events", HttpMethod.POST, request, EventViewModel.class);
		assertEquals(201,response.getStatusCodeValue());
	}
}
