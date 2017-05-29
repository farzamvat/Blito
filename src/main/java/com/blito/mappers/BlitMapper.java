package com.blito.mappers;

import org.springframework.stereotype.Component;

import com.blito.enums.BlitTypeEnum;
import com.blito.models.Blit;
import com.blito.models.CommonBlit;
import com.blito.rest.viewmodels.blit.BlitViewModel;

@Component
public class BlitMapper implements GenericMapper<Blit, BlitViewModel>{

	@Override
	public Blit createFromViewModel(BlitViewModel vmodel) {
		CommonBlit cBlit = new CommonBlit();
		cBlit.setCount(vmodel.getCount());
		cBlit.setType(BlitTypeEnum.COMMON);
		return null;
	}

	@Override
	public BlitViewModel createFromEntity(Blit entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonBlit updateEntity(BlitViewModel viewModel, Blit entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
