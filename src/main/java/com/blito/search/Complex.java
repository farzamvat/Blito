package com.blito.search;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public class Complex<T> extends AbstractSearchViewModel<T> {

    private Operator operator;
    private List<AbstractSearchViewModel<T>> restrictions;

    public Complex(Operator operator, List<AbstractSearchViewModel<T>> restrictions) {
        this.operator = operator;
        this.restrictions = restrictions;
    }

    @Override
    public Specification<T> action() {
        return (root,query,cb) ->
                restrictions.stream().map(r -> r.action()).reduce((s1,s2) ->
                        SearchServiceUtil.combineSpecifications(s1,s2, Optional.ofNullable(this.operator))).get().toPredicate(root,query,cb);
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        if(operator == null || operator.name().isEmpty())
            this.operator = Operator.and;
        this.operator = operator;
    }

    public List<AbstractSearchViewModel<T>> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<AbstractSearchViewModel<T>> restrictions) {
        this.restrictions = restrictions;
    }
}
