package blito.test.unit;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.EventService;

import antlr.collections.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
//@Transactional
public class EventServiceTest {

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
	EventHost eventHost;
	private EventViewModel eventViewModel = null;

	@Before
	public void init() {
			
			
			User user = new User();
			user.setEmail("farzam.vat@gmail.com");
			user.setActive(true);
			user.setFirstname("farzam");
			user.setLastname("vatanzadeh");
			user.setMobile("09124337522");

			user = userRepository.save(user);

			eventHost = new EventHost();
			eventHost.setHostName("hostname12");
			eventHost.setHostType(HostType.THEATER);
			eventHost.setTelephone("02188002116");
			eventHost.setUser(user);

			eventHost = eventHostRepository.save(eventHost);
			
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
			
			eventViewModel = new EventViewModel();
			eventViewModel.setAddress("Amirabad");
			eventViewModel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
			eventViewModel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
			eventViewModel.setDescription("Description");
			eventViewModel.setEventHostId(eventHost.getEventHostId());
			eventViewModel.setEventHostName(eventHost.getHostName());
			eventViewModel.setEventName("My Event");
			eventViewModel.setEventType(EventType.CONCERT);
			
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
	
//	@Test
//	public void create()
//	{
//		eventViewModel = eventService.create(eventViewModel);
//		System.out.println(eventViewModel.getEventLink() + "ASJKDASJKDAJKSDJAKSJDAKSJDAKSJDKAJSDKJASKDJAKSJDAKSJDAKSJDKAJSDKAJSDKJ");
//		assertNotNull(eventRepository.findOne(eventViewModel.getEventId()));
//		assertEquals(6,eventHostRepository.findOne(eventHost.getEventHostId()).getEvents().size());
//	}
	
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
	
//	@Test(expected=NotFoundException.class)
//	public void getEventByIdNotFoundException()
//	{
//		eventService.getEventById(2000);
//	}
//	
//	@Test
//	public void getEventById()
//	{
//		EventViewModel vmodel = eventService.create(eventViewModel);
//		vmodel = eventService.getEventById(vmodel.getEventId());
//		assertNotNull(eventRepository.findOne(vmodel.getEventId()));
//	}
//	
//	@Test
//	public void delete()
//	{
//		assertEquals(5, eventRepository.count());
//		assertEquals(5, eventHostRepository.findOne(eventViewModel.getEventHostId()).getEvents().size());
//		EventViewModel vmodel = eventService.create(eventViewModel);
//		eventService.delete(vmodel.getEventId());
//		assertNull(eventRepository.findOne(vmodel.getEventId()));
//		assertEquals(5, eventHostRepository.findOne(eventViewModel.getEventHostId()).getEvents().size());
//	}
//	
//	@Test(expected=NotFoundException.class)
//	public void createEventWithEventHostNotFoundException()
//	{
//		EventViewModel vmodel = null;
//		vmodel = new EventViewModel();
//		vmodel.setAddress("Amirabad");
//		vmodel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
//		vmodel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
//		vmodel.setDescription("Description");
//		vmodel.setEventHostName("Mamad");
//		vmodel.setEventName("My Event");
//		vmodel.setEventType(EventType.CONCERT);
//		
//		EventDateViewModel eventDateViewModel = new EventDateViewModel();
//		BlitTypeViewModel blitTypeViewModel1= new BlitTypeViewModel();
//		BlitTypeViewModel blitTypeViewModel2 = new BlitTypeViewModel();
//		eventDateViewModel.setDate(Timestamp.from(ZonedDateTime.now().plusDays(10).toInstant()));
//		eventDateViewModel.setDayOfWeek(DayOfWeek.SATURDAY);
//		
//		blitTypeViewModel1.setCapacity(20);
//		blitTypeViewModel1.setFree(false);
//		blitTypeViewModel1.setName("vaysade");
//		blitTypeViewModel1.setPrice(20000);
//		
//		blitTypeViewModel2.setCapacity(30);
//		blitTypeViewModel2.setFree(false);
//		blitTypeViewModel2.setName("neshaste");
//		blitTypeViewModel2.setPrice(40000);
//		
//		eventDateViewModel.setBlitTypes(Arrays.asList(blitTypeViewModel1,blitTypeViewModel2));
//		vmodel.setEventDates(Arrays.asList(eventDateViewModel));
//		vmodel.setEventHostId(1000);
//		vmodel = eventService.create(vmodel);
//	}
//	
//	@Test(expected = InconsistentDataException.class)
//	public void createEventWithInconsistentData() {
//		eventViewModel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
//		eventViewModel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
//		eventViewModel = eventService.create(eventViewModel);
//	}
//
//	@Test
//	public void search() {
//		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
//		Simple<Event> simple = new Simple<>();
//		simple.setField("eventType");
//		simple.setValue(EventType.SPORT);
//		simple.setOperation(Operation.eq);
//
//		searchViewModel.setRestrictions(new ArrayList<>());
//		searchViewModel.getRestrictions().add(simple);
//		Pageable pageable = new PageRequest(0, 5);
//
//		Page<EventViewModel> eventsPage = eventService.searchEvents(searchViewModel, pageable);
//		assertEquals(1,eventsPage.getNumberOfElements());
//	}
//	
////	@Test
////	public void emptySearch() {
////		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
////		searchViewModel.setRestrictions(new ArrayList<>());
////		Pageable pageable = new PageRequest(0, 5);
////
////		Page<EventViewModel> eventsPage = eventService.searchEvents(searchViewModel, pageable);
////		
////	}
//	
//	@Test
//	public void getAllEventsTest() {
//		Pageable pageable = new PageRequest(0,5);
//		Page<EventViewModel> allEvents = eventService.getAllEvents(pageable);
//		assertEquals(4, allEvents.getNumberOfElements());
//		allEvents.getContent().stream().forEach(e -> System.err.print(e.getEventName()));
//	}
//
//	@Test
//	public void collectionAndSimpleSearch() {
//		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
//		Collection<Event> collection = new Collection<>();
//		collection.setField("offers");
//		collection.setValues(Arrays.asList(OfferTypeEnum.OUR_OFFER, OfferTypeEnum.SPECIAL_OFFER));
//		Simple<Event> simple = new Simple<>();
//		simple.setField("eventType");
//		simple.setOperation(Operation.eq);
//		simple.setValue(EventType.CONCERT);
//		searchViewModel.setRestrictions(new ArrayList<>());
//		searchViewModel.getRestrictions().add(collection);
//		searchViewModel.getRestrictions().add(simple);
//		Pageable pageable = new PageRequest(0, 5);
//
//		Page<EventViewModel> eventsPage = eventService.searchEvents(searchViewModel, pageable);
//		assertEquals(1,eventsPage.getNumberOfElements());
//	}
//	
//	@Test
//	public void likeSearch()
//	{
//		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
//		Simple<Event> simple = new Simple<>();
//		simple.setField("address");
//		simple.setOperation(Operation.like);
//		simple.setValue("BC");
//		searchViewModel.setRestrictions(new ArrayList<>());
//		searchViewModel.getRestrictions().add(simple);
//		
//		Pageable pageable = new PageRequest(0,5);
//		Page<EventViewModel> eventsPage = eventService.searchEvents(searchViewModel, pageable);
//		assertEquals(4,eventsPage.getNumberOfElements());
//	}
//	
//	@Test
//	public void setDiscountCodeTest() {
//		assertEquals(0, discountRepo.count());
//		eventViewModel = eventService.create(eventViewModel);
//		
//		DiscountViewModel vmodel = new DiscountViewModel();
//		
//		vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream().flatMap(ed->ed.getBlitTypes().stream().map(bt->bt.getBlitTypeId())).collect(Collectors.toList()));
//		vmodel.setCode("TAKHFIF!@#$");
//		vmodel.setReusability(5);
//		vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
//		vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
//		vmodel.setPercent(true);
//		vmodel.setPercent(30);
//		
//		vmodel = eventService.setDiscountCode(vmodel); 
//		assertEquals(2, vmodel.getBlitTypeIds().size());
//		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(0)).getDiscounts().size());
//		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(1)).getDiscounts().size());
//	}
//	
//	@Test(expected=InconsistentDataException.class)
//	public void setDiscountCodeTestInvalidPercentage()
//	{
//		assertEquals(0, discountRepo.count());
//		eventViewModel = eventService.create(eventViewModel);
//		
//		DiscountViewModel vmodel = new DiscountViewModel();
//		
//		vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream().flatMap(ed->ed.getBlitTypes().stream().map(bt->bt.getBlitTypeId())).collect(Collectors.toList()));
//		vmodel.setCode("TAKHFIF!@#$");
//		vmodel.setReusability(5);
//		vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
//		vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
//		vmodel.setPercent(true);
//		vmodel.setPercent(0);
//		
//		vmodel = eventService.setDiscountCode(vmodel); 
//	}
//	
//	@Test(expected=InconsistentDataException.class)
//	public void setDiscountCodeTestInvalidPercentageWithAmount()
//	{
//		assertEquals(0, discountRepo.count());
//		eventViewModel = eventService.create(eventViewModel);
//		
//		DiscountViewModel vmodel = new DiscountViewModel();
//		
//		vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream().flatMap(ed->ed.getBlitTypes().stream().map(bt->bt.getBlitTypeId())).collect(Collectors.toList()));
//		vmodel.setCode("TAKHFIF!@#$");
//		vmodel.setReusability(5);
//		vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
//		vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
//		vmodel.setPercent(true);
//		vmodel.setPercent(30);
//		vmodel.setAmount(100);
//		vmodel = eventService.setDiscountCode(vmodel); 
//	}
//	
//	@Test(expected=InconsistentDataException.class)
//	public void setDiscountCodeTestInvalidAmount()
//	{
//		assertEquals(0, discountRepo.count());
//		eventViewModel = eventService.create(eventViewModel);
//		
//		DiscountViewModel vmodel = new DiscountViewModel();
//		
//		vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream().flatMap(ed->ed.getBlitTypes().stream().map(bt->bt.getBlitTypeId())).collect(Collectors.toList()));
//		vmodel.setCode("TAKHFIF!@#$");
//		vmodel.setReusability(5);
//		vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
//		vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
//		vmodel.setPercent(false);
//		vmodel.setAmount(-20);
//		
//		vmodel = eventService.setDiscountCode(vmodel); 
//		assertEquals(2, vmodel.getBlitTypeIds().size());
//		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(0)).getDiscounts().size());
//		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(1)).getDiscounts().size());
//	}
//	
//	@Test(expected=InconsistentDataException.class)
//	public void setDiscountCodeTestInvalidPercentageWhenIsPercentIsFalse()
//	{
//		assertEquals(0, discountRepo.count());
//		eventViewModel = eventService.create(eventViewModel);
//		
//		DiscountViewModel vmodel = new DiscountViewModel();
//		
//		vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream().flatMap(ed->ed.getBlitTypes().stream().map(bt->bt.getBlitTypeId())).collect(Collectors.toList()));
//		vmodel.setCode("TAKHFIF!@#$");
//		vmodel.setReusability(5);
//		vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
//		vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
//		vmodel.setPercent(false);
//		vmodel.setPercent(30);
//		vmodel.setAmount(1000);
//		
//		vmodel = eventService.setDiscountCode(vmodel); 
//		assertEquals(2, vmodel.getBlitTypeIds().size());
//		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(0)).getDiscounts().size());
//		assertEquals(1, blitTypeRepo.findOne(vmodel.getBlitTypeIds().get(1)).getDiscounts().size());
//	}
//	
//	

//	@Test
//	public void rangeSearch() {
//		SearchViewModel<Event> searchViewModel = new SearchViewModel<>();
//		Range<Event> range = new Range<>();
//		range.setField("blitSaleStartDate");
//		range.setMinValue(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()).getTime());
//		range.setMaxValue(Timestamp.from(ZonedDateTime.now().plusHours(15).toInstant()).getTime());
//		searchViewModel.setRestrictions(new ArrayList<>());
//		searchViewModel.getRestrictions().add(range);
//		Pageable pageable = new PageRequest(0, 5);
//
//		Page<Event> eventsPage = eventService.searchEvents(searchViewModel, pageable);
//		assertEquals(eventsPage.getNumberOfElements(), 2);
//
//	}

}
