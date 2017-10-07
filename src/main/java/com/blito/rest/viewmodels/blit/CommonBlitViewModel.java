package com.blito.rest.viewmodels.blit;

import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.NotNull;

public class CommonBlitViewModel extends AbstractBlitViewModel {
	@NotNull
	@JsonView(View.SimpleBlit.class)
	private Long blitTypeId;

	public Long getBlitTypeId() {
		return blitTypeId;
	}

	public void setBlitTypeId(Long blitTypeId) {
		this.blitTypeId = blitTypeId;
	}
}
