package blito.test.integration;

import com.blito.Application;
import com.blito.payments.payir.viewmodel.PayDotIrClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertTrue;

/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/
@ActiveProfiles("test")
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class PayDotIrIntegrationTest {

    @Autowired
    private PayDotIrClient payDotIrClient;

    @Test
    public void createPaymentRequest_success() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String api = "e4038e4a7a57b45400aeb9e4fbd5d121";
        payDotIrClient.createPaymentRequest(1000,"09124337522","23")
                .onSuccess(payDotIrResponse -> {
                    System.out.println(payDotIrResponse.toString());
                            assertTrue(true);
                }).onFailure(throwable -> {
                    System.out.println(throwable.getMessage());
                    assertTrue(false);
        });
    }

    @Test
    public void createPaymentRequest_failure() {

    }
}
