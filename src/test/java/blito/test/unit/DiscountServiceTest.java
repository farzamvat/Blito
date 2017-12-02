package blito.test.unit;


import com.blito.Application;
import com.blito.enums.*;
import com.blito.mappers.EventFlatMapper;
import com.blito.models.Event;
import com.blito.models.EventHost;
import com.blito.models.User;
import com.blito.repositories.*;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.DiscountService;
import com.blito.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Transactional
public class DiscountServiceTest {

    @Autowired
    TestRestTemplate rest;
    @Autowired
    ObjectMapper objectMapper;
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
    EventFlatMapper eventFlatMapper;
    @Autowired
    DiscountService discountService;

    Event event;
    Event event1;
    Event event2;
    Event event3;
    Event event4;
    EventHost eventHost1;
    EventHost eventHost2;
    private EventViewModel eventViewModel = null;
    private EventViewModel eventViewModel2 = null;
    private User user2 = new User();
    private User user = new User();

    @Before
    public void init() {

        user.setEmail("farzam.vat@gmail.com");
        user.setActive(true);
        user.setFirstname("farzam");
        user.setLastname("vatanzadeh");
        user.setMobile("09124337522");

        user = userRepository.save(user);

        user2.setEmail("hasti.sahabi@gmail.com");
        user2.setActive(true);
        user2.setFirstname("hasti");
        user2.setLastname("sahabi");
        user2.setMobile("09127976837");

        user2 = userRepository.save(user2);

        eventHost1 = new EventHost();
        eventHost1.setHostName("hostname12");
        eventHost1.setHostType(HostType.THEATER.name());
        eventHost1.setTelephone("02188002116");
        eventHost1.setUser(user);

        eventHost1 = eventHostRepository.save(eventHost1);

        eventHost2 = new EventHost();
        eventHost2.setHostName("hostnamekkkk");
        eventHost2.setHostType(HostType.THEATER.name());
        eventHost2.setTelephone("02188002116");
        eventHost2.setUser(user2);

        eventHost2 = eventHostRepository.save(eventHost2);

        SecurityContextHolder.setCurrentUser(user);

        event = new Event();
        event.setAddress("ABC");
        event.setEventState(State.SOLD.name());
        event.setOperatorState(OperatorState.APPROVED.name());
        event.setEventName("A");
        event.setEventType(EventType.CINEMA.name());
        event.setLatitude(2D);
        event.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
        event.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));



        event1 = new Event();
        event1.setAddress("ABC");
        event1.setEventState(State.OPEN.name());
        event1.setOperatorState(OperatorState.APPROVED.name());
        event1.setEventName("B");
        event1.setLatitude(1D);
        event1.setEventType(EventType.CINEMA.name());
        event1.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(10).toInstant()));
        event1.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(1).toInstant()));

        event2 = new Event();
        event2.setAddress("ABCD");
        event2.setEventState(State.CLOSED.name());
        event2.setOperatorState(OperatorState.APPROVED.name());
        event2.setEventName("C");
        event2.setLatitude(4D);
        event2.setEventType(EventType.CINEMA.name());
        event2.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
        event2.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));

        event3 = new Event();
        event3.setAddress("DFG");
        event3.setEventState(State.OPEN.name());
        event3.setOperatorState(OperatorState.APPROVED.name());
        event3.setOffers(Arrays.asList(OfferTypeEnum.OUR_OFFER.name(), OfferTypeEnum.SPECIAL_OFFER.name()).stream().collect(Collectors.toSet()));
        event3.setEventName("D");
        event3.setLatitude(1D);
        event3.setEventType(EventType.ENTERTAINMENT.name());
        event3.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
        event3.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));

        event4 = new Event();
        event4.setAddress("ABC");
        event4.setEventState(State.OPEN.name());
        event4.setOperatorState(OperatorState.APPROVED.name());
        event4.setOffers(Arrays.asList(OfferTypeEnum.OUR_OFFER.name(), OfferTypeEnum.SPECIAL_OFFER.name()).stream().collect(Collectors.toSet()));
        event4.setEventName("E");
        event4.setLatitude(1D);
        event4.setEventType(EventType.CONCERT.name());
        event4.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().minusHours(24).toInstant()));
        event4.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(2).toInstant()));

        event.setEventHost(eventHost1);
        event1.setEventHost(eventHost1);
        event2.setEventHost(eventHost1);
        event3.setEventHost(eventHost1);
        event4.setEventHost(eventHost1);

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
        eventViewModel.setEventHostId(eventHost1.getEventHostId());
        eventViewModel.setEventHostName(eventHost1.getHostName());
        eventViewModel.setEventName("My Event");
        eventViewModel.setEventType(EventType.CONCERT);

        EventDateViewModel eventDateViewModel = new EventDateViewModel();
        BlitTypeViewModel blitTypeViewModel1 = new BlitTypeViewModel();
        BlitTypeViewModel blitTypeViewModel2 = new BlitTypeViewModel();
        eventDateViewModel.setDate(Timestamp.from(ZonedDateTime.now().plusDays(10).toInstant()));


        blitTypeViewModel1.setCapacity(20);
        blitTypeViewModel1.setFree(false);
        blitTypeViewModel1.setName("vaysade");
        blitTypeViewModel1.setPrice(20000);

        blitTypeViewModel2.setCapacity(30);
        blitTypeViewModel2.setFree(false);
        blitTypeViewModel2.setName("neshaste");
        blitTypeViewModel2.setPrice(40000);

        eventDateViewModel.setBlitTypes(Arrays.asList(blitTypeViewModel1, blitTypeViewModel2).stream().collect(Collectors.toSet()));
        eventViewModel.setEventDates(Arrays.asList(eventDateViewModel).stream().collect(Collectors.toSet()));

    }

    @Test
    public void setDiscountCodeTest() {
        assertEquals(0, discountRepo.count());
        eventViewModel = eventService.create(eventViewModel);

        Event event = eventRepository.findOne(eventViewModel.getEventId());
        event.setEventState(State.OPEN.name());
        event.setOperatorState(OperatorState.APPROVED.name());
        DiscountViewModel vmodel = new DiscountViewModel();

        vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream()
                .flatMap(ed -> ed.getBlitTypes().stream().map(bt -> bt.getBlitTypeId())).collect(Collectors.toSet()));
        vmodel.setCode("TAKHFIF!@#$");
        vmodel.setReusability(5);
        vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
        vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
        vmodel.setPercentage(30D);
        vmodel.setPercent(true);
        vmodel.setAmount(0L);

        DiscountViewModel discountViewModel = discountService.setDiscountCodeByUser(vmodel,user).get();
        assertEquals(1, discountRepo.count());
        assertEquals("TAKHFIF!@#$", discountRepo.findAll().stream().findFirst().get().getCode());
    }

    @Test
    public void setDiscountCodeTestInvalidPercentage() {
        assertEquals(0, discountRepo.count());
        eventViewModel = eventService.create(eventViewModel);

        Event event = eventRepository.findOne(eventViewModel.getEventId());
        event.setEventState(State.OPEN.name());
        event.setOperatorState(OperatorState.APPROVED.name());
        DiscountViewModel vmodel = new DiscountViewModel();

        vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream()
                .flatMap(ed -> ed.getBlitTypes().stream().map(bt -> bt.getBlitTypeId())).collect(Collectors.toSet()));
        vmodel.setCode("TAKHFIF!@#$");
        vmodel.setReusability(5);
        vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
        vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
        vmodel.setPercentage(0D);
        vmodel.setPercent(true);
        vmodel.setAmount(0L);

        Either<ExceptionViewModel,DiscountViewModel> either = discountService.setDiscountCodeByUser(vmodel,user);
        if(either.isLeft()){
            assertTrue(true);
            System.out.println("****");
            System.out.println(either.getLeft().getMessage());
            System.out.println(either.getLeft().getError());
        }
        else {
            assertTrue(false);
        }
    }

    @Test
    public void setDiscountCodeTestInvalidPercentageWithAmount() {
        assertEquals(0, discountRepo.count());
        eventViewModel = eventService.create(eventViewModel);

        Event event = eventRepository.findOne(eventViewModel.getEventId());
        event.setEventState(State.OPEN.name());
        event.setOperatorState(OperatorState.APPROVED.name());
        DiscountViewModel vmodel = new DiscountViewModel();

        vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream()
                .flatMap(ed -> ed.getBlitTypes().stream().map(bt -> bt.getBlitTypeId())).collect(Collectors.toSet()));
        vmodel.setCode("TAKHFIF!@#$");
        vmodel.setReusability(5);
        vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
        vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
        vmodel.setPercentage(30D);
        vmodel.setPercent(true);
        vmodel.setAmount(100L);
        Either<ExceptionViewModel,DiscountViewModel> either = discountService.setDiscountCodeByUser(vmodel,user);
        if(either.isLeft()){
            assertTrue(true);
            System.out.println("****");
            System.out.println(either.getLeft().getMessage());
            System.out.println(either.getLeft().getError());
        }
        else {
            assertTrue(false);
        }
    }

    @Test
    public void setDiscountCodeTestInvalidAmount() {
        assertEquals(0, discountRepo.count());
        eventViewModel = eventService.create(eventViewModel);

        Event event = eventRepository.findOne(eventViewModel.getEventId());
        event.setEventState(State.OPEN.name());
        event.setOperatorState(OperatorState.APPROVED.name());
        DiscountViewModel vmodel = new DiscountViewModel();

        vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream()
                .flatMap(ed -> ed.getBlitTypes().stream().map(bt -> bt.getBlitTypeId())).collect(Collectors.toSet()));
        vmodel.setCode("TAKHFIF!@#$");
        vmodel.setReusability(5);
        vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
        vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
        vmodel.setPercent(false);
        vmodel.setAmount(-20L);

        Either<ExceptionViewModel,DiscountViewModel> either = discountService.setDiscountCodeByUser(vmodel,user);
        if(either.isLeft()){
            assertTrue(true);
            System.out.println("****");
            System.out.println(either.getLeft().getMessage());
            System.out.println(either.getLeft().getError());
        }
        else {
            assertTrue(false);
        }
    }

    @Test
    public void setDiscountCodeTestInvalidPercentageWhenIsPercentIsFalse() {
        assertEquals(0, discountRepo.count());
        eventViewModel = eventService.create(eventViewModel);

        Event event = eventRepository.findOne(eventViewModel.getEventId());
        event.setEventState(State.OPEN.name());
        event.setOperatorState(OperatorState.APPROVED.name());

        DiscountViewModel vmodel = new DiscountViewModel();

        vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream()
                .flatMap(ed -> ed.getBlitTypes().stream().map(bt -> bt.getBlitTypeId())).collect(Collectors.toSet()));
        vmodel.setCode("TAKHFIF!@#$");
        vmodel.setReusability(5);
        vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
        vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
        vmodel.setPercentage(30D);
        vmodel.setPercent(false);
        vmodel.setAmount(1000L);

        Either<ExceptionViewModel,DiscountViewModel> either = discountService.setDiscountCodeByUser(vmodel,user);
        if(either.isLeft()){
            assertTrue(true);
            System.out.println("****");
            System.out.println(either.getLeft().getMessage());
            System.out.println(either.getLeft().getError());
        }
        else {
            assertTrue(false);
        }
    }

    @Test
    public void updateDiscountCodeTest(){
        assertEquals(0, discountRepo.count());
        eventViewModel = eventService.create(eventViewModel);

        Event event = eventRepository.findOne(eventViewModel.getEventId());
        event.setEventState(State.OPEN.name());
        event.setOperatorState(OperatorState.APPROVED.name());
        DiscountViewModel vmodel = new DiscountViewModel();

        vmodel.setBlitTypeIds(eventViewModel.getEventDates().stream()
                .flatMap(ed -> ed.getBlitTypes().stream().map(bt -> bt.getBlitTypeId())).collect(Collectors.toSet()));
        vmodel.setCode("TAKHFIF!@#$");
        vmodel.setReusability(5);
        vmodel.setEffectDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
        vmodel.setExpirationDate(Timestamp.from(ZonedDateTime.now().plusDays(6).toInstant()));
        vmodel.setPercentage(30D);
        vmodel.setAmount(0L);
        vmodel.setPercent(true);

        vmodel = discountService.setDiscountCodeByUser(vmodel,user).get();
        assertEquals(1, discountRepo.count());

        vmodel.setCode("changed code");
        if(discountService.updateDiscountCodeByUser(vmodel, user).isLeft())
            assertTrue(true);
        else
            assertTrue(false);

        assertEquals(1, discountRepo.count());
        assertEquals("TAKHFIF!@#$", discountRepo.findAll().stream().findFirst().get().getCode());

    }



}
