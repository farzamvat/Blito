package blito.test.integration;

import com.blito.Application;
import com.blito.exceptions.PaymentException;
import com.blito.models.Blit;
import com.blito.payments.payir.viewmodel.request.PayDotIrCallbackRequest;
import com.blito.repositories.BlitRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.controllers.PaymentCallbackController;
import com.blito.services.PaymentService;
import io.restassured.RestAssured;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author Farzam Vatanzadeh
 * 12/3/17
 * Mailto : farzam.vat@gmail.com
 **/
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class PaymentRestIntegrationTest {
    @Value("${serverAddress}")
    private String serverAddress;
    @Value("${api.base.url}")
    private String baseUrl;
    @LocalServerPort
    private int port;
    @MockBean
    private PaymentService paymentService;
    @MockBean
    private BlitRepository blitRepository;
    @SpyBean
    private PaymentCallbackController paymentCallbackController;

    private String getServerAddress() {
        return serverAddress + port + baseUrl;
    }

    @Test
    public void zarinpalCallback_fail() {
        Blit blit = new Blit();
        blit.setTrackCode("12345");
        blit.setToken("myTestToken");
        Mockito.when(paymentService.finalizingPayment(Mockito.any()))
                .thenThrow(new PaymentException(ResourceUtil.getMessage(com.blito.enums.Response.PAYMENT_ERROR)));
        Mockito.when(blitRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.of(blit));
        Mockito.when(paymentCallbackController.getServerAddress()).thenReturn(serverAddress + port);
        RestAssured.given()
                .when()
                .get(getServerAddress() + "/zarinpal-payment-callback?Authority=myTestToken&Status=OK")
                .then().statusCode(200);
    }
    @Test
    public void zarinpalCallback_success() {
        Blit blit = new Blit();
        blit.setTrackCode("12345");
        blit.setToken("myTestToken");
        Mockito.when(paymentService.finalizingPayment(Mockito.any()))
                .thenReturn(blit);
        Mockito.when(blitRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.of(blit));
        Mockito.when(paymentCallbackController.getServerAddress()).thenReturn(serverAddress + port);
        RestAssured.given()
                .when()
                .get(getServerAddress() + "/zarinpal-payment-callback?Authority=myTestToken&Status=OK")
                .then().statusCode(200);
    }

    @Test
    public void payDotIrCallback_fail() {
        Blit blit = new Blit();
        blit.setTrackCode("12345");
        blit.setToken("893487");
        Mockito.when(paymentService.finalizingPayment(Mockito.any()))
                .thenThrow(new PaymentException(ResourceUtil.getMessage(com.blito.enums.Response.PAYMENT_ERROR)));
        Mockito.when(blitRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.of(blit));
        Mockito.when(paymentCallbackController.getServerAddress()).thenReturn(serverAddress + port);
        PayDotIrCallbackRequest request = new PayDotIrCallbackRequest();
        request.setMessage("message");
        request.setStatus(1);
        request.setTransId(Integer.parseInt(blit.getToken()));
        request.setFactorNumber("12345");
        request.setMobile("09124337522");
        RestAssured.given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .body(request)
                .post(getServerAddress() + "/pay-payment-callback")
                .then().header("location",equalTo(serverAddress + port + "/payment/" + blit.getTrackCode())).statusCode(302);
    }

    @Test
    public void payDotIrCallback_success() {
        Blit blit = new Blit();
        blit.setTrackCode("12345");
        blit.setToken("893487");
        Mockito.when(paymentService.finalizingPayment(Mockito.any()))
                .thenReturn(blit);
        Mockito.when(blitRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.of(blit));
        Mockito.when(paymentCallbackController.getServerAddress()).thenReturn(serverAddress + port);
        PayDotIrCallbackRequest request = new PayDotIrCallbackRequest();
        request.setMessage("message");
        request.setStatus(1);
        request.setTransId(Integer.parseInt(blit.getToken()));
        request.setFactorNumber("12345");
        request.setMobile("09124337522");
        RestAssured.given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .when()
                .body(request)
                .post(getServerAddress() + "/pay-payment-callback")
                .then().header("location",equalTo(serverAddress + port + "/payment/" + blit.getTrackCode())).statusCode(302);
    }
}
