package com.blito.search;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.blito.exceptions.NotAllowedException;

public class OperationService {

	public static <T> Predicate doOperation(Operation operation, Object value, CriteriaBuilder cb, Root<T> root,
			String field) {

		switch (operation) {
		case eq:
			return cb.equal(joinQueryBuilder(field, root), value);
		case ge:
			return cb.ge(root.get(field), (long) value);
		case gt:
			return cb.gt(root.get(field), (long) value);
		case le:
			return cb.le(root.get(field), (long) value);
		case like:
			return cb.like(root.get(field), '%' + value.toString() + '%');
		case lt:
			return cb.lt(root.get(field), (long) value);
		case neq:
			return cb.notEqual(root.get(field), value);
		default:
			return cb.or();
		}
	}

	public static <T> Predicate doOperation(Operation operation, Timestamp value, CriteriaBuilder cb, Root<T> root,
			String field) {
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

	private static <T, U> Path<U> joinQueryBuilder(String field, Root<T> root) {
		List<String> splitted = Arrays.stream(field.split("-")).collect(Collectors.toList());
		if (splitted.size() == 1)
			return root.get(field);
		String query_field = splitted.remove(splitted.size() - 1);
		return recursiveJoin(root, splitted.stream().reduce((s1, s2) -> String.valueOf(new StringBuilder().append(s1).append("-").append(s2))).get()).get(query_field);

	}
	
	private static <Z,X,T> Join<Z,X> recursiveJoin(Root<T> root,String query)
	{
		List<String> splitted = Arrays.stream(query.split("-")).collect(Collectors.toList());
		if (splitted.size() == 1)
			return root.join(query);
		String query_field = splitted.remove(splitted.size() - 1);
		return recursiveJoin(root, splitted.stream().reduce((s1, s2) -> String.valueOf(new StringBuilder().append(s1).append("-").append(s2))).get()).join(query_field);
	
	}

}
