package blito.test.integration;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.configs.Constants;
import com.blito.enums.BankGateway;
import com.blito.enums.OperatorState;
import com.blito.enums.SeatType;
import com.blito.enums.State;
import com.blito.models.BlitType;
import com.blito.models.Discount;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.EventRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.discount.DiscountEnableViewModel;
import com.blito.rest.viewmodels.discount.DiscountValidationViewModel;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.AdditionalField;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.search.Operation;
import com.blito.search.SearchViewModel;
import com.blito.search.Simple;
import com.blito.utils.test.util.AbstractEventRestControllerTest;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventCreatingScenarioRestIntegrationTest extends AbstractEventRestControllerTest {
    private static boolean isInit = false;
    private static EventViewModel eventViewModel;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private BlitTypeRepository blitTypeRepository;

    @Before
    public void init()
    {
        if(!isInit)
        {
            isInit = true;
            eventViewModel = createEvent_approve_open_event_eventDate_blitType_success();
        }

    }

    public EventViewModel createEvent_approve_open_event_eventDate_blitType_success() {
        EventViewModel eventViewModel = createEvent_success("My Event", "Event Host",Optional.empty()).thenReturn().body().as(EventViewModel.class);
        approveEvent_success(eventViewModel.getEventId(), OperatorState.APPROVED);
        openEventState_success(eventViewModel.getEventId(), State.OPEN);
        openEventDateState_success(eventViewModel.getEventDates().stream().findFirst().get().getEventDateId(), State.OPEN);
        openBlitTypeState_success(eventViewModel.getEventDates().stream().flatMap(eventDate -> eventDate.getBlitTypes().stream()).map(blitType -> blitType.getBlitTypeId()).collect(Collectors.toList()), State.OPEN);
        return eventViewModel;
    }

    public void createAdditionalEventsForTest() {
        Long id1 = createEvent_success("TestEvent1", "eventHost_1",Optional.empty()).thenReturn().as(EventViewModel.class).getEventId();
        Long id2 = createEvent_success("TestEvent2", "eventHost_2",Optional.empty()).thenReturn().as(EventViewModel.class).getEventId();
        Long id3 = createEvent_success("TestEvent3", "eventHost_3",Optional.empty()).thenReturn().as(EventViewModel.class).getEventId();
        Long id4 = createEvent_success("TestEvent4", "eventHost_4",Optional.empty()).thenReturn().as(EventViewModel.class).getEventId();

        approveEvent_success(id1, OperatorState.REJECTED);
        approveEvent_success(id2, OperatorState.APPROVED);
        approveEvent_success(id3, OperatorState.APPROVED);
        approveEvent_success(id4, OperatorState.APPROVED);

        openEventState_success(id1, State.OPEN);
        openEventState_success(id2, State.ENDED);
        openEventState_success(id3, State.CLOSED);
        openEventState_success(id4, State.SOLD);

        assertEquals(State.ENDED.name(), eventRepository.findOne(id2).getEventState());

        deleteEvent_success(id4);
    }

    @Test
    public void buyRequest_freeBlit_success() {

        EventViewModel eventViewModel =
                createEvent_success("TestEvent1",
                        "eventHostNameTest1",
                        Optional.of(Arrays.asList(new AdditionalField("age","string"),new AdditionalField("gender","string"))))
                        .thenReturn().as(EventViewModel.class);
        approveEvent_success(eventViewModel.getEventId(),OperatorState.APPROVED);
        openEventState_success(eventViewModel.getEventId(),State.OPEN);
        openEventDateState_success(eventViewModel.getEventId(),State.OPEN);
        Long freeBlitId = eventViewModel.getEventDates().stream().flatMap(ed -> ed.getBlitTypes().stream()).filter(bt -> bt.getName().equals("FREE")).findFirst().get().getBlitTypeId();
        openBlitTypeState_success(eventViewModel.getEventDates()
                .stream()
                .flatMap(ed -> ed.getBlitTypes().stream())
                .map(BlitTypeViewModel::getBlitTypeId)
                .collect(Collectors.toList()),State.OPEN);
        CommonBlitViewModel commonBlitViewModel = new CommonBlitViewModel();
        commonBlitViewModel.setAdditionalFields(Arrays.asList(new AdditionalField("age","12"),
                new AdditionalField("gender","male")));
        commonBlitViewModel.setBankGateway(BankGateway.NONE);
        commonBlitViewModel.setBlitTypeId(freeBlitId);
        commonBlitViewModel.setBlitTypeName("FREE");
        commonBlitViewModel.setCount(2);
        commonBlitViewModel.setCustomerEmail("farzam.vat@gmail.com");
        commonBlitViewModel.setCustomerMobileNumber("09124337522");
        commonBlitViewModel.setSeatType(SeatType.COMMON);
        commonBlitViewModel.setCustomerName("Farzam");
        commonBlitViewModel.setEventName("event name");
        commonBlitViewModel.setEventDateAndTime("date and time 14:00");
        commonBlitViewModel.setEventAddress("address");
        commonBlitViewModel.setEventDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).plusDays(2).toInstant()));
        Response response =
                givenRestIntegration()
                .body(commonBlitViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/blits/buy-request");

        givenRestIntegration()
                .body(commonBlitViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/blits/buy-request");
        response.then().statusCode(200);

    }


    @Test
    public void createEvent_additionalFields_validation_fail()
    {
        EventHostViewModel eventHostViewModel =
                createEventHost_success("eventHostName2").thenReturn().body().as(EventHostViewModel.class);
        EventViewModel eventViewModel = createSampleEventViewModel(eventHostViewModel, "Event2");
        eventViewModel.setAdditionalFields(Collections.singletonList(new AdditionalField("student number",Constants.FIELD_INT_TYPE)));

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
                createSampleEventViewModel(eventHostViewModel, "Event3");
        eventViewModel.setAdditionalFields(Collections.singletonList(new AdditionalField("student number","testInvalidField")));
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
                createSampleEventViewModel(eventHostViewModel, "Event4");
        eventViewModel.setAdditionalFields(Arrays.asList(new AdditionalField("student number",Constants.FIELD_STRING_TYPE),
                new AdditionalField("age",Constants.FIELD_STRING_TYPE)));
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
        EventViewModel eventViewModel = createSampleEventViewModel(eventHostViewModel, "Event5");
        eventViewModel.setAdditionalFields(Collections.singletonList(new AdditionalField("student number",Constants.FIELD_STRING_TYPE)));

        Response eventCreationResponse =
                givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events");
        eventCreationResponse.then().statusCode(201);
        eventViewModel = eventCreationResponse.body().as(EventViewModel.class);

        eventViewModel.setAdditionalFields(Collections.singletonList(new AdditionalField("age",Constants.FIELD_STRING_TYPE)));

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
    public void searchDiscount_success() {
        createDiscountCode_success("searchDiscount1");
        createDiscountCode_success("searchDiscount2");
        createDiscountCode_success("searchDiscount3");
        createDiscountCode_success("searchDiscount4");
        createDiscountCode_success("searchDiscount5");

        SearchViewModel<Discount> searchViewModel = new SearchViewModel<>();
        Simple<Discount> eventId = new Simple<>(Operation.eq,"blitTypes-eventDate-event-eventId",eventViewModel.getEventId());
        searchViewModel.setRestrictions(Collections.singletonList(eventId));
        givenRestIntegration()
                .body(searchViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/search?page=0&size=2")
                .then()
                .statusCode(200).body("numberOfElements",equalTo(2));
    }

    @Test
    public void disableDiscountCode_success() {
        DiscountViewModel discountViewModel = createDiscountCode_success("discountDisableName");
        DiscountEnableViewModel discountEnableViewModel = new DiscountEnableViewModel(discountViewModel.getDiscountId(),false);
        givenRestIntegration()
                .when()
                .body(discountEnableViewModel)
                .put(getServerAddress() + "/api/blito/v1.0/discount/set-enable")
                .then()
                .statusCode(200).body("message",equalTo(ResourceUtil.getMessage(com.blito.enums.Response.SUCCESS)));
    }

    @Test
    public void disableDiscountCodeL_validation_fail() {
        DiscountViewModel discountViewModel = createDiscountCode_success("discountCodeValidationFailed");
        DiscountEnableViewModel discountEnableViewModel = new DiscountEnableViewModel(discountViewModel.getDiscountId(),null);
        givenRestIntegration()
                .when()
                .body(discountEnableViewModel)
                .put(getServerAddress() + "/api/blito/v1.0/discount/set-enable")
                .then()
                .statusCode(400)
                .body("message",equalTo(ResourceUtil.getMessage(com.blito.enums.Response.VALIDATION)));
    }

    @Test
    public void disableDiscountCode_notAllowed_fail() {
        DiscountViewModel discountViewModel = createDiscountCode_success("discountCodeNotAllowed");
        DiscountEnableViewModel discountEnableViewModel = new DiscountEnableViewModel(discountViewModel.getDiscountId(),false);
        givenRestIntegrationUser()
                .when()
                .body(discountEnableViewModel)
                .put(getServerAddress() + "/api/blito/v1.0/discount/set-enable")
                .then()
                .statusCode(400)
                .body("message",equalTo(ResourceUtil.getMessage(com.blito.enums.Response.NOT_ALLOWED)));
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
                .post(getServerAddress() + "/api/blito/v1.0/validate-discount-code");
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
                .post(getServerAddress() + "/api/blito/v1.0/validate-discount-code");
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
                .post(getServerAddress() + "/api/blito/v1.0/validate-discount-code");
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
                .post(getServerAddress() + "/api/blito/v1.0/validate-discount-code");
        response.then().statusCode(400);

        discountValidationViewModel.setCode("vaysade");
        discountValidationViewModel.setCount(0);
        discountValidationViewModel.setBlitTypeId(eventViewModel.getEventDates()
                .stream().flatMap(ed->ed.getBlitTypes().stream()).filter(bt->bt.getName().equals("vaysade")).findFirst().get().getBlitTypeId());

        Response response2 = givenRestIntegration()
                .body(discountValidationViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/validate-discount-code");
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
                .post(getServerAddress() + "/api/blito/v1.0/admin/discount/set-discount-code");
        response.then().statusCode(200).body("amount", equalTo(0));
    }

    @Test
    public void updateDiscountCodeByOperator_success(){
        DiscountViewModel discountViewModel = createDiscountCode_success("testUpdate");
        discountViewModel.setPercent(true);
        discountViewModel.setCode("testUpdate");
        discountViewModel.setExpirationDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).plusDays(2).toInstant()));
        discountViewModel.setEffectDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(1).toInstant()));
        discountViewModel.setReusability(50);
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
                .put(getServerAddress() + "/api/blito/v1.0/admin/discount/update-discount-code");
        response.then().statusCode(200).body("reusability", equalTo(50));
    }

    @Test
    public void updateDiscountCode_failNotFound(){
        DiscountViewModel discountViewModel = createDiscountCode_success("testUpdateNotFound");
        discountViewModel.setDiscountId(230L);
        discountViewModel.setPercent(true);
        discountViewModel.setCode("afterUpdateTest");
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
                .put(getServerAddress() + "/api/blito/v1.0/admin/discount/update-discount-code");
        response.then().statusCode(400).body("message", equalTo(ResourceUtil.getMessage(com.blito.enums.Response.DISCOUNT_CODE_NOT_FOUND)));
    }

    @Test
    public void updateDiscountCode_failInvalidPercentage(){
        DiscountViewModel discountViewModel = createDiscountCode_success("testUpdateInvalidPercentage");
        discountViewModel.setPercent(true);
        discountViewModel.setCode("afterUpdateTest");
        discountViewModel.setExpirationDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).plusDays(2).toInstant()));
        discountViewModel.setEffectDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(1).toInstant()));
        discountViewModel.setReusability(10);
        discountViewModel.setPercentage(120D);
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
                .put(getServerAddress() + "/api/blito/v1.0/admin/discount/update-discount-code");
        response.then().statusCode(400).body("message", equalTo(ResourceUtil.getMessage(com.blito.enums.Response.INCONSISTENT_PERCENT)));
    }

    @Test
    public void updateDiscountCode_failBlitTypesNotFound(){
        DiscountViewModel discountViewModel = createDiscountCode_success("testUpdateBlitTypeNotFound");
        discountViewModel.setPercent(true);
        discountViewModel.setCode("afterUpdateTest");
        discountViewModel.setExpirationDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).plusDays(2).toInstant()));
        discountViewModel.setEffectDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(1).toInstant()));
        discountViewModel.setReusability(10);
        discountViewModel.setPercentage(20D);
        discountViewModel.setAmount(0L);
        discountViewModel.setBlitTypeIds(new HashSet<>(Arrays.asList(100L,120L,130L)));

        Response response = givenRestIntegration()
                .body(discountViewModel)
                .when()
                .put(getServerAddress() + "/api/blito/v1.0/admin/discount/update-discount-code");
        response.then().statusCode(400).body("message", equalTo(ResourceUtil.getMessage(com.blito.enums.Response.BLIT_TYPE_NOT_FOUND)));
    }

    @Test
    public void setDiscountCode_failInavalidBlitTypes() {
        DiscountViewModel discountViewModel = new DiscountViewModel();
        discountViewModel.setPercent(true);
        discountViewModel.setCode("UserDiscountCode");
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


        Response response = givenRestIntegrationUser()
                .body(discountViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/discount/set-discount-code");
        response.then().statusCode(400).body("message", equalTo(ResourceUtil.getMessage(com.blito.enums.Response.NOT_ALLOWED)));
    }

    @Test
    public void setDiscountAllEvents_sucess() {
        createAdditionalEventsForTest();
        DiscountViewModel discountViewModel = new DiscountViewModel();
        discountViewModel.setPercent(true);
        discountViewModel.setCode("DiscountAllEvents");
        discountViewModel.setExpirationDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).plusDays(1).toInstant()));
        discountViewModel.setEffectDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(1).toInstant()));
        discountViewModel.setReusability(10);
        discountViewModel.setPercentage(30D);
        discountViewModel.setAmount(0L);
        discountViewModel.setBlitTypeIds(new HashSet<Long>(Arrays.asList(0L)));

        Response response = givenRestIntegration()
                .body(discountViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/admin/discount/set-discount-all-events");
        response.then().statusCode(200);

        DiscountViewModel discountViewModel1 = response.thenReturn().as(DiscountViewModel.class);
        System.out.println("************");
        discountViewModel1.getBlitTypeIds().forEach(bt -> {
            BlitType blitType= blitTypeRepository.findOne(bt);
            System.out.println(blitType.getEventDate().getEvent().getEventName());
        });
    }
}
