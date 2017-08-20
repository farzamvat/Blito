package blito.test.integration;

import com.blito.Application;
import com.blito.configs.Constants;
import com.blito.enums.*;
import com.blito.models.*;
import com.blito.repositories.*;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class EventControllerTest {
	@Autowired
	TestRestTemplate rest;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	EventRepository eventRepository;
	@Autowired
	EventService eventService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	EventHostRepository eventHostRepository;
	@Autowired
	DiscountRepository discountRepo;
	@Autowired
	BlitTypeRepository blitTypeRepo;
	@Autowired
	ImageRepository imageRepository;
	@Value("${serverAddress}")
	private String serverAddress;
	Event event;
	Event event1;
	Event event2;
	Event event3;
	Event event4;
	EventHost eventHost1;
	EventHost eventHost2;
	private EventViewModel eventViewModel = null;
	private User user2 = new User();
	private User user = new User();
	private static boolean isInit = false;

	@Before
	public void init() {
		if(!isInit)
		{
			isInit = true;
			user.setEmail("farzam.vat@gmail.com");
			user.setActive(true);
			user.setFirstname("farzam");
			user.setLastname("vatanzadeh");
			user.setMobile("09124337522");

			user = userRepository.save(user);

			user2.setEmail("hasti.sahabi@gmail.com");
			user2.setActive(true);
			user2.setFirstname("hasti");
			user2.setLastname("sahabi");
			user2.setMobile("09127976837");

			user2 = userRepository.save(user2);

			eventHost1 = new EventHost();
			eventHost1.setHostName("hostname12");
			eventHost1.setHostType(HostType.THEATER.name());
			eventHost1.setTelephone("02188002116");
			eventHost1.setUser(user);

			eventHost1 = eventHostRepository.save(eventHost1);

			eventHost2 = new EventHost();
			eventHost2.setHostName("hostnamekkkk");
			eventHost2.setHostType(HostType.THEATER.name());
			eventHost2.setTelephone("02188002116");
			eventHost2.setUser(user2);

			eventHost2 = eventHostRepository.save(eventHost2);

			SecurityContextHolder.setCurrentUser(user);

			event = new Event();
			event.setAddress("سلام جطوری");
			event.setEventState(State.SOLD.name());
			event.setOperatorState(OperatorState.APPROVED.name());
			event.setEventName("A");
			event.setEventType(EventType.CINEMA.name());
			event.setLatitude(2D);
			event.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
			event.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));
			event.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(10).toInstant()));
			
			EventDate eventDate = new EventDate();
			eventDate.setDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(1).toInstant()));
			eventDate.setEventDateState(State.CLOSED.name());
			eventDate.setEvent(event);
			
			EventDate eventDate1 = new EventDate();
			eventDate1.setDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(2).toInstant()));
			eventDate1.setEventDateState(State.CLOSED.name());
			eventDate1.setEvent(event);
			
			EventDate eventDate2 = new EventDate();
			eventDate2.setDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(3).toInstant()));
			eventDate2.setEventDateState(State.CLOSED.name());
			eventDate2.setEvent(event);

			event1 = new Event();
			event1.setAddress("سلام جطوریسس");
			event1.setEventState(State.OPEN.name());
			event1.setOperatorState(OperatorState.APPROVED.name());
			event1.setEventName("B");
			event1.setLatitude(1D);
			event1.setEventType(EventType.CINEMA.name());
			event1.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(10).toInstant()));
			event1.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(1).toInstant()));
			event1.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(7).toInstant()));

			event2 = new Event();
			event2.setAddress("سلام");
			event2.setEventState(State.CLOSED.name());
			event2.setOperatorState(OperatorState.APPROVED.name());
			event2.setEventName("C");
			event2.setLatitude(4D);
			event2.setEventType(EventType.CINEMA.name());
			event2.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
			event2.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));
			event2.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(5).toInstant()));
			
			event3 = new Event();
			event3.setAddress("DFG");
			event3.setEventState(State.OPEN.name());
			event3.setOperatorState(OperatorState.APPROVED.name());
			event3.setOffers(Arrays.asList(OfferTypeEnum.OUR_OFFER.name()).stream().collect(Collectors.toSet()));
			event3.setEventName("D");
			event3.setLatitude(1D);
			event3.setEventType(EventType.ENTERTAINMENT.name());
			event3.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
			event3.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));
			event3.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(3).toInstant()));

			event4 = new Event();
			event4.setAddress("سلام جطوری");
			event4.setEventState(State.OPEN.name());
			event4.setOperatorState(OperatorState.APPROVED.name());
			event4.setOffers(Arrays.asList(OfferTypeEnum.OUR_OFFER.name(), OfferTypeEnum.SPECIAL_OFFER.name()).stream().collect(Collectors.toSet()));
			event4.setEventName("E");
			event4.setLatitude(1D);
			event4.setEventType(EventType.CONCERT.name());
			event4.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
			event4.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));
			event4.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(2).toInstant()));

			event.setEventHost(eventHost1);
			event1.setEventHost(eventHost1);
			event2.setEventHost(eventHost1);
			event3.setEventHost(eventHost1);
			event4.setEventHost(eventHost1);

			eventRepository.save(event);
			eventRepository.save(event1);
			eventRepository.save(event2);
			eventRepository.save(event3);
			eventRepository.save(event4);

			eventViewModel = new EventViewModel();
			eventViewModel.setAddress("Amirabad");
			eventViewModel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
			eventViewModel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
			eventViewModel.setDescription("Description");
			eventViewModel.setEventHostId(eventHost1.getEventHostId());
			eventViewModel.setEventHostName(eventHost1.getHostName());
			eventViewModel.setEventName("My Event");
			eventViewModel.setEventType(EventType.CONCERT);

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

			Image image = new Image();
			image.setImageType(ImageType.EVENT_PHOTO.name());
			image.setImageUUID(Constants.DEFAULT_HOST_PHOTO);

			Image hostCoverPhoto = new Image();
			image.setImageType(ImageType.HOST_COVER_PHOTO.name());
			image.setImageUUID(Constants.DEFAULT_HOST_COVER_PHOTO);

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

			System.err.println(eventRepository.count() + "*************************");
		}
	}

	@Test
	public void timeRangeSearch() throws JSONException {
		JSONObject simple = new JSONObject();
		simple.put("type", "time");
		simple.put("field", "createdAt");
		simple.put("operation", "gt");
		simple.put("value", Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(12).toInstant()).getTime());

		JSONObject simpleLessThan = new JSONObject();
		simpleLessThan.put("type", "time");
		simpleLessThan.put("field", "createdAt");
		simpleLessThan.put("operation", "lt");
		simpleLessThan.put("value", Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()).getTime());

		JSONArray restrictions = new JSONArray();
		restrictions.put(simple);
		restrictions.put(simpleLessThan);
		JSONObject requestBody = new JSONObject();
		requestBody.put("restrictions",restrictions);

		given().header("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE)
				.body(requestBody.toString())
				.when()
				.post( serverAddress +"/api/blito/v1.0/public/events/search/?page=0&size=20")
				.then().assertThat().statusCode(200).body("numberOfElements",equalTo(5));
	}
	
	@Test
	public void integrationSimpleSearch() throws URISyntaxException, JSONException {
		JSONObject requestBody = new JSONObject();
		JSONArray restrictions = new JSONArray();
		JSONObject simple = new JSONObject();
		simple.put("type", "simple");
		simple.put("field", "address");
		simple.put("operation", "like");
		simple.put("value", "سلام");
		restrictions.put(simple);
		requestBody.put("restrictions", restrictions);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		RequestEntity<String> request = 
				new RequestEntity<>(requestBody.toString(),headers,HttpMethod.POST,new URI("/api/blito/v1.0/public/events/search?page=0&size=5"));
		ResponseEntity<String> response = 
				rest.exchange("/api/blito/v1.0/public/events/search?page=0&size=5",HttpMethod.POST,request, String.class);
		JSONObject responseJson = null;
		try {
			responseJson = new JSONObject(response.getBody());
		} catch (Exception e)
		{
			assertTrue(false);
		}
		assertEquals(4,responseJson.get("numberOfElements"));
	}
	
	@Test
	public void greaterThanTest() throws URISyntaxException, JSONException
	{
		JSONObject requestBody = new JSONObject();
		JSONArray restrictions = new JSONArray();
		JSONObject simple = new JSONObject();
		simple.put("type", "time");
		simple.put("field", "createdAt");
		simple.put("operation", "gt");
		simple.put("value", Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(11).toInstant()).getTime());
		restrictions.put(simple);
		requestBody.put("restrictions", restrictions);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		RequestEntity<String> request = 
				new RequestEntity<>(requestBody.toString(),headers,HttpMethod.POST,new URI("/api/blito/v1.0/public/events/search?page=0&size=5"));
		ResponseEntity<String> response = 
				rest.exchange("/api/blito/v1.0/public/events/search?page=0&size=5",HttpMethod.POST,request, String.class);
		JSONObject responseJson = null;
		try {
			responseJson = new JSONObject(response.getBody());
		} catch (Exception e)
		{
			assertTrue(false);
		}
		assertEquals(5,responseJson.get("numberOfElements"));
	}
	
	@Test
	public void sortByCreatedAt() throws URISyntaxException, JSONException
	{
		JSONObject requestBody = new JSONObject();
		JSONArray restrictions = new JSONArray();
		requestBody.put("restrictions", restrictions);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		RequestEntity<String> request = 
				new RequestEntity<>(requestBody.toString(),headers,HttpMethod.POST,new URI("/api/blito/v1.0/public/events/search?page=0&size=5&sort=createdAt,desc"));
		ResponseEntity<String> response = 
				rest.exchange("/api/blito/v1.0/public/events/search?page=0&size=5&sort=createdAt,desc",HttpMethod.POST,request, String.class);
		JSONObject responseJson = null;
		try {
			responseJson = new JSONObject(response.getBody());
		} catch (Exception e)
		{
			assertTrue(false);
		}
		assertEquals(5,responseJson.get("numberOfElements"));
	}
	
	@Test
	public void nestedSortingTest() throws URISyntaxException, JSONException
	{
		JSONObject requestBody = new JSONObject();
		JSONArray restrictions = new JSONArray();
		JSONObject simple = new JSONObject();
		simple.put("type", "simple");
		simple.put("field", "eventName");
		simple.put("operation", "eq");
		simple.put("value", "A");
		restrictions.put(simple);
		requestBody.put("restrictions", restrictions);
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		RequestEntity<String> request = 
				new RequestEntity<>(requestBody.toString(),headers,HttpMethod.POST,new URI("/api/blito/v1.0/public/events/search?page=0&size=5&sort=createdAt,desc,eventDates.date,desc"));
		ResponseEntity<String> response = 
				rest.exchange("/api/blito/v1.0/public/events/search?page=0&size=5&sort=eventDates_date,desc",HttpMethod.POST,request, String.class);
		JSONObject responseJson = null;
		try {
			responseJson = new JSONObject(response.getBody());
		} catch (Exception e)
		{
			assertTrue(false);
		}
		assertEquals(1,responseJson.get("numberOfElements"));
	}
	
	@Test
	public void createdAtDescWithASimpleRestrictionTest() throws URISyntaxException, JSONException
	{
		JSONObject requestBody = new JSONObject();
		JSONArray restrictions = new JSONArray();
		JSONObject simple = new JSONObject();
		simple.put("type", "simple");
		simple.put("field", "address");
		simple.put("operation", "like");
		simple.put("value", "سلام");
		restrictions.put(simple);
		requestBody.put("restrictions", restrictions);
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		RequestEntity<String> request = 
				new RequestEntity<>(requestBody.toString(),headers,HttpMethod.POST,new URI("/api/blito/v1.0/public/events/search?page=0&size=5&sort=createdAt,desc"));
		ResponseEntity<String> response = 
				rest.exchange("/api/blito/v1.0/public/events/search?page=0&size=5&sort=createdAt,desc",HttpMethod.POST,request, String.class);
		JSONObject responseJson = null;
		try {
			responseJson = new JSONObject(response.getBody());
		} catch (Exception e)
		{
			assertTrue(false);
		}
		assertEquals(4,responseJson.get("numberOfElements"));
	}
	
	@Test
	public void integrationCollectionSearch() throws URISyntaxException, JSONException
	{
		JSONObject requestBody = new JSONObject();
		JSONArray restrictions = new JSONArray();
		JSONObject collection = new JSONObject();
		collection.put("type", "collection");
		collection.put("field", "offers");
		collection.put("values", new JSONArray(Arrays.asList("OUR_OFFER")));
		restrictions.put(collection);
		requestBody.put("restrictions", restrictions);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		RequestEntity<String> request = 
				new RequestEntity<>(requestBody.toString(),headers,HttpMethod.POST,new URI("/api/blito/v1.0/public/events/search?page=0&size=5"));
		ResponseEntity<String> response = 
				rest.exchange("/api/blito/v1.0/public/events/search?page=0&size=5",HttpMethod.POST,request, String.class);
		JSONObject responseJson = null;
		try {
			responseJson = new JSONObject(response.getBody());
		} catch (Exception e)
		{
			assertTrue(false);
		}
		assertEquals(2,Integer.parseInt(responseJson.get("numberOfElements").toString()));
		
	}

	@Test
	public void orTest() throws JSONException {
		JSONObject requestBody = new JSONObject();
		JSONArray restrictions = new JSONArray();
		JSONObject collection = new JSONObject();
		collection.put("type", "collection");
		collection.put("field", "offers");
		collection.put("values", new JSONArray(Arrays.asList(OfferTypeEnum.OUR_OFFER.name(),OfferTypeEnum.SPECIAL_OFFER.name())));
		JSONObject simple = new JSONObject();
		simple.put("type", "simple");
		simple.put("field", "eventType");
		simple.put("operation", "eq");
		simple.put("value", EventType.CINEMA.name());
		restrictions.put(collection);
		restrictions.put(simple);
		requestBody.put("restrictions", restrictions);
		requestBody.put("operator","or");

		Response response = given().header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
				.body(requestBody.toString())
				.when()
				.post( serverAddress +"/api/blito/v1.0/public/events/search/?page=0&size=5");

		response.then().assertThat().statusCode(200).body("numberOfElements",equalTo(5));

	}

	@Test
	public void collectionTest() throws JSONException {
		JSONObject requestBody = new JSONObject();
		JSONArray restrictions = new JSONArray();
		JSONObject collection = new JSONObject();
		collection.put("type", "collection");
		collection.put("field", "offers");
		collection.put("values", new JSONArray(Arrays.asList(OfferTypeEnum.OUR_OFFER.name(),OfferTypeEnum.SPECIAL_OFFER.name())));
		restrictions.put(collection);
		requestBody.put("restrictions",restrictions);

		given().header("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE)
				.body(requestBody.toString())
				.when()
				.post( serverAddress +"/api/blito/v1.0/public/events/search/?page=0&size=5")
				.then().assertThat().statusCode(200).body("numberOfElements",equalTo(1));
	}
}
