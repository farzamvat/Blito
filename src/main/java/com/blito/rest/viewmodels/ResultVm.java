package com.blito.rest.viewmodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResultVm {
	private boolean status;
	private String message;
	
	public ResultVm(String message,boolean status)
	{
		this.message = message;
		this.status = status;
	}
	
	public ResultVm(String message)
	{
		this.message = message;
		this.status = true;
	}
	

	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
