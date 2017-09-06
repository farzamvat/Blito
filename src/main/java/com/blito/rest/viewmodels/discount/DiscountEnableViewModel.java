package com.blito.rest.viewmodels.discount;
/*
    @author Farzam Vatanzadeh
*/

import javax.validation.constraints.NotNull;

public class DiscountEnableViewModel {
    @NotNull(message = "{discount.id.not.null}")
    private Long discountId;
    @NotNull(message = "{discount.enable.not.null}")
    private Boolean enable;

    public DiscountEnableViewModel() {}
    public DiscountEnableViewModel(Long discountId, Boolean enable) {
        this.discountId = discountId;
        this.enable = enable;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
