package blito.test.unit;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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
import com.blito.enums.OperatorState;
import com.blito.enums.SeatType;
import com.blito.enums.State;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.BlitTypeMapper;
import com.blito.mappers.EventDateMapper;
import com.blito.models.BlitType;
import com.blito.models.CommonBlit;
import com.blito.models.Event;
import com.blito.models.EventDate;
import com.blito.models.EventHost;
import com.blito.models.User;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.repositories.EventDateRepository;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.adminreport.BlitBuyerViewModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.event.AdminChangeEventOperatorStateVm;
import com.blito.rest.viewmodels.event.ChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventFlatViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.AdminEventService;
import com.blito.services.EventService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class AdminEventServiceTest {
	
	@Autowired
	UserRepository userRepo;
	@Autowired
	EventRepository eventRepo;
	@Autowired
	EventHostRepository eventHostRepo;
	@Autowired
	EventDateRepository eventDateRepo;
	@Autowired
	BlitTypeRepository blitTypeRepo;
	@Autowired
	AdminEventService adminEventService;
	@Autowired
	EventService eventService;
	@Autowired
	EventDateMapper dateMapper;
	@Autowired
	BlitTypeMapper blitTypeMapper;
	@Autowired
	CommonBlitRepository cBlitRepo;
	
	private User user = new User();
	
	private EventHost eventHost= new EventHost();
	
	private Event event1 = new Event();
	private Event event2 = new Event();
	
	private EventViewModel eventVm1 = new EventViewModel();
	private EventViewModel eventVm2 = new EventViewModel();
	
	private EventDate sans1_1 = new EventDate();
	private EventDate sans1_2 = new EventDate();
	
	private EventDate sans2_1 = new EventDate();
	
	private BlitType blitType1_1_1 = new BlitType();
	private BlitType blitType1_2_1 = new BlitType();
	
	private BlitType blitType2_1_1 = new BlitType();
	
	private ChangeEventStateVm changeEventStateTestVmodel = new ChangeEventStateVm();
	private AdminChangeEventOperatorStateVm changeEventOperatorStateTestVmodel = new AdminChangeEventOperatorStateVm();
	private EventViewModel eventUpdateVmodel = new EventViewModel();
	private EventDateViewModel eventDateUpdateVmodel1 = new EventDateViewModel();
	private EventDateViewModel eventDateUpdateVmodel2 = new EventDateViewModel();
	private BlitTypeViewModel blitTypeUpdateVmodel1 = new BlitTypeViewModel();
	private BlitTypeViewModel blitTypeUpdateVmodel2 = new BlitTypeViewModel();

	private CommonBlit cblit1 = new CommonBlit();
	private CommonBlit cblit2 = new CommonBlit();
	private CommonBlit cblit3 = new CommonBlit();
	private CommonBlit cblit4 = new CommonBlit();

	@Before
	public void init() {
			user.setFirstname("hasti");
			user.setEmail("hasti.sahabi@gmail.com");
			user.setMobile("09127976837");
			user.setActive(true);
			SecurityContextHolder.setCurrentUser(userRepo.save(user));
			
			eventHost.setHostName("paliz theater");
			eventHost.setHostType(HostType.CULTURALCENTER.name());
			eventHost.setTelephone("22431103");
			eventHost.setUser(user);
			eventHost = eventHostRepo.save(eventHost);
			
			event1.setEventName("akharin naameh");
			event1.setEventHost(eventHost);
			event1.setOperatorState(OperatorState.PENDING.name());
			
			event2.setEventName("jashne piroozi");
			event2.setEventHost(eventHost);
			event2.setOperatorState(OperatorState.PENDING.name());
			
			sans1_1.setDate(new Timestamp(1495264909518L));
			sans1_1.setEventDateState(State.CLOSED.name());
			sans1_2.setDate(new Timestamp(1495264909518L));
			sans1_2.setEventDateState(State.CLOSED.name());
			sans2_1.setDate(new Timestamp(1495264909518L));
			sans2_1.setEventDateState(State.CLOSED.name());
			
			blitType1_1_1.setName("seated1");
			blitType1_1_1.setCapacity(50);
			blitType1_1_1.setPrice(2500);
			blitType1_1_1.setBlitTypeState(State.CLOSED.name());
			blitType1_1_1.setEventDate(sans1_1);
			
			blitType1_2_1.setName("seated2");
			blitType1_2_1.setCapacity(50);
			blitType1_2_1.setBlitTypeState(State.CLOSED.name());
			blitType1_2_1.setPrice(2500);
			blitType1_2_1.setEventDate(sans1_2);
			
			
			blitType2_1_1.setName("standing");
			blitType2_1_1.setCapacity(100);
			blitType2_1_1.setPrice(1000);
			blitType2_1_1.setBlitTypeState(State.CLOSED.name());
			blitType2_1_1.setEventDate(sans2_1);
			
			sans1_1.setBlitTypes(Arrays.asList(blitType1_1_1).stream().collect(Collectors.toSet()));
			sans1_1.setEvent(event1);
			
			sans1_2.setBlitTypes(Arrays.asList(blitType1_2_1).stream().collect(Collectors.toSet()));
			sans1_2.setEvent(event1);
			
			sans2_1.setBlitTypes(Arrays.asList(blitType2_1_1).stream().collect(Collectors.toSet()));
			sans2_1.setEvent(event2);
			
			event1.setEventDates(Arrays.asList(sans1_1, sans1_2).stream().collect(Collectors.toSet()));
			event1.setEventState(State.CLOSED.name());
			event1.setEventType(EventType.CINEMA.name());
			event1 = eventRepo.save(event1);
			
			event2.setEventDates(Arrays.asList(sans2_1).stream().collect(Collectors.toSet()));
			event2.setEventState(State.CLOSED.name());
			event2.setEventType(EventType.CINEMA.name());
			event2 = eventRepo.save(event2);
			
			changeEventStateTestVmodel.setEventId(event1.getEventId());
			changeEventStateTestVmodel.setState(State.CLOSED);
			
			changeEventOperatorStateTestVmodel.setEventId(event1.getEventId());
			changeEventOperatorStateTestVmodel.setOperatorState(OperatorState.APPROVED);
			
			
			eventUpdateVmodel.setEventId(event1.getEventId());
			eventUpdateVmodel.setEventName("jashne banafsh");
			eventDateUpdateVmodel1 = dateMapper.createFromEntity(sans1_1);
			eventDateUpdateVmodel2 = dateMapper.createFromEntity(sans1_2);
			blitTypeUpdateVmodel1 = blitTypeMapper.createFromEntity(blitType1_1_1);
			blitTypeUpdateVmodel1.setName("VIP");
			blitTypeUpdateVmodel2 = blitTypeMapper.createFromEntity(blitType1_2_1);
			blitTypeUpdateVmodel2.setName("Economic");
			eventDateUpdateVmodel1.setBlitTypes(Arrays.asList(blitTypeUpdateVmodel1).stream().collect(Collectors.toSet()));
			eventDateUpdateVmodel2.setBlitTypes(Arrays.asList(blitTypeUpdateVmodel2).stream().collect(Collectors.toSet()));
			eventUpdateVmodel.setEventDates(Arrays.asList(eventDateUpdateVmodel1, eventDateUpdateVmodel2).stream().collect(Collectors.toSet()));
			
			cblit1.setCount(5);
			cblit1.setEventName(event1.getEventName());
			cblit1.setSeatType(SeatType.COMMON.name());
			cblit1.setBlitType(blitType1_1_1);
			cblit1.setUser(user);
			cblit1 = cBlitRepo.save(cblit1);
			
			
			cblit2.setCount(2);
			cblit2.setEventName(event1.getEventName());
			cblit2.setSeatType(SeatType.COMMON.name());
			cblit2.setBlitType(blitType1_1_1);
			cblit2.setUser(user);
			cblit2 = cBlitRepo.save(cblit2);
			
			blitType1_1_1.setCommonBlits(Arrays.asList(cblit1, cblit2).stream().collect(Collectors.toSet()));
			
			cblit3.setCount(4);
			cblit3.setEventName(event1.getEventName());
			cblit3.setSeatType(SeatType.COMMON.name());
			cblit3.setBlitType(blitType1_2_1);
			cblit3.setUser(user);
			cblit3 = cBlitRepo.save(cblit3);
			
			cblit4.setCount(7);
			cblit4.setEventName(event1.getEventName());
			cblit4.setSeatType(SeatType.COMMON.name());
			cblit4.setBlitType(blitType1_2_1);
			cblit4.setUser(user);
			cblit4 = cBlitRepo.save(cblit4);
			
	}
	
	@Test
	public void changeEventStateTest() {
		adminEventService.changeEventState(changeEventStateTestVmodel);
		assertEquals(State.CLOSED.name(), eventRepo.findByEventIdAndIsDeletedFalse(event1.getEventId()).get().getEventState());
		changeEventStateTestVmodel.setState(State.OPEN);
		adminEventService.changeEventState(changeEventStateTestVmodel);
		assertEquals(State.OPEN.name(), eventRepo.findByEventIdAndIsDeletedFalse(event1.getEventId()).get().getEventState());
	}
	
	@Test
	public void changeOperatorStateTest() {
		adminEventService.changeOperatorState(changeEventOperatorStateTestVmodel);
		assertEquals(OperatorState.APPROVED.name(), eventRepo.findByEventIdAndIsDeletedFalse(event1.getEventId()).get().getOperatorState());
	}
	
	@Test
	public void setEventOrderNumberTest() {
		adminEventService.setEventOrderNumber(event1.getEventId(), 1);
		adminEventService.setEventOrderNumber(event2.getEventId(), 2);
		assertEquals(1, eventRepo.findByEventIdAndIsDeletedFalse(event1.getEventId()).get().getOrderNumber());
		assertEquals(2, eventRepo.findByEventIdAndIsDeletedFalse(event2.getEventId()).get().getOrderNumber());
	}
	
	@Test
	public void getAllEventsTest() {
		Pageable pageable = new PageRequest(0,5);
		Page<EventFlatViewModel> vmodels = adminEventService.getAllEvents(pageable);
		assertEquals(2, vmodels.getNumberOfElements());
	}
	
	@Test
	public void getEventTest() {
		EventFlatViewModel vmodel = adminEventService.getFlatEvent(event2.getEventId());
		assertEquals(event2.getEventId(), vmodel.getEventId());
		assertEquals(1000, vmodel.getEventDates().stream().findFirst().get().getPrice());
	}
	
	
	@Test(expected = NotFoundException.class)
	public void getFromRepoTest() {
		adminEventService.getEventFromRepository(3453);
	}
	
	@Test
	public void getEventBlitBuyersByEventDateTest() {
		Pageable pageable = new PageRequest(0,5);
		Page<BlitBuyerViewModel> lists = adminEventService.getEventBlitBuyersByEventDate(sans1_1.getEventDateId(), pageable);
		assertEquals(2, lists.getNumberOfElements());
	}
	
	@Test
	public void getAllPendingEventsTest() {
		Pageable pageable = new PageRequest(0,5);
		Page<EventViewModel> pendingEvents = adminEventService.getAllPendingEvents(pageable);
		assertEquals(2, pendingEvents.getNumberOfElements());
		adminEventService.changeOperatorState(changeEventOperatorStateTestVmodel);
		pendingEvents = adminEventService.getAllPendingEvents(pageable);
		assertEquals(1, pendingEvents.getNumberOfElements());
	}
}
