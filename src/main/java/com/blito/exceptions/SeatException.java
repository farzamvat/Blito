package com.blito.exceptions;

import com.blito.rest.viewmodels.blit.SeatErrorViewModel;

import java.util.Set;

/**
 * @author Farzam Vatanzadeh
 * 10/15/17
 * Mailto : farzam.vat@gmail.com
 **/

public class SeatException extends RuntimeException {
    Set<SeatErrorViewModel> seatErrors;
    public SeatException(String message) {
        super(message);
    }

    public SeatException(String message, Set<SeatErrorViewModel> seatErrors) {
        super(message);
        this.seatErrors = seatErrors;
    }

    public Set<SeatErrorViewModel> getSeatErrors() {
        return seatErrors;
    }

    public void setSeatErrors(Set<SeatErrorViewModel> seatErrors) {
        this.seatErrors = seatErrors;
    }
}
