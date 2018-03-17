package blito.test.unit;

import com.blito.Application;
import com.blito.enums.*;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.EventFlatMapper;
import com.blito.models.Event;
import com.blito.models.EventHost;
import com.blito.models.User;
import com.blito.repositories.*;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.AdminChangeEventOperatorStateVm;
import com.blito.rest.viewmodels.event.EventFlatViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.search.Collection;
import com.blito.search.Operation;
import com.blito.search.SearchViewModel;
import com.blito.search.Simple;
import com.blito.security.SecurityContextHolder;
import com.blito.services.AdminEventService;
import com.blito.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment=WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class EventServiceTest {

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
	@Autowired
	private AdminEventService adminEventService;
	@Autowired
	EventFlatMapper eventFlatMapper;
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

	@Before
	public void init() {

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
		event.setAddress("ABC");
		event.setEventState(State.SOLD.name());
		event.setOperatorState(OperatorState.APPROVED.name());
		event.setEventName("A");
		event.setEventType(EventType.CINEMA.name());
		event.setLatitude(2D);
		event.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
		event.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));
		

		event1 = new Event();
		event1.setAddress("ABC");
		event1.setEventState(State.OPEN.name());
		event1.setOperatorState(OperatorState.APPROVED.name());
		event1.setEventName("B");
		event1.setLatitude(1D);
		event1.setEventType(EventType.CINEMA.name());
		event1.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(10).toInstant()));
		event1.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(1).toInstant()));

		event2 = new Event();
		event2.setAddress("ABCD");
		event2.setEventState(State.CLOSED.name());
		event2.setOperatorState(OperatorState.APPROVED.name());
		event2.setEventName("C");
		event2.setLatitude(4D);
		event2.setEventType(EventType.CINEMA.name());
		event2.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
		event2.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));

		event3 = new Event();
		event3.setAddress("DFG");
		event3.setEventState(State.OPEN.name());
		event3.setOperatorState(OperatorState.APPROVED.name());
		event3.setOffers(Arrays.asList(OfferTypeEnum.OUR_OFFER.name(), OfferTypeEnum.SPECIAL_OFFER.name()).stream().collect(Collectors.toSet()));
		event3.setEventName("D");
		event3.setLatitude(1D);
		event3.setEventType(EventType.ENTERTAINMENT.name());
		event3.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
		event3.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));

		event4 = new Event();
		event4.setAddress("ABC");
		event4.setEventState(State.OPEN.name());
		event4.setOperatorState(OperatorState.APPROVED.name());
		event4.setOffers(Arrays.asList(OfferTypeEnum.OUR_OFFER.name(), OfferTypeEnum.SPECIAL_OFFER.name()).stream().collect(Collectors.toSet()));
		event4.setEventName("E");
		event4.setLatitude(1D);
		event4.setEventType(EventType.CONCERT.name());
		event4.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
		event4.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));

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
		eventViewModel.setPrivate(false);

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
	}

	@Test
	public void create() {
		eventViewModel = eventService.create(eventViewModel);
		System.out.println(eventViewModel.getEventLink()
				+ "ASJKDASJKDAJKSDJAKSJDAKSJDAKSJDKAJSDKJASKDJAKSJDAKSJDAKSJDKAJSDKAJSDKJ");
		assertNotNull(eventRepository.findByEventIdAndIsDeletedFalse(eventViewModel.getEventId()));
		assertEquals(6,eventHostRepository.findByEventHostIdAndIsDeletedFalse(eventHost1.getEventHostId()).get().getEvents().size());
	}
	
	@Test
	public void update()
	{
		EventViewModel vmodel = eventService.create(eventViewModel);
		vmodel.setAddress("YousefAbad");
		vmodel.getEventDates().stream().findFirst().get().getBlitTypes().stream().findFirst().get().setName("Roo Hava");
		EventDateViewModel eventDateViewModel = new EventDateViewModel();
		eventDateViewModel.setDate(Timestamp.from(ZonedDateTime.now().plusDays(10).toInstant()));
		
		BlitTypeViewModel blitTypeViewModel= new BlitTypeViewModel();
		blitTypeViewModel.setCapacity(30);
		blitTypeViewModel.setFree(false);
		blitTypeViewModel.setName("jadid");
		blitTypeViewModel.setPrice(40000);
		
		
		eventDateViewModel.setBlitTypes(Arrays.asList(blitTypeViewModel).stream().collect(Collectors.toSet()));
		vmodel.getEventDates().add(eventDateViewModel);
		
		vmodel = eventService.update(vmodel);
		
		assertEquals("YousefAbad", vmodel.getAddress());
	}
	
	@Test(expected=NotFoundException.class)
	public void getEventByIdNotFoundException()
	{
		eventService.getEventById(2000);
	}
	
	@Test
	public void getEventById()
	{
		EventViewModel vmodel = eventService.create(eventViewModel);
		vmodel = eventService.getEventById(vmodel.getEventId());
		assertNotNull(eventRepository.findByEventIdAndIsDeletedFalse(vmodel.getEventId()));
	}
	
	@Test
	public void delete()
	{
		assertEquals(5, eventRepository.count());
		assertEquals(5, eventHostRepository.findByEventHostIdAndIsDeletedFalse(eventViewModel.getEventHostId()).get().getEvents().size());
		EventViewModel vmodel = eventService.create(eventViewModel);
		Event event = eventRepository.findOne(vmodel.getEventId());
		event.setOperatorState(OperatorState.REJECTED.name());
		eventRepository.save(event);
		eventService.delete(vmodel.getEventId());
		assertEquals(0,eventRepository.findOne(vmodel.getEventId()).getImages().size());
		assertEquals(6, eventHostRepository.findByEventHostIdAndIsDeletedFalse(eventViewModel.getEventHostId()).get().getEvents().size());
		assertEquals(true, eventRepository.findOne(vmodel.getEventId()).isDeleted());
	}
	
	@Test(expected=NotFoundException.class)
	public void createEventWithEventHostNotFoundException()
	{
		EventViewModel vmodel = null;
		vmodel = new EventViewModel();
		vmodel.setAddress("Amirabad");
		vmodel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
		vmodel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
		vmodel.setDescription("Description");
		vmodel.setEventHostName("Mamad");
		vmodel.setEventName("My Event");
		vmodel.setEventType(EventType.CONCERT);
		
		EventDateViewModel eventDateViewModel = new EventDateViewModel();
		BlitTypeViewModel blitTypeViewModel1= new BlitTypeViewModel();
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
		
		eventDateViewModel.setBlitTypes(Arrays.asList(blitTypeViewModel1,blitTypeViewModel2).stream().collect(Collectors.toSet()));
		vmodel.setEventDates(Arrays.asList(eventDateViewModel).stream().collect(Collectors.toSet()));
		vmodel.setEventHostId(1000L);
		vmodel = eventService.create(vmodel);
	}
	
	@Test(expected = InconsistentDataException.class)
	public void createEventWithInconsistentData() {
		eventViewModel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
		eventViewModel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
		eventViewModel = eventService.create(eventViewModel);
	}

	@Test
	public void search() {
		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
		Simple<Event> simple = new Simple<>();
		simple.setField("eventType");
		simple.setValue(EventType.ENTERTAINMENT.name());
		simple.setOperation(Operation.eq);

		searchViewModel.setRestrictions(new ArrayList<>());
		searchViewModel.getRestrictions().add(simple);
		Pageable pageable = new PageRequest(0, 5);

		Page<EventFlatViewModel> eventsPage = eventService.searchEvents(searchViewModel, pageable,eventFlatMapper);
		assertEquals(1,eventsPage.getNumberOfElements());
	}
	
//	@Test
//	public void emptySearch() {
//		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
//		searchViewModel.setRestrictions(new ArrayList<>());
//		Pageable pageable = new PageRequest(0, 5);
//
//		Page<EventViewModel> eventsPage = eventService.searchEvents(searchViewModel, pageable);
//		
//	}
	
	@Test
	public void getAllEventsTest() {
		Pageable pageable = new PageRequest(0,5);
		Page<EventViewModel> allEvents = eventService.getAllEvents(pageable);
		assertEquals(4, allEvents.getNumberOfElements());
		allEvents.getContent().stream().forEach(e -> System.err.print(e.getEventName()));
	}
	
	@Test
	public void eventDateSortTest()
	{
		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
		Simple<Event> simple = new Simple<>();
		simple.setField("eventId");
		simple.setOperation(Operation.eq);
		simple.setValue(String.valueOf(event.getEventId()));
		searchViewModel.setRestrictions(new ArrayList<>());
		searchViewModel.getRestrictions().add(simple);
		Pageable pageable = new PageRequest(0,5);
		Page<EventFlatViewModel> events = eventService.searchEvents(searchViewModel, pageable,eventFlatMapper);
		assertEquals(1, events.getNumberOfElements());
	}

	@Test
	public void collectionAndSimpleSearch() {
		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
		Collection<Event> collection = new Collection<>();
		collection.setField("offers");
		collection.setValues(Arrays.asList(OfferTypeEnum.OUR_OFFER.name(), OfferTypeEnum.SPECIAL_OFFER.name()));
		Simple<Event> simple = new Simple<>();
		simple.setField("eventType");
		simple.setOperation(Operation.eq);
		simple.setValue(EventType.CONCERT.name());
		searchViewModel.setRestrictions(new ArrayList<>());
		searchViewModel.getRestrictions().add(collection);
		searchViewModel.getRestrictions().add(simple);
		Pageable pageable = new PageRequest(0, 5);

		Page<EventFlatViewModel> eventsPage = eventService.searchEvents(searchViewModel, pageable,eventFlatMapper);
		assertEquals(1, eventsPage.getNumberOfElements());
	}

	@Test
	public void likeSearch() {
		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
		Simple<Event> simple = new Simple<>();
		simple.setField("address");
		simple.setOperation(Operation.like);
		simple.setValue("BC");
		searchViewModel.setRestrictions(new ArrayList<>());
		searchViewModel.getRestrictions().add(simple);

		Pageable pageable = new PageRequest(0, 5);
		Page<EventFlatViewModel> eventsPage = eventService.searchEvents(searchViewModel, pageable,eventFlatMapper);
		assertEquals(4, eventsPage.getNumberOfElements());
	}

	// ****************************************** Search Tests
	// ****************************************** //



	@Test
	public void getAllUserEventsTest() {
		Pageable pageable = new PageRequest(0,10);
		Page<EventViewModel> events = eventService.getUserEvents(pageable);
		assertEquals(5, events.getNumberOfElements());
		
		eventViewModel = eventService.create(eventViewModel);
		SecurityContextHolder.setCurrentUser(user);
		events = eventService.getUserEvents(pageable);
		assertEquals(6, events.getNumberOfElements());
		AdminChangeEventOperatorStateVm operatorStateVm = new AdminChangeEventOperatorStateVm();
		operatorStateVm.setOperatorState(OperatorState.REJECTED);
		operatorStateVm.setEventId(eventViewModel.getEventId());
		adminEventService.changeOperatorState(operatorStateVm);
		eventService.delete(eventViewModel.getEventId());
		events = eventService.getUserEvents(pageable);
		assertEquals(5, events.getNumberOfElements());
	}
	
	@Test
	public void privateEventTest() {
		eventViewModel.setPrivate(true);
		eventViewModel = eventService.create(eventViewModel);
		assertTrue(eventViewModel.isPrivate());
	}
}
