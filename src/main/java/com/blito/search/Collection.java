package com.blito.search;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public class Collection<T> extends AbstractSearchViewModel<T>{
	
	public List<Object> value;

	@Override
	public Specification<T> action() {
		return (root,query,cb)-> {
			return value.stream().map(v->
				cb.equal(root.get(field), v)
			).reduce( (p1,p2)->
				 cb.and(p1,p2)
			).map(m -> m)
				.orElseGet(()-> cb.or());
		};
	}

	public List<Object> getValue() {
		return value;
	}

	public void setValue(List<Object> value) {
		this.value = value;
	}
}
