package com.blito.rest.viewmodels.address;

import com.blito.rest.viewmodels.AbstractViewModel;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class AddressViewModel extends AbstractViewModel {
    @JsonView(View.OwnerEventHost.class)
    private Long id;
    @JsonView(View.OwnerEventHost.class)
    @NotEmpty
    private String name;
    @JsonView(View.OwnerEventHost.class)
    @NotNull
    private Double latitude;
    @JsonView(View.OwnerEventHost.class)
    @NotNull
    private Double longitude;
    @JsonView(View.OwnerEventHost.class)
    @NotEmpty
    private String address;

    private String eventHostLink;
    @NotNull
    private Long eventHostId;

    public Long getEventHostId() {
        return eventHostId;
    }

    public void setEventHostId(Long eventHostId) {
        this.eventHostId = eventHostId;
    }

    public String getEventHostLink() {
        return eventHostLink;
    }

    public void setEventHostLink(String eventHostLink) {
        this.eventHostLink = eventHostLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
