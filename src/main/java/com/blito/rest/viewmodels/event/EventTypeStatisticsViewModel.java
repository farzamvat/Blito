package com.blito.rest.viewmodels.event;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Farzam Vatanzadeh
 * 11/6/17
 * Mailto : farzam.vat@gmail.com
 **/
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EventTypeStatisticsViewModel {
    private String eventType;
    private Double avgCapacity;
    private Double avgPrice;
    private Double salePercentage;
    private Long count;

    public EventTypeStatisticsViewModel(String eventType, Double avgCapacity, Double avgPrice, Long sumSoldCount, Long sumCapacity,Long count) {
        this.eventType = eventType;
        this.avgCapacity = avgCapacity;
        this.avgPrice = avgPrice;
        this.salePercentage = sumSoldCount.doubleValue() * 100 / sumCapacity.doubleValue();
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public EventTypeStatisticsViewModel() {
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Double getAvgCapacity() {
        return avgCapacity;
    }

    public void setAvgCapacity(Double avgCapacity) {
        this.avgCapacity = avgCapacity;
    }

    public Double getAvgPrice() {
        return avgPrice;
    }

    @Override
    public String toString() {
        return "EventTypeStatisticsViewModel{" +
                "eventType='" + eventType + '\'' +
                ", avgCapacity=" + avgCapacity +
                ", avgPrice=" + avgPrice +
                ", salePercentage=" + salePercentage +
                ", count=" + count +
                '}';
    }

    public void setAvgPrice(Double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Double getSalePercentage() {
        return salePercentage;
    }

    public void setSalePercentage(Double salePercentage) {
        this.salePercentage = salePercentage;
    }
}
