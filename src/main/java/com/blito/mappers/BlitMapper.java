package com.blito.mappers;

import com.blito.enums.BankGateway;
import com.blito.enums.PaymentStatus;
import com.blito.enums.SeatType;
import com.blito.models.Blit;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.blit.SeatBlitViewModel;
import com.blito.rest.viewmodels.event.AdditionalField;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @author Farzam Vatanzadeh
 * 11/30/17
 * Mailto : farzam.vat@gmail.com
 **/
@Component
public class BlitMapper implements GenericMapper<Blit,SeatBlitViewModel> {
    @Override
    public Blit createFromViewModel(SeatBlitViewModel viewModel) {
        return null;
    }

    @Override
    public SeatBlitViewModel createFromEntity(Blit blit) {
        SeatBlitViewModel vmodel = new SeatBlitViewModel();
        vmodel.setDiscountCode(blit.getDiscountCode());
        vmodel.setPrimaryAmount(blit.getPrimaryAmount());
        vmodel.setBlitId(blit.getBlitId());
        vmodel.setCount(blit.getCount());
        vmodel.setTotalAmount(blit.getTotalAmount());
        vmodel.setTrackCode(blit.getTrackCode());
        vmodel.setEventName(blit.getEventName());
        vmodel.setEventDateAndTime(blit.getEventDateAndTime());
        vmodel.setCustomerName(blit.getCustomerName());
        vmodel.setEventDate(blit.getEventDate());
        vmodel.setCustomerMobileNumber(blit.getCustomerMobileNumber());
        vmodel.setCustomerEmail(blit.getCustomerEmail());
        vmodel.setEventAddress(blit.getEventAddress());
        vmodel.setBlitTypeName(blit.getBlitTypeName());
        vmodel.setPaymentStatus(Enum.valueOf(PaymentStatus.class, blit.getPaymentStatus()));
        vmodel.setPaymentError(blit.getPaymentError());
        vmodel.setSamanBankToken(blit.getToken());
        vmodel.setRefNum(blit.getRefNum());
        vmodel.setSeatType(SeatType.SEAT_BLIT);
        vmodel.setBankGateway(Enum.valueOf(BankGateway.class, blit.getBankGateway()));
        vmodel.setCreatedAt(blit.getCreatedAt());
        vmodel.setUserId(blit.getUser() == null ? 0 : blit.getUser().getUserId());
        vmodel.setAdditionalFields(blit.getAdditionalFields().entrySet().stream().map(AdditionalField::new).collect(Collectors.toList()));
        vmodel.setResult(new ResultVm("success",true));
        return vmodel;
    }

    @Override
    public Blit updateEntity(SeatBlitViewModel viewModel, Blit entity) {
        return null;
    }
}
