package blito.test.integration;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.configs.Constants;
import com.blito.enums.EventType;
import com.blito.enums.HostType;
import com.blito.enums.ImageType;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.account.TokenModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.services.JwtService;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment=WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EventControllerAdditionalFieldsTest {

	@Autowired
	private TestRestTemplate rest;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EventHostRepository eventHostRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private JwtService jwtService;
	private EventHost eventHost;
	private User user = null;
	
	@Before
	public void init()
	{
		user = new User();
		user.setEmail("farzam.vat@gmail.com");	
		user = userRepository.save(user);
		
		eventHost = new EventHost();
		eventHost.setHostName("hostname12");
		eventHost.setHostType(HostType.THEATER.name());
		eventHost.setTelephone("02188002116");
		eventHost.setUser(user);
		eventHost = eventHostRepository.save(eventHost);
		
		Image image = new Image();
		image.setImageType(ImageType.EVENT_PHOTO.name());
		image.setImageUUID(Constants.DEFAULT_HOST_PHOTO);

		Image hostCoverPhoto = new Image();
		image.setImageType(ImageType.HOST_COVER_PHOTO.name());
		image.setImageUUID(Constants.DEFAULT_HOST_COVER_PHOTO_1);

		Image exchangeBlitPhoto = new Image();
		image.setImageType(ImageType.EXCHANGEBLIT_PHOTO.name());
		image.setImageUUID(Constants.DEFAULT_EXCHANGEBLIT_PHOTO);

		Image eventPhoto = new Image();
		image.setImageType(ImageType.EVENT_PHOTO.name());
		image.setImageUUID(Constants.DEFAULT_EVENT_PHOTO);

		imageRepository.save(image);
		imageRepository.save(hostCoverPhoto);
		imageRepository.save(exchangeBlitPhoto);
		imageRepository.save(eventPhoto);
	}
	
	@Test
	public void create() throws URISyntaxException
	{
		TokenModel tokenModel = jwtService.generateAccessToken(userRepository.findByEmail("farzam.vat@gmail.com").get().getEmail()).join();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.add("X-AUTH-TOKEN", "Bearer " + tokenModel.getAccessToken());
		
		EventViewModel eventViewModel = new EventViewModel();
		eventViewModel.setAddress("Amirabad");
		eventViewModel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
		eventViewModel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
		eventViewModel.setDescription("Description");
		eventViewModel.setEventHostId(eventHost.getEventHostId());
		eventViewModel.setEventHostName(eventHost.getHostName());
		eventViewModel.setEventName("My Event");
		eventViewModel.setEventType(EventType.CONCERT);
		
		eventViewModel.setAdditionalFields(new HashMap<String, String>() {
			
			{
				this.put("jensiat", "string");
				this.put("sen", "int");
			}
		});

		EventDateViewModel eventDateViewModel = new EventDateViewModel();
		BlitTypeViewModel blitTypeViewModel1 = new BlitTypeViewModel();
		BlitTypeViewModel blitTypeViewModel2 = new BlitTypeViewModel();
		eventDateViewModel.setDate(Timestamp.from(ZonedDateTime.now().plusDays(10).toInstant()));

		blitTypeViewModel1.setCapacity(20);
		blitTypeViewModel1.setFree(false);
		blitTypeViewModel1.setName("vaysade");
		blitTypeViewModel1.setPrice(20000);

		blitTypeViewModel2.setCapacity(30);
		blitTypeViewModel2.setFree(false);
		blitTypeViewModel2.setName("neshaste");
		blitTypeViewModel2.setPrice(40000);

		eventDateViewModel.setBlitTypes(Arrays.asList(blitTypeViewModel1, blitTypeViewModel2).stream().collect(Collectors.toSet()));
		eventViewModel.setEventDates(Arrays.asList(eventDateViewModel).stream().collect(Collectors.toSet()));
		
		RequestEntity<EventViewModel> request = 
				new RequestEntity<>(eventViewModel,headers,HttpMethod.POST,new URI("/api/blito/v1.0/events"));
		ResponseEntity<String> response = 
				rest.exchange("/api/blito/v1.0/events",HttpMethod.POST,request, String.class);
		
		assertEquals(201,response.getStatusCodeValue());
	}
}
