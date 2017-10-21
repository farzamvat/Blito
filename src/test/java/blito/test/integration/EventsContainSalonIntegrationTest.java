package blito.test.integration;

import com.blito.common.Salon;
import com.blito.common.Seat;
import com.blito.configs.Constants;
import com.blito.enums.*;
import com.blito.payments.zarinpal.PaymentVerificationResponse;
import com.blito.payments.zarinpal.client.ZarinpalClient;
import com.blito.rest.viewmodels.blit.SeatBlitViewModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.rest.viewmodels.payments.ZarinpalPayRequestResponseViewModel;
import com.blito.services.PaymentRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.vavr.control.Try;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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
            blitTypeViewModel.setCapacity(5);
            salon.getSections()
                    .stream()
                    .flatMap(section -> section.getRows().stream())
                    .filter(row -> row.getName().equals("2"))
                    .flatMap(row -> row.getSeats().stream())
                    .sorted(Comparator.comparing(Seat::getName))
                    .skip(3)
                    .limit(5)
                    .forEachOrdered(seat -> blitTypeViewModel.getSeatUids().add(seat.getUid()));


            eventDateViewModel.setBlitTypes(new HashSet<>(Collections.singletonList(blitTypeViewModel)));
        });
        EventViewModel responseViewModel = givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events")
                .then().statusCode(201).extract().body().as(EventViewModel.class);

        responseViewModel.setSalonUid(salon.getUid());
        responseViewModel.getEventDates().forEach(eventDateViewModel -> {
            BlitTypeViewModel blitTypeViewModel = new BlitTypeViewModel();
            blitTypeViewModel.setName("update");
            blitTypeViewModel.setFree(false);
            blitTypeViewModel.setCapacity(7);
            blitTypeViewModel.setPrice(1000);
            blitTypeViewModel.setSeatUids(new HashSet<>());
            eventDateViewModel.getBlitTypes().add(blitTypeViewModel);
            salon.getSections()
                    .stream()
                    .flatMap(section -> section.getRows().stream())
                    .filter(row -> row.getName().equals("2"))
                    .flatMap(row -> row.getSeats().stream())
                    .sorted(Comparator.comparing(Seat::getName))
                    .limit(7)
                    .forEachOrdered(seat -> blitTypeViewModel.getSeatUids().add(seat.getUid()));
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
        seatBlitViewModel.setBlitTypeId(eventViewModel.getEventDates().stream().flatMap(eventDateViewModel -> eventDateViewModel.getBlitTypes().stream()).filter(blitTypeViewModel -> blitTypeViewModel.getName().equals("update")).findFirst().get().getBlitTypeId());
        seatBlitViewModel.setEventDateId(eventViewModel.getEventDates().stream().findFirst().get().getEventDateId());
        seatBlitViewModel.setBankGateway(BankGateway.ZARINPAL);
        seatBlitViewModel.setBlitTypeName(eventViewModel.getEventDates().stream().flatMap(eventDateViewModel -> eventDateViewModel.getBlitTypes().stream()).filter(blitTypeViewModel -> blitTypeViewModel.getName().equals("update")).findFirst().get().getName());
        seatBlitViewModel.setCount(3);
        seatBlitViewModel.setSeats("2,3,4 row 2");
        seatBlitViewModel.setCustomerEmail("farzam.vat@gmail.com");
        seatBlitViewModel.setCustomerName("fifi");
        seatBlitViewModel.setEventAddress(eventViewModel.getAddress());
        seatBlitViewModel.setEventDateAndTime("event time");
        seatBlitViewModel.setTotalAmount(3000L);
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
//                .skip(1)
                .limit(3)
                .forEachOrdered(seat -> seatBlitViewModel.getSeatUids().add(seat.getUid()));
        Response response =
                givenRestIntegration()
                .body(seatBlitViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/blits/buy-request-with-seat");
        response.then().statusCode(200).body("zarinpalWebGatewayURL",equalTo(zarinpalGatewayURL + "testToken"))
        .body("gateway",equalTo(BankGateway.ZARINPAL.name()));

        givenRestIntegration()
                .when()
                .get(getServerAddress() + "/api/blito/v1.0/zarinpal?Authority=testToken&Status=OK");

        givenRestIntegration()
                .when()
                .get(getServerAddress() + "/api/blito/v1.0/salons/populated-schema/"+ eventViewModel.getEventDates().stream().findAny().get().getEventDateId());

    }

    public Salon getTestSalonSchema() {
        return Try.of(() -> new File(EventsContainSalonIntegrationTest.class.getResource(Constants.BASE_SALON_SCHEMAS + "/TestSalon" ).toURI()))
                .flatMapTry(file -> Try.of(() -> objectMapper.readValue(file,Salon.class))).get();
    }
}
