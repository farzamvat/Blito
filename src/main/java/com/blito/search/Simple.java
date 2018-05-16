package com.blito.search;

import org.springframework.data.jpa.domain.Specification;

public class Simple<T> extends AbstractSearchViewModel<T> {

    public Operation operation;

    private Object value;

    public Simple() {

    }

    public Simple(Operation operation, String field, Object value) {
        super.field = field;
        this.operation = operation;
        setValue(value);
    }


    @Override
    public Specification<T> action() {
        return (root, query, cb) -> {
            query.distinct(true);
            return OperationService.doOperation(operation, value, cb, root, field);
        };
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        if (field.contains("isFree") || field.contains("isEvento") || field.contains("isDeleted") || field.contains("isPrivate"))
            this.value = Boolean.parseBoolean(value.toString());
        else
            this.value = value;
    }
}
