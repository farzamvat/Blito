package blito.test.integration;

import com.blito.configs.Constants;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.EventRepository;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.event.AdditionalField;
import com.blito.rest.viewmodels.event.AdminChangeEventOperatorStateVm;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.utils.test.util.AbstractEventRestControllerTest;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class UpdateEventWithEditeVersionTest extends AbstractEventRestControllerTest {
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
        EventViewModel eventViewModel = createEvent_success("My Event", "Event Host", Optional.empty()).thenReturn().body().as(EventViewModel.class);
        approveEvent_success(eventViewModel.getEventId(), OperatorState.APPROVED);
        openEventState_success(eventViewModel.getEventId(), State.OPEN);
        openEventDateState_success(eventViewModel.getEventDates().stream().findFirst().get().getEventDateId(), State.OPEN);
        openBlitTypeState_success(eventViewModel.getEventDates().stream().flatMap(eventDate -> eventDate.getBlitTypes().stream()).map(blitType -> blitType.getBlitTypeId()).collect(Collectors.toList()), State.OPEN);
        return eventViewModel;
    }

    @Test
    public void updateEvent() {

        eventViewModel.setEventName("Edited Name Event");


        Response eventUpdateResponse =
                givenRestIntegration()
                        .body(eventViewModel)
                        .when()
                        .put(getServerAddress() + "/api/blito/v1.0/events");
        eventUpdateResponse.then().statusCode(202);

        EventViewModel afterUpdateViewModel = eventUpdateResponse.thenReturn().as(EventViewModel.class);

        AdminChangeEventOperatorStateVm operatorStateVm = new AdminChangeEventOperatorStateVm();
        operatorStateVm.setOperatorState(OperatorState.APPROVED);
        operatorStateVm.setEventId(afterUpdateViewModel.getEventId());
        Response changeOperatorStateResponse =
                givenRestIntegration()
                    .body(operatorStateVm)
                    .when()
                    .put(getServerAddress() + "/api/blito/v1.0/admin/events/change-event-operator-state");
        changeOperatorStateResponse.then().statusCode(200);

    }

}
