package com.blito.rest.viewmodels.discount;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

public class DiscountValidationViewModel {

    private boolean isValid;
    private Long totalAmount;
    @NotEmpty
    private String code;
    @Min(1)
    private int count;
    @Min(1)
    private long blitTypeId;

    @JsonProperty("isValid")
    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
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
