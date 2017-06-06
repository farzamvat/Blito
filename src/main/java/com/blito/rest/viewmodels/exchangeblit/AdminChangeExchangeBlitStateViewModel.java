package com.blito.rest.viewmodels.exchangeblit;

import javax.validation.constraints.NotNull;

import com.blito.enums.OperatorState;

public class AdminChangeExchangeBlitStateViewModel {
	@NotNull
	long exchangeBlitId;
	@NotNull
	OperatorState operatorState;
	public long getExchangeBlitId() {
		return exchangeBlitId;
	}
	public void setExchangeBlitId(long exchangeBlitId) {
		this.exchangeBlitId = exchangeBlitId;
	}
	public OperatorState getOperatorState() {
		return operatorState;
	}
	public void setOperatorState(OperatorState operatorState) {
		this.operatorState = operatorState;
	}
}
