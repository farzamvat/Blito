package com.blito.utils.test.util;

import com.blito.configs.Constants;
import com.blito.enums.EventType;
import com.blito.enums.HostType;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.blittype.ChangeBlitTypeStateVm;
import com.blito.rest.viewmodels.event.AdditionalField;
import com.blito.rest.viewmodels.event.AdminChangeEventOperatorStateVm;
import com.blito.rest.viewmodels.event.ChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.ChangeEventDateStateVm;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import io.restassured.response.Response;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * @author Farzam Vatanzadeh
 * 10/21/17
 * Mailto : farzam.vat@gmail.com
 **/

public class AbstractEventRestControllerTest extends AbstractRestControllerTest {
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

    public EventViewModel createSampleEventViewModel(EventHostViewModel eventHostViewModel, String eventname)
    {
        EventViewModel eventViewModel = new EventViewModel();
        eventViewModel.setAddress("Amirabad");
        eventViewModel.setBlitSaleEndDate(Timestamp.from(ZonedDateTime.now().plusDays(9).toInstant()));
        eventViewModel.setBlitSaleStartDate(Timestamp.from(ZonedDateTime.now().plusDays(3).toInstant()));
        eventViewModel.setDescription("Description");
        eventViewModel.setEventHostId(eventHostViewModel.getEventHostId());
        eventViewModel.setEventHostName(eventHostViewModel.getHostName());
        eventViewModel.setEventName(eventname);
        eventViewModel.setEventType(EventType.CONCERT);

        EventDateViewModel eventDateViewModel = new EventDateViewModel();
        BlitTypeViewModel blitTypeViewModel1 = new BlitTypeViewModel();
        BlitTypeViewModel blitTypeViewModel2 = new BlitTypeViewModel();
        BlitTypeViewModel blitTypeViewModel3 = new BlitTypeViewModel();
        eventDateViewModel.setDate(Timestamp.from(ZonedDateTime.now().plusDays(10).toInstant()));


        blitTypeViewModel1.setCapacity(20);
        blitTypeViewModel1.setFree(false);
        blitTypeViewModel1.setName("vaysade");
        blitTypeViewModel1.setPrice(20000);

        blitTypeViewModel2.setCapacity(30);
        blitTypeViewModel2.setFree(false);
        blitTypeViewModel2.setName("neshaste");
        blitTypeViewModel2.setPrice(40000);

        blitTypeViewModel3.setCapacity(20);
        blitTypeViewModel3.setFree(true);
        blitTypeViewModel3.setName(Constants.HOST_RESERVED_SEATS);

        eventDateViewModel.setBlitTypes(new HashSet<>(Arrays.asList(blitTypeViewModel1, blitTypeViewModel2,blitTypeViewModel3)));
        eventViewModel.setEventDates(new HashSet<>(Arrays.asList(eventDateViewModel)));
        return eventViewModel;
    }

    public Response createEvent_success(String eventName, String eventHostName,Optional<List<AdditionalField>> optionalAdditionalFields)
    {
        EventHostViewModel eventHostViewModel = createEventHost_success(eventHostName).thenReturn().body().as(EventHostViewModel.class);

        EventViewModel eventViewModel = createSampleEventViewModel(eventHostViewModel, eventName);
        optionalAdditionalFields.ifPresent(eventViewModel::setAdditionalFields);
        Response response = givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events");
        response.then().statusCode(201);
        return response;
    }

    public void openEventState_success(long eventId, State state) {
        ChangeEventStateVm changeEventStateVm = new ChangeEventStateVm();
        changeEventStateVm.setState(state);
        changeEventStateVm.setEventId(eventId);

        givenRestIntegration()
                .body(changeEventStateVm)
                .when()
                .put(getServerAddress() + "/api/blito/v1.0/admin/events/change-event-state")
                .then().statusCode(200);
    }

    public void openEventDateState_success(long eventDateId, State state) {
        ChangeEventDateStateVm changeEventDateStateVm = new ChangeEventDateStateVm();
        changeEventDateStateVm.setEventDateId(eventDateId);
        changeEventDateStateVm.setEventDateState(state);

        givenRestIntegration()
                .body(changeEventDateStateVm)
                .when()
                .put(getServerAddress()  +"/api/blito/v1.0/admin/events/change-event-date-state")
                .then().statusCode(200);
    }

    public void openBlitTypeState_success(List<Long> blitTypeIds, State state) {
        blitTypeIds.forEach(blitTypeId -> {
            ChangeBlitTypeStateVm changeBlitTypeStateVm = new ChangeBlitTypeStateVm();
            changeBlitTypeStateVm.setBlitTypeId(blitTypeId);
            changeBlitTypeStateVm.setBlitTypeState(state);

            givenRestIntegration()
                    .body(changeBlitTypeStateVm)
                    .when()
                    .put(getServerAddress() + "/api/blito/v1.0/admin/events/change-blit-type-state")
                    .then().statusCode(200);
        });
    }

    public void approveEvent_success(long eventId, OperatorState operatorState) {
        AdminChangeEventOperatorStateVm operatorStateVm = new AdminChangeEventOperatorStateVm();
        operatorStateVm.setEventId(eventId);
        operatorStateVm.setOperatorState(operatorState);
        givenRestIntegration()
                .body(operatorStateVm)
                .when()
                .put(getServerAddress() + "/api/blito/v1.0/admin/events/change-event-operator-state")
                .then().statusCode(200);
    }

    public void deleteEvent_success(long eventId) {
        givenRestIntegration()
                .when()
                .delete(getServerAddress() + "/api/blito/v1.0/admin/events/"+eventId)
                .then().statusCode(200);
    }
}
