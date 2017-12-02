package com.blito.common;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.common.base.AbstractBaseSalonEntity;
import com.blito.common.base.SalonComponent;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class Salon extends AbstractBaseSalonEntity implements SalonComponent<Salon> {
    @JsonView(View.SalonSchema.class)
    private String address;
    @JsonView(View.SalonSchema.class)
    private Double longitude;
    @JsonView(View.SalonSchema.class)
    private Double latitude;
    @JsonView(View.SalonSchema.class)
    private Integer numberOfSections;
    @JsonView(View.SalonSchema.class)
    private List<Section> sections;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public Salon(String uid, String name,String address,Double longitude,Double latitude) {
        super(uid, name);
        sections = new ArrayList<>();
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public Salon() {
    }

    public Integer getNumberOfSections() {
        return numberOfSections;
    }

    public void setNumberOfSections(Integer numberOfSections) {
        this.numberOfSections = numberOfSections;
    }

    @Override
    public String toJson(ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }
}
