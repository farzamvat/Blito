package com.blito.payments.payir.viewmodel.request;


/**
 * @author Farzam Vatanzadeh
 * 12/2/17
 * Mailto : farzam.vat@gmail.com
 **/

public class PayDotIrPurchaseRequest extends AbstractPayDotIrRequest {
    private Integer amount;
    private String redirect;
    private String factorNumber;
    private String mobile;

    public PayDotIrPurchaseRequest(Integer amount, String redirect, String factorNumber, String mobile,String api) {
        this.amount = amount;
        this.redirect = redirect;
        this.factorNumber = factorNumber;
        this.mobile = mobile;
        this.setApi(api);
    }

    public PayDotIrPurchaseRequest() {
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getFactorNumber() {
        return factorNumber;
    }

    public void setFactorNumber(String factorNumber) {
        this.factorNumber = factorNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "PayDotIrPurchaseRequest{" +
                "amount=" + amount +
                ", redirect='" + redirect + '\'' +
                ", factorNumber='" + factorNumber + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
