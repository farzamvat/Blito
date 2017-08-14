package blito.test.unit;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.configs.Constants;
import com.blito.enums.EventType;
import com.blito.enums.HostType;
import com.blito.enums.ImageType;
import com.blito.enums.State;
import com.blito.models.CommonBlit;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.blittype.ChangeBlitTypeStateVm;
import com.blito.rest.viewmodels.event.ChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.ChangeEventDateStateVm;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.AdminEventService;
import com.blito.services.EventService;
import com.blito.services.PaymentService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class PaymentServiceTest {
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	EventService eventService;
	@Autowired
	EventHostRepository eventHostRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	BlitTypeRepository blitTypeRepository;
	@Autowired
	PaymentService paymentService;
	@Autowired
	AdminEventService adminEventService;
	@Autowired
	EventRepository eventRepository;
	public EventViewModel eventViewModel = null;
	public EventHost eventHost = null;
	public User user = null;
	@Autowired
	public CommonBlitRepository blitRepository;
	
	public EventViewModel createEvent()
	{
		user = new User();
		user.setEmail("farzam.vat@gmail.com");
		user.setActive(true);
		user.setFirstname("farzam");
		user.setLastname("vatanzadeh");
		user.setMobile("09124337522");

		user = userRepository.save(user);

		eventHost = new EventHost();
		eventHost.setHostName("hostname12");
		eventHost.setHostType(HostType.THEATER.name());
		eventHost.setTelephone("02188002116");
		eventHost.setUser(user);

		eventHost = eventHostRepository.save(eventHost);
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
		BlitTypeViewModel blitTypeViewModel1 = new BlitTypeViewModel();
		eventDateViewModel.setDate(Timestamp.from(ZonedDateTime.now().plusDays(10).toInstant()));
		

		blitTypeViewModel1.setCapacity(20);
		blitTypeViewModel1.setFree(false);
		blitTypeViewModel1.setName("vaysade");
		blitTypeViewModel1.setPrice(2000);


		eventDateViewModel.setBlitTypes(Arrays.asList(blitTypeViewModel1).stream().collect(Collectors.toSet()));
		eventViewModel.setEventDates(Arrays.asList(eventDateViewModel).stream().collect(Collectors.toSet()));

		Image image = new Image();
		image.setImageType(ImageType.EVENT_PHOTO.name());
		image.setImageUUID(Constants.DEFAULT_HOST_PHOTO);

		Image hostCoverPhoto = new Image();
		image.setImageType(ImageType.HOST_COVER_PHOTO.name());
		image.setImageUUID(Constants.DEFAULT_HOST_COVER_PHOTO);

		Image exchangeBlitPhoto = new Image();
		image.setImageType(ImageType.EXCHANGEBLIT_PHOTO.name());
		image.setImageUUID(Constants.DEFAULT_EXCHANGEBLIT_PHOTO);

		Image eventPhoto = new Image();
		image.setImageType(ImageType.EVENT_PHOTO.name());
		image.setImageUUID(Constants.DEFAULT_EVENT_PHOTO);

		imageRepository.save(image);
		imageRepository.save(hostCoverPhoto);
		imageRepository.save(exchangeBlitPhoto);
		imageRepository.save(eventPhoto);
		
		SecurityContextHolder.setCurrentUser(user);
		
		return eventService.create(eventViewModel);

	}
	
	@Test
	public void persistCommonBlit()
	{
		
		eventViewModel = createEvent();
		ChangeEventStateVm changeEventState = new ChangeEventStateVm();
		changeEventState.setState(State.OPEN);
		changeEventState.setEventId(1L);
		adminEventService.changeEventState(changeEventState);
		
		ChangeEventDateStateVm eventDateState = new ChangeEventDateStateVm();
		eventDateState.setEventDateId(1L);
		eventDateState.setEventDateState(State.OPEN);
		adminEventService.changeEventDateState(eventDateState);
		ChangeBlitTypeStateVm blitTypeState = new ChangeBlitTypeStateVm();
//		blitTypeState.setBlitTypeState(State.OPEN);
//		blitTypeState.setBlitTypeId(1L);
//		adminEventService.changeBlitTypeState(blitTypeState);
		
		CommonBlit blit = new CommonBlit();
		blit.setBankGateway("ZARINPAL");
		blit.setBlitType(blitTypeRepository.findOne(1L));
		blit.setBlitTypeName(blitTypeRepository.findOne(1L).getName());
		blit.setCount(20);
		blit.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
		blit.setCustomerEmail("farzam.vat@gmail.com");
		blit.setCustomerMobileNumber("09124337522");
		blit.setCustomerName("Farzam Vatanzadeh");
		blit.setEventAddress("Address");
		blit.setEventDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).plusDays(2L).toInstant()));
		blit.setEventDateAndTime("Event Date and Time");
		blit.setPaymentStatus("PENDING");
		blit.setTrackCode("12345678");
		blit.setTotalAmount(40000);
		blit.setToken("000000000123");
		blit.setSeatType("COMMON");
		blit = blitRepository.save(blit);
		paymentService.persistZarinpalBoughtBlit(blit, blit.getToken(), "refNum", "pardakht shod");
		
		assertEquals("SOLD",eventRepository.findOne(1L).getEventState());
		
	}
	
}
