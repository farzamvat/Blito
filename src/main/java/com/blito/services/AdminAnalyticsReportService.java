package com.blito.services;

import com.blito.enums.OperatorState;
import com.blito.enums.PaymentStatus;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.adminreport.AdminAnalyticViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Farzam Vatanzadeh
 * 11/6/17
 * Mailto : farzam.vat@gmail.com
 **/
@Service
public class AdminAnalyticsReportService {
    private UserRepository userRepository;
    private BlitRepository blitRepository;
    private EventRepository eventRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setBlitRepository(BlitRepository blitRepository) {
        this.blitRepository = blitRepository;
    }

    @Autowired
    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public AdminAnalyticViewModel getAdminAnalyticsReport() {
        AdminAnalyticViewModel analyticViewModel = new AdminAnalyticViewModel();
        analyticViewModel.setTotalNumberOfUsers(Long.valueOf(userRepository.count()));
        analyticViewModel.setTotalNumberOfPaidBlits(blitRepository.countByPaymentStatus(PaymentStatus.PAID.name()));
        analyticViewModel.setNumberOfPaidBlitsInLastMonth(blitRepository.countByPaymentStatusAndCreatedAtGreaterThan(PaymentStatus.PAID.name(),
                Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusMonths(1).toInstant())));
        blitRepository.numberOfUsersWhoBoughtMoreThanOne().map(BigInteger::longValue).ifPresent(analyticViewModel::setNumberOfRegisteredWhoBoughtMoreThanOne);
        blitRepository.numberOfCustomersWhoBoughtMoreThanOne().map(BigInteger::longValue).ifPresent(analyticViewModel::setNumberOfUnregisteredWhoBoughtMoreThanOne);
        blitRepository.countDistinctCustomerEmailByPaymentStatus(PaymentStatus.PAID.name()).map(BigInteger::longValue).ifPresent(analyticViewModel::setCountOfBlitBuyers);
        blitRepository.countDistinctCustomerEmailByPaymentStatusAndUserIsNull(PaymentStatus.PAID.name()).map(BigInteger::longValue).ifPresent(analyticViewModel::setCountOfUnregisteredBlitBuyers);
        blitRepository.countDistinctCustomerEmailByPaymentStatusAndUserIsNotNull(PaymentStatus.PAID.name()).map(BigInteger::longValue).ifPresent(analyticViewModel::setCountOfRegisteredBlitBuyers);
        blitRepository.sumOfTotalAmountFromDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusYears(20).toInstant()),PaymentStatus.PAID.name())
                .map(BigDecimal::longValue).ifPresent(analyticViewModel::setSumOfTotalAmountFromStart);
        blitRepository.sumOfTotalAmountFromDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusMonths(1).toInstant()),PaymentStatus.PAID.name())
                .map(BigDecimal::longValue).ifPresent(analyticViewModel::setSumOfTotalAmountFromDateFromLastMonth);
        analyticViewModel.setStatisticsPerEventType(eventRepository.averageCapacityAndPriceGroupByEventType(OperatorState.APPROVED.name()));
        return analyticViewModel;
    }
}
