package de.fhg.fokus.mdc.payments.exceptions;

import javax.ws.rs.core.Response;

import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;

@SuppressWarnings("serial")
public class PaymentAlreadyExistsException extends ExpectedBusinessException {

	public PaymentAlreadyExistsException() {
		this(
				"Your payment already exists. Use GET and you receive a list of all your existing payments.");
	}

	public PaymentAlreadyExistsException(String message) {
		super(PaymentAlreadyExistsException.class, Response.Status.CONFLICT,
				message);
	}
}
