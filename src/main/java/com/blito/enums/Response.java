package com.blito.enums;

public enum Response {
	SUCCESS("success.operation"),
	RESET_PASSWORD_EMAIL_SENT("success.reset.email.sent"),
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
    IDP_CONFLICT("error.idp.conflict"),
    REGISTER_SUCCESS("success.registration.operation"),
    ACTIVATE_ACCOUNT_EMAIL("activation.account.email"),
    IMAGE_NOT_FOUND("error.not.found.image"), 
    EVENT_HOST_NOT_FOUND("error.event.host.not.found"), 
    NOT_ALLOWED("error.operation.not.allowed"), 
    BLIT_NOT_FOUND("error.blit.not.found"),
	EVENT_NOT_FOUND("error.event.not.found"),
	REFRESH_TOKEN_NOT_PRESENT("error.refresh.token.not.present"), 
	EVENT_LINK_EXISTS("error.event.link.exists"),
	EVENT_DATE_NOT_FOUND("error.event.date.not.found"),
	SEARCH_UNSUCCESSFUL("error.search.unsuccessful"),
	BLIT_TYPE_NOT_FOUND("error.blit.type.not.found"),
	INDEX_BANNER_NOT_FOUND("error.index.banner.not.found"),
	DISCOUNT_CODE_ALREADY_EXISTS("error.discount.code.exists"),
	INCONSISTENT_DATES("error.inconsistent.dates"),
	INCONSISTENT_PERCENT("error.inconsistent.percentage"),
	INCONSISTENT_AMOUNT_WHEN_PERCENT_IS_TRUE("error.inconsistent.data.is.percentage"),
	INCONSISTENT_AMOUNT("error.inconsistent.data.amount"),
	EVENT_HOST_ALREADY_EXISTS("event.host.exists"),
	ISFREE_AND_PRICE_NOT_MATCHED("error.is.free.price.not.matched"),
	INCONSISTENT_PERCENTAGE_WHEN_PERCENT_IS_FALSE("error.inconsistent.data.is.not.percentage"),
	EVENT_IS_SOLD("error.event.is.sold");
    

    private final String message;

    Response(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
