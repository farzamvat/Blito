package com.blito.mappers;

import com.blito.models.SeatBlit;
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
    public SeatBlitViewModel createFromEntity(SeatBlit entity) {
        return null;
    }

    @Override
    public SeatBlit updateEntity(SeatBlitViewModel viewModel, SeatBlit entity) {
        return null;
    }
}
