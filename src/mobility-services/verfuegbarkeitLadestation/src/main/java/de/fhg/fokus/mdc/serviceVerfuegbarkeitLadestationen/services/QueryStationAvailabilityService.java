package de.fhg.fokus.mdc.serviceVerfuegbarkeitLadestationen.services;

// imports
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.clients.ChargingStationAvailabilityDataStoreClient;
import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;
import de.fhg.fokus.mdc.exceptions.ValidationException;
import de.fhg.fokus.mdc.jsonObjects.ChargingStationReservation;
import de.fhg.fokus.mdc.serviceVerfuegbarkeitLadestationen.services.lib.Consts;
import de.fhg.fokus.mdc.serviceVerfuegbarkeitLadestationen.services.responses.QueryStationAvailabilityDataResponse;
import de.fhg.fokus.mdc.utils.SimpleValidator;

/**
 * The class implements the querying of the charging station.
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 */
@Path("/queryAvailability")
public class QueryStationAvailabilityService {

	/**
	 * The logger of the class.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
	 * 
	 * query against the availability of specific charging stations
	 * 
	 * <pre>
	 * Test-URL: http://localhost:8080/verfuegbarkeitLadestation/queryAvailability/station?chargingstationID=1&resbegin=2014-07-03%2016:10:00.0&resend=2014-07-03%2017:29:00.0
	 * </pre>
	 * 
	 * @param chargingstationID
	 * @param resbegin
	 * @param resend
	 * @return
	 * @throws ValidationException
	 * @throws IOException
	 */
	@GET
	@Path("/station")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response queryStationAvailability(
			@Context Request req,
			@QueryParam(Consts.PARAM_CHARGINGSTATIONID) String chargingstationID,
			@QueryParam("resbegin") String resbegin,
			@QueryParam("resend") String resend, @Context HttpHeaders headers)
			throws ValidationException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		LOGGER.error("1. queryStationData : validate");
		Response res = Response.ok().build();
		try {
			// ---------------------[ validation ]-----------------------------
			// There we start a "first deny" validation with ArrayList and
			// SimpleValidator utils:
			validateFormParams(chargingstationID, resbegin, resend);

			LOGGER.error("2. query availability");

			/************ [configure availability data store client] ****************/
			ChargingStationAvailabilityDataStoreClient availabilityClient = new ChargingStationAvailabilityDataStoreClient();

			/********* [forward authentication headers] ***********/
			// availabilityClient.setAuthHeaders(headers);

			// ------------[ front controller business logic]------------------
			// pass the valid and well formed parameters to the availability
			// check
			// method and expect an empty array of none overlapping reservations
			// (means station is free)
			List<ChargingStationReservation> overlappingReservations = ChargingStationAvailabilityDataStoreClient
					.getInstance().getOverlappingReservations(
							Integer.parseInt(chargingstationID),
							SimpleValidator.stringToDateTime(resbegin),
							SimpleValidator.stringToDateTime(resend));
			// business validation:
			if (overlappingReservations.size() > 0) {
				// okay, echo the overlapping reservations. later we wanna offer
				// gaps between them
				String gaps = getWellFormattedOverlappingResevations(overlappingReservations);
				throw new ValidationException(String.format(
						"%s overlapping reservation(s) (time spans):" + gaps,
						overlappingReservations.size(), new SimpleDateFormat(
								SimpleValidator.DATE_TIME_READABLE_PATTERN)
								.format(SimpleValidator
										.stringToDateTime(resbegin)),
						new SimpleDateFormat(
								SimpleValidator.DATE_TIME_READABLE_PATTERN)
								.format(SimpleValidator
										.stringToDateTime(resend))),
						Status.CONFLICT);
			}
			LOGGER.error("3. build expected response");
			String serializeJson = mapper
					.writeValueAsString(new QueryStationAvailabilityDataResponse());
			res = Response.ok(serializeJson).build();
		} catch (ExpectedBusinessException expEx) {
			LOGGER.error("A. build expected exception");
			Integer status = expEx.getResponse().getStatus();
			String serializeJson = mapper.writeValueAsString(expEx
					.getBusinessException());
			res = Response.status(status).entity(serializeJson).build();
		} catch (IOException e) {
			LOGGER.error("B. build unexpected exception");
			String serializeJson = mapper.writeValueAsString(e);
			res = Response.status(500).entity(serializeJson).build();
		}
		LOGGER.error("return response object");
		return res;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~[ Helper Methods ]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * returns a protocol of all overlapping reservations
	 * 
	 * @param overlappingReservations
	 * @return
	 */
	private String getWellFormattedOverlappingResevations(
			List<ChargingStationReservation> overlappingReservations) {
		String protocol = "";
		for (int i = 0; i <= overlappingReservations.size() - 1; i++) {
			// long duration = ((overlappingReservations.get(i).getResend()
			// .getTime() / 60000) - (overlappingReservations.get(i)
			// .getResbegin().getTime() / 60000));
			// protocol += (int) duration
			protocol += "("
					+ new SimpleDateFormat(
							SimpleValidator.DATE_TIME_READABLE_PATTERN)
							.format(overlappingReservations.get(i)
									.getResbegin())
					+ " - "
					+ new SimpleDateFormat(
							SimpleValidator.DATE_TIME_READABLE_PATTERN)
							.format(overlappingReservations.get(i).getResend())
					+ "); ";
		}
		return protocol;
	}

	/**
	 * simple validation of incoming form parameters. Throws an exception if
	 * validation fails.
	 * 
	 * @param chargingstationID
	 * @param resbegin
	 * @param resend
	 * @return void
	 */
	private void validateFormParams(String chargingstationID, String resbegin,
			String resend) throws ValidationException {
		List<String> validationErrors = new ArrayList<String>();

		Date resBegin;
		Date resEnd;

		// the charging station ID
		if (!SimpleValidator.notNull(chargingstationID))
			validationErrors.add("Paramater " + Consts.PARAM_CHARGINGSTATIONID
					+ " not found. Must be a valid number!");
		try {
			Integer.parseInt(chargingstationID);
		} catch (NumberFormatException e) {
			validationErrors.add("Invalid " + Consts.PARAM_CHARGINGSTATIONID
					+ " '" + chargingstationID + "'.  Must be a valid number!");
		}

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
		}
		if (!validationErrors.isEmpty())
			throw new ValidationException(validationErrors);
	}
}
