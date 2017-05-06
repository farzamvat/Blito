package com.blito.mappers;

import org.springframework.stereotype.Component;

import com.blito.models.Blit;
import com.blito.rest.viewmodels.blit.BlitViewModel;

@Component
public class BlitMapper implements GenericMapper<Blit, BlitViewModel>{

	@Override
	public Blit createFromViewModel(BlitViewModel viewModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlitViewModel createFromEntity(Blit entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blit updateEntity(BlitViewModel viewModel, Blit entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
