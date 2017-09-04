package blito.test.integration;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.Application;
import com.blito.models.Role;
import com.blito.models.User;
import com.blito.repositories.RoleRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.account.RegisterVm;
import com.blito.rest.viewmodels.account.TokenModel;
import com.blito.rest.viewmodels.account.UserViewModel;
import com.blito.services.JwtService;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

import static io.restassured.RestAssured.given;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractRestControllerTest {
    @Value("${serverAddress}")
    private String serverAddress;
    @Value("${blito.admin.username}")
    private String admin_username;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    public static boolean initialized = false;
    private static String token;
    private static String userToken;

    @Transactional
    @Before
    public void initialize()
    {
        if (!initialized)
        {
            initialized = true;
            TokenModel tokenModel = jwtService.generateAccessToken(admin_username).join();
            TokenModel userTokenModel = jwtService.generateAccessToken(createUser()).join();
            token = tokenModel.getAccessToken();
            userToken = userTokenModel.getAccessToken();

        }
    }

    protected RequestSpecification givenRestIntegration()
    {
        return given()
                .header("X-AUTH-TOKEN","Bearer " + token)
                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    protected RequestSpecification givenRestIntegrationUser()
    {
        return given()
                .header("X-AUTH-TOKEN","Bearer " + userToken)
                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    protected String getAdmin_username()
    {
        return admin_username;
    }

    protected String getServerAddress()
    {
        return serverAddress;
    }

    protected String createUser(){
        RegisterVm registerVm = new RegisterVm();
        registerVm.setFirstname("Hasti");
        registerVm.setLastname("Sahabi");
        registerVm.setEmail("hasti.sahabi@yahoo.com");
        registerVm.setMobile("09127976837");
        registerVm.setPassword("12345678");
        registerVm.setConfirmPassword("12345678");

        Response response = given()
                                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .body(registerVm)
                                .when()
                                .post(getServerAddress() + "/api/blito/v1.0/register");
        response.then().statusCode(201);
        return registerVm.getEmail();
    }
}
