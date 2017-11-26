package blito.test.integration;

import com.blito.common.Salon;
import com.blito.common.Seat;
import com.blito.configs.Constants;
import com.blito.enums.*;
import com.blito.models.User;
import com.blito.payments.zarinpal.PaymentVerificationResponse;
import com.blito.payments.zarinpal.client.ZarinpalClient;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.blit.ReservedBlitViewModel;
import com.blito.rest.viewmodels.blit.SeatBlitViewModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.rest.viewmodels.payments.ZarinpalPayRequestResponseViewModel;
import com.blito.services.PaymentRequestService;
import com.blito.services.blit.CommonBlitService;
import com.blito.services.blit.SeatBlitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.vavr.control.Try;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.File;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author Farzam Vatanzadeh
 * 10/10/17
 * Mailto : farzam.vat@gmail.com
 **/

public class EventsContainSalonIntegrationTest extends AbstractEventRestControllerTest {
    private ObjectMapper objectMapper;
    @Value("${zarinpal.web.gateway}")
    private String zarinpalGatewayURL;
    @MockBean
    private PaymentRequestService paymentRequestService;
    @MockBean
    private ZarinpalClient zarinpalClient;
    @SpyBean
    private SeatBlitService seatBlitService;
    @Autowired
    private CommonBlitService commonBlitService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public EventViewModel createAndUpdateEventWithSalon() {
        EventHostViewModel eventHostViewModel = new EventHostViewModel();
        eventHostViewModel.setHostName("salonHostName");
        eventHostViewModel.setDescription("description");
        eventHostViewModel.setHostType(HostType.INDIVIDUAL);
        eventHostViewModel.setTelephone("88002116");

        eventHostViewModel = givenRestIntegration()
                .body(eventHostViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/event-hosts")
                .then().statusCode(201).extract().body().as(EventHostViewModel.class);

        EventViewModel eventViewModel = createSampleEventViewModel(eventHostViewModel,"salonEvent");
        Salon salon = getTestSalonSchema();
        eventViewModel.getEventDates().stream().findAny().ifPresent(eventDateViewModel -> {
            BlitTypeViewModel blitTypeViewModel = eventDateViewModel.getBlitTypes().stream()
                    .filter(btvm -> btvm.getName().equals("neshaste")).findFirst().get();
            eventViewModel.setSalonUid(salon.getUid());
            blitTypeViewModel.setSeatUids(new HashSet<>());
            blitTypeViewModel.setCapacity(16);
            salon.getSections()
                    .stream()
                    .flatMap(section -> section.getRows().stream())
//                    .filter(row -> row.getName().equals("2"))
                    .flatMap(row -> row.getSeats().stream())
                    .sorted(Comparator.comparing(Seat::getName))
//                    .skip(3)
//                    .limit(5)
                    .forEachOrdered(seat -> blitTypeViewModel.getSeatUids().add(seat.getUid()));


            eventDateViewModel.setBlitTypes(new HashSet<>(Collections.singletonList(blitTypeViewModel)));
        });
        EventViewModel responseViewModel = givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events")
                .then().statusCode(201).extract().body().as(EventViewModel.class);


//        System.out.println(givenRestIntegration()
//                .when()
//                .get(getServerAddress() + "/api/blito/v1.0/events/all-user-events")
//                .thenReturn().body().as(String.class));

        System.out.println("**************************************" + responseViewModel.toString());
        responseViewModel.setSalonUid(salon.getUid());
        responseViewModel.getEventDates().forEach(eventDateViewModel -> {
            BlitTypeViewModel blitTypeViewModel = new BlitTypeViewModel();
            blitTypeViewModel.setName("update");
            blitTypeViewModel.setFree(false);
            blitTypeViewModel.setCapacity(8);
            blitTypeViewModel.setPrice(1000);
            blitTypeViewModel.setSeatUids(new HashSet<>());

            salon.getSections()
                    .stream()
                    .flatMap(section -> section.getRows().stream())
                    .filter(row -> row.getName().equals("2"))
                    .flatMap(row -> row.getSeats().stream())
                    .sorted(Comparator.comparing(Seat::getName))
                    .forEachOrdered(seat -> blitTypeViewModel.getSeatUids().add(seat.getUid()));
            eventDateViewModel.setBlitTypes(new HashSet<>(Collections.singletonList(blitTypeViewModel)));


            BlitTypeViewModel blitTypeViewModel2 = new BlitTypeViewModel();
            blitTypeViewModel2.setName("update2");
            blitTypeViewModel2.setFree(false);
            blitTypeViewModel2.setCapacity(8);
            blitTypeViewModel2.setPrice(5000);
            blitTypeViewModel2.setSeatUids(new HashSet<>());

            salon.getSections()
                    .stream()
                    .flatMap(section -> section.getRows().stream())
                    .filter(row -> row.getName().equals("1"))
                    .flatMap(row -> row.getSeats().stream())
                    .sorted(Comparator.comparing(Seat::getName))
                    .forEachOrdered(seat -> blitTypeViewModel2.getSeatUids().add(seat.getUid()));
            eventDateViewModel.getBlitTypes().add(blitTypeViewModel2);
         });


        Response response = givenRestIntegration()
                .body(responseViewModel)
                .when()
                .put(getServerAddress() + "/api/blito/v1.0/events");
        response.then().statusCode(202);
        return response.thenReturn().body().as(EventViewModel.class);
    }


    @Test
    public void buySeatBlit_success() {
        ZarinpalPayRequestResponseViewModel zarinpalResponse = new ZarinpalPayRequestResponseViewModel();
        zarinpalResponse.setGateway(BankGateway.ZARINPAL);
        zarinpalResponse.setZarinpalWebGatewayURL(zarinpalGatewayURL + "testToken");
        Mockito.when(paymentRequestService.createPurchaseRequest(Mockito.any())).thenReturn("testToken");
        Mockito.when(paymentRequestService.createZarinpalResponse("testToken")).thenReturn(zarinpalResponse);
        Mockito.when(seatBlitService.generateTrackCode()).thenReturn("123456");

        PaymentVerificationResponse paymentVerificationResponse = new PaymentVerificationResponse();
        paymentVerificationResponse.setRefID(123123L);
        paymentVerificationResponse.setStatus(100);
        Mockito.when(zarinpalClient.getPaymentVerificationResponse(Mockito.anyInt(),Mockito.anyString())).thenReturn(paymentVerificationResponse);

        EventViewModel eventViewModel = createAndUpdateEventWithSalon();
        approveEvent_success(eventViewModel.getEventId(), OperatorState.APPROVED);
        openEventState_success(eventViewModel.getEventId(), State.OPEN);
        eventViewModel.getEventDates().forEach(eventDateViewModel -> openEventDateState_success(eventDateViewModel.getEventDateId(),State.OPEN));
        openBlitTypeState_success(new ArrayList<>(eventViewModel.getEventDates().stream().flatMap(eventDateViewModel -> eventDateViewModel.getBlitTypes().stream())
                .map(BlitTypeViewModel::getBlitTypeId).collect(Collectors.toSet())),State.OPEN);
        SeatBlitViewModel seatBlitViewModel = new SeatBlitViewModel();
        seatBlitViewModel.setEventDateId(eventViewModel.getEventDates().stream().findFirst().get().getEventDateId());
        seatBlitViewModel.setBankGateway(BankGateway.ZARINPAL);
        seatBlitViewModel.setCount(6);
        seatBlitViewModel.setSeats("2,3,4 row 2");
        seatBlitViewModel.setCustomerEmail("blito.adm@gmail.com");
        seatBlitViewModel.setCustomerName("fifi");
        seatBlitViewModel.setEventAddress(eventViewModel.getAddress());
        seatBlitViewModel.setEventDateAndTime("event time");
        seatBlitViewModel.setTotalAmount(18000L);
        seatBlitViewModel.setSeatType(SeatType.SEAT_BLIT);
        seatBlitViewModel.setSeatUids(new HashSet<>());
        seatBlitViewModel.setEventName(eventViewModel.getEventName());
        seatBlitViewModel.setEventDate(eventViewModel.getEventDates().stream().findFirst().get().getDate());
        seatBlitViewModel.setCustomerMobileNumber("09124337522");

        getTestSalonSchema().getSections()
                .stream()
                .flatMap(section -> section.getRows().stream())
                .filter(row -> row.getName().equals("2"))
                .flatMap(row -> row.getSeats().stream())
                .sorted(Comparator.comparing(Seat::getName))
                .limit(3)
                .forEachOrdered(seat -> seatBlitViewModel.getSeatUids().add(seat.getUid()));

        getTestSalonSchema().getSections()
                .stream()
                .flatMap(section -> section.getRows().stream())
                .filter(row -> row.getName().equals("1"))
                .flatMap(row -> row.getSeats().stream())
                .sorted(Comparator.comparing(Seat::getName))
                .limit(3)
                .forEachOrdered(seat -> seatBlitViewModel.getSeatUids().add(seat.getUid()));
        Response response =
                givenRestIntegration()
                .body(seatBlitViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/blits/buy-request-with-seat");
        response.then().statusCode(200).body("zarinpalWebGatewayURL",equalTo(zarinpalGatewayURL + "testToken"))
        .body("gateway",equalTo(BankGateway.ZARINPAL.name()));

        Response getUserBlitsResposne =
                givenRestIntegration()
                .when()
                .get(getServerAddress() + "/api/blito/v1.0/blits/user-seat");
        getUserBlitsResposne.then().statusCode(200).body("numberOfElements",equalTo(0));
    }

    public Salon getTestSalonSchema() {
        return Try.of(() -> new File(EventsContainSalonIntegrationTest.class.getResource(Constants.BASE_SALON_SCHEMAS + "/TestSalon" ).toURI()))
                .flatMapTry(file -> Try.of(() -> objectMapper.readValue(file,Salon.class))).get();
    }

    @Test
    public void createEventWithoutSeats() {
        EventHostViewModel eventHostViewModel = new EventHostViewModel();
        eventHostViewModel.setHostName("NoSeatEventHost");
        eventHostViewModel.setDescription("description");
        eventHostViewModel.setHostType(HostType.INDIVIDUAL);
        eventHostViewModel.setTelephone("88002116");

        eventHostViewModel = givenRestIntegration()
                .body(eventHostViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/event-hosts")
                .then().statusCode(201).extract().body().as(EventHostViewModel.class);

        EventViewModel eventViewModel = createSampleEventViewModel(eventHostViewModel,"NoSeatEvent");

        EventViewModel responseViewModel = givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events")
                .then().statusCode(201).extract().body().as(EventViewModel.class);

    }

    @Test
    public void createAndUpdateEventWithUnavailableSeats() {
        EventHostViewModel eventHostViewModel = new EventHostViewModel();
        eventHostViewModel.setHostName("salonWithUnvailableSeatsHostName");
        eventHostViewModel.setDescription("description");
        eventHostViewModel.setHostType(HostType.INDIVIDUAL);
        eventHostViewModel.setTelephone("88002116");

        eventHostViewModel = givenRestIntegration()
                .body(eventHostViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/event-hosts")
                .then().statusCode(201).extract().body().as(EventHostViewModel.class);

        EventViewModel eventViewModel = createSampleEventViewModel(eventHostViewModel,"salonEventWithUnavailableSeats");
        Salon salon = getTestSalonSchema();
        eventViewModel.setSalonUid(salon.getUid());
        eventViewModel.getEventDates().stream().findAny().ifPresent(eventDateViewModel -> {
            BlitTypeViewModel blitTypeViewModel = eventDateViewModel.getBlitTypes().stream()
                    .filter(btvm -> btvm.getName().equals("neshaste")).findFirst().get();
            blitTypeViewModel.setCapacity(8);
            blitTypeViewModel.setSeatUids(new HashSet<>());
            salon.getSections()
                    .stream()
                    .flatMap(section -> section.getRows().stream())
                    .filter(row -> row.getName().equals("2"))
                    .flatMap(row -> row.getSeats().stream())
                    .sorted(Comparator.comparing(Seat::getName))
                    .forEachOrdered(seat -> blitTypeViewModel.getSeatUids().add(seat.getUid()));
        });

        eventViewModel.getEventDates().stream().findAny().ifPresent(eventDateViewModel -> {
            BlitTypeViewModel blitTypeViewModel = eventDateViewModel.getBlitTypes().stream()
                    .filter(btvm -> btvm.getName().equals(Constants.HOST_RESERVED_SEATS)).findFirst().get();
            blitTypeViewModel.setCapacity(8);
            blitTypeViewModel.setSeatUids(new HashSet<>());
            salon.getSections()
                    .stream()
                    .flatMap(section -> section.getRows().stream())
                    .filter(row -> row.getName().equals("1"))
                    .flatMap(row -> row.getSeats().stream())
                    .sorted(Comparator.comparing(Seat::getName))
                    .forEachOrdered(seat -> blitTypeViewModel.getSeatUids().add(seat.getUid()));
        });

        EventViewModel responseViewModel = givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events")
                .then().statusCode(201).extract().body().as(EventViewModel.class);

        responseViewModel.setSalonUid(salon.getUid());
        responseViewModel.getEventDates().forEach(eventDateViewModel -> {
           eventDateViewModel.getBlitTypes()
                   .stream()
                   .filter(blitTypeViewModel -> blitTypeViewModel.getName().equals(Constants.HOST_RESERVED_SEATS))
                   .findAny().ifPresent(blitTypeViewModel -> {
                       blitTypeViewModel.setSeatUids(new HashSet<>());
                       blitTypeViewModel.setCapacity(5);
                       salon.getSections().stream()
                               .flatMap(section -> section.getRows().stream())
                               .filter(row -> row.getName().equals("1"))
                               .flatMap(row -> row.getSeats().stream())
                               .sorted(Comparator.comparing(Seat::getName))
                               .limit(5)
                               .forEachOrdered(seat -> blitTypeViewModel.getSeatUids().add(seat.getUid()));
           });
        });

        responseViewModel.getEventDates().forEach(eventDateViewModel -> {
            eventDateViewModel.getBlitTypes()
                    .stream()
                    .filter(blitTypeViewModel -> blitTypeViewModel.getName().equals("neshaste"))
                    .findAny().ifPresent(blitTypeViewModel -> {
                        blitTypeViewModel.setCapacity(11);
                        salon.getSections().stream()
                                .flatMap(section -> section.getRows().stream())
                                .filter(row -> row.getName().equals("1"))
                                .flatMap(row -> row.getSeats().stream())
                                .sorted(Comparator.comparing(Seat::getName))
                                .skip(5)
                                .limit(3)
                                .forEachOrdered(seat -> blitTypeViewModel.getSeatUids().add(seat.getUid()));

                salon.getSections().stream()
                        .flatMap(section -> section.getRows().stream())
                        .filter(row -> row.getName().equals("2"))
                        .flatMap(row -> row.getSeats().stream())
                        .sorted(Comparator.comparing(Seat::getName))
                        .forEachOrdered(seat -> blitTypeViewModel.getSeatUids().add(seat.getUid()));

            });
        });

        Response response = givenRestIntegration()
                .body(responseViewModel)
                .when()
                .put(getServerAddress() + "/api/blito/v1.0/events");
        response.then().statusCode(202).extract().body().as(EventViewModel.class);

        ReservedBlitViewModel reservedBlitViewModel = new ReservedBlitViewModel();
        reservedBlitViewModel.setEventDateAndTime("alana");
        reservedBlitViewModel.setEventDateId(responseViewModel.getEventDates().stream().findFirst().get().getEventDateId());
        reservedBlitViewModel.setSeatUid(salon.getSections().stream()
                .flatMap(section -> section.getRows().stream())
                .filter(row -> row.getName().equals("1"))
                .flatMap(row -> row.getSeats().stream())
                .sorted(Comparator.comparing(Seat::getName))
                .limit(1).findFirst().get().getUid());


        User user = userRepository.findByEmail("blito.adm@gmail.com").get();
        Map<String, Object> map = seatBlitService.generateReservedBlit(reservedBlitViewModel, user);
    }


    @Test
    public void createAndUpdateEventTest() {
        EventHostViewModel eventHostViewModel = new EventHostViewModel();
        eventHostViewModel.setHostName("createAndUpdateEvent(Host)");
        eventHostViewModel.setDescription("description");
        eventHostViewModel.setHostType(HostType.INDIVIDUAL);
        eventHostViewModel.setTelephone("88002116");

        eventHostViewModel = givenRestIntegration()
                .body(eventHostViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/event-hosts")
                .then().statusCode(201).extract().body().as(EventHostViewModel.class);

        EventViewModel eventViewModel = createSampleEventViewModel(eventHostViewModel, "createAndUpdateEvent");
        Salon salon = getTestSalonSchema();
        eventViewModel.setSalonUid(salon.getUid());
        EventDateViewModel eventDateViewModel = new EventDateViewModel();
        eventDateViewModel.setDate(Timestamp.from(ZonedDateTime.now().plusDays(10).toInstant()));
        eventDateViewModel.setHasSalon(true);
        BlitTypeViewModel blitTypeViewModel = new BlitTypeViewModel();
        blitTypeViewModel.setName("createBlitType");
        blitTypeViewModel.setPrice(1000);
        blitTypeViewModel.setCapacity(16);
        blitTypeViewModel.setFree(false);
        blitTypeViewModel.setSeatUids(salon.getSections()
                .stream()
                .flatMap(section -> section.getRows().stream())
                .flatMap(row -> row.getSeats().stream())
                .map(Seat::getUid).collect(Collectors.toSet()));
        eventDateViewModel.setBlitTypes(new HashSet<>(Collections.singletonList(blitTypeViewModel)));
        eventViewModel.setEventDates(new HashSet<>(Collections.singletonList(eventDateViewModel)));

        EventViewModel responseViewModel = givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events")
                .then().statusCode(201).extract().body().as(EventViewModel.class);

        EventDateViewModel eventDateViewModel1 = new EventDateViewModel();
        EventDateViewModel eventDateViewModel2 = new EventDateViewModel();

        BlitTypeViewModel blitTypeViewModel1_1 = new BlitTypeViewModel();
        BlitTypeViewModel blitTypeViewModel1_2 = new BlitTypeViewModel();
        BlitTypeViewModel blitTypeViewModel2_1 = new BlitTypeViewModel();
        BlitTypeViewModel blitTypeViewModel2_2 = new BlitTypeViewModel();

        eventDateViewModel1.setDate(Timestamp.from(ZonedDateTime.now().plusDays(10).toInstant()));
        eventDateViewModel1.setHasSalon(true);

        eventDateViewModel2.setDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
        eventDateViewModel2.setHasSalon(true);

        blitTypeViewModel1_1.setName("sans1update1");
        blitTypeViewModel1_1.setPrice(5000);
        blitTypeViewModel1_1.setCapacity(8);
        blitTypeViewModel1_1.setFree(false);
        blitTypeViewModel1_1.setSeatUids(salon.getSections()
                .stream()
                .flatMap(section -> section.getRows().stream())
                .filter(row -> row.getName().equals("1"))
                .flatMap(row -> row.getSeats().stream())
                .map(Seat::getUid).collect(Collectors.toSet()));

        blitTypeViewModel1_2.setName("sans1update2");
        blitTypeViewModel1_2.setPrice(2000);
        blitTypeViewModel1_2.setCapacity(8);
        blitTypeViewModel1_2.setFree(false);
        blitTypeViewModel1_2.setSeatUids(salon.getSections()
                .stream()
                .flatMap(section -> section.getRows().stream())
                .filter(row -> row.getName().equals("2"))
                .flatMap(row -> row.getSeats().stream())
                .map(Seat::getUid).collect(Collectors.toSet()));

        eventDateViewModel1.setBlitTypes(new HashSet<>(Arrays.asList(blitTypeViewModel1_1, blitTypeViewModel1_2)));

        blitTypeViewModel2_1.setName("sans2update1");
        blitTypeViewModel2_1.setPrice(5000);
        blitTypeViewModel2_1.setCapacity(8);
        blitTypeViewModel2_1.setFree(false);
        blitTypeViewModel2_1.setSeatUids(salon.getSections()
                .stream()
                .flatMap(section -> section.getRows().stream())
                .filter(row -> row.getName().equals("1"))
                .flatMap(row -> row.getSeats().stream())
                .map(Seat::getUid).collect(Collectors.toSet()));

        blitTypeViewModel2_2.setName("sans2update2");
        blitTypeViewModel2_2.setPrice(2000);
        blitTypeViewModel2_2.setCapacity(8);
        blitTypeViewModel2_2.setFree(false);
        blitTypeViewModel2_2.setSeatUids(salon.getSections()
                .stream()
                .flatMap(section -> section.getRows().stream())
                .filter(row -> row.getName().equals("2"))
                .flatMap(row -> row.getSeats().stream())
                .map(Seat::getUid).collect(Collectors.toSet()));

        eventDateViewModel2.setBlitTypes(new HashSet<>(Arrays.asList(blitTypeViewModel2_1, blitTypeViewModel2_2)));

        responseViewModel.setEventDates(new HashSet<>(Arrays.asList(eventDateViewModel1, eventDateViewModel2)));


        Response response = givenRestIntegration()
                .body(responseViewModel)
                .when()
                .put(getServerAddress() + "/api/blito/v1.0/events");
        response.then().statusCode(202);
        EventViewModel updateResponseViewModel =  response.thenReturn().body().as(EventViewModel.class);
        updateResponseViewModel.getEventName();



    }
}
