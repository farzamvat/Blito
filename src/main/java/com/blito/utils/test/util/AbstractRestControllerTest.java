package com.blito.utils.test.util;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.Application;
import com.blito.repositories.RoleRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.account.RegisterVm;
import com.blito.rest.viewmodels.account.TokenModel;
import com.blito.services.JwtService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AbstractRestControllerTest {
    @Value("${serverAddress}")
    private String serverAddress;
    @LocalServerPort
    private int port;
    @Value("${blito.admin.username}")
    protected String admin_username;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    protected JwtService jwtService;

    private static boolean initialized = false;
    private static String token;
    private static String userToken;
    protected static TokenModel tokenModel;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        AbstractRestControllerTest.token = token;
    }

    public static String getUserToken() {
        return userToken;
    }

    public static void setUserToken(String userToken) {
        AbstractRestControllerTest.userToken = userToken;
    }

    @Test
    public void test() {

    }

    @Transactional
    @Before
    public void initialize()
    {
        if (!initialized)
        {
            initialized = true;
            TokenModel tokenModel = jwtService.generateToken(admin_username).join();
            TokenModel userTokenModel = jwtService.generateAccessToken(createUser()).join();
            token = tokenModel.getAccessToken();
            userToken = userTokenModel.getAccessToken();
            this.tokenModel = tokenModel;
        }
    }

    public RequestSpecification givenRestIntegration()
    {
        return RestAssured.given()
                .header("X-AUTH-TOKEN","Bearer " + token)
                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    protected RequestSpecification givenRestIntegrationUser()
    {
        createUser();
        return RestAssured.given()
                .header("X-AUTH-TOKEN","Bearer " + userToken)
                .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    protected String getAdmin_username()
    {
        return admin_username;
    }

    protected String getServerAddress()
    {
        return serverAddress + port;
    }

    private String createUser(){
        RegisterVm registerVm = new RegisterVm();
        registerVm.setFirstname("Hasti");
        registerVm.setLastname("Sahabi");
        registerVm.setEmail("hasti.sahabi@yahoo.com");
        registerVm.setMobile("09127976837");
        registerVm.setPassword("12345678");
        registerVm.setConfirmPassword("12345678");

        if(!userRepository.findByEmail(registerVm.getEmail()).isPresent()) {
            Response response = RestAssured.given()
                    .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .body(registerVm)
                    .when()
                    .post(getServerAddress() + "/api/blito/v1.0/register");
            response.then().statusCode(200);
        }
        return registerVm.getEmail();
    }
}
