package com.blito.search;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;



public class OperationService {

	public static <T> Predicate doOperation(Operation operation, String value, CriteriaBuilder cb, Root<T> root, String field) {
		switch (operation) {
		case eq:
			return cb.equal(root.get(field), value);
		case ge:
			return cb.ge(root.get(field), Integer.valueOf(value));
		case gt:
			return cb.gt(root.get(field), Integer.valueOf(value));
		case le:
			return cb.le(root.get(field), Integer.valueOf(value));
		case like:
			return cb.like(root.get(field), value);
		case lt:
			return cb.lt(root.get(field), Integer.valueOf(value));
		default:
			return cb.or();
		}
	}
}
