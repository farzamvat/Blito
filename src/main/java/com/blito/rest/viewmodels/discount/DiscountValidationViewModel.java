package com.blito.rest.viewmodels.discount;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class DiscountValidationViewModel {

    private boolean isValid;
    private double totalAmount;
    @NotNull
    private String code;
    @NotNull
    private int count;
    @NotNull
    private long blitTypeId;

    @JsonProperty("isValid")
    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getBlitTypeId() {
        return blitTypeId;
    }

    public void setBlitTypeId(long blitTypeId) {
        this.blitTypeId = blitTypeId;
    }
}
