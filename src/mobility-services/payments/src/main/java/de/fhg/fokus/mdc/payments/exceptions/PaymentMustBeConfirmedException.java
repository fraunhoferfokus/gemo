package de.fhg.fokus.mdc.payments.exceptions;

import javax.ws.rs.core.Response;

import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;

@SuppressWarnings("serial")
public class PaymentMustBeConfirmedException extends ExpectedBusinessException {

	public PaymentMustBeConfirmedException() {
		this("Aborted: payment must be confirmed first.");
	}

	public PaymentMustBeConfirmedException(String message) {
		super(PaymentMustBeConfirmedException.class,
				Response.Status.PRECONDITION_FAILED, message);
	}
}
