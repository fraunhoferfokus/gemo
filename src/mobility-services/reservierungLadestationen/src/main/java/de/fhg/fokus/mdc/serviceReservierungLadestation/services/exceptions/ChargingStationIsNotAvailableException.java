package de.fhg.fokus.mdc.serviceReservierungLadestation.services.exceptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.Response;

import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;
import de.fhg.fokus.mdc.utils.SimpleValidator;

@SuppressWarnings("serial")
public class ChargingStationIsNotAvailableException extends
		ExpectedBusinessException {

	public ChargingStationIsNotAvailableException(int chargingstationID,
			Date resbegin, Date resend) {
		this(String.format(
				"Charging station %s is not available from %s to %s.",
				chargingstationID, new SimpleDateFormat(
						SimpleValidator.DATE_TIME_READABLE_PATTERN)
						.format(resbegin), new SimpleDateFormat(
						SimpleValidator.DATE_TIME_READABLE_PATTERN)
						.format(resend)));
	}

	public ChargingStationIsNotAvailableException(String message) {
		super(ChargingStationIsNotAvailableException.class, Response.Status.OK,
				message);
	}
}
