package com.blito.search;

import java.sql.Timestamp;

import org.springframework.data.jpa.domain.Specification;

public class Time<T> extends AbstractSearchViewModel<T> {

	private Timestamp value;
	public Operation operation;
	@Override
	public Specification<T> action() {
		return (root, query, cb) -> 
			OperationService.doOperation(operation, value, cb, root, field);
	}
	public Timestamp getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = new Timestamp(value);
	}
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
}
