package com.blito.search;


import org.springframework.data.jpa.domain.Specification;


public class Range<T> extends AbstractSearchViewModel<T> {

	public long minValue;

	public long maxValue;

	@Override
	public Specification<T> action() {
		return (root, query, cb) -> {
			return cb.between(root.get(field), minValue, maxValue);
		};
	}

	public long getMinValue() {
		return minValue;
	}

	public void setMinValue(long minValue) {
		this.minValue = minValue;
	}

	public long getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(long maxValue) {
		this.maxValue = maxValue;
	}
}
