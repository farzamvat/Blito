package com.blito.search;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public class Collection<T> extends AbstractSearchViewModel<T>{
	
	public List<Object> values;

	@Override
	public Specification<T> action() {
		return (root,query,cb) -> 
			values.stream().map(v -> cb.isMember(v, root.get(field)))
			.reduce((p1,p2) -> cb.and(p1, p2))
			.get();
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}
}
