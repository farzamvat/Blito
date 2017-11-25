package blito.test.integration;

import org.junit.Test;

/**
 * @author Farzam Vatanzadeh
 * 11/4/17
 * Mailto : farzam.vat@gmail.com
 **/

public class RefreshTokenIntegrationTest extends AbstractRestControllerTest {

    @Test
    public void refresh_token_success() {
        givenRestIntegration()
                .get(getServerAddress() + "/api/blito/v1.0/refresh?refresh_token=" + super.tokenModel.getRefreshToken())
                .then().statusCode(200);
    }
}
