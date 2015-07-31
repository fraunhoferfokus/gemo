package de.fhg.fokus.mdc.payments.exceptions;

import javax.ws.rs.core.Response;

import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;

@SuppressWarnings("serial")
public class ReservationMustContainSegmentsException extends
		ExpectedBusinessException {

	public ReservationMustContainSegmentsException(Integer reservationID) {
		this("Reservation (id:" + reservationID
				+ ") must contain a minimum of one reservation segment.");
	}

	public ReservationMustContainSegmentsException(String message) {
		super(ReservationMustContainSegmentsException.class,
				Response.Status.PRECONDITION_FAILED, message);
	}
}