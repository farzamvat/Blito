package blito.test.integration;

import com.blito.common.Salon;
import com.blito.configs.Constants;
import com.blito.rest.viewmodels.salon.SalonViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * @author Farzam Vatanzadeh
 * 10/28/17
 * Mailto : farzam.vat@gmail.com
 **/

public class SalonSvgUploadIntegrationTest extends AbstractRestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void uploadSalonAndSectionsSvg_success() {
        Salon salon = Try.of(() -> new File(EventsContainSalonIntegrationTest.class.getResource(Constants.BASE_SALON_SCHEMAS + "/TestSalon" ).toURI()))
                .flatMapTry(file -> Try.of(() -> objectMapper.readValue(file,Salon.class))).get();
        SalonViewModel salonViewModel =
                givenRestIntegration()
                .when()
                .get(getServerAddress() + "/api/blito/v1.0/salons/"+ salon.getUid())
                .thenReturn().body().as(SalonViewModel.class);

    }
}
