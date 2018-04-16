package com.blito.models;

import javax.persistence.*;

@Entity(name = "address")
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    @Column(columnDefinition="TEXT")
    private String address;
    @ManyToOne(optional = false)
    @JoinColumn(name = "eventHostId")
    private EventHost eventHost;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventHost getEventHost() {
        return eventHost;
    }

    public void setEventHost(EventHost eventHost) {
        this.eventHost = eventHost;
        eventHost.getAddresses().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
