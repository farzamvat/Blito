package com.blito.rest.viewmodels.blit;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class SeatBlitViewModel extends AbstractBlitViewModel {
    @NotEmpty
    private Set<String> seatUids;
    @JsonView(View.SimpleBlit.class)
    private String seats;
    @NotNull
    private Long eventDateId;

    public SeatBlitViewModel(ResultVm result) {
        super.result = result;
    }

    public SeatBlitViewModel() {
        super();
    }

    public Set<String> getSeatUids() {
        return seatUids;
    }

    public void setSeatUids(Set<String> seatUids) {
        this.seatUids = seatUids;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public Long getEventDateId() {
        return eventDateId;
    }

    public void setEventDateId(Long eventDateId) {
        this.eventDateId = eventDateId;
    }

    @Override
    public String toString() {
        return "SeatBlitViewModel{" + super.toString() +
                "seatUids=" + seatUids +
                ", seats='" + seats + '\'' +
                ", eventDateId=" + eventDateId +
                ", result=" + result +
                '}';
    }
}
