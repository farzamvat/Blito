package com.blito.rest.viewmodels.blit;

import java.io.Serializable;

/**
 * @author Farzam Vatanzadeh
 * 10/15/17
 * Mailto : farzam.vat@gmail.com
 **/

public class SeatErrorViewModel implements Serializable {
    private String seatUid;
    private String message;

    public SeatErrorViewModel() {
    }

    public SeatErrorViewModel(String seatUid, String message) {
        this.seatUid = seatUid;
        this.message = message;
    }

    public String getSeatUid() {
        return seatUid;
    }

    public void setSeatUid(String seatUid) {
        this.seatUid = seatUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
