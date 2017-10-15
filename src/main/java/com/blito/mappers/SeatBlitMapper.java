package com.blito.mappers;

import com.blito.enums.BankGateway;
import com.blito.enums.ImageType;
import com.blito.enums.PaymentStatus;
import com.blito.models.BlitType;
import com.blito.models.SeatBlit;
import com.blito.rest.viewmodels.LocationViewModel;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.blit.SeatBlitViewModel;
import com.blito.rest.viewmodels.event.AdditionalField;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

/**
 * @author Farzam Vatanzadeh
 * 10/15/17
 * Mailto : farzam.vat@gmail.com
 **/
@Component
public class SeatBlitMapper implements GenericMapper<SeatBlit,SeatBlitViewModel> {
    @Override
    public SeatBlit createFromViewModel(SeatBlitViewModel vmodel) {
        SeatBlit blit = new SeatBlit();
        blit.setCount(vmodel.getCount());
        blit.setTotalAmount(vmodel.getTotalAmount());
        blit.setEventName(vmodel.getEventName());
        blit.setEventDateAndTime(vmodel.getEventDateAndTime());
        blit.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
        blit.setEventDate(vmodel.getEventDate());

        blit.setCustomerName(vmodel.getCustomerName());
        blit.setCustomerMobileNumber(vmodel.getCustomerMobileNumber());
        blit.setCustomerEmail(vmodel.getCustomerEmail());
        blit.setEventAddress(vmodel.getEventAddress());
        blit.setBlitTypeName(vmodel.getBlitTypeName());
        blit.setPrimaryAmount(vmodel.getPrimaryAmount());
        blit.setDiscountCode(vmodel.getDiscountCode());
        blit.setSeatType(vmodel.getSeatType().name());
        blit.setBankGateway(vmodel.getBankGateway().name());
        blit.setAdditionalFields(vmodel.getAdditionalFields().stream().collect(Collectors.toMap(AdditionalField::getKey,AdditionalField::getValue)));
        blit.setSeats(vmodel.getSeats());
        return blit;
    }

    @Override
    public SeatBlitViewModel createFromEntity(SeatBlit blit) {
        SeatBlitViewModel vmodel = new SeatBlitViewModel();
        vmodel.setDiscountCode(blit.getDiscountCode());
        vmodel.setPrimaryAmount(blit.getPrimaryAmount());
        vmodel.setBlitId(blit.getBlitId());
        BlitType blitType = blit.getBlitTypeSeats().stream().findAny().get().getBlitType();
        vmodel.setBlitTypeId(blitType.getBlitTypeId());
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
        vmodel.setBankGateway(Enum.valueOf(BankGateway.class, blit.getBankGateway()));
        vmodel.setCreatedAt(blit.getCreatedAt());
        vmodel.setUserId(blit.getUser() == null ? 0 : blit.getUser().getUserId());
        vmodel.setAdditionalFields(blit.getAdditionalFields().entrySet().stream().map(AdditionalField::new).collect(Collectors.toList()));
        if(blitType.getEventDate().getEvent().getLongitude() != null && blitType.getEventDate().getEvent().getLatitude() != null)
            vmodel.setLocation(new LocationViewModel(blitType.getEventDate().getEvent().getLatitude(),blitType.getEventDate().getEvent().getLongitude()));
        vmodel.setEventPhotoId(blitType.getEventDate().getEvent().getImages().stream().filter(i -> i.getImageType().equals(ImageType.EVENT_PHOTO.name())).findFirst().get().getImageUUID());
        vmodel.setResult(new ResultVm("success",true));
        vmodel.setSeats(blit.getSeats());
        return vmodel;
    }

    @Override
    public SeatBlit updateEntity(SeatBlitViewModel viewModel, SeatBlit entity) {
        return null;
    }
}
