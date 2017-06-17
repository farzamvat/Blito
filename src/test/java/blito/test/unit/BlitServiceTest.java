package blito.test.unit;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.blito.Application;
import com.blito.enums.DayOfWeek;
import com.blito.enums.EventType;
import com.blito.enums.HostType;
import com.blito.models.BlitType;
import com.blito.models.EventHost;
import com.blito.models.User;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.DiscountRepository;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.BlitService;
import com.blito.services.EventService;

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
	EventHost eventHost;
	private EventViewModel eventViewModel = null;
	private BlitTypeViewModel blitTypeViewModel = null;
	private User user = null;

	@Before
	public void init() {

		user = new User();
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
		eventDateViewModel.setDayOfWeek(DayOfWeek.SATURDAY);

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

	}

	@Test
	public void testFreeBlit()
	{
		eventViewModel = eventService.create(eventViewModel);
		
		CommonBlitViewModel vmodel = new CommonBlitViewModel();
		vmodel.setBlitTypeId(eventViewModel.getEventDates().stream().flatMap(ed -> ed.getBlitTypes().stream()).filter(bt -> bt.isFree()).findFirst().get().getBlitTypeId());
		vmodel.setBlitTypeName(blitTypeViewModel.getName());
		vmodel.setCount(11);
		vmodel.setCustomerEmail(user.getEmail());
		vmodel.setCustomerMobileNumber(user.getMobile());
		vmodel.setCustomerName(user.getFirstname() + " " + user.getLastname());
		vmodel.setTotalAmount(0);
		vmodel.setEventAddress(eventViewModel.getAddress());
		vmodel.setEventDate(eventViewModel.getEventDates().get(0).getDate());
		vmodel.setEventName(eventViewModel.getEventName());
		IntStream.range(0,100).parallel().forEach(i -> {
			blitService.createCommonBlit(vmodel);
		});
		
		BlitType blitType = blitTypeRepo.findOne(vmodel.getBlitTypeId());
		assertEquals(11,blitType.getSoldCount());
	}
}
