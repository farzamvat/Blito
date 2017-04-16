package com.blito.enums;

public enum Response {
	SUCCESS("success.operation"),
    CONCURRENCY_FAILURE("error.concurrency.failure"),
    ACCESS_DENIED("error.access.denied"),
    VALIDATION("error.validation"),
    METHOD_NOT_SUPPORTED("error.method.not.supported"),
    INTERNAL_SERVER_ERROR("error.internal.server.error"),
    USER_LOCKED("error.user.locked"),
    USER_NOT_ACTIVATED("error.user.not.activated"),
    MAX_RETRY_EXCEEDED("error.max.retry.exceeded"),
    INVALID_USER_STATUS_TO_SEND_MSG("error.invalid.user.status.to.send.sms"),
    USER_NOT_FOUND("error.user.not.found"),
    INVALID_NATIONAL_ID("error.invalid.national.id"),
    INVALID_EMAIL("error.invalid.email"),
    LOGIN_ALREADY_IN_USE("error.login.already.in.use"),
    EMAIL_ALREADY_IN_USE("error.email.already.in.use"),
    UNKNOWN_PHONE("error.unknown.phone"),
    EMAIL_VALIDATION_ERROR("error.email.validation"),
    INCORRECT_PASSWORD("error.incorrect.password"),
    FORGETING_PASSWORD_USER_EMAIL("forgeting.password.user.email"),
    ROLE_ALREADY_EXISTS("error.role.already.in.use"),
    ROLE_NOT_FOUND("error.role.not.found"),
    MERCHANT_ALREADY_REGISTERED("error.merchant.registered"),
    MERCHANT_TYPE_NOT_FOUND("error.merchant.type.not.found"),
    SERVICE_REQUEST_NOT_FOUND("error.service.request.not.found"),
    OPERATION_NOT_ALLOWED("operation.not.allowed"),
    IDP_CONFLICT("error.idp.conflict");

    private final String message;

    Response(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
