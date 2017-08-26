package com.blito.search;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.Optional;

public class SearchServiceUtil {
    public static <E> Specifications<E> combineSpecifications(Specification<E> s1, Specification<E> s2, Optional<Operator> operator) {
        return operator.map(op -> {
            switch (op) {
                case and:
                    return Specifications.where(s1).and(s2);
                case or:
                    return Specifications.where(s1).or(s2);
                default:
                    return Specifications.where(s1).and(s2);
            }
        }).orElseGet(() -> Specifications.where(s1).and(s2));
    }
}
