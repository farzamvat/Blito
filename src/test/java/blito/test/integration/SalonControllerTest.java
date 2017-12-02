package blito.test.integration;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.repositories.SalonRepository;
import com.blito.utils.test.util.AbstractRestControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.notNullValue;

public class SalonControllerTest extends AbstractRestControllerTest {
    @Autowired
    private SalonRepository salonRepository;

    @Test
    public void getSalons_api_status2xx() {
        givenRestIntegration()
                .when()
                .get(getServerAddress() + "/api/blito/v1.0/salons")
                .then().statusCode(200);
    }

    @Test
    public void getSalonByUid_api_status2xx() {
        givenRestIntegration()
                .when()
                .get(getServerAddress() + "/api/blito/v1.0/salons/" + salonRepository.findAll().stream().findAny().get().getSalonUid())
                .then().statusCode(200).body("schema",notNullValue());
    }
}