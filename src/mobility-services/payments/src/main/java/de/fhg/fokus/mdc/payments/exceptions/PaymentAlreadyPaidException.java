package de.fhg.fokus.mdc.payments.exceptions;

import javax.ws.rs.core.Response;

import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;
import de.fhg.fokus.mdc.jsonObjects.Payment;

@SuppressWarnings("serial")
public class PaymentAlreadyPaidException extends ExpectedBusinessException {

	public PaymentAlreadyPaidException(Payment payment) {
		this("Payment with id=" + payment.getId() + " already paid.");
	}

	public PaymentAlreadyPaidException(String message) {
		super(PaymentAlreadyPaidException.class, Response.Status.CONFLICT,
				message);
	}
}
