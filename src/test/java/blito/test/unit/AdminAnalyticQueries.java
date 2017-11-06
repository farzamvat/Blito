package blito.test.unit;

import com.blito.Application;
import com.blito.enums.OperatorState;
import com.blito.enums.PaymentStatus;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.EventRepository;
import com.blito.services.AdminAnalyticsReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

/**
 * @author Farzam Vatanzadeh
 * 11/6/17
 * Mailto : farzam.vat@gmail.com
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("local")
public class AdminAnalyticQueries {
    @Autowired
    private BlitRepository blitRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AdminAnalyticsReportService analyticsReportService;

    @Test
    public void analyticQueriesBlitEntityTest() {
        assertEquals(798,blitRepository.countByPaymentStatus(PaymentStatus.PAID.name()).intValue());

        assertEquals(29,blitRepository.countByPaymentStatusAndCreatedAtGreaterThan(PaymentStatus.PAID.name(),
                Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusMonths(1).minusDays(1).toInstant())).intValue());

        assertEquals(213000,blitRepository.sumOfTotalAmountFromDate( Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusMonths(1).minusDays(1).toInstant()),
                PaymentStatus.PAID.name()).get().intValue());

        assertEquals(49,blitRepository.countDistinctCustomerEmailByPaymentStatusAndUserIsNotNull(PaymentStatus.PAID.name()).get().intValue());

        assertEquals(616,blitRepository.countDistinctCustomerEmailByPaymentStatusAndUserIsNull(PaymentStatus.PAID.name()).get().intValue());

        assertEquals(663,blitRepository.countDistinctCustomerEmailByPaymentStatus(PaymentStatus.PAID.name()).get().intValue());

        assertEquals(13,blitRepository.numberOfUsersWhoBoughtMoreThanOne().get().intValue());

        assertEquals(76,blitRepository.numberOfCustomersWhoBoughtMoreThanOne().get().intValue());

    }

    @Test
    public void analyticQueriesEventEntityTest() {
        eventRepository.averageCapacityAndPriceGroupByEventType(OperatorState.APPROVED.name()).forEach(o -> {
            System.out.println(o.toString());
        });
    }

    @Test
    public void adminAnalyticsReportTest() {
        System.out.println(analyticsReportService.getAdminAnalyticsReport());
    }
}
