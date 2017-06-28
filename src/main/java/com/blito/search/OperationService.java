package com.blito.search;

import java.sql.Timestamp;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.blito.exceptions.NotAllowedException;



public class OperationService {

	public static <T> Predicate doOperation(Operation operation, Object value, CriteriaBuilder cb, Root<T> root, String field) {
		switch (operation) {
		case eq:
			return cb.equal(root.get(field), value);
		case ge:
			return cb.ge(root.get(field), (long)value);
		case gt:
			return cb.gt(root.get(field), (long)value);
		case le:
			return cb.le(root.get(field), (long)value);
		case like:
			return cb.like(root.get(field), '%' + value.toString() + '%');
		case lt:
			return cb.lt(root.get(field), (long)value);
		default:
			return cb.or();
		}
	}
	
	public static <T> Predicate doOperation(Operation operation, Timestamp value, CriteriaBuilder cb, Root<T> root, String field) {
		switch (operation) {
		case ge:
			return cb.greaterThanOrEqualTo(root.get(field), value);
		case gt:
			return cb.greaterThan(root.get(field), value);
		case le:
			return cb.lessThanOrEqualTo(root.get(field), value);
		case lt:
			return cb.lessThan(root.get(field), value);
		default:
			throw new NotAllowedException("Operation not allowed");
		}
	}
	

}
