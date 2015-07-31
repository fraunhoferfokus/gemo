package de.fhg.fokus.mdc.serviceReservierungEfz.services.exceptions;

import javax.ws.rs.core.Response;

import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;

/**
 * Exception for not reservation does not exist
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
@SuppressWarnings("serial")
public class ReservationNotFoundException extends ExpectedBusinessException {

	public ReservationNotFoundException(int reservationID) {
		this("Reservation with id:" + reservationID + " not found.");
	}

	public ReservationNotFoundException(String message) {
		super(ReservationNotFoundException.class,
				Response.Status.NOT_ACCEPTABLE, message);
	}
}