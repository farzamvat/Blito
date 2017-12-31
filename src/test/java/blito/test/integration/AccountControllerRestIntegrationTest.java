package blito.test.integration;

import com.blito.enums.Response;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.account.ChangePasswordViewModel;
import com.blito.rest.viewmodels.account.LoginViewModel;
import com.blito.rest.viewmodels.account.RegisterVm;
import com.blito.rest.viewmodels.account.TokenModel;
import com.blito.utils.test.util.AbstractRestControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Farzam Vatanzadeh
 * 12/31/17
 * Mailto : farzam.vat@gmail.com
 **/

public class AccountControllerRestIntegrationTest extends AbstractRestControllerTest {

    @Value("${api.base.url}")
    private String baseUrl;
    @Autowired
    private UserRepository userRepository;
    @Test
    public void registerUser_activate_changePassword_success() {
        RegisterVm registerVm = new RegisterVm();
        registerVm.setEmail("farzam.vat@gmail.com");
        registerVm.setFirstname("farzam");
        registerVm.setLastname("vatanzadeh");
        registerVm.setMobile("09124337522");
        registerVm.setPassword("password");
        registerVm.setConfirmPassword("password");
        given()
            .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
            .when()
            .body(registerVm)
            .post(getServerAddress() + baseUrl + "/register")
            .then().statusCode(200)
            .body("status",equalTo(true))
            .body("message", equalTo(ResourceUtil.getMessage(Response.REGISTER_SUCCESS)));
        assertEquals(1,userRepository.findByEmail(registerVm.getEmail()).get().getRoles().size());
        assertNotNull(userRepository.findByEmail(registerVm.getEmail()).get().getActivationKey());

        String activationKey = userRepository.findByEmail(registerVm.getEmail()).get().getActivationKey();

        given()
            .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
            .when()
            .get(getServerAddress() + baseUrl + "/activate?email="+ registerVm.getEmail() + "&key=" + activationKey)
            .then().statusCode(200);

        LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.setEmail(registerVm.getEmail());
        loginViewModel.setPassword(registerVm.getPassword());
        TokenModel tokenModel =
                given()
                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when().body(loginViewModel)
                .post(getServerAddress() + baseUrl + "/login")
                .then().extract().body().as(TokenModel.class);

        ChangePasswordViewModel changePasswordViewModel = new ChangePasswordViewModel();
        changePasswordViewModel.setOldPassowrd("password");
        changePasswordViewModel.setNewPassword("password1");
        changePasswordViewModel.setConfirmNewPassword("password1");
        given().header("X-AUTH-TOKEN","Bearer " + tokenModel.getAccessToken())
                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE).when().body(changePasswordViewModel)
                .post(getServerAddress() + baseUrl + "/account/change-password")
                .then().statusCode(200);
    }

    @Test
    public void registerUser_retrySendingActivationKey_succes() {
        RegisterVm registerVm = new RegisterVm();
        registerVm.setEmail("farzam.vat@gmail.com");
        registerVm.setFirstname("farzam");
        registerVm.setLastname("vatanzadeh");
        registerVm.setMobile("09124337522");
        registerVm.setPassword("password");
        registerVm.setConfirmPassword("password");
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .body(registerVm)
                .post(getServerAddress() + baseUrl + "/register")
                .then().statusCode(200)
                .body("status",equalTo(true))
                .body("message", equalTo(ResourceUtil.getMessage(Response.REGISTER_SUCCESS)));
        assertEquals(1,userRepository.findByEmail(registerVm.getEmail()).get().getRoles().size());
        assertNotNull(userRepository.findByEmail(registerVm.getEmail()).get().getActivationKey());

        given()
                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(getServerAddress() + baseUrl + "/retry-activation?email="+ registerVm.getEmail())
                .then().statusCode(200).body("message",equalTo(ResourceUtil.getMessage(Response.SUCCESS)));
    }

    @Test
    public void retrySendingActivationKey_userNotFound_fail() {
        given()
                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .get(getServerAddress() + baseUrl + "/retry-activation?email="+ "fifi@gmail.com")
                .then().statusCode(400).body("message",equalTo(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
    }
}
