package com.blito.search;

import org.springframework.data.jpa.domain.Specification;

public class Simple<T> extends AbstractSearchViewModel<T>{

	public String value;
	
	public Operation operation;
	
	
	@Override
	public Specification<T> action() {
		return (root,query,cb)->
		OperationService.doOperation(operation,value,cb,root,field);
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
