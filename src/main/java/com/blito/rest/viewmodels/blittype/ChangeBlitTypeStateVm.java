package com.blito.rest.viewmodels.blittype;

import javax.validation.constraints.NotNull;

import com.blito.enums.State;

public class ChangeBlitTypeStateVm {

	@NotNull
	long blitTypeId;
	
	@NotNull
	State blitTypeState;

	public long getBlitTypeId() {
		return blitTypeId;
	}

	public void setBlitTypeId(long blitTypeId) {
		this.blitTypeId = blitTypeId;
	}

	public State getBlitTypeState() {
		return blitTypeState;
	}

	public void setBlitTypeState(State blitTypeState) {
		this.blitTypeState = blitTypeState;
	}

}
