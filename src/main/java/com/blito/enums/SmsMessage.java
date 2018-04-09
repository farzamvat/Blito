package com.blito.enums;

public enum SmsMessage {
    OPERATOR_STATE_BASE_MESSAGE("operator.state.message"),
    OPERATOR_STATE_ACCEPTED("accepted.operator.state"),
    OPERATOR_STATE_REJECTED("rejected.operator.state"),
    OPERATOR_STATE_EDIT_REJECTED_MESSAGE("edit.rejected.operator.state.message"),
    OPERATOR_STATE_ACCEPTED_MESSAGE("accepted.message"),
    OPERATOR_STATE_REJECTED_MESSAGE("rejected.message"),
    OPERATOR_STATE_DEFAULT_MESSAGE("default.operator.state.message");

    private final String message;

    SmsMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
