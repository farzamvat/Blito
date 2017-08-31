package blito.test.unit;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.Application;
import com.blito.services.MailService;
import com.blito.services.SmsService;
import com.blito.services.util.AsyncUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = Application.class)
public class AsyncUtilTest {
    @Autowired
    private MailService mailService;
    @Autowired
    private SmsService smsService;
    @Test
    public void sendMailMockException_fail()
    {
        Optional<Throwable> sendMailResult =
                AsyncUtil.run(() -> mailService.sendEmail("123","123","123")).join();
        assertTrue(sendMailResult.isPresent());
        assertEquals("failed",sendMailResult.get().getMessage());
    }

    @Test
    public void sendSmsMock_success() {
        assertFalse(AsyncUtil.run(() -> smsService.sendBlitRecieptSms("123","123")).join().isPresent());
    }
}
