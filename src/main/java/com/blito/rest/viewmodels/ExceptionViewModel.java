package com.blito.rest.viewmodels;

import java.util.ArrayList;
import java.util.List;

public class ExceptionViewModel {
	private long timestamp;
    private int status;
    private String error;
    private String exception;
    private String message;
    private String path;
    private List<Object> errors;
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<Object> getErrors() {
		if(this.errors == null)
				errors = new ArrayList<>();
		return errors;
	}
	public void setErrors(List<Object> errors) {
		this.errors = errors;
	}
    
    
}
