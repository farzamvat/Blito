package com.blito.rest.viewmodels.discount;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Farzam Vatanzadeh
 * 11/14/17
 * Mailto : farzam.vat@gmail.com
 **/

public class SeatBlitDiscountValidationViewModel {
    private Boolean isValid;
    @NotEmpty
    private Set<String> seatUids;
    @NotNull
    private Long eventDateId;
    @NotBlank
    private String code;
    private Long totalAmount;

    public SeatBlitDiscountValidationViewModel() {
    }

    public SeatBlitDiscountValidationViewModel(Set<String> seatUids, Long eventDateId, String code) {
        this.seatUids = seatUids;
        this.eventDateId = eventDateId;
        this.code = code;
    }

    @JsonProperty("isValid")
    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public Set<String> getSeatUids() {
        return seatUids;
    }

    public void setSeatUids(Set<String> seatUids) {
        this.seatUids = seatUids;
    }

    public Long getEventDateId() {
        return eventDateId;
    }

    public void setEventDateId(Long eventDateId) {
        this.eventDateId = eventDateId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }
}
