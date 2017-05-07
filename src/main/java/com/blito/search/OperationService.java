package com.blito.search;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;



public class OperationService {

	public static <T> Predicate doOperation(Operation operation, Object value, CriteriaBuilder cb, Root<T> root, String field) {
		switch (operation) {
		case eq:
			return cb.equal(root.get(field), value);
		case ge:
			return cb.ge(root.get(field), (double)value);
		case gt:
			return cb.gt(root.get(field), (double)value);
		case le:
			return cb.le(root.get(field), (double)value);
		case like:
			return cb.like(root.get(field), value.toString());
		case lt:
			return cb.lt(root.get(field), (double)value);
		default:
			return cb.or();
		}
	}
}
