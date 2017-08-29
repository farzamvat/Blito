package blito.test.integration;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.enums.EventType;
import com.blito.enums.HostType;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.blittype.ChangeBlitTypeStateVm;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.AdminChangeEventOperatorStateVm;
import com.blito.rest.viewmodels.event.ChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.ChangeEventDateStateVm;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DiscountEffectIntegrationTest extends AbstractRestControllerTest {
    private static boolean isInit = false;
    private static EventViewModel eventViewModel;

    @Before
    public void init()
    {
        if(!isInit)
        {
            isInit = true;
            eventViewModel = createEvent_approve_open_event_eventDate_blitType_success();
        }

    }

    public Response createEventHost_success()
    {
        EventHostViewModel eventHostViewModel = new EventHostViewModel();
        eventHostViewModel.setHostName("Farzam");
        eventHostViewModel.setDescription("description");
        eventHostViewModel.setHostType(HostType.INDIVIDUAL);
        eventHostViewModel.setTelephone("88002116");
        Response response =
                givenRestIntegration()
                .body(eventHostViewModel)
                .post(getServerAddress() + "/api/blito/v1.0/event-hosts");
        response.then().statusCode(201);
        return response;
    }

    public Response createEvent_success()
    {
        EventHostViewModel eventHostViewModel = createEventHost_success().thenReturn().body().as(EventHostViewModel.class);

        EventViewModel eventViewModel = new EventViewModel();
        eventViewModel.setAddress("Amirabad");
        eventViewModel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
        eventViewModel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
        eventViewModel.setDescription("Description");
        eventViewModel.setEventHostId(eventHostViewModel.getEventHostId());
        eventViewModel.setEventHostName(eventHostViewModel.getHostName());
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

        Response response = givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events");
        response.then().statusCode(201);
        return response;
    }

    public void openEventState_success(long eventId) {
        ChangeEventStateVm changeEventStateVm = new ChangeEventStateVm();
        changeEventStateVm.setState(State.OPEN);
        changeEventStateVm.setEventId(eventId);

        givenRestIntegration()
                .body(changeEventStateVm)
                .when()
                .put(getServerAddress() + "/api/blito/v1.0/admin/events/change-event-state")
                .then().statusCode(200);
    }

    public void openEventDateState_success(long eventDateId) {
        ChangeEventDateStateVm changeEventDateStateVm = new ChangeEventDateStateVm();
        changeEventDateStateVm.setEventDateId(eventDateId);
        changeEventDateStateVm.setEventDateState(State.OPEN);

        givenRestIntegration()
                .body(changeEventDateStateVm)
                .when()
                .put(getServerAddress()  +"/api/blito/v1.0/admin/events/change-event-date-state")
                .then().statusCode(200);
    }

    public void openBlitTypeState_success(List<Long> blitTypeIds) {
        blitTypeIds.forEach(blitTypeId -> {
            ChangeBlitTypeStateVm changeBlitTypeStateVm = new ChangeBlitTypeStateVm();
            changeBlitTypeStateVm.setBlitTypeId(blitTypeId);
            changeBlitTypeStateVm.setBlitTypeState(State.OPEN);

            givenRestIntegration()
                    .body(changeBlitTypeStateVm)
                    .when()
                    .put(getServerAddress() + "/api/blito/v1.0/admin/events/change-blit-type-state")
                    .then().statusCode(200);
        });
    }

    public void approveEvent_success(long eventId) {
        AdminChangeEventOperatorStateVm operatorStateVm = new AdminChangeEventOperatorStateVm();
        operatorStateVm.setEventId(eventId);
        operatorStateVm.setOperatorState(OperatorState.APPROVED);
        givenRestIntegration()
                .body(operatorStateVm)
                .when()
                .put(getServerAddress() + "/api/blito/v1.0/admin/events/change-event-operator-state")
                .then().statusCode(200);
    }

    public EventViewModel createEvent_approve_open_event_eventDate_blitType_success() {
        EventViewModel eventViewModel = createEvent_success().thenReturn().body().as(EventViewModel.class);
        approveEvent_success(eventViewModel.getEventId());
        openEventState_success(eventViewModel.getEventId());
        openEventDateState_success(eventViewModel.getEventDates().stream().findFirst().get().getEventDateId());
        openBlitTypeState_success(eventViewModel.getEventDates().stream().flatMap(eventDate -> eventDate.getBlitTypes().stream()).map(blitType -> blitType.getBlitTypeId()).collect(Collectors.toList()));
        return eventViewModel;
    }

    @Test
    public void createDiscountCode_success()
    {
        DiscountViewModel discountViewModel = new DiscountViewModel();
        discountViewModel.setPercent(true);
        discountViewModel.setCode("discount");
        discountViewModel.setExpirationDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).plusDays(1).toInstant()));
        discountViewModel.setEffectDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(1).toInstant()));
        discountViewModel.setReusability(10);
        discountViewModel.setPercentage(30);
        discountViewModel.setBlitTypeIds(new HashSet<>(
                Arrays.asList(eventViewModel.getEventDates()
                        .stream()
                        .flatMap(ed -> ed.getBlitTypes().stream())
                        .filter(bt -> bt.getName().equals("vaysade"))
                        .findFirst().get().getBlitTypeId())));

        Response response = givenRestIntegration()
                .body(discountViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/set-discount-code");
        response.then().statusCode(200);
        DiscountViewModel discountViewModelResponse = response.thenReturn().body().as(DiscountViewModel.class);
    }

    @Test
    public void validateDiscountCode_valid(){
        createDiscountCode_success();
        DiscountValidationViewModel discountValidationViewModel = new DiscountValidationViewModel();
        discountValidationViewModel.setCode("discount");
        discountValidationViewModel.setCount(5);
        discountValidationViewModel.setBlitTypeId(eventViewModel.getEventDates()
                .stream().flatMap(ed->ed.getBlitTypes().stream()).filter(bt->bt.getName().equals("vaysade")).findFirst().get().getBlitTypeId());

        Response response = givenRestIntegration()
                .body(discountValidationViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/validate-discount-code");
        response.then().statusCode(200);
        DiscountValidationViewModel discountValidationViewModelResponse = response.thenReturn().body().as(DiscountValidationViewModel.class);
        assertTrue(discountValidationViewModelResponse.isValid());
        assertEquals(70000, discountValidationViewModelResponse.getTotalAmount(), 0.001);
    }

    @Test
    public void validateDiscountCode_invalidCode(){
        createDiscountCode_success();
        DiscountValidationViewModel discountValidationViewModel = new DiscountValidationViewModel();
        discountValidationViewModel.setCode("non existing code");
        discountValidationViewModel.setCount(5);
        discountValidationViewModel.setBlitTypeId(eventViewModel.getEventDates()
                .stream().flatMap(ed->ed.getBlitTypes().stream()).filter(bt->bt.getName().equals("vaysade")).findFirst().get().getBlitTypeId());

        Response response = givenRestIntegration()
                .body(discountValidationViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/validate-discount-code");
        response.then().statusCode(200);
        DiscountValidationViewModel discountValidationViewModelResponse = response.thenReturn().body().as(DiscountValidationViewModel.class);
        assertFalse(discountValidationViewModelResponse.isValid());
    }

    @Test
    public void validateDiscountCode_invalidCount(){
        createDiscountCode_success();
        DiscountValidationViewModel discountValidationViewModel = new DiscountValidationViewModel();
        discountValidationViewModel.setCode("vaysade");
        discountValidationViewModel.setCount(11);
        discountValidationViewModel.setBlitTypeId(eventViewModel.getEventDates()
                .stream().flatMap(ed->ed.getBlitTypes().stream()).filter(bt->bt.getName().equals("vaysade")).findFirst().get().getBlitTypeId());

        Response response = givenRestIntegration()
                .body(discountValidationViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/validate-discount-code");
        response.then().statusCode(200);
        DiscountValidationViewModel discountValidationViewModelResponse = response.thenReturn().body().as(DiscountValidationViewModel.class);
        assertFalse(discountValidationViewModelResponse.isValid());
    }

    @Test
    public void validateDiscountCode_argumentValidationFail(){
        createDiscountCode_success();
        DiscountValidationViewModel discountValidationViewModel = new DiscountValidationViewModel();
        discountValidationViewModel.setCode("");
        discountValidationViewModel.setCount(5);
        discountValidationViewModel.setBlitTypeId(eventViewModel.getEventDates()
                .stream().flatMap(ed->ed.getBlitTypes().stream()).filter(bt->bt.getName().equals("vaysade")).findFirst().get().getBlitTypeId());

        Response response = givenRestIntegration()
                .body(discountValidationViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/validate-discount-code");
        response.then().statusCode(400);

        discountValidationViewModel.setCode("vaysade");
        discountValidationViewModel.setCount(0);
        discountValidationViewModel.setBlitTypeId(eventViewModel.getEventDates()
                .stream().flatMap(ed->ed.getBlitTypes().stream()).filter(bt->bt.getName().equals("vaysade")).findFirst().get().getBlitTypeId());

        Response response2 = givenRestIntegration()
                .body(discountValidationViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/validate-discount-code");
        response2.then().statusCode(400);

    }

}
