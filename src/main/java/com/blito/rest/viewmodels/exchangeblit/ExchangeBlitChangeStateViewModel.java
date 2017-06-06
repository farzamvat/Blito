package com.blito.rest.viewmodels.exchangeblit;

import javax.validation.constraints.NotNull;

import com.blito.enums.State;

public class ExchangeBlitChangeStateViewModel {
	@NotNull
	long exchangeBlitId;
	@NotNull
	State state;
	public long getExchangeBlitId() {
		return exchangeBlitId;
	}
	public void setExchangeBlitId(long exchangeBlitId) {
		this.exchangeBlitId = exchangeBlitId;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
}
