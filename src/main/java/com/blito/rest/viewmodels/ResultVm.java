package com.blito.rest.viewmodels;

public class ResultVm {
	String message;
	
	public ResultVm(String message)
	{
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
