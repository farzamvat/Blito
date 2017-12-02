package com.blito.mappers;

import com.blito.enums.BankGateway;
import com.blito.enums.ImageType;
import com.blito.enums.PaymentStatus;
import com.blito.enums.SeatType;
import com.blito.models.CommonBlit;
import com.blito.rest.viewmodels.LocationViewModel;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.event.AdditionalField;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@Component
public class CommonBlitMapper implements GenericMapper<CommonBlit, CommonBlitViewModel>{

	@Override
	public CommonBlit createFromViewModel(CommonBlitViewModel vmodel) {
		CommonBlit blit = new CommonBlit();
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
		blit.setSeatType(SeatType.COMMON.name());
		blit.setBankGateway(vmodel.getBankGateway().name());
		blit.setAdditionalFields(vmodel.getAdditionalFields().stream().collect(Collectors.toMap(AdditionalField::getKey,AdditionalField::getValue)));
		return blit;
	}

	@Override
	public CommonBlitViewModel createFromEntity(CommonBlit blit) {
		CommonBlitViewModel vmodel = new CommonBlitViewModel();
		vmodel.setDiscountCode(blit.getDiscountCode());
		vmodel.setPrimaryAmount(blit.getPrimaryAmount());
		vmodel.setBlitId(blit.getBlitId());
		vmodel.setBlitTypeId(blit.getBlitType().getBlitTypeId());
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
		vmodel.setSeatType(SeatType.COMMON);
		vmodel.setRefNum(blit.getRefNum());
		vmodel.setBankGateway(Enum.valueOf(BankGateway.class, blit.getBankGateway()));
		vmodel.setCreatedAt(blit.getCreatedAt());
		vmodel.setUserId(blit.getUser() == null ? 0 : blit.getUser().getUserId());
		vmodel.setAdditionalFields(blit.getAdditionalFields().entrySet().stream().map(AdditionalField::new).collect(Collectors.toList()));
		if(blit.getBlitType().getEventDate().getEvent().getLongitude() != null && blit.getBlitType().getEventDate().getEvent().getLatitude() != null)
			vmodel.setLocation(new LocationViewModel(blit.getBlitType().getEventDate().getEvent().getLatitude(),blit.getBlitType().getEventDate().getEvent().getLongitude()));
		vmodel.setEventPhotoId(blit.getBlitType().getEventDate().getEvent().getImages().stream().filter(i -> i.getImageType().equals(ImageType.EVENT_PHOTO.name())).findFirst().get().getImageUUID());
		vmodel.setResult(new ResultVm("success",true));
		return vmodel;
	}

	@Override
	public CommonBlit updateEntity(CommonBlitViewModel viewModel, CommonBlit entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
