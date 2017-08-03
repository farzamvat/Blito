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

	public Object value;

	public Operation operation;

	private Object val;

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
//		if (field.contains("eventState") 
//				|| field.contains("eventDateState") 
//				|| field.contains("blitTypeState"))
//			this.val = Enum.valueOf(State.class, value.toString());
//		else if (field.contains("operatorState"))
//			this.val = Enum.valueOf(OperatorState.class, value.toString());
//		else if (field.contains("state"))
//			this.val = Enum.valueOf(State.class, value.toString());
//		else if (field.contains("eventType"))
//			this.val = Enum.valueOf(EventType.class, value.toString());
//		else if (field.contains("hostType"))
//			this.val = Enum.valueOf(HostType.class, value.toString());
//		else if (field.contains("exchangeBlitType"))
//			this.val = Enum.valueOf(ExchangeBlitType.class, value.toString());
//		else if (field.contains("imageType"))
//			this.val = Enum.valueOf(ImageType.class, value.toString());
//		else if (field.contains("paymentStatus"))
//			this.val = Enum.valueOf(PaymentStatus.class, value.toString());
//		else if (field.contains("seatType"))
//			this.val = Enum.valueOf(SeatType.class, value.toString());
//		else if (field.contains("bankGateway"))
//			this.val = Enum.valueOf(BankGateway.class, value.toString());
		if(field.contains("isFree") || field.contains("isEvento") || field.contains("isDeleted"))
			this.val = Boolean.parseBoolean(value.toString());
		else
			this.val = value;
	}
}
