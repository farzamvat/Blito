package com.blito.rest.viewmodels.blit;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.annotations.Email;
import com.blito.annotations.MobileNumber;
import com.blito.enums.BankGateway;
import com.blito.enums.PaymentStatus;
import com.blito.enums.Response;
import com.blito.enums.SeatType;
import com.blito.models.Blit;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.AbstractViewModel;
import com.blito.rest.viewmodels.LocationViewModel;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.event.AdditionalField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AbstractBlitViewModel extends AbstractViewModel {

    @JsonView(View.SimpleBlit.class)
    private Long blitId;

    private Timestamp createdAt;

    @JsonView(View.SimpleBlit.class)
    private Integer count;

    @JsonView(View.SimpleBlit.class)
    private Long totalAmount;
    @JsonView(View.SimpleBlit.class)
    private String trackCode;
    @JsonView(View.SimpleBlit.class)
    @NotEmpty
    private String eventName;
    @JsonView(View.SimpleBlit.class)
    @NotEmpty
    private String eventDateAndTime;
    @JsonIgnore
    @JsonView(View.SimpleBlit.class)
    private Long userId;
    @JsonView(View.Blit.class)
    @NotEmpty
    private String customerName;
    @JsonView(View.Blit.class)
    @NotNull
    private Timestamp eventDate;
    @JsonView(View.Blit.class)
    @MobileNumber
    private String customerMobileNumber;
    @JsonView(View.Blit.class)
    @Email
    private String customerEmail;
    @JsonView(View.Blit.class)
    @NotEmpty
    private String eventAddress;
    @JsonView(View.Blit.class)
    @NotEmpty
    private String blitTypeName;
    @JsonView(View.SimpleBlit.class)
    @NotNull
    private SeatType seatType;
    @JsonView(View.SimpleBlit.class)
    private PaymentStatus paymentStatus;
    @JsonView(View.AdminBlit.class)
    private String paymentError;
    @JsonView(View.AdminBlit.class)
    private String samanBankToken;
    @JsonView(View.AdminBlit.class)
    private String refNum;
    @JsonView(View.Blit.class)
    private BankGateway bankGateway;
    @Valid
    @JsonView(View.Blit.class)
    private List<AdditionalField> additionalFields;
    @JsonView(View.Blit.class)
    private LocationViewModel location;
    @JsonView(View.Blit.class)
    private String eventPhotoId;
    @JsonView(View.SimpleBlit.class)
    private String discountCode;
    @JsonView(View.SimpleBlit.class)
    private Long primaryAmount;

    public static <E extends Blit> ResultVm createResultVmInCaseOfPaymentStatusError(E blit) {
        return new ResultVm(blit.getPaymentError(), false);
    }

    public static <E extends Blit> ResultVm createResultVmInCaseOfPaymentStatusPending(E blit) {
        return new ResultVm((blit.getPaymentError() == null || blit.getPaymentError().isEmpty())
                ? ResourceUtil.getMessage(Response.PAYMENT_ERROR)
                : blit.getPaymentError(), false);
    }


    public AbstractBlitViewModel() {
        additionalFields = new ArrayList<>();
    }

    public Long getBlitId() {
        return blitId;
    }

    public void setBlitId(Long blitId) {
        this.blitId = blitId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTrackCode() {
        return trackCode;
    }

    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDateAndTime() {
        return eventDateAndTime;
    }

    public void setEventDateAndTime(String eventDateAndTime) {
        this.eventDateAndTime = eventDateAndTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Timestamp getEventDate() {
        return eventDate;
    }

    public void setEventDate(Timestamp eventDate) {
        this.eventDate = eventDate;
    }

    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getBlitTypeName() {
        return blitTypeName;
    }

    public void setBlitTypeName(String blitTypeName) {
        this.blitTypeName = blitTypeName;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentError() {
        return paymentError;
    }

    public void setPaymentError(String paymentError) {
        this.paymentError = paymentError;
    }

    public String getSamanBankToken() {
        return samanBankToken;
    }

    public void setSamanBankToken(String samanBankToken) {
        this.samanBankToken = samanBankToken;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public BankGateway getBankGateway() {
        return bankGateway;
    }

    public void setBankGateway(BankGateway bankGateway) {
        this.bankGateway = bankGateway;
    }

    public List<AdditionalField> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(List<AdditionalField> additionalFields) {
        this.additionalFields = additionalFields;
    }

    public LocationViewModel getLocation() {
        return location;
    }

    public void setLocation(LocationViewModel location) {
        this.location = location;
    }

    public String getEventPhotoId() {
        return eventPhotoId;
    }

    public void setEventPhotoId(String eventPhotoId) {
        this.eventPhotoId = eventPhotoId;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public Long getPrimaryAmount() {
        return primaryAmount;
    }

    public void setPrimaryAmount(Long primaryAmount) {
        this.primaryAmount = primaryAmount;
    }


    @Override
    public String toString() {
        return "AbstractBlitViewModel{" +
                " blitId=" + blitId +
                ", createdAt=" + createdAt +
                ", count=" + count +
                ", totalAmount=" + totalAmount +
                ", trackCode='" + trackCode + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDateAndTime='" + eventDateAndTime + '\'' +
                ", userId=" + userId +
                ", customerName='" + customerName + '\'' +
                ", eventDate=" + eventDate +
                ", customerMobileNumber='" + customerMobileNumber + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", eventAddress='" + eventAddress + '\'' +
                ", blitTypeName='" + blitTypeName + '\'' +
                ", seatType=" + seatType +
                ", paymentStatus=" + paymentStatus +
                ", paymentError='" + paymentError + '\'' +
                ", samanBankToken='" + samanBankToken + '\'' +
                ", refNum='" + refNum + '\'' +
                ", bankGateway=" + bankGateway +
                ", additionalFields=" + additionalFields +
                ", location=" + location +
                ", eventPhotoId='" + eventPhotoId + '\'' +
                ", discountCode='" + discountCode + '\'' +
                ", primaryAmount=" + primaryAmount +
                '}';
    }
}
