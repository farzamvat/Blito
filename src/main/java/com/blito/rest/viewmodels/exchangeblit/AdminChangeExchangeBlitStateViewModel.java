package com.blito.rest.viewmodels.exchangeblit;

import javax.validation.constraints.NotNull;

import com.blito.enums.State;

public class AdminChangeExchangeBlitStateViewModel {
	@NotNull
	private State state;
	@NotNull
	private long exchangeBlitId;
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public long getExchangeBlitId() {
		return exchangeBlitId;
	}
	public void setExchangeBlitId(long exchangeBlitId) {
		this.exchangeBlitId = exchangeBlitId;
	}
}
