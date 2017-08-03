package com.blito.search;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchViewModel<T> {
	
	List<AbstractSearchViewModel<T>> restrictions;

	public List<AbstractSearchViewModel<T>> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(List<AbstractSearchViewModel<T>> restrictions) {
		this.restrictions = restrictions;
	}
	
	
}
