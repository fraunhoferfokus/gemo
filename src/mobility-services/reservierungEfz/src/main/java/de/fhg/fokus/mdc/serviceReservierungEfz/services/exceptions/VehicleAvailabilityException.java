package de.fhg.fokus.mdc.serviceReservierungEfz.services.exceptions;

import javax.ws.rs.core.Response;

import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;

/**
 * Exception for not available vehicles
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
@SuppressWarnings("serial")
public class VehicleAvailabilityException extends ExpectedBusinessException {

	public VehicleAvailabilityException(int eVehicleID) {
		this("Vehicle with id:" + eVehicleID + ") is not available.");
	}

	public VehicleAvailabilityException(String eVehicleID,
			String conflictedResultSet) {
		this("Conflicted reservations for vehicle " + eVehicleID + ":  "
				+ conflictedResultSet.toString());
	}

	public VehicleAvailabilityException(String message) {
		super(VehicleAvailabilityException.class,
				Response.Status.NOT_ACCEPTABLE, message);
	}
}