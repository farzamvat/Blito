package blito.test.unit;

import com.blito.Application;
import com.blito.enums.BankGateway;
import com.blito.enums.PaymentStatus;
import com.blito.enums.Response;
import com.blito.exceptions.PayDotIrException;
import com.blito.exceptions.ZarinpalException;
import com.blito.models.CommonBlit;
import com.blito.payments.payir.viewmodel.PayDotIrClient;
import com.blito.payments.payir.viewmodel.response.PayDotIrVerificationResponse;
import com.blito.payments.zarinpal.PaymentVerificationResponse;
import com.blito.payments.zarinpal.client.ZarinpalClient;
import com.blito.repositories.BlitRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.services.PaymentService;
import io.vavr.control.Try;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertTrue;

/**
 * @author Farzam Vatanzadeh
 * 12/3/17
 * Mailto : farzam.vat@gmail.com
 **/
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentServiceTest {
    @MockBean
    private ZarinpalClient zarinpalClient;
    @MockBean
    private PayDotIrClient payDotIrClient;
    @SpyBean
    private PaymentService paymentService;
    @MockBean
    private BlitRepository blitRepository;


    private CommonBlit getCommonblit()
    {
        CommonBlit blit = new CommonBlit();
        blit.setTrackCode("123456");
        blit.setCustomerEmail("farzam.vat@gmail.com");
        blit.setCustomerMobileNumber("09124337522");
        blit.setCustomerName("Farzam Vat");
        blit.setPrimaryAmount(2000L);
        blit.setTotalAmount(2000L);
        blit.setBankGateway(BankGateway.ZARINPAL.name());
        blit.setPaymentStatus(PaymentStatus.PENDING.name());
        blit.setCount(1);
        blit.setEventAddress("Address");
        blit.setEventName("event name");
        blit.setToken("8190251");
        return blit;
    }

    @Test
    public void verifyAndFinalizePayment_zarinpalVerificationSuccess() {
        CommonBlit blit = getCommonblit();
        PaymentVerificationResponse zarinpalResponse =
                new PaymentVerificationResponse();
        zarinpalResponse.setStatus(100);
        zarinpalResponse.setRefID(234);

        Mockito.when(zarinpalClient.getPaymentVerificationResponse(Mockito.anyInt(),Mockito.anyString()))
                .thenReturn(Try.of(() -> zarinpalResponse));
        Mockito.when(blitRepository.save(blit))
                .thenReturn(blit);

        Try.of(() -> paymentService.verifyAndFinalizePayment(blit,(b,verification) -> {
            assertTrue(true);
            return b;
        })).onFailure((throwable) -> assertTrue(false));
    }
    @Test
    public void verifyAndFinalizePayment_zarinpalVerificationFailure() {
        CommonBlit blit = getCommonblit();
        PaymentVerificationResponse zarinpalResponse =
                new PaymentVerificationResponse();
        zarinpalResponse.setStatus(101);
        zarinpalResponse.setRefID(234);
        Mockito.when(zarinpalClient.getPaymentVerificationResponse(Mockito.anyInt(),Mockito.anyString()))
                .thenReturn(Try.of(() -> {
                    throw new ZarinpalException(ZarinpalException.generateMessage(zarinpalResponse.getStatus()));
                }));
        Mockito.when(blitRepository.save(blit))
                .thenReturn(blit);
        Try.ofSupplier(() -> paymentService.verifyAndFinalizePayment(blit,(b,verification) -> {
            assertTrue(false);
            return b;
        })).onFailure((throwable) -> assertTrue(true));
    }

    @Test
    public void verifyAndFinalizePayment_payVerificationSuccess() {
        CommonBlit blit = getCommonblit();
        blit.setBankGateway(BankGateway.PAYDOTIR.name());
        PayDotIrVerificationResponse response =
                new PayDotIrVerificationResponse();
        response.setStatus(1);
        response.setAmount(blit.getTotalAmount().intValue());
        Mockito.when(payDotIrClient.verifyPaymentRequest(Mockito.anyInt()))
                .thenReturn(Try.of(() -> response));
        Mockito.when(blitRepository.save(blit))
                .thenReturn(blit);
        Try.of(() -> paymentService.verifyAndFinalizePayment(blit,(b,verification) -> {
            assertTrue(true);
            return b;
        })).onFailure(throwable -> assertTrue(false));
    }

    @Test
    public void verifyAndFinalizePayment_payVerificationFailure() {
        CommonBlit blit = getCommonblit();
        blit.setBankGateway(BankGateway.PAYDOTIR.name());
        PayDotIrVerificationResponse response =
                new PayDotIrVerificationResponse();
        response.setStatus(0);
        response.setAmount(blit.getTotalAmount().intValue());
        Mockito.when(payDotIrClient.verifyPaymentRequest(Mockito.anyInt()))
                .thenReturn(Try.of(() -> {
                    throw new PayDotIrException(ResourceUtil.getMessage(Response.PAY_DOT_IR_ERROR));
                }));
        Mockito.when(blitRepository.save(blit))
                .thenReturn(blit);
        Try.of(() -> paymentService.verifyAndFinalizePayment(blit,(b,verification) -> {
            assertTrue(false);
            return b;
        })).onFailure(throwable -> assertTrue(true));
    }
}
