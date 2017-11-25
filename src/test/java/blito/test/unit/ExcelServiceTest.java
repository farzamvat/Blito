package blito.test.unit;

import com.blito.Application;
import com.blito.configs.Constants;
import com.blito.enums.*;
import com.blito.mappers.CommonBlitMapper;
import com.blito.models.CommonBlit;
import com.blito.models.EventHost;
import com.blito.models.User;
import com.blito.repositories.*;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.blittype.ChangeBlitTypeStateVm;
import com.blito.rest.viewmodels.event.AdditionalField;
import com.blito.rest.viewmodels.event.ChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.ChangeEventDateStateVm;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.search.Operation;
import com.blito.search.SearchViewModel;
import com.blito.search.Simple;
import com.blito.security.SecurityContextHolder;
import com.blito.services.*;
import com.blito.services.blit.CommonBlitService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class ExcelServiceTest {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	EventHostRepository eventHostRepository;
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	EventService eventService;
	@Autowired
	AdminEventService adminEventService;
	@Autowired
	CommonBlitService blitService;
	@Autowired
	SearchService searchService;
	@Autowired
	CommonBlitMapper commonBlitMapper;
	@Autowired
	CommonBlitRepository commonBlitRepository;
	@Autowired
	EventRepository eventRepository;
	@Autowired
	PaymentRequestService paymentRequestService;
	
	private EventHost eventHost = new EventHost();
	private EventViewModel eventViewModel = new EventViewModel();
	private User user1 = new User();
	private User user2 = new User();
	private CommonBlitViewModel commonBlitViewModel1 = new CommonBlitViewModel();
	private CommonBlitViewModel commonBlitViewModel2 = new CommonBlitViewModel();
	private EventDateViewModel eventDateViewModel = new EventDateViewModel();
	private BlitTypeViewModel blitTypeViewModel1 = new BlitTypeViewModel();
	private BlitTypeViewModel blitTypeViewModel2 = new BlitTypeViewModel();
	private ChangeBlitTypeStateVm changeBlitTypeStateVm = new ChangeBlitTypeStateVm();
	private ChangeEventStateVm changeEventStateVm = new ChangeEventStateVm();
	private ChangeEventDateStateVm changeEventDateStateVm = new ChangeEventDateStateVm();
	

	@Before
	public void init() {
		user1.setEmail("farzam.vat@gmail.com");
		user1.setActive(true);
		user1.setFirstname("farzam");
		user1.setLastname("vatanzadeh");
		user1.setMobile("09124337522");

		user1 = userRepository.save(user1);

		SecurityContextHolder.setCurrentUser(user1);
		
		user2.setEmail("hasti.sahabi@gmail.com");
		user2.setActive(true);
		user2.setFirstname("hasti");
		user2.setLastname("sahabi");
		user2.setMobile("09127976837");

		user2 = userRepository.save(user2);

		eventHost.setHostName("hostname12");
		eventHost.setHostType(HostType.THEATER.name());
		eventHost.setTelephone("02188002116");
		eventHost.setUser(user1);

		eventHost = eventHostRepository.save(eventHost);

		eventViewModel.setAddress("Amirabad");
		eventViewModel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
		eventViewModel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
		eventViewModel.setDescription("Description");
		eventViewModel.setEventHostId(eventHost.getEventHostId());
		eventViewModel.setEventHostName(eventHost.getHostName());
		eventViewModel.setEventName("My Event");
		eventViewModel.setEventType(EventType.CONCERT);

		eventViewModel.setAdditionalFields(Arrays.asList(new AdditionalField("Student Number", Constants.FIELD_INT_TYPE),
				new AdditionalField("Gender",Constants.FIELD_STRING_TYPE),
				new AdditionalField("Father's Name",Constants.FIELD_STRING_TYPE),
				new AdditionalField("Weight",Constants.FIELD_DOUBLE_TYPE)));

		eventDateViewModel.setDate(Timestamp.from(ZonedDateTime.now().plusDays(10).toInstant()));

		blitTypeViewModel1.setCapacity(20);
		blitTypeViewModel1.setFree(true);
		blitTypeViewModel1.setName("FREE");

		blitTypeViewModel2.setCapacity(30);
		blitTypeViewModel2.setFree(false);
		blitTypeViewModel2.setName("POOLI");
		blitTypeViewModel2.setPrice(40000);

		eventDateViewModel.getBlitTypes().add(blitTypeViewModel1);
		eventDateViewModel.getBlitTypes().add(blitTypeViewModel2);
		eventViewModel.getEventDates().add(eventDateViewModel);

	}
	
	@Test
	public void getBlitsExcelMapWithAdditionalFieldsTest() {
		eventViewModel = eventService.create(eventViewModel);

		assertEquals(4, eventViewModel.getAdditionalFields().size());
		
		long blitTypeId = eventViewModel.getEventDates().stream().flatMap(ed -> ed.getBlitTypes().stream())
				.filter(bt -> bt.getName().equals("FREE")).findFirst().get().getBlitTypeId();
		
		changeBlitTypeStateVm.setBlitTypeId(blitTypeId);
		changeBlitTypeStateVm.setBlitTypeState(State.OPEN);
		adminEventService.changeBlitTypeState(changeBlitTypeStateVm);
		
		changeEventStateVm.setEventId(eventViewModel.getEventId());
		changeEventStateVm.setState(State.OPEN);
		adminEventService.changeEventState(changeEventStateVm);
		
		changeEventDateStateVm.setEventDateId(eventViewModel.getEventDates().stream().findFirst().get().getEventDateId());
		changeEventDateStateVm.setEventDateState(State.OPEN);
		adminEventService.changeEventDateState(changeEventDateStateVm);
		
		commonBlitViewModel1.setBlitTypeId(blitTypeId);
		commonBlitViewModel1.setCount(4);
		commonBlitViewModel1.setSeatType(SeatType.COMMON);
		commonBlitViewModel1.setEventName(eventViewModel.getEventName());
		commonBlitViewModel1.setUserId(user1.getUserId());
		commonBlitViewModel1.setCustomerEmail(user1.getEmail());
		commonBlitViewModel1.setCustomerName(user1.getFirstname()+ " " + user1.getLastname());
		commonBlitViewModel1.setEventDate(eventDateViewModel.getDate());
		commonBlitViewModel1.setBankGateway(BankGateway.NONE);

		commonBlitViewModel1.setAdditionalFields(Arrays.asList(new AdditionalField("Student Number", "90213164"),
				new AdditionalField("Gender", "male"),
				new AdditionalField("Father's Name", "Mostafa"),
				new AdditionalField("Weight", "94")));

		commonBlitViewModel2.setBlitTypeId(blitTypeId);
		commonBlitViewModel2.setCount(5);
		commonBlitViewModel2.setSeatType(SeatType.COMMON);
		commonBlitViewModel2.setEventName(eventViewModel.getEventName());
		commonBlitViewModel2.setUserId(user2.getUserId());
		commonBlitViewModel2.setCustomerName(user2.getFirstname()+ " " + user2.getLastname());
		commonBlitViewModel2.setCustomerEmail(user2.getEmail());
		commonBlitViewModel2.setEventDate(eventDateViewModel.getDate());
		commonBlitViewModel2.setBankGateway(BankGateway.NONE);

		commonBlitViewModel2.setAdditionalFields(Arrays.asList(new AdditionalField("Student Number", "90213086"),
				new AdditionalField("Gender", "female"),
				new AdditionalField("Father's Name", "Hamed"),
				new AdditionalField("Weight", "40.7")));

		blitService.createBlitAuthorized(commonBlitViewModel1,SecurityContextHolder.currentUser());
		blitService.createBlitAuthorized(commonBlitViewModel2,SecurityContextHolder.currentUser());

		SearchViewModel<CommonBlit> searchViewModel = new SearchViewModel<>();
		Simple<CommonBlit> simple = new Simple<>();
		simple.setField("blitType-blitTypeId");
		simple.setValue(blitTypeId);
		simple.setOperation(Operation.eq);

		searchViewModel.setRestrictions(new ArrayList<>());
		searchViewModel.getRestrictions().add(simple);

		Map<String, Object> map = blitService.searchBlitsForExcel(searchViewModel);
		
		map.values();
	}

}
