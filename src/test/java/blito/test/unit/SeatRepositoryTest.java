package blito.test.unit;

import com.blito.Application;
import com.blito.common.Salon;
import com.blito.configs.Constants;
import com.blito.models.Seat;
import com.blito.repositories.SalonRepository;
import com.blito.repositories.SeatRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Farzam Vatanzadeh
 * 10/10/17
 * Mailto : farzam.vat@gmail.com
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class SeatRepositoryTest {

    private SeatRepository seatRepository;
    private SalonRepository salonRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setSeatRepository(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }
    @Autowired
    public void setSalonRepository(SalonRepository salonRepository) {
        this.salonRepository = salonRepository;
    }

    @Test
    public void selectSeatsBySeatUids_inCaseOf_notIncludes_all_uids() {
        Salon salon = Try.of(() -> new File(SeatRepositoryTest.class.getResource(Constants.BASE_SALON_SCHEMAS + "/" + salonRepository.findAll().stream().findAny().get().getPlanPath()).toURI()))
                .flatMap(file -> Try.of(() -> objectMapper.readValue(file,Salon.class))).get();
        List<Seat> result = seatRepository.findBySeatUidIn(Arrays.asList(
                salon.getSections().stream().flatMap(section -> section.getRows().stream()).flatMap(row -> row.getSeats().stream()).findAny().get().getUid(),
                "asdasdasd"));
        assertEquals(1,result.size());
    }
}
