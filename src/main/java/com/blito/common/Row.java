package com.blito.common;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.common.base.AbstractBaseSalonEntity;
import com.blito.common.base.SalonComponent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class Row extends AbstractBaseSalonEntity implements SalonComponent<Row> {
    @JsonIgnore
    private SalonComponent<Section> sectionSalonComponent;
    private Direction direction;
    private Integer numberOfSeats;
    private Integer firstSeatStartingNumber;
    private Integer lastSeatEndingNumber;
    private List<Seat> seats;

    public Row(String uid, String name, SalonComponent<Section> sectionSalonComponent, Integer numberOfSeats, Integer firstSeatStartingNumber, Integer lastSeatEndingNumber,Direction direction) {
        super(uid, name);
        this.sectionSalonComponent = sectionSalonComponent;
        this.numberOfSeats = numberOfSeats;
        this.firstSeatStartingNumber = firstSeatStartingNumber;
        this.lastSeatEndingNumber = lastSeatEndingNumber;
        this.direction = direction;
        this.seats = new ArrayList<>();
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public SalonComponent<Section> getSectionSalonComponent() {
        return sectionSalonComponent;
    }

    public void setSectionSalonComponent(SalonComponent<Section> sectionSalonComponent) {
        this.sectionSalonComponent = sectionSalonComponent;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Integer getFirstSeatStartingNumber() {
        return firstSeatStartingNumber;
    }

    public void setFirstSeatStartingNumber(Integer firstSeatStartingNumber) {
        this.firstSeatStartingNumber = firstSeatStartingNumber;
    }

    public Integer getLastSeatEndingNumber() {
        return lastSeatEndingNumber;
    }

    public void setLastSeatEndingNumber(Integer lastSeatEndingNumber) {
        this.lastSeatEndingNumber = lastSeatEndingNumber;
    }

    public Row(String uid, String name) {
        super(uid, name);
    }

    public Row() {
    }

    @Override
    public String toJson(ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
