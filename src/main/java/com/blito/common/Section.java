package com.blito.common;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.salon.common.base.AbstractBaseSalonEntity;
import com.blito.salon.common.base.SalonComponent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class Section extends AbstractBaseSalonEntity implements SalonComponent<Section> {
    @JsonIgnore
    private SalonComponent<Salon> salonComponent;
    private Integer numberOfRows;
    private List<Row> rows;


    public Section(String uid, String name, Integer numberOfRows,SalonComponent<Salon> salonComponent) {
        super(uid, name);
        this.numberOfRows = numberOfRows;
        this.salonComponent = salonComponent;
        this.rows = new ArrayList<>();
    }

    public Section(Integer numberOfRows, List<Row> rows) {
        this.numberOfRows = numberOfRows;
        this.rows = rows;
    }

    public Integer getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(Integer numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public Section(String uid, String name) {
        super(uid, name);
    }

    public Section() {
    }

    @Override
    public String toJson(ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
