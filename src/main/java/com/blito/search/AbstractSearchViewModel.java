package com.blito.search;


import org.springframework.data.jpa.domain.Specification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
		@Type(value = Simple.class, name = "simple"),
		@Type(value = Collection.class, name = "collection"),
		@Type(value = Range.class, name = "range"),
		@Type(value = Time.class , name = "time")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractSearchViewModel<T> {

	protected String field;

	public abstract Specification<T> action();

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}
