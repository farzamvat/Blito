package com.blito.common;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.common.base.AbstractBaseSalonEntity;
import com.blito.common.base.SalonComponent;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;

public class Seat extends AbstractBaseSalonEntity implements SalonComponent<Seat> {
    @JsonIgnore
    private SalonComponent<Row> rowSalonComponent;
    @JsonView(View.SalonSchema.class)
    private String nextUid;
    @JsonView(View.SalonSchema.class)
    private String prevUid;
    @JsonView(View.SalonSchema.class)
    private long blitTypeSeatId;
    @JsonView(View.SalonSchema.class)
    private Timestamp soldDate;
    @JsonView(View.SalonSchema.class)
    private Timestamp reserveDate;
    @JsonView(View.IncludingCustomerNameSalonSchema.class)
    private String customerName;

    public Seat(String uid, String name, SalonComponent<Row> rowSalonComponent) {
        super(uid, name);
        this.rowSalonComponent = rowSalonComponent;
    }

    public Seat() {
    }
    public Seat getNextSeat(Row row) {
        return row.getSeats().stream().filter(seat -> this.getNextUid().equals(seat.getUid())).findAny().orElseGet(() -> null);
    }
    public Seat getPreviousSeat(Row row) {
        return row.getSeats().stream().filter(seat -> this.getPrevUid().equals(seat.getUid())).findAny().orElseGet(() -> null);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getBlitTypeSeatId() {
        return blitTypeSeatId;
    }

    public void setBlitTypeSeatId(long blitTypeSeatId) {
        this.blitTypeSeatId = blitTypeSeatId;
    }

    public Timestamp getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(Timestamp soldDate) {
        this.soldDate = soldDate;
    }

    public Timestamp getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(Timestamp reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getNextUid() {
        return nextUid;
    }

    public void setNextUid(String nextUid) {
        this.nextUid = nextUid;
    }

    public String getPrevUid() {
        return prevUid;
    }

    public void setPrevUid(String prevUid) {
        this.prevUid = prevUid;
    }

    @Override
    public String toJson(ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
