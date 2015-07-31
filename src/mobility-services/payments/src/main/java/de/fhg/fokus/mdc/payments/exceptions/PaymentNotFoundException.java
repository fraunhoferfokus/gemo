package de.fhg.fokus.mdc.payments.exceptions;

import javax.ws.rs.core.Response;

import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;

@SuppressWarnings("serial")
public class PaymentNotFoundException extends ExpectedBusinessException {

	public PaymentNotFoundException(Integer id, Integer userprofileID) {
		this("Could not find payment (id:" + id + ",userprofileID:"
				+ userprofileID + ")");
	}

	public PaymentNotFoundException(String message) {
		super(PaymentNotFoundException.class, Response.Status.NOT_ACCEPTABLE,
				message);
	}
}
