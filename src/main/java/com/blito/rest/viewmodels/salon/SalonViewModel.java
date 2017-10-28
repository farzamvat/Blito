package com.blito.rest.viewmodels.salon;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.common.Salon;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SalonViewModel implements Serializable {
    public SalonViewModel() {
        sections = new HashSet<>();
    }

    @JsonView(View.SimpleSalon.class)
    @NotBlank
    private String salonUid;
    @JsonView(View.SimpleSalon.class)
    private String name;
    @JsonView(View.SimpleSalon.class)
    private Double latitude;
    @JsonView(View.SimpleSalon.class)
    private Double longitude;
    @JsonView(View.SimpleSalon.class)
    private String address;
    @JsonView(View.SalonSchema.class)
    private Salon schema;
    @JsonView(View.SimpleSalon.class)
    @NotBlank
    private String salonSvg;
    @JsonView(View.SalonSchema.class)
    private Set<SectionViewModel> sections;

    public Set<SectionViewModel> getSections() {
        return sections;
    }

    public void setSections(Set<SectionViewModel> sections) {
        this.sections = sections;
    }

    public String getSalonSvg() {
        return salonSvg;
    }

    public void setSalonSvg(String salonSvg) {
        this.salonSvg = salonSvg;
    }

    public String getSalonUid() {
        return salonUid;
    }

    public void setSalonUid(String salonUid) {
        this.salonUid = salonUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Salon getSchema() {
        return schema;
    }

    public void setSchema(Salon schema) {
        this.schema = schema;
    }
}
