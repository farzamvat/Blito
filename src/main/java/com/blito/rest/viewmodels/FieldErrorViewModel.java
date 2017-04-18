package com.blito.rest.viewmodels;

public class FieldErrorViewModel {
	String defaultMessage;
	String field;
	Object rejectedValue;
	boolean bindingFailure;
	
	public boolean isBindingFailure() {
		return bindingFailure;
	}
	public void setBindingFailure(boolean bindingFailure) {
		this.bindingFailure = bindingFailure;
	}
	public String getDefaultMessage() {
		return defaultMessage;
	}
	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Object getRejectedValue() {
		return rejectedValue;
	}
	public void setRejectedValue(Object rejectedValue) {
		this.rejectedValue = rejectedValue;
	}
	
	
}
