package com.blito.search;

import org.springframework.data.jpa.domain.Specification;

public class Simple<T> extends AbstractSearchViewModel<T>{

	public Object value;
	
	public Operation operation;
	
	
	@Override
	public Specification<T> action() {
		return (root,query,cb)->
		
		OperationService.doOperation(operation,value,cb,root,field);
	}
	

	public Operation getOperation() {
		return operation;
	}


	public void setOperation(Operation operation) {
		this.operation = operation;
	}


	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
