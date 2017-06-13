package blito.test.unit;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Arrays;

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
import org.springframework.transaction.annotation.Transactional;

import com.blito.Application;
import com.blito.enums.BlitTypeEnum;
import com.blito.enums.HostType;
import com.blito.enums.OperatorState;
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
import com.blito.rest.viewmodels.event.AdminChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventFlatViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.AdminEventService;

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
	EventDateMapper dateMapper;
	@Autowired
	BlitTypeMapper blitTypeMapper;
	@Autowired
	CommonBlitRepository cBlitRepo;
	
	private User user = new User();
	
	private EventHost eventHost= new EventHost();
	
	private Event event1 = new Event();
	private Event event2 = new Event();
	
	private EventDate sans1_1 = new EventDate();
	private EventDate sans1_2 = new EventDate();
	
	private EventDate sans2_1 = new EventDate();
	
	private BlitType blitType1_1_1 = new BlitType();
	private BlitType blitType1_2_1 = new BlitType();
	
	private BlitType blitType2_1_1 = new BlitType();
	
	private AdminChangeEventStateVm changeEventStateTestVmodel = new AdminChangeEventStateVm();
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
			user.setActive(true);
			SecurityContextHolder.setCurrentUser(userRepo.save(user));
			
			eventHost.setHostName("paliz theater");
			eventHost.setHostType(HostType.CULTURALCENTER);
			eventHost.setTelephone("22431103");
			eventHost.setUser(user);
			eventHost = eventHostRepo.save(eventHost);
			
			event1.setEventName("akharin naameh");
			event1.setEventHost(eventHost);
			
			event2.setEventName("jashne piroozi");
			event2.setEventHost(eventHost);
			
			sans1_1.setDate(new Timestamp(1495264909518L));
			sans1_2.setDate(new Timestamp(1495264909518L));
			sans2_1.setDate(new Timestamp(1495264909518L));
			
			blitType1_1_1.setName("seated1");
			blitType1_1_1.setCapacity(50);
			blitType1_1_1.setPrice(2500);
			blitType1_1_1.setEventDate(sans1_1);
			
			blitType1_2_1.setName("seated2");
			blitType1_2_1.setCapacity(50);
			blitType1_2_1.setPrice(2500);
			blitType1_2_1.setEventDate(sans1_2);
			
			
			blitType2_1_1.setName("standing");
			blitType2_1_1.setCapacity(100);
			blitType2_1_1.setPrice(1000);
			blitType2_1_1.setEventDate(sans2_1);
			
			sans1_1.setBlitTypes(Arrays.asList(blitType1_1_1));
			sans1_1.setEvent(event1);
			
			sans1_2.setBlitTypes(Arrays.asList(blitType1_2_1));
			sans1_2.setEvent(event1);
			
			sans2_1.setBlitTypes(Arrays.asList(blitType2_1_1));
			sans2_1.setEvent(event2);
			
			event1.setEventDates(Arrays.asList(sans1_1, sans1_2));
			event1 = eventRepo.save(event1);
			
			event2.setEventDates(Arrays.asList(sans2_1));
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
			eventDateUpdateVmodel1.setBlitTypes(Arrays.asList(blitTypeUpdateVmodel1));
			eventDateUpdateVmodel2.setBlitTypes(Arrays.asList(blitTypeUpdateVmodel2));
			eventUpdateVmodel.setEventDates(Arrays.asList(eventDateUpdateVmodel1, eventDateUpdateVmodel2));
			
			cblit1.setCount(5);
			cblit1.setEventName(event1.getEventName());
			cblit1.setType(BlitTypeEnum.COMMON);
			cblit1.setBlitType(blitType1_1_1);
			cblit1.setUser(user);
			cblit1 = cBlitRepo.save(cblit1);
			
			
			cblit2.setCount(2);
			cblit2.setEventName(event1.getEventName());
			cblit2.setType(BlitTypeEnum.COMMON);
			cblit2.setBlitType(blitType1_1_1);
			cblit2.setUser(user);
			cblit2 = cBlitRepo.save(cblit2);
			
			blitType1_1_1.setCommonBlits(Arrays.asList(cblit1, cblit2));
			
			cblit3.setCount(4);
			cblit3.setEventName(event1.getEventName());
			cblit3.setType(BlitTypeEnum.COMMON);
			cblit3.setBlitType(blitType1_2_1);
			cblit3.setUser(user);
			cblit3 = cBlitRepo.save(cblit3);
			
			cblit4.setCount(7);
			cblit4.setEventName(event1.getEventName());
			cblit4.setType(BlitTypeEnum.COMMON);
			cblit4.setBlitType(blitType1_2_1);
			cblit4.setUser(user);
			cblit4 = cBlitRepo.save(cblit4);
			
	}
	
	@Test
	public void changeEventStateTest() {
		adminEventService.changeEventState(changeEventStateTestVmodel);
		assertEquals(State.CLOSED, eventRepo.findOne(event1.getEventId()).getEventState());
		changeEventStateTestVmodel.setState(State.OPEN);
		adminEventService.changeEventState(changeEventStateTestVmodel);
		assertEquals(State.OPEN, eventRepo.findOne(event1.getEventId()).getEventState());
	}
	
	@Test
	public void changeOperatorStateTest() {
		adminEventService.changeOperatorState(changeEventOperatorStateTestVmodel);
		assertEquals(OperatorState.APPROVED, eventRepo.findOne(event1.getEventId()).getOperatorState());
	}
	
	@Test
	public void setEventOrderNumberTest() {
		adminEventService.setEventOrderNumber(event1.getEventId(), 1);
		adminEventService.setEventOrderNumber(event2.getEventId(), 2);
		assertEquals(1, eventRepo.findOne(event1.getEventId()).getOrderNumber());
		assertEquals(2, eventRepo.findOne(event2.getEventId()).getOrderNumber());
	}
	
	@Test
	public void getAllEventsTest() {
		Pageable pageable = new PageRequest(0,5);
		Page<EventFlatViewModel> vmodels = adminEventService.getAllEvents(pageable);
		assertEquals(2, vmodels.getNumberOfElements());
	}
	
	@Test
	public void getEventTest() {
		EventFlatViewModel vmodel = adminEventService.getEvent(event2.getEventId());
		assertEquals(event2.getEventId(), vmodel.getEventId());
		assertEquals(1000, vmodel.getEventDates().get(0).getPrice());
	}
	
	@Test
	public void updateEventTest() {
		EventFlatViewModel vmodel = adminEventService.updateEvent(eventUpdateVmodel);
		assertEquals("jashne banafsh", vmodel.getEventName());
		assertEquals("jashne banafsh", eventRepo.findOne(event1.getEventId()).getEventName());
		assertEquals("VIP", vmodel.getEventDates().get(0).getName());
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
}
