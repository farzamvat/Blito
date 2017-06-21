package blito.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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

import com.blito.Application;
import com.blito.configs.Constants;
import com.blito.enums.DayOfWeek;
import com.blito.enums.EventType;
import com.blito.enums.HostType;
import com.blito.enums.ImageType;
import com.blito.enums.OfferTypeEnum;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotFoundException;
import com.blito.models.Event;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.DiscountRepository;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.search.Collection;
import com.blito.search.Operation;
import com.blito.search.SearchViewModel;
import com.blito.search.Simple;
import com.blito.security.SecurityContextHolder;
import com.blito.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		eventHost1.setHostType(HostType.THEATER);
		eventHost1.setTelephone("02188002116");
		eventHost1.setUser(user);

		eventHost1 = eventHostRepository.save(eventHost1);

		eventHost2 = new EventHost();
		eventHost2.setHostName("hostnamekkkk");
		eventHost2.setHostType(HostType.THEATER);
		eventHost2.setTelephone("02188002116");
		eventHost2.setUser(user2);

		eventHost2 = eventHostRepository.save(eventHost2);

		SecurityContextHolder.setCurrentUser(user);

		event = new Event();
		event.setAddress("ABC");
		event.setEventState(State.SOLD);
		event.setOperatorState(OperatorState.PENDING);
		event.setEventName("A");
		event.setEventType(EventType.CINEMA);
		event.setLatitude(2D);
		event.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
		event.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));

		event1 = new Event();
		event1.setAddress("ABC");
		event1.setEventState(State.OPEN);
		event1.setOperatorState(OperatorState.APPROVED);
		event1.setEventName("B");
		event1.setLatitude(1D);
		event1.setEventType(EventType.CINEMA);
		event1.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(10).toInstant()));
		event1.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(1).toInstant()));

		event2 = new Event();
		event2.setAddress("ABCD");
		event2.setEventState(State.CLOSED);
		event2.setOperatorState(OperatorState.PENDING);
		event2.setEventName("C");
		event2.setLatitude(4D);
		event2.setEventType(EventType.CINEMA);

		event3 = new Event();
		event3.setAddress("DFG");
		event3.setEventState(State.OPEN);
		event3.setOperatorState(OperatorState.REJECTED);
		event3.setOffers(Arrays.asList(OfferTypeEnum.OUR_OFFER, OfferTypeEnum.SPECIAL_OFFER));
		event3.setEventName("D");
		event3.setLatitude(1D);
		event3.setEventType(EventType.SPORT);

		event4 = new Event();
		event4.setAddress("ABC");
		event4.setEventState(State.OPEN);
		event4.setOperatorState(OperatorState.REJECTED);
		event4.setOffers(Arrays.asList(OfferTypeEnum.OUR_OFFER, OfferTypeEnum.SPECIAL_OFFER));
		event4.setEventName("E");
		event4.setLatitude(1D);
		event4.setEventType(EventType.CONCERT);

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
		eventDateViewModel.setDayOfWeek(DayOfWeek.SATURDAY);

		blitTypeViewModel1.setCapacity(20);
		blitTypeViewModel1.setFree(false);
		blitTypeViewModel1.setName("vaysade");
		blitTypeViewModel1.setPrice(20000);

		blitTypeViewModel2.setCapacity(30);
		blitTypeViewModel2.setFree(false);
		blitTypeViewModel2.setName("neshaste");
		blitTypeViewModel2.setPrice(40000);

		eventDateViewModel.setBlitTypes(Arrays.asList(blitTypeViewModel1, blitTypeViewModel2));
		eventViewModel.setEventDates(Arrays.asList(eventDateViewModel));

		Image image = new Image();
		image.setImageType(ImageType.EVENT_PHOTO);
		image.setImageUUID(Constants.DEFAULT_HOST_PHOTO);

		Image hostCoverPhoto = new Image();
		image.setImageType(ImageType.HOST_COVER_PHOTO);
		image.setImageUUID(Constants.DEFAULT_HOST_COVER_PHOTO);

		Image exchangeBlitPhoto = new Image();
		image.setImageType(ImageType.EXCHANGEBLIT_PHOTO);
		image.setImageUUID(Constants.DEFAULT_EXCHANGEBLIT_PHOTO);

		Image eventPhoto = new Image();
		image.setImageType(ImageType.EVENT_PHOTO);
		image.setImageUUID(Constants.DEFAULT_EVENT_PHOTO);

		imageRepository.save(image);
		imageRepository.save(hostCoverPhoto);
		imageRepository.save(exchangeBlitPhoto);
		imageRepository.save(eventPhoto);

		System.err.println(eventRepository.count() + "*************************");
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
		vmodel.getEventDates().get(0).getBlitTypes().get(0).setName("Roo Hava");
		EventDateViewModel eventDateViewModel = new EventDateViewModel();
		eventDateViewModel.setDate(Timestamp.from(ZonedDateTime.now().plusDays(10).toInstant()));
		eventDateViewModel.setDayOfWeek(DayOfWeek.SATURDAY);
		
		BlitTypeViewModel blitTypeViewModel= new BlitTypeViewModel();
		blitTypeViewModel.setCapacity(30);
		blitTypeViewModel.setFree(false);
		blitTypeViewModel.setName("jadid");
		blitTypeViewModel.setPrice(40000);
		
		
		eventDateViewModel.setBlitTypes(Arrays.asList(blitTypeViewModel));
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
		eventService.delete(vmodel.getEventId());
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
		eventDateViewModel.setDayOfWeek(DayOfWeek.SATURDAY);
		
		blitTypeViewModel1.setCapacity(20);
		blitTypeViewModel1.setFree(false);
		blitTypeViewModel1.setName("vaysade");
		blitTypeViewModel1.setPrice(20000);
		
		blitTypeViewModel2.setCapacity(30);
		blitTypeViewModel2.setFree(false);
		blitTypeViewModel2.setName("neshaste");
		blitTypeViewModel2.setPrice(40000);
		
		eventDateViewModel.setBlitTypes(Arrays.asList(blitTypeViewModel1,blitTypeViewModel2));
		vmodel.setEventDates(Arrays.asList(eventDateViewModel));
		vmodel.setEventHostId(1000);
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
		simple.setValue(EventType.SPORT.name());
		simple.setOperation(Operation.eq);

		searchViewModel.setRestrictions(new ArrayList<>());
		searchViewModel.getRestrictions().add(simple);
		Pageable pageable = new PageRequest(0, 5);

		Page<EventViewModel> eventsPage = eventService.searchEvents(searchViewModel, pageable);
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

		Page<EventViewModel> eventsPage = eventService.searchEvents(searchViewModel, pageable);
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
		Page<EventViewModel> eventsPage = eventService.searchEvents(searchViewModel, pageable);
		assertEquals(4, eventsPage.getNumberOfElements());
	}

	// ****************************************** Search Tests
	// ****************************************** //

	@Test
	public void setDiscountCodeTest() {
		assertEquals(0, discountRepo.count());
		eventViewModel = eventService.create(eventViewModel);

		DiscountViewModel vmodel = new DiscountViewModel();

		vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream()
				.flatMap(ed -> ed.getBlitTypes().stream().map(bt -> bt.getBlitTypeId())).collect(Collectors.toList()));
		vmodel.setCode("TAKHFIF!@#$");
		vmodel.setReusability(5);
		vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
		vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
		vmodel.setPercent(true);
		vmodel.setPercent(30);

		vmodel = eventService.setDiscountCode(vmodel);
		assertEquals(2, vmodel.getBlitTypeIds().size());
		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(0)).getDiscounts().size());
		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(1)).getDiscounts().size());
	}

	@Test(expected = InconsistentDataException.class)
	public void setDiscountCodeTestInvalidPercentage() {
		assertEquals(0, discountRepo.count());
		eventViewModel = eventService.create(eventViewModel);
		
		DiscountViewModel vmodel = new DiscountViewModel();

		vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream()
				.flatMap(ed -> ed.getBlitTypes().stream().map(bt -> bt.getBlitTypeId())).collect(Collectors.toList()));
		vmodel.setCode("TAKHFIF!@#$");
		vmodel.setReusability(5);
		vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
		vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
		vmodel.setPercent(true);
		vmodel.setPercent(0);

		vmodel = eventService.setDiscountCode(vmodel);
	}

	@Test(expected = InconsistentDataException.class)
	public void setDiscountCodeTestInvalidPercentageWithAmount() {
		assertEquals(0, discountRepo.count());
		eventViewModel = eventService.create(eventViewModel);
		
		DiscountViewModel vmodel = new DiscountViewModel();

		vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream()
				.flatMap(ed -> ed.getBlitTypes().stream().map(bt -> bt.getBlitTypeId())).collect(Collectors.toList()));
		vmodel.setCode("TAKHFIF!@#$");
		vmodel.setReusability(5);
		vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
		vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
		vmodel.setPercent(true);
		vmodel.setPercent(30);
		vmodel.setAmount(100);
		vmodel = eventService.setDiscountCode(vmodel); 
	}

	@Test(expected = InconsistentDataException.class)
	public void setDiscountCodeTestInvalidAmount() {
		assertEquals(0, discountRepo.count());
		eventViewModel = eventService.create(eventViewModel);
		
		DiscountViewModel vmodel = new DiscountViewModel();

		vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream()
				.flatMap(ed -> ed.getBlitTypes().stream().map(bt -> bt.getBlitTypeId())).collect(Collectors.toList()));
		vmodel.setCode("TAKHFIF!@#$");
		vmodel.setReusability(5);
		vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
		vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
		vmodel.setPercent(false);
		vmodel.setAmount(-20);

		vmodel = eventService.setDiscountCode(vmodel);
		assertEquals(2, vmodel.getBlitTypeIds().size());
		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(0)).getDiscounts().size());
		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(1)).getDiscounts().size());
	}

	@Test(expected = InconsistentDataException.class)
	public void setDiscountCodeTestInvalidPercentageWhenIsPercentIsFalse() {
		assertEquals(0, discountRepo.count());
		eventViewModel = eventService.create(eventViewModel);

		DiscountViewModel vmodel = new DiscountViewModel();

		vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream()
				.flatMap(ed -> ed.getBlitTypes().stream().map(bt -> bt.getBlitTypeId())).collect(Collectors.toList()));
		vmodel.setCode("TAKHFIF!@#$");
		vmodel.setReusability(5);
		vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
		vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
		vmodel.setPercent(false);
		vmodel.setPercent(30);
		vmodel.setAmount(1000);

		vmodel = eventService.setDiscountCode(vmodel);
		assertEquals(2, vmodel.getBlitTypeIds().size());
		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(0)).getDiscounts().size());
		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(1)).getDiscounts().size());
	}

	@Test
	public void getAllUserEventsTest() {
		Pageable pageable = new PageRequest(0,10);
		Page<EventViewModel> events = eventService.getUserEvents(pageable);
		assertEquals(5, events.getNumberOfElements());
		
		eventViewModel = eventService.create(eventViewModel);
		SecurityContextHolder.setCurrentUser(user2);
		eventViewModel.setEventHostId(eventHost2.getEventHostId());
		eventViewModel.setEventHostName(eventHost2.getHostName());
		eventViewModel = eventService.create(eventViewModel);
		SecurityContextHolder.setCurrentUser(user);
		events = eventService.getUserEvents(pageable);
		assertEquals(6, events.getNumberOfElements());
		eventService.delete(event1.getEventId());
		events = eventService.getUserEvents(pageable);
		assertEquals(5, events.getNumberOfElements());
	}

	// @Test
	// public void rangeSearch() {
	// SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
	// Range<Event> range = new Range<>();
	// range.setField("blitSaleStartDate");
	// range.setMinValue(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()).getTime());
	// range.setMaxValue(Timestamp.from(ZonedDateTime.now().plusHours(15).toInstant()).getTime());
	// searchViewModel.setRestrictions(new ArrayList<>());
	// searchViewModel.getRestrictions().add(range);
	// Pageable pageable = new PageRequest(0, 5);
	//
	// Page<Event> eventsPage = eventService.searchEvents(searchViewModel,
	// pageable);
	// assertEquals(eventsPage.getNumberOfElements(), 2);
	//
	// }

}
