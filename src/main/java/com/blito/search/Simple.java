package com.blito.search;

import org.springframework.data.jpa.domain.Specification;

import com.blito.enums.BankGateway;
import com.blito.enums.EventType;
import com.blito.enums.ExchangeBlitType;
import com.blito.enums.HostType;
import com.blito.enums.ImageType;
import com.blito.enums.OperatorState;
import com.blito.enums.PaymentStatus;
import com.blito.enums.SeatType;
import com.blito.enums.State;

public class Simple<T> extends AbstractSearchViewModel<T> {

	public String value;

	public Operation operation;

	private Object val;

	@Override
	public Specification<T> action() {
		return (root, query, cb) ->

		OperationService.doOperation(operation, val, cb, root, field);
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

	public void setValue(String value) {
		if (field.equals("eventState") 
				|| field.equals("eventDateState") 
				|| field.equals("blitTypeState"))
			this.val = Enum.valueOf(State.class, value);
		else if (field.equals("operatorState"))
			this.val = Enum.valueOf(OperatorState.class, value);
		else if (field.equals("eventType"))
			this.val = Enum.valueOf(EventType.class, value);
		else if (field.equals("hostType"))
			this.val = Enum.valueOf(HostType.class, value);
		else if (field.equals("exchangeBlitType"))
			this.val = Enum.valueOf(ExchangeBlitType.class, value);
		else if (field.equals("imageType"))
			this.val = Enum.valueOf(ImageType.class, value);
		else if (field.equals("paymentStatus"))
			this.val = Enum.valueOf(PaymentStatus.class, value);
		else if (field.equals("seatType"))
			this.val = Enum.valueOf(SeatType.class, value);
		else if (field.equals("bankGateway"))
			this.val = Enum.valueOf(BankGateway.class, value);
		else if(field.equals("isDeleted"))
			this.val = Boolean.parseBoolean(value);
		else
			this.val = value;
	}
}
