package blito.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.configs.Constants;
import com.blito.enums.DayOfWeek;
import com.blito.enums.EventType;
import com.blito.enums.HostType;
import com.blito.enums.ImageType;
import com.blito.enums.State;
import com.blito.models.Blit;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.DiscountRepository;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.payments.SamanPaymentRequestResponseViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.BlitService;
import com.blito.services.EventService;
import com.blito.services.PaymentRequestServiceAsync;
import com.blito.services.PaymentService;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

@ActiveProfiles("test")
@RunWith(JMockit.class)
@SpringBootTest(classes=Application.class)
public class BlitServicePaymentTest {
	
	@Tested
	BlitService blitService;
	@Injectable
	BlitRepository blitRepository;
	@Injectable
	EventRepository eventRepository;
	@Injectable
	EventService eventService;
	@Injectable
	UserRepository userRepository;
	@Injectable
	EventHostRepository eventHostRepository;
	@Injectable
	DiscountRepository discountRepo;
	@Injectable
	BlitTypeRepository blitTypeRepo;
	@Injectable
	ImageRepository imageRepository;
	@Injectable
	PaymentRequestServiceAsync paymentService;
	EventHost eventHost;
	private EventViewModel eventViewModel = null;
	private BlitTypeViewModel blitTypeViewModel = null;
	private User user = null;

	@Before
	public void init()
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

		SecurityContextHolder.setCurrentUser(user);

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
		BlitTypeViewModel blitTypeViewModel2 = new BlitTypeViewModel();
		eventDateViewModel.setDate(Timestamp.from(ZonedDateTime.now().plusDays(10).toInstant()));

		blitTypeViewModel1.setCapacity(20);
		blitTypeViewModel1.setFree(true);
		blitTypeViewModel1.setName("MYBLIT");

		blitTypeViewModel2.setCapacity(30);
		blitTypeViewModel2.setFree(false);
		blitTypeViewModel2.setName("neshaste");
		blitTypeViewModel2.setPrice(40000);

		eventDateViewModel.getBlitTypes().add(blitTypeViewModel1);
		eventDateViewModel.getBlitTypes().add(blitTypeViewModel2);
		eventViewModel.getEventDates().add(eventDateViewModel);

		System.err.println(eventRepository.count() + "*************************");
		blitTypeViewModel = blitTypeViewModel1;

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

	}

	
	@Test
	public void buyCommonBlitTest()
	{
		new Expectations(paymentService) {{
			blitService.generateTrackCode();
			result = "85394723";
			paymentService.samanBankRequestToken("85394723", 440000);
			result = "myToken";
		}};
		
		eventViewModel = eventService.create(eventViewModel);
		blitTypeRepo.save(blitTypeRepo.findAll().stream().map(b -> {
			b.setBlitTypeState(State.OPEN.name());
			return b;
		}).collect(Collectors.toList()));

		CommonBlitViewModel vmodel = new CommonBlitViewModel();
		vmodel.setBlitTypeId(eventViewModel.getEventDates().stream().flatMap(ed -> ed.getBlitTypes().stream())
				.filter(bt -> !bt.isFree()).findFirst().get().getBlitTypeId());
		vmodel.setBlitTypeName(blitTypeViewModel.getName());
		vmodel.setCount(11);
		vmodel.setCustomerEmail(user.getEmail());
		vmodel.setCustomerMobileNumber(user.getMobile());
		vmodel.setCustomerName(user.getFirstname() + " " + user.getLastname());
		vmodel.setTotalAmount(0);
		vmodel.setEventAddress(eventViewModel.getAddress());
		vmodel.setEventDate(eventViewModel.getEventDates().stream().findFirst().get().getDate());
		vmodel.setEventName(eventViewModel.getEventName());
		
		blitService.createCommonBlitAuthorized(vmodel)
		.thenAccept(res -> {
			SamanPaymentRequestResponseViewModel samanRes = 
					(SamanPaymentRequestResponseViewModel)res;
			assertEquals("myToken",samanRes.getToken());
			Optional<Blit> blitRes = blitRepository.findByToken(samanRes.getToken());
			if(blitRes.isPresent())
			{
				assertEquals("85394723",blitRes.get().getTrackCode());
			} else {
				assertTrue(false);
			}
		});
		
	}
	
}
