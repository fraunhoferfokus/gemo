package de.fhg.fokus.mdc.payments.exceptions;

import javax.ws.rs.core.Response;

import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;

@SuppressWarnings("serial")
public class ReservationSegmentsNotFoundException extends
		ExpectedBusinessException {

	public ReservationSegmentsNotFoundException(Integer reservationID) {
		this("Could not find any segment for reservation (id:" + reservationID
				+ ")");
	}

	public ReservationSegmentsNotFoundException(String message) {
		super(ReservationSegmentsNotFoundException.class,
				Response.Status.NOT_FOUND, message);
	}
}