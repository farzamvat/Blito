package blito.test.integration;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.blito.Application;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.Event;
import com.blito.repositories.EventRepository;
import com.blito.search.SearchViewModel;
import com.blito.search.Simple;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EventSearchApiIntegrationTest {

	@Autowired EventRepository eventRepository;
	RestTemplate rest;
	@Value("${api.base.url}") String baseUrl;
	
	@Before
	public void init()
	{
		rest = new RestTemplate();
		Event event = new Event() {{
			this.setAddress("ABC");
			this.setEventState(State.OPEN);
			this.setOperatorState(OperatorState.PENDING);
			this.setEventName("A");
			this.setLatitude(2D);
		}};
		Event event1 = new Event() {{
			this.setAddress("ABC");
			this.setEventState(State.OPEN);
			this.setOperatorState(OperatorState.APPROVED);
			this.setEventName("A");
			this.setLatitude(1D);
		}};
		Event event2 = new Event() {{
			this.setAddress("ABC");
			this.setEventState(State.CLOSED);
			this.setOperatorState(OperatorState.PENDING);
			this.setEventName("A");
			this.setLatitude(4D);
		}};
		Event event3 = new Event() {{
			this.setAddress("ABC");
			this.setEventState(State.OPEN);
			this.setOperatorState(OperatorState.REJECTED);
			this.setEventName("B");
			this.setLatitude(1D);
		}};
		eventRepository.save(event);
		eventRepository.save(event1);
		eventRepository.save(event2);
		eventRepository.save(event3);
	}
	
	@Test
	public void searchApi() {
		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
		Simple<Event> simple = new Simple<>();
		simple.setField("eventName");
		simple.setValue("B");
		searchViewModel.setRestrictions(new ArrayList<>());
		searchViewModel.getRestrictions().add(simple);
		
		RequestEntity<SearchViewModel<Event>> request = new RequestEntity<>(HttpMethod.POST, null);
		@SuppressWarnings("unused")
		ResponseEntity<Object> response = 
				rest.exchange(baseUrl + "/events/search", HttpMethod.POST, request, Object.class);
		assertEquals(response.getStatusCodeValue(),200);
		
	}
}
