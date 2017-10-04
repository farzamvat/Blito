package com.blito.common;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.salon.common.base.AbstractBaseSalonEntity;
import com.blito.salon.common.base.SalonComponent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Seat extends AbstractBaseSalonEntity implements SalonComponent<Seat> {
    @JsonIgnore
    private SalonComponent<Row> rowSalonComponent;
    private String nextUid;
    private String prevUid;

    public Seat(String uid, String name, SalonComponent<Row> rowSalonComponent) {
        super(uid, name);
        this.rowSalonComponent = rowSalonComponent;
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
