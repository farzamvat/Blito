package com.blito.rest.viewmodels.blit;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

public class SeatBlitViewModel extends AbstractBlitViewModel {
    @NotEmpty
    private Set<Long> blitTypeSeatIds;
    @JsonView(View.SimpleBlit.class)
    private String seats;

    SeatBlitViewModel(ResultVm result) {
        super.result = result;
    }

    public SeatBlitViewModel() {
    }

    public Set<Long> getBlitTypeSeatIds() {
        return blitTypeSeatIds;
    }

    public void setBlitTypeSeatIds(Set<Long> blitTypeSeatIds) {
        this.blitTypeSeatIds = blitTypeSeatIds;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }
}
