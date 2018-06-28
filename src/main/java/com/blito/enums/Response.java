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
	INVALID_START_END_DATE("error.invalid.start.end.date"),
	INCONSISTENT_PERCENT("error.inconsistent.percentage"),
	INCONSISTENT_AMOUNT_WHEN_PERCENT_IS_TRUE("error.inconsistent.data.is.percentage"),
	INCONSISTENT_AMOUNT("error.inconsistent.data.amount"),
	EVENT_HOST_ALREADY_EXISTS("event.host.exists"),
	ISFREE_AND_PRICE_NOT_MATCHED("error.is.free.price.not.matched"),
	INCONSISTENT_PERCENTAGE_WHEN_PERCENT_IS_FALSE("error.inconsistent.data.is.not.percentage"),
	EVENT_IS_SOLD("error.event.is.sold"),
	EXCHANGE_BLIT_IS_SOLD("error.exchange.blit.is.sold"),
	EVENT_NOT_APPROVED("error.event.not.approved"),
	FILE_UPLOAD_SIZE_EXCEEDS("error.file.upload.max.size.exceeds"), 
	EXCHANGE_BLIT_LINK_EXISTS("error.exchange.blit.link.exists"),
	BANK_GATEWAY_NOT_FOUND("error.bank.gateway.not.found"),
	FORGET_PASS_EMAIL("forget.password.email"),
	USER_IS_BANNED("error.user.is.banned"),
	ROLE_NAME_ALREADY_EXISTS("error.role.name.already.exists"),
	SENDING_EMAIL_ERROR("error.sending.email"),
	BLIT_NOT_AVAILABLE("error.blit.not.available"),
	ADDITIONAL_FIELDS_CANT_BE_EMPTY("error.additional.fields.are.empty"),
	ERROR_FIELD_TYPE_INT("error.field.type.is.int"),
	ERROR_FIELD_TYPE_DOUBLE("error.field.type.is.double"),
	BLIT_TYPE_SOLD("error.blit.type.sold"),
	BLIT_TYPE_CLOSED("error.blit.type.closed"),
	REQUESTED_BLIT_COUNT_IS_MORE_THAN_CAPACITY("error.requested.blits.are.more.than.capacity"),
	EVENT_DATE_NOT_OPEN("error.event.date.not.open"),
	EVENT_NOT_OPEN("error.event.no.open"),
	INCONSISTENT_TOTAL_AMOUNT("error.inconsistent.total.amount.blit.price.requested.count"),
	BLIT_COUNT_EXCEEDS_LIMIT("error.blit.count.exceeds.limit"),
	BLIT_COUNT_EXCEEDS_LIMIT_TOTAL("error.blit.count.exceeds.limit.total"),
	INVALID_ACTIVATION_KEY("error.invalid.activation.key"),
	USER_ALREADY_ACTIVATED("error.user.already.activated"),
	PAYMENT_PENDING("info.payment.pending"),
	PAYMENT_ERROR("info.payment.error"),
	EVENT_HOST_CAN_NOT_DELETE_WHEN_EVENT_EXISTS("error.event.host.can.not.delete.when.event.exists"),
	EVENT_HOST_LINK_ALREADY_EXIST("error.event.host.link.already.exists"),
	CANNOT_EDIT_EVENT_WHEN_CLOSED("error.event.is.closed"),
	BLIT_RECIEPT("blit.reciept"),
	PAYMENT_SUCCESS("info.payment.success"),
	DISCOUNT_CODE_NOT_FOUND("error.discount.code.not.found"),
	DISCOUNT_CODE_NOT_VALID("error.discount.code.not.valid"),
	EVENT_NOT_OPEN_DISCOUNT_CODE("error.event.not.open.discount.code"),
	DISCOUNT_PERCENTAGE_IS_FINAL("error.discount.percentage.is.final"),
	DISCOUNT_CODE_IS_FINAL("error.discount.code.is.final"),
	DISCOUNT_CODE_EXPIRED("error.discount.code.expired"),
	ADDITIONAL_FIELDS_VALIDATION_ERROR("error.additional.fields.validation"),
	SEAT_BLIT_RESERVED_TIME_OUT_OF_DATE("error.seat.blit.reserved.time.out.of.date"),
	INDIVIDUAL_SEAT_ERROR("error.individual.seat"),
	NO_SEATS_PICKED_ERROR("error.not.seat.picked"),
	SELECTED_SEAT_IS_SOLD_OR_RESERVED("error.sold.reserved.selected.seats"),
	SALON_NOT_FOUND("error.salon.not.found"),
	INCONSISTENT_SECTION_UIDS("error.inconsistent.section.uid"),
	SALON_OR_SECTIONS_SVG_CANNOT_BE_EMPTY("error.salon.sections.svg.empty"),
	INCONSISTENT_SEAT_COUNTS("error.seat.count.not.conforming"),
    Blit_Type_SEAT_NOT_FOUND("error.blit.type.set.not.found"),
    NOT_RESERVED_FOR_HOST("error.not.reserved.seat.for.host"),
	SHARED_SEAT_AND_INCONSISTENT_CAPACITY_ERROR("error.shared.seat.and.inconsistent.capacity"),
	ERROR_PESSIMISTIC_LOCK("error.pessimistic.lock"),
	PAY_DOT_IR_ERROR("error.pay.ir"),
	JIBIT_ERROR("error.jibit"),
	USER_ACTIVATION_RETRY_TIMEOUT("error.user.activation.retry.timeout"),
	ADDRESS_NOT_FOUND("error.address.not.found"),
	CANT_REMOVE_BLIT_TYPE("error.cant.remove.blit.type"),
	CANT_REMOVE_EVENT_DATE("error.cant.remove.event.date"),
	SEAT_INFORMATION("seat.info");
    

    private final String message;

    Response(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
