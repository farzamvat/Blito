package blito.test.unit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.enums.EventType;
import com.blito.enums.HostType;
import com.blito.enums.OfferTypeEnum;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.Event;
import com.blito.models.EventHost;
import com.blito.models.User;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.UserRepository;
import com.blito.search.Collection;
import com.blito.search.Operation;
import com.blito.search.SearchViewModel;
import com.blito.search.Simple;
import com.blito.services.EventService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class EventServiceTest {

	@Autowired EventRepository eventRepository;
	@Autowired EventService eventService;
	@Autowired UserRepository userRepository;
	@Autowired EventHostRepository eventHostRepository;
	Event event;
	Event event1;
	Event event2;
	Event event3;
	Event event4;
	private static boolean isInit = false;
	
	@Before
	public void init() {
		if(!isInit)
		{
			User user = new User();
			user.setEmail("farzam.vat@gmail.com");
			user.setActive(true);
			user.setFirstname("farzam");
			user.setLastname("vatanzadeh");
			user.setMobile("09124337522");
			
			user = userRepository.save(user);
			
			EventHost eventHost = new EventHost();
			eventHost.setHostName("hostname12");
			eventHost.setHostType(HostType.THEATER);
			eventHost.setTelephone("02188002116");
			eventHost.setUser(user);
			
			eventHost = eventHostRepository.save(eventHost);
			
			event = new Event();
			event.setAddress("ABC");
			event.setEventState(State.OPEN);
			event.setOperatorState(OperatorState.PENDING);
			event.setEventName("A");
			event.setEventType(EventType.CINEMA);
			event.setLatitude(2D);
			
			event1 = new Event();
			event1.setAddress("ABC");
			event1.setEventState(State.OPEN);
			event1.setOperatorState(OperatorState.APPROVED);
			event1.setEventName("A");
			event1.setLatitude(1D);
			event1.setEventType(EventType.CINEMA);
			
			event2 = new Event();
			event1.setAddress("ABC");
			event2.setEventState(State.CLOSED);
			event2.setOperatorState(OperatorState.PENDING);
			event2.setEventName("A");
			event2.setLatitude(4D);
			event2.setEventType(EventType.CINEMA);
			
			event3 = new Event();
			event3.setAddress("ABC");
			event3.setEventState(State.OPEN);
			event3.setOperatorState(OperatorState.REJECTED);
			event3.setOffers(Arrays.asList(OfferTypeEnum.OUR_OFFER,OfferTypeEnum.SPECIAL_OFFER));
			event3.setEventName("B");
			event3.setLatitude(1D);
			event3.setEventType(EventType.CONCERT);
			
			event4 = new Event();
			event4.setAddress("ABC");
			event4.setEventState(State.OPEN);
			event4.setOperatorState(OperatorState.REJECTED);
			event4.setOffers(Arrays.asList(OfferTypeEnum.OUR_OFFER,OfferTypeEnum.SPECIAL_OFFER));
			event4.setEventName("B");
			event4.setLatitude(1D);
			event4.setEventType(EventType.CONCERT);
			
			event.setEventHost(eventHost);
			event1.setEventHost(eventHost);
			event2.setEventHost(eventHost);
			event3.setEventHost(eventHost);
			event4.setEventHost(eventHost);
			
			eventRepository.save(event);
			eventRepository.save(event1);
			eventRepository.save(event2);
			eventRepository.save(event3);
			eventRepository.save(event4);
			isInit = true;
		}
	}
	
	@Test
	public void search() {
		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
		Simple<Event> simple = new Simple<>();
		simple.setField("eventName");
		simple.setValue("A");
		simple.setOperation(Operation.eq);
		
		searchViewModel.setRestrictions(new ArrayList<>());
		searchViewModel.getRestrictions().add(simple);
		Pageable pageable = new PageRequest(0, 5);
		
		Page<Event> eventsPage = eventService.searchEvents(searchViewModel, pageable);
		assertEquals(eventsPage.getNumberOfElements(),3);
	}
	
	@Test
	public void collectionAndSimpleSearch()
	{
		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
		Collection<Event> collection = new Collection<>();
		collection.setField("offers");
		collection.setValues(Arrays.asList(OfferTypeEnum.OUR_OFFER,OfferTypeEnum.SPECIAL_OFFER));
		Simple<Event> simple = new Simple<>();
		simple.setField("eventType");
		simple.setOperation(Operation.eq);
		simple.setValue(EventType.CONCERT);
		searchViewModel.setRestrictions(new ArrayList<>());
		searchViewModel.getRestrictions().add(collection);
		searchViewModel.getRestrictions().add(simple);
		Pageable pageable = new PageRequest(0,5);
		
		Page<Event> eventsPage = eventService.searchEvents(searchViewModel, pageable);
		assertEquals(eventsPage.getNumberOfElements(),2);
	}
	
}
