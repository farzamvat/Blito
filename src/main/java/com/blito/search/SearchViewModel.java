package com.blito.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchViewModel<T> {
	
	private List<AbstractSearchViewModel<T>> restrictions;

	private Operator operator;

	public List<AbstractSearchViewModel<T>> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(List<AbstractSearchViewModel<T>> restrictions) {
		this.restrictions = restrictions;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		if(operator == null || operator.name().isEmpty())
			this.operator = Operator.and;
		this.operator = operator;
	}
}
