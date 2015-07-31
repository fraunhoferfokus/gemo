package de.fhg.fokus.mdc.serviceReservierungLadestation.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.clients.ChargingStationAvailabilityClient;
import de.fhg.fokus.mdc.clients.ChargingStationDataStoreClient;
import de.fhg.fokus.mdc.clients.UserprofileDataStoreClient;
import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;
import de.fhg.fokus.mdc.exceptions.ServiceFailedException;
import de.fhg.fokus.mdc.exceptions.ValidationException;
import de.fhg.fokus.mdc.jsonObjects.ChargingStationReservation;
import de.fhg.fokus.mdc.serviceReservierungLadestation.services.exceptions.ChargingStationIsNotAvailableException;
import de.fhg.fokus.mdc.serviceReservierungLadestation.services.lib.Consts;
import de.fhg.fokus.mdc.serviceReservierungLadestation.services.responses.CreateReservationDataResponse;
import de.fhg.fokus.mdc.utils.SimpleValidator;

@Path("/reservation")
public class ChargingStationReservationService {

	/**
	 * The logger of the class.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
	 * POST method for creating a new charging station reservation.
	 * 
	 * <pre>
	 * Test-URL: http://localhost:8080/reservierungLadestation/reservation?chargingstationID=1&userID=2&resbegin=2014-10-25%2020:05:00.0&resend=2014-10-25%2021:00:00.0
	 * </pre>
	 * 
	 * @param chargingstationID
	 * @param resbegin
	 * @param resend
	 * @return
	 * @throws ValidationException
	 * @throws ServiceFailedException
	 * @throws IOException
	 * @throws MyMappedThrowingException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String /* Reservation */createReservation(
			@FormParam(Consts.PARAM_CHARGINGSTATIONID) String chargingstationID,
			@FormParam(Consts.PARAM_RESBEGIN) String resbegin,
			@FormParam(Consts.PARAM_RESEND) String resend,
			@Context HttpHeaders headers) throws ValidationException,
			ServiceFailedException, IOException,
			ChargingStationIsNotAvailableException {

		// ---------------map username to userid
		UserprofileDataStoreClient.getInstance().setAuthHeaders(headers);
		// TODO change this, setting headers twice is horrible
		UserprofileDataStoreClient.getInstance().setAuthHeadersJersey(headers);
		Integer userprofileID = UserprofileDataStoreClient.getInstance()
				.getUseridByUsername();
		// ---------------map username to userid end

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String json = "";
		try {

			// ---------------------[ validation ]-----------------------------
			// There we start a "first deny" validation with ArrayList and
			// SimpleValidator utils:
			ChargingStationReservation chargingStationReservation = validateFormParams(
					chargingstationID, resbegin, resend, userprofileID);

			// ------------[ front controller business validation ]------------

			// check availability using the availabilityService
			// here the types are SAFE, because of the validation
			ChargingStationAvailabilityClient availabilityClient = ChargingStationAvailabilityClient
					.getInstance();
			availabilityClient.setAuthHeadersJersey(headers);

			boolean stationIsAvailable = availabilityClient
					.requestChargingStationAvailability(
							Integer.parseInt(chargingstationID),
							SimpleValidator.stringToDateTime(resbegin),
							SimpleValidator.stringToDateTime(resend));
			if (!stationIsAvailable)
				throw new ChargingStationIsNotAvailableException(
						Integer.parseInt(chargingstationID),
						SimpleValidator.stringToDateTime(resbegin),
						SimpleValidator.stringToDateTime(resend));

			// ------------[ front controller business logic ]------------
			// create an accesscode:
			chargingStationReservation.setAccesscode("test");
			// chargingStationReservation.setUserid();

			// -------------[ configure data store client]----------------
			ChargingStationDataStoreClient dataStoreClient = ChargingStationDataStoreClient
					.getInstance();
			dataStoreClient.setAuthHeaders(headers);
			// persistence and instance update
			chargingStationReservation = dataStoreClient
					.insertChargingStationReservation(chargingStationReservation);

			json = new CreateReservationDataResponse(chargingStationReservation)
					.toString();
			// json = mapper.writeValueAsString(chargingStationReservation);

		} catch (ExpectedBusinessException expEx) {
			json = mapper.writeValueAsString(expEx.getResponse().getEntity());
		} catch (IOException e) {
			json = mapper.writeValueAsString(e);
		}
		return json;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~[ Helper Methods ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * simple validation of incoming form parameters. Throws an exception if
	 * validation fails returns a new instance of the target class type
	 * 
	 * 
	 * @param chargingstationID
	 * @param userID
	 * @param resbegin
	 * @param resend
	 * @return validation errors
	 */
	private ChargingStationReservation validateFormParams(
			String chargingstationID, String resbegin, String resend,
			Integer userID) throws ValidationException {
		List<String> validationErrors = new ArrayList<String>();

		Integer chargingstationid = -1;
		Integer userid = -1;
		Date resBegin;
		Date resEnd;
		ChargingStationReservation res = new ChargingStationReservation();

		// the charging station ID
		if (!SimpleValidator.notNull(chargingstationID))
			validationErrors.add("Paramater " + Consts.PARAM_CHARGINGSTATIONID
					+ " not found. Must be a valid number!");
		try {
			chargingstationid = Integer.parseInt(chargingstationID);
		} catch (NumberFormatException e) {
			validationErrors.add("Invalid " + Consts.PARAM_CHARGINGSTATIONID
					+ " '" + chargingstationID + "'.  Must be a valid number!");
		}

		// the userID
		if (!SimpleValidator.notNull(userID))
			validationErrors.add("Paramater " + Consts.PARAM_USERID
					+ " not found. Must be a valid number!");
		// try {
		// userid = Integer.parseInt(userID);
		// } catch (NumberFormatException e) {
		// validationErrors.add("Invalid " + Consts.PARAM_USERID + " '"
		// + userID + "'. Must be a valid number!");
		// }

		if (resbegin == null)
			validationErrors
					.add("Parameter "
							+ Consts.PARAM_RESBEGIN
							+ " not found. Must be a valid date (format YYYY-MM-DD%20HH:MM:SS.0)");
		if (resend == null)
			validationErrors
					.add("Parameter "
							+ Consts.PARAM_RESEND
							+ " not found. Must be a valid date (format YYYY-MM-DD%20HH:MM:SS.0)");
		// begin < end?
		if (resbegin != null && resend != null) {
			// we expect a valid "from"-parameter
			resBegin = SimpleValidator.stringToDateTime(resbegin);
			// we expect a valid "to"-parameter
			resEnd = SimpleValidator.stringToDateTime(resend);
			if (resBegin == null)
				validationErrors
						.add("Invalid "
								+ Consts.PARAM_RESBEGIN
								+ ". Must be a valid date (format YYYY-MM-DD%20HH:MM:SS.0)");
			if (resEnd == null)
				validationErrors
						.add("Invalid "
								+ Consts.PARAM_RESEND
								+ ". Must be a valid date (format YYYY-MM-DD%20HH:MM:SS.0)");
			if (resBegin.compareTo(resEnd) >= 0)
				validationErrors.add("invalid "
						+ Consts.PARAM_RESBEGIN
						+ "/"
						+ Consts.PARAM_RESEND
						+ " constellation. "
						+ Consts.PARAM_RESBEGIN
						+ " '"
						+ new SimpleDateFormat(
								SimpleValidator.DATE_TIME_PATTERN)
								.format(resBegin)
						+ "' must be earlier than "
						+ new SimpleDateFormat(
								SimpleValidator.DATE_TIME_PATTERN)
								.format(resEnd));
			// begin > now?
			if (resBegin.compareTo(new Date()) <= 0)
				validationErrors.add("invalid "
						+ Consts.PARAM_RESBEGIN
						+ ". '"
						+ new SimpleDateFormat(
								SimpleValidator.DATE_TIME_PATTERN)
								.format(resBegin) + "' must be in the future.");
			// prepare the instance
			res.setChargingstationid(chargingstationid);
			res.setUserid(userid);
			res.setResbegin(resBegin);
			res.setResend(resEnd);
		}
		if (!validationErrors.isEmpty())
			throw new ValidationException(validationErrors);

		return res;
	}

}
