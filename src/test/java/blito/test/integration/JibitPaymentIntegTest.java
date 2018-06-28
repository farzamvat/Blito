package blito.test.integration;

import com.blito.Application;
import com.blito.payments.jibit.JibitPaymentRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class JibitPaymentIntegTest {
    @Value("${jibit.merchant.id}")
    private String merchantId;
    @Value("${jibit.payment.url}")
    private String paymentUrl;
    @Test
    public void createJibitPurchaseRequest() {
        JibitPaymentRequest request =
                new JibitPaymentRequest(1000L,"http://www.google.com",77,"09124337522");
        given().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(request)
                .post(paymentUrl)
                .then().body("",equalTo(""));
    }
}
