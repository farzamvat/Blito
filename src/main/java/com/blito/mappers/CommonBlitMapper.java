package com.blito.mappers;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

import com.blito.models.CommonBlit;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;

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
		blit.setSeatType(vmodel.getSeatType());
		blit.setBankGateway(vmodel.getBankGateway());
		return blit;
	}

	@Override
	public CommonBlitViewModel createFromEntity(CommonBlit entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonBlit updateEntity(CommonBlitViewModel viewModel, CommonBlit entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
