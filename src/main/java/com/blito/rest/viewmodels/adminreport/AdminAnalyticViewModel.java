package com.blito.rest.viewmodels.adminreport;

import com.blito.rest.viewmodels.event.EventTypeStatisticsViewModel;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author Farzam Vatanzadeh
 * 11/6/17
 * Mailto : farzam.vat@gmail.com
 **/
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AdminAnalyticViewModel {
    private Long totalNumberOfPaidBlits;
    private Long numberOfPaidBlitsInLastMonth;
    private Long totalNumberOfUsers;
    private Long sumOfTotalAmountFromStart;
    private Long sumOfTotalAmountFromDateFromLastMonth;
    private Long countOfUnregisteredBlitBuyers;
    private Long countOfRegisteredBlitBuyers;
    private Long countOfBlitBuyers;
    private Long numberOfRegisteredWhoBoughtMoreThanOne;
    private Long numberOfUnregisteredWhoBoughtMoreThanOne;
    private List<EventTypeStatisticsViewModel> statisticsPerEventType;

    @Override
    public String toString() {
        return "AdminAnalyticViewModel{" +
                "totalNumberOfPaidBlits=" + totalNumberOfPaidBlits +
                ", numberOfPaidBlitsInLastMonth=" + numberOfPaidBlitsInLastMonth +
                ", totalNumberOfUsers=" + totalNumberOfUsers +
                ", sumOfTotalAmountFromStart=" + sumOfTotalAmountFromStart +
                ", sumOfTotalAmountFromDateFromLastMonth=" + sumOfTotalAmountFromDateFromLastMonth +
                ", countOfUnregisteredBlitBuyers=" + countOfUnregisteredBlitBuyers +
                ", countOfRegisteredBlitBuyers=" + countOfRegisteredBlitBuyers +
                ", countOfBlitBuyers=" + countOfBlitBuyers +
                ", numberOfRegisteredWhoBoughtMoreThanOne=" + numberOfRegisteredWhoBoughtMoreThanOne +
                ", numberOfUnregisteredWhoBoughtMoreThanOne=" + numberOfUnregisteredWhoBoughtMoreThanOne +
                ", statisticsPerEventType=" + statisticsPerEventType +
                '}';
    }

    public Long getTotalNumberOfPaidBlits() {
        return totalNumberOfPaidBlits;
    }

    public void setTotalNumberOfPaidBlits(Long totalNumberOfPaidBlits) {
        this.totalNumberOfPaidBlits = totalNumberOfPaidBlits;
    }

    public Long getNumberOfPaidBlitsInLastMonth() {
        return numberOfPaidBlitsInLastMonth;
    }

    public void setNumberOfPaidBlitsInLastMonth(Long numberOfPaidBlitsInLastMonth) {
        this.numberOfPaidBlitsInLastMonth = numberOfPaidBlitsInLastMonth;
    }

    public Long getTotalNumberOfUsers() {
        return totalNumberOfUsers;
    }

    public void setTotalNumberOfUsers(Long totalNumberOfUsers) {
        this.totalNumberOfUsers = totalNumberOfUsers;
    }

    public Long getSumOfTotalAmountFromStart() {
        return sumOfTotalAmountFromStart;
    }

    public void setSumOfTotalAmountFromStart(Long sumOfTotalAmountFromStart) {
        this.sumOfTotalAmountFromStart = sumOfTotalAmountFromStart;
    }

    public Long getSumOfTotalAmountFromDateFromLastMonth() {
        return sumOfTotalAmountFromDateFromLastMonth;
    }

    public void setSumOfTotalAmountFromDateFromLastMonth(Long sumOfTotalAmountFromDateFromLastMonth) {
        this.sumOfTotalAmountFromDateFromLastMonth = sumOfTotalAmountFromDateFromLastMonth;
    }

    public Long getCountOfUnregisteredBlitBuyers() {
        return countOfUnregisteredBlitBuyers;
    }

    public void setCountOfUnregisteredBlitBuyers(Long countOfUnregisteredBlitBuyers) {
        this.countOfUnregisteredBlitBuyers = countOfUnregisteredBlitBuyers;
    }

    public Long getCountOfRegisteredBlitBuyers() {
        return countOfRegisteredBlitBuyers;
    }

    public void setCountOfRegisteredBlitBuyers(Long countOfRegisteredBlitBuyers) {
        this.countOfRegisteredBlitBuyers = countOfRegisteredBlitBuyers;
    }

    public Long getCountOfBlitBuyers() {
        return countOfBlitBuyers;
    }

    public void setCountOfBlitBuyers(Long countOfBlitBuyers) {
        this.countOfBlitBuyers = countOfBlitBuyers;
    }

    public Long getNumberOfRegisteredWhoBoughtMoreThanOne() {
        return numberOfRegisteredWhoBoughtMoreThanOne;
    }

    public void setNumberOfRegisteredWhoBoughtMoreThanOne(Long numberOfRegisteredWhoBoughtMoreThanOne) {
        this.numberOfRegisteredWhoBoughtMoreThanOne = numberOfRegisteredWhoBoughtMoreThanOne;
    }

    public Long getNumberOfUnregisteredWhoBoughtMoreThanOne() {
        return numberOfUnregisteredWhoBoughtMoreThanOne;
    }

    public void setNumberOfUnregisteredWhoBoughtMoreThanOne(Long numberOfUnregisteredWhoBoughtMoreThanOne) {
        this.numberOfUnregisteredWhoBoughtMoreThanOne = numberOfUnregisteredWhoBoughtMoreThanOne;
    }

    public List<EventTypeStatisticsViewModel> getStatisticsPerEventType() {
        return statisticsPerEventType;
    }

    public void setStatisticsPerEventType(List<EventTypeStatisticsViewModel> statisticsPerEventType) {
        this.statisticsPerEventType = statisticsPerEventType;
    }
}
