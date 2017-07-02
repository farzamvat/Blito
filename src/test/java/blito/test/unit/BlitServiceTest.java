package blito.test.unit;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.configs.Constants;
import com.blito.enums.EventType;
import com.blito.enums.HostType;
import com.blito.enums.ImageType;
import com.blito.enums.State;
import com.blito.models.BlitType;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.repositories.DiscountRepository;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.blittype.ChangeBlitTypeStateVm;
import com.blito.rest.viewmodels.event.ChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.AdminEventService;
import com.blito.services.BlitService;
import com.blito.services.EventService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
//@Transactional
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
	

	private EventHost eventHost = new EventHost();
	private EventViewModel eventViewModel = new EventViewModel();
	private User user = new User();
	private CommonBlitViewModel commonBlitViewModel = new CommonBlitViewModel();
	private EventDateViewModel eventDateViewModel = new EventDateViewModel();
	private BlitTypeViewModel blitTypeViewModel1 = new BlitTypeViewModel();
	private BlitTypeViewModel blitTypeViewModel2 = new BlitTypeViewModel();
	private ChangeBlitTypeStateVm changeBlitTypeStateVm = new ChangeBlitTypeStateVm();
	private ChangeEventStateVm changeEventStateVm = new ChangeEventStateVm();
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Before
	public void init() {

		user.setEmail("farzam.vat@gmail.com");
		user.setActive(true);
		user.setFirstname("farzam");
		user.setLastname("vatanzadeh");
		user.setMobile("09124337522");

		user = userRepository.save(user);

		SecurityContextHolder.setCurrentUser(user);

		eventHost.setHostName("hostname12");
		eventHost.setHostType(HostType.THEATER);
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
		image.setImageType(ImageType.EVENT_PHOTO);
		image.setImageUUID(Constants.DEFAULT_HOST_PHOTO);

		Image hostCoverPhoto = new Image();
		hostCoverPhoto.setImageType(ImageType.HOST_COVER_PHOTO);
		hostCoverPhoto.setImageUUID(Constants.DEFAULT_HOST_COVER_PHOTO);

		Image exchangeBlitPhoto = new Image();
		exchangeBlitPhoto.setImageType(ImageType.EXCHANGEBLIT_PHOTO);
		exchangeBlitPhoto.setImageUUID(Constants.DEFAULT_EXCHANGEBLIT_PHOTO);

		Image eventPhoto = new Image();
		eventPhoto.setImageType(ImageType.EVENT_PHOTO);
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
		
		commonBlitViewModel.setBlitTypeId(blitTypeId);
		commonBlitViewModel.setCount(20);
		commonBlitViewModel.setEventName(eventViewModel.getEventName());
		commonBlitViewModel.setUserId(user.getUserId());
		commonBlitViewModel.setCustomerName(user.getFirstname()+ " " + user.getLastname());
		commonBlitViewModel.setEventDate(eventDateViewModel.getDate());
		
		blitService.createCommonBlit(commonBlitViewModel);

		BlitType blitType = blitTypeRepo.findOne(blitTypeId);
		assertEquals(1, commonBlitRepo.count());
		assertEquals(20, blitType.getSoldCount());
		assertEquals(State.SOLD, blitType.getBlitTypeState());
		
	}
}
