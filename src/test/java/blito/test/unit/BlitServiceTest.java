package blito.test.unit;

import com.blito.Application;
import com.blito.configs.Constants;
import com.blito.enums.*;
import com.blito.models.BlitType;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.*;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.blittype.ChangeBlitTypeStateVm;
import com.blito.rest.viewmodels.event.ChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.ChangeEventDateStateVm;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.AdminEventService;
import com.blito.services.BlitService;
import com.blito.services.EventService;
import com.blito.services.PaymentRequestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class BlitServiceTest {
	@Autowired
	BlitService blitService;
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
	AdminEventService adminEventService;
	@Autowired
	CommonBlitRepository commonBlitRepo;
	@Autowired
	PaymentRequestService paymentRequestService;
	

	private EventHost eventHost = new EventHost();
	private EventViewModel eventViewModel = new EventViewModel();
	private User user = new User();
	private EventDateViewModel eventDateViewModel = new EventDateViewModel();
	private BlitTypeViewModel blitTypeViewModel1 = new BlitTypeViewModel();
	private BlitTypeViewModel blitTypeViewModel2 = new BlitTypeViewModel();
	private ChangeBlitTypeStateVm changeBlitTypeStateVm = new ChangeBlitTypeStateVm();
	private ChangeEventStateVm changeEventStateVm = new ChangeEventStateVm();
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Before
	public void init() {

		user.setEmail("hasti.sahabi@yahoo.com");
		user.setActive(true);
		user.setFirstname("hasti");
		user.setLastname("sahabi");
		user.setMobile("09127976837");

		user = userRepository.save(user);

		SecurityContextHolder.setCurrentUser(user);

		eventHost.setHostName("hostname12");
		eventHost.setHostType(HostType.THEATER.name());
		eventHost.setTelephone("02188002116");
		eventHost.setUser(user);

		eventHost = eventHostRepository.save(eventHost);

		eventViewModel.setAddress("Amirabad");
		eventViewModel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
		eventViewModel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
		eventViewModel.setDescription("Description");
		eventViewModel.setEventHostId(eventHost.getEventHostId());
		eventViewModel.setEventHostName(eventHost.getHostName());
		eventViewModel.setEventName("My Event");
		eventViewModel.setEventType(EventType.CONCERT);
		eventViewModel.setLatitude(2.2);
		eventViewModel.setLongitude(3.2);

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

		Image image = new Image();
		image.setImageType(ImageType.EVENT_PHOTO.name());
		image.setImageUUID(Constants.DEFAULT_HOST_PHOTO);

		Image hostCoverPhoto = new Image();
		hostCoverPhoto.setImageType(ImageType.HOST_COVER_PHOTO.name());
		hostCoverPhoto.setImageUUID(Constants.DEFAULT_HOST_COVER_PHOTO_1);

		Image exchangeBlitPhoto = new Image();
		exchangeBlitPhoto.setImageType(ImageType.EXCHANGEBLIT_PHOTO.name());
		exchangeBlitPhoto.setImageUUID(Constants.DEFAULT_EXCHANGEBLIT_PHOTO);

		Image eventPhoto = new Image();
		eventPhoto.setImageType(ImageType.EVENT_PHOTO.name());
		eventPhoto.setImageUUID(Constants.DEFAULT_EVENT_PHOTO);

		imageRepository.save(image);
		imageRepository.save(hostCoverPhoto);
		imageRepository.save(exchangeBlitPhoto);
		imageRepository.save(eventPhoto);

	}

	@Test
	public void testFreeBlit() throws InterruptedException {
		eventViewModel = eventService.create(eventViewModel);

		long blitTypeId = eventViewModel.getEventDates().stream().flatMap(ed -> ed.getBlitTypes().stream())
				.filter(bt -> bt.getName().equals("FREE")).findFirst().get().getBlitTypeId();
		
		changeBlitTypeStateVm.setBlitTypeId(blitTypeId);
		changeBlitTypeStateVm.setBlitTypeState(State.OPEN);
		adminEventService.changeBlitTypeState(changeBlitTypeStateVm);
		
		changeEventStateVm.setEventId(eventViewModel.getEventId());
		changeEventStateVm.setState(State.OPEN);
		adminEventService.changeEventState(changeEventStateVm);
		
		ChangeEventDateStateVm vm = new ChangeEventDateStateVm();
		vm.setEventDateId(eventViewModel.getEventDates().stream().findFirst().get().getEventDateId());
		vm.setEventDateState(State.OPEN);
		adminEventService.changeEventDateState(vm);
		
		CommonBlitViewModel commonBlitViewModel = new CommonBlitViewModel();
		commonBlitViewModel.setBlitTypeId(blitTypeId);
		commonBlitViewModel.setCount(1);
		commonBlitViewModel.setCustomerEmail(user.getEmail());
		commonBlitViewModel.setSeatType(SeatType.COMMON);
		commonBlitViewModel.setEventName(eventViewModel.getEventName());
		commonBlitViewModel.setUserId(user.getUserId());
		commonBlitViewModel.setCustomerName(user.getFirstname()+ " " + user.getLastname());
		commonBlitViewModel.setEventDate(eventDateViewModel.getDate());
		commonBlitViewModel.setBankGateway(BankGateway.NONE);
		IntStream.range(1, 3).parallel().forEach(i -> {
			SecurityContextHolder.setCurrentUser(user);
			blitService.createCommonBlitAuthorized(commonBlitViewModel,SecurityContextHolder.currentUser());
		});

		BlitType blitType = blitTypeRepo.findOne(blitTypeId);
		assertEquals(1, commonBlitRepo.count());
		assertEquals(20, blitType.getSoldCount());
		assertEquals(State.SOLD.name(), blitType.getBlitTypeState());
		
	}
}
