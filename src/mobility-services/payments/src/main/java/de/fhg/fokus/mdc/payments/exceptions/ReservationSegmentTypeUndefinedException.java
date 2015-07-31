package de.fhg.fokus.mdc.payments.exceptions;

import javax.ws.rs.core.Response;

import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;
import de.fhg.fokus.mdc.jsonObjects.ReservationSegment;

@SuppressWarnings("serial")
public class ReservationSegmentTypeUndefinedException extends
		ExpectedBusinessException {

	public ReservationSegmentTypeUndefinedException(ReservationSegment segment) {
		this(
				"The type \""
						+ segment.getReservationtype()
						+ "\" of the following reservation segment is undefined or unknown for price calculation: ("
						+ segment.toString() + ")");
	}

	public ReservationSegmentTypeUndefinedException(String message) {
		super(ReservationSegmentTypeUndefinedException.class,
				Response.Status.NOT_ACCEPTABLE, message);
	}
}