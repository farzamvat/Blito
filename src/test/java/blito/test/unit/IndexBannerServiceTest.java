package blito.test.unit;

import static org.junit.Assert.assertNotNull;

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
import org.springframework.transaction.annotation.Transactional;

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
import com.blito.repositories.IndexBannerRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.BannerViewModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.IndexBannerService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@Transactional
public class IndexBannerServiceTest {

	@Autowired
	EventRepository eventRepository;
	@Autowired
	IndexBannerService indexBannerService;
	@Autowired
	IndexBannerRepository indexBannerRepository;
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
	private Image image1 = null;

	@Before
	public void init() {
			
			
			User user = new User();
			user.setEmail("farzam.vat@gmail.com");
			user.setActive(true);
			user.setFirstname("farzam");
			user.setLastname("vatanzadeh");
			user.setMobile("09124337522");

			user = userRepository.save(user);
			
			image1 = new Image();
			image1.setImageUUID(Constants.DEFAULT_HOST_PHOTO);
			image1.setImageType(ImageType.HOST_PHOTO);
			imageRepository.save(image1);

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
			
			System.err.println(eventRepository.count() + "*************************");
	}
	
	@Test
	public void create()
	{
		BannerViewModel vmodel = new BannerViewModel();
		vmodel.setTitle("Title");
		vmodel.setDescription("description");
		vmodel.setEventId(event.getEventId());
		vmodel.setActive(true);
		ImageViewModel image = new ImageViewModel();
		image.setImageUUID(this.image1.getImageUUID());
		image.setType(this.image1.getImageType());
		vmodel.setImage(image);
		
		BannerViewModel resultViewModel = indexBannerService.create(vmodel);
		assertNotNull(indexBannerRepository.findOne(resultViewModel.getIndexBannerId()));
		assertNotNull(indexBannerRepository.findOne(resultViewModel.getIndexBannerId()).getEvent());
	}
}