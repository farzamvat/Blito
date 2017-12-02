package com.blito.rest.viewmodels.blit;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class ReservedBlitViewModel {

    @NotBlank
    private String seatUid;
    @NotNull
    private Long eventDateId;
    @NotBlank
    private String eventDateAndTime;

    public String getSeatUid() {
        return seatUid;
    }

    public void setSeatUid(String seatUid) {
        this.seatUid = seatUid;
    }

    public Long getEventDateId() {
        return eventDateId;
    }

    public void setEventDateId(Long eventDateId) {
        this.eventDateId = eventDateId;
    }

    public String getEventDateAndTime() {
        return eventDateAndTime;
    }

    public void setEventDateAndTime(String eventDateAndTime) {
        this.eventDateAndTime = eventDateAndTime;
    }
}
