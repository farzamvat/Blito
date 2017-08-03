package com.blito.rest.viewmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AbstractViewModel {
	
	private ResultVm result;

	public ResultVm getResult() {
		return result;
	}

	public void setResult(ResultVm result) {
		this.result = result;
	}
}
