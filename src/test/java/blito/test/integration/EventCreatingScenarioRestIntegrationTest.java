package blito.test.integration;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.configs.Constants;
import com.blito.enums.EventType;
import com.blito.enums.HostType;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.repositories.EventRepository;
import com.blito.resourceUtil.ResourceUtil;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventCreatingScenarioRestIntegrationTest extends AbstractRestControllerTest {
    private static boolean isInit = false;
    private static EventViewModel eventViewModel;

    @Autowired
    private EventRepository eventRepository;

    @Before
    public void init()
    {
        if(!isInit)
        {
            isInit = true;
            eventViewModel = createEvent_approve_open_event_eventDate_blitType_success();
        }

    }

    public Response createEventHost_success(String eventHostName)
    {
        EventHostViewModel eventHostViewModel = new EventHostViewModel();
        eventHostViewModel.setHostName(eventHostName);
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

    public EventViewModel createSampleEventViewModel(EventHostViewModel eventHostViewModel)
    {
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

        eventDateViewModel.setBlitTypes(new HashSet<>(Arrays.asList(blitTypeViewModel1, blitTypeViewModel2)));
        eventViewModel.setEventDates(new HashSet<>(Arrays.asList(eventDateViewModel)));
        return eventViewModel;
    }

    public Response createEvent_success()
    {
        EventHostViewModel eventHostViewModel = createEventHost_success("eventHostName1").thenReturn().body().as(EventHostViewModel.class);

        EventViewModel eventViewModel = createSampleEventViewModel(eventHostViewModel);

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
    public void createEvent_additionalFields_validation_fail()
    {
        EventHostViewModel eventHostViewModel =
                createEventHost_success("eventHostName2").thenReturn().body().as(EventHostViewModel.class);
        EventViewModel eventViewModel = createSampleEventViewModel(eventHostViewModel);
        Map<String,String> map = new HashMap<>();
        // type int validation error
        map.put("student number", Constants.FIELD_INT_TYPE);
        eventViewModel.setAdditionalFields(map);

        Response response =
                givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events");
        response.then().statusCode(400);
    }

    @Test
    public void createEvent_additionalFields_validation_fail_in_case_of_invalid_schema_type() {
        EventHostViewModel eventHostViewModel =
                createEventHost_success("eventHostName3")
                        .thenReturn()
                        .body()
                        .as(EventHostViewModel.class);
        EventViewModel eventViewModel =
                createSampleEventViewModel(eventHostViewModel);
        Map<String,String> map = new HashMap<>();
        map.put("student number", "testInvalidField");
        eventViewModel.setAdditionalFields(map);
        Response eventResponse =
                givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events");
        eventResponse.then().statusCode(400);
    }

    @Test
    public void createEvent_additionalFields_success()
    {
        EventHostViewModel eventHostViewModel =
                createEventHost_success("eventHostName4").thenReturn().body().as(EventHostViewModel.class);
        EventViewModel eventViewModel =
                createSampleEventViewModel(eventHostViewModel);
        Map<String,String> map = new HashMap<>();

        map.put("student number", Constants.FIELD_STRING_TYPE);
        map.put("age",Constants.FIELD_STRING_TYPE);
        eventViewModel.setAdditionalFields(map);
        Response response =
                givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events");
        response.then().statusCode(201);
        eventViewModel =
                response.thenReturn().body().as(EventViewModel.class);
        assertEquals(2,eventRepository.findOne(eventViewModel.getEventId()).getAdditionalFields().size());
    }

    @Test
    public void updateEvent_additionalFields_success() {
        EventHostViewModel eventHostViewModel =
                createEventHost_success("eventHostName5").thenReturn().body().as(EventHostViewModel.class);
        EventViewModel eventViewModel = createSampleEventViewModel(eventHostViewModel);
        Map<String,String> map = new HashMap<>();
        map.put("student number",Constants.FIELD_STRING_TYPE);
        eventViewModel.setAdditionalFields(map);

        Response eventCreationResponse =
                givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events");
        eventCreationResponse.then().statusCode(201);
        eventViewModel = eventCreationResponse.body().as(EventViewModel.class);

        map.clear();
        map.put("age",Constants.FIELD_STRING_TYPE);
        eventViewModel.setAdditionalFields(map);

        Response eventUpdateResponse =
                givenRestIntegration()
                .body(eventViewModel)
                .when()
                .put(getServerAddress() + "/api/blito/v1.0/events");
        eventUpdateResponse.then().statusCode(202);
        assertTrue(eventRepository.findOne(eventViewModel.getEventId()).getAdditionalFields().containsKey("age"));
    }



    @Test
    public void createDiscountCode_success()
    {
        createDiscountCode_success("discount");
    }

    @Test
    public void createDiscountCode_BlitType_notFound()
    {
        DiscountViewModel discountViewModel = new DiscountViewModel();
        discountViewModel.setPercent(true);
        discountViewModel.setCode("notFound");
        discountViewModel.setExpirationDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).plusDays(1).toInstant()));
        discountViewModel.setEffectDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(1).toInstant()));
        discountViewModel.setReusability(10);
        discountViewModel.setPercentage(30D);
        discountViewModel.setAmount(0L);
        discountViewModel.setBlitTypeIds(new HashSet<Long>(Arrays.asList(100L,200L,300L)));

        Response response = givenRestIntegration()
                .body(discountViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/set-discount-code");
        response.then().statusCode(400).body("message",equalTo(ResourceUtil.getMessage(com.blito.enums.Response.BLIT_TYPE_NOT_FOUND)));
    }

    public DiscountViewModel createDiscountCode_success(String code)
    {
        DiscountViewModel discountViewModel = new DiscountViewModel();
        discountViewModel.setPercent(true);
        discountViewModel.setCode(code);
        discountViewModel.setExpirationDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).plusDays(1).toInstant()));
        discountViewModel.setEffectDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(1).toInstant()));
        discountViewModel.setReusability(10);
        discountViewModel.setPercentage(30D);
        discountViewModel.setAmount(0L);
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

        return response.thenReturn().body().as(DiscountViewModel.class);
    }

    @Test
    public void validateDiscountCode_valid(){
        //create discount
        DiscountViewModel responseDiscountViewModel = createDiscountCode_success("discount2");
        ////////

        DiscountValidationViewModel discountValidationViewModel = new DiscountValidationViewModel();
        discountValidationViewModel.setCode(responseDiscountViewModel.getCode());
        discountValidationViewModel.setCount(5);
        discountValidationViewModel.setBlitTypeId(eventViewModel.getEventDates()
                .stream().flatMap(ed->ed.getBlitTypes().stream()).filter(bt->bt.getName().equals("vaysade")).findFirst().get().getBlitTypeId());

        Response response = givenRestIntegration()
                .body(discountValidationViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/validate-discount-code");
        response.then().statusCode(200).body("isValid",equalTo(true)).body("totalAmount",equalTo(70000));
    }

    @Test
    public void validateDiscountCode_invalidCode(){
        //create discount
        DiscountViewModel responseDiscountViewModel = createDiscountCode_success("discount3");
        ///////
        DiscountValidationViewModel discountValidationViewModel = new DiscountValidationViewModel();
        discountValidationViewModel.setCode("non existing code");
        discountValidationViewModel.setCount(5);
        discountValidationViewModel.setBlitTypeId(eventViewModel.getEventDates()
                .stream().flatMap(ed->ed.getBlitTypes().stream()).filter(bt->bt.getName().equals("vaysade")).findFirst().get().getBlitTypeId());

        Response response = givenRestIntegration()
                .body(discountValidationViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/validate-discount-code");
        response.then().statusCode(200).body("isValid", equalTo(false));

    }

    @Test
    public void validateDiscountCode_invalidCount(){
        //create discount
        DiscountViewModel responseDiscountViewModel = createDiscountCode_success("discount4");
        ///////
        DiscountValidationViewModel discountValidationViewModel = new DiscountValidationViewModel();
        discountValidationViewModel.setCode(responseDiscountViewModel.getCode());
        discountValidationViewModel.setCount(11);
        discountValidationViewModel.setBlitTypeId(eventViewModel.getEventDates()
                .stream().flatMap(ed->ed.getBlitTypes().stream()).filter(bt->bt.getName().equals("vaysade")).findFirst().get().getBlitTypeId());

        Response response = givenRestIntegration()
                .body(discountValidationViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/validate-discount-code");
        response.then().statusCode(200).body("isValid", equalTo(false));
    }

    @Test
    public void validateDiscountCode_argumentValidationFail(){
        //create discount
        DiscountViewModel responseDiscountViewModel = createDiscountCode_success("discount5");
        ///////
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

    @Test
    public void adminSetDiscountCode_success(){
        DiscountViewModel discountViewModel = new DiscountViewModel();
        discountViewModel.setAmount(0L);
        discountViewModel.setCode("code");
        discountViewModel.setEffectDate(new Timestamp(1504341720000L));
        discountViewModel.setExpirationDate(new Timestamp(1504341720000L));
        discountViewModel.setPercent(true);
        discountViewModel.setPercentage(25.0);
        discountViewModel.setReusability(2);

        discountViewModel.setBlitTypeIds(new HashSet<>(
                Arrays.asList(eventViewModel.getEventDates()
                        .stream()
                        .flatMap(ed -> ed.getBlitTypes().stream())
                        .filter(bt -> bt.getName().equals("vaysade"))
                        .findFirst().get().getBlitTypeId())));


        Response response = givenRestIntegration()
                .body(discountViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/admin-set-discount-code");
        response.then().statusCode(200).body("amount", equalTo(0));
    }

    @Test
    public void updateDiscountCodeByOperator_success(){
        DiscountViewModel discountViewModel = createDiscountCode_success("test update");
        discountViewModel.setPercent(true);
        discountViewModel.setCode("after update test");
        discountViewModel.setExpirationDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).plusDays(2).toInstant()));
        discountViewModel.setEffectDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(1).toInstant()));
        discountViewModel.setReusability(10);
        discountViewModel.setPercentage(30D);
        discountViewModel.setAmount(0L);
        discountViewModel.setBlitTypeIds(new HashSet<>(
                Arrays.asList(eventViewModel.getEventDates()
                        .stream()
                        .flatMap(ed -> ed.getBlitTypes().stream())
                        .filter(bt -> bt.getName().equals("vaysade"))
                        .findFirst().get().getBlitTypeId())));

        Response response = givenRestIntegration()
                .body(discountViewModel)
                .when()
                .put(getServerAddress() + "/api/blito/v1.0/discount/update-discount-code");
        response.then().statusCode(200).body("code", equalTo("after update test"));
    }
}