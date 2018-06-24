package com.blito.payments.jibit;

public class JibitPaymentRequest {
    private Integer userId = 0;
    private Long amount;
    private String callBackUrl;
    private Integer merchantId;
    private String mobile;

    public JibitPaymentRequest(Long amount, String callBackUrl, Integer merchantId, String mobile) {
        this.amount = amount;
        this.callBackUrl = callBackUrl;
        this.merchantId = merchantId;
        this.mobile = mobile;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
