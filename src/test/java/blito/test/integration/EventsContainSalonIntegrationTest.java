package blito.test.integration;

import com.blito.common.Salon;
import com.blito.common.Seat;
import com.blito.configs.Constants;
import com.blito.enums.HostType;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @author Farzam Vatanzadeh
 * 10/10/17
 * Mailto : farzam.vat@gmail.com
 **/

public class EventsContainSalonIntegrationTest extends AbstractRestControllerTest {
    private EventCreatingScenarioRestIntegrationTest createEventUtil = new EventCreatingScenarioRestIntegrationTest();
    private ObjectMapper objectMapper;
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Test
    public void createEventWithSalon() {
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

        EventViewModel eventViewModel = createEventUtil.createSampleEventViewModel(eventHostViewModel,"salonEvent");
        eventViewModel.getEventDates().stream().findAny().ifPresent(eventDateViewModel -> {
            BlitTypeViewModel blitTypeViewModel = eventDateViewModel.getBlitTypes().stream()
                    .filter(btvm -> btvm.getName().equals("neshaste")).findFirst().get();
            Salon salon = Try.of(() -> new File(EventsContainSalonIntegrationTest.class.getResource(Constants.BASE_SALON_SCHEMAS + "/TestSalon" ).toURI()))
                    .flatMapTry(file -> Try.of(() -> objectMapper.readValue(file,Salon.class))).get();
            eventViewModel.setSalonUid(salon.getUid());
            blitTypeViewModel.setSeatUids(
                    salon.getSections().stream().flatMap(section -> section.getRows().stream())
                    .filter(row -> row.getName().equals("2")).flatMap(row -> row.getSeats().stream()).map(Seat::getUid).collect(Collectors.toSet())
            );
            eventDateViewModel.setBlitTypes(new HashSet<>(Collections.singletonList(blitTypeViewModel)));
        });

        givenRestIntegration()
                .body(eventViewModel)
                .when()
                .post(getServerAddress() + "/api/blito/v1.0/events")
                .then().statusCode(201);
    }
}
