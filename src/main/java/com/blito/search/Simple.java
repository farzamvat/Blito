package com.blito.search;

import org.springframework.data.jpa.domain.Specification;

public class Simple<T> extends AbstractSearchViewModel<T> {

    public Object value;

    public Operation operation;

    private Object val;

    public Simple() {

    }

    public Simple(Operation operation, String field, Object value) {
        super.field = field;
        this.operation = operation;
        setValue(value);
        this.value = value;
    }


    @Override
    public Specification<T> action() {
        return (root, query, cb) -> {
            return OperationService.doOperation(operation, val, cb, root, field);
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
            this.val = Boolean.parseBoolean(value.toString());
        else
            this.val = value;
    }
}
