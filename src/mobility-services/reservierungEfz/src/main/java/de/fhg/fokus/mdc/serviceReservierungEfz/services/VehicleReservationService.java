package de.fhg.fokus.mdc.serviceReservierungEfz.services;

// imports
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.clients.ReservationDataStoreClient;
import de.fhg.fokus.mdc.clients.UserprofileDataStoreClient;
import de.fhg.fokus.mdc.clients.VehicleAvailabilityClient;
import de.fhg.fokus.mdc.exceptions.ServiceFailedException;
import de.fhg.fokus.mdc.exceptions.ValidationException;
import de.fhg.fokus.mdc.jsonObjects.Reservation;
import de.fhg.fokus.mdc.jsonObjects.VehicleState;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.mdc.serviceReservierungEfz.services.exceptions.ReservationNotFoundException;
import de.fhg.fokus.mdc.serviceReservierungEfz.services.exceptions.VehicleAvailabilityException;
import de.fhg.fokus.mdc.utils.SimpleValidator;

//import java.util.logging.Level;
/**
 * The class implements the reservierungEFz service.
 * 
 * @author Begum Ilke Zilci, ilke.zilci@fokus.fraunhofer.de
 * @author Thomas Scheel, thomas.scheel@fokus.fraunhofer.de
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 */
@Path("/reservations")
public class VehicleReservationService {

	/**
	 * The logger of the class.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final String DATE_TIME_REGEX = "(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})((-|\\+)\\d{2}:\\d{2}|Z)?";
	private static final String RES_TYPE_ENUM_STRING;
	private static final String[] RES_TYPE_ENUM;

	private String isAvailable = null;
	List<VehicleState> vehicleStates = null;

	private static Properties props = null;

	static {
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RES_TYPE_ENUM_STRING = props.getProperty("gemo.reservation.type");
		RES_TYPE_ENUM = props.getProperty("gemo.reservation.type").split(",");
		// RES_TYPE_ENUM = props.getProperty(RESERVATION_TYPE_ENUM_NAME)
		// .split(",");

	}

	/**
	 * GET "reservations/{id}" returns the reservation with {id}
	 * 
	 * @param id
	 * @return reservation as json
	 * @throws ReservationNotFoundException
	 * @throws IOException
	 */
	@GET
	@Path("{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String /* Reservation */getReservation(@PathParam("id") Integer id,
			@Context HttpHeaders headers) throws ReservationNotFoundException,
			IOException {
		/********************* input validation ************************/
		LOGGER.error("getReservation: start GET with id={}", id);
		// here we start a "first deny" validation with ArrayList and
		// SimpleValidator utils
		List<String> validationErrors = new ArrayList<String>();

		if (!SimpleValidator.notNull(id))
			validationErrors.add("invalid id \"" + id + "\"");
		if (!validationErrors.isEmpty())
			throw new ValidationException(validationErrors);

		LOGGER.error("getReservation: validation okay");
		LOGGER.error("getReservation: requesting storage");

		/********************* client config **************************/
		// get an instance of the client
		ReservationDataStoreClient reservationClient = ReservationDataStoreClient
				.getInstance();
		if (reservationClient == null) {
			LOGGER.error("getReservation: storage is null");
			throw new ServiceFailedException(
					VehicleReservationService.class.getName(),
					ReservationDataStoreClient.class.getName());
		}
		/********* [forward authentication headers] ***********/
		reservationClient.setAuthHeaders(headers);

		/********************** logic ***********************/
		LOGGER.error("getReservation: requesting reservation");
		Reservation res = reservationClient.getReservation(id);
		if (res == null) {
			LOGGER.error("getReservation: reservation is null");
			throw new ReservationNotFoundException(id);
		}
		LOGGER.error("getReservation: got reservation: {}", res.toString());

		// return res;
		// dsc: we build and return a json manually, because the auto
		// serialization
		// doesn't work for this project on the live server
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String json = mapper.writeValueAsString(res);
		return json;
	}

	/**
	 * The method realizes the reservation request for a particular eFz.
	 * 
	 * @param eVehicleID
	 *            the Id of the eFz to query.
	 * @param timeFrom
	 *            the time from
	 * @param timeTo
	 *            the time to
	 * @param longStart
	 *            the long start
	 * @param latStart
	 *            the lat start
	 * @param longEnd
	 *            the long end
	 * @param latEnd
	 *            the lat end
	 * @param resType
	 *            the res type
	 * @return Reservation as JSON
	 * @throws IOException
	 *             an IO Exception in case of a corresponding fault.
	 * @throws URISyntaxException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String /* Reservation */createReservation(
			@FormParam("evehicleid") String eVehicleID,
			@FormParam("resbegin") String timeFrom,
			@FormParam("resend") String timeTo,
			@FormParam("longstart") String longStart,
			@FormParam("latstart") String latStart,
			@DefaultValue("0.0") @FormParam("longend") String longEnd,
			@DefaultValue("0.0") @FormParam("latend") String latEnd,
			@FormParam("restype") String resType, @Context HttpHeaders headers,
			@FormParam("macaddress") String macaddress)
			throws ValidationException, ServiceFailedException,
			VehicleAvailabilityException, IOException, URISyntaxException {
		LOGGER.error("createReservation: start ");

		UserprofileDataStoreClient.getInstance().setAuthHeaders(headers);
		// TODO change this, setting headers twice is horrible
		UserprofileDataStoreClient.getInstance().setAuthHeadersJersey(headers);
		Integer userprofileID = UserprofileDataStoreClient.getInstance()
				.getUseridByUsername();

		/********* [ deny first - validation ] ********/

		// here we start a "first deny" validation with ArrayList and
		// SimpleValidator utils
		List<String> validationErrors = new ArrayList<String>();

		if (!SimpleValidator.notNull(eVehicleID))
			validationErrors.add("invalid evehicleid '" + eVehicleID + "'");

		if (!SimpleValidator.notNull(userprofileID))
			validationErrors.add("invalid userid '" + userprofileID + "'");
		// date time constellation (create from passed String and fall back to
		// a default value if necessary)
		Date resBegin = SimpleValidator.stringToDateTime(timeFrom, new Date());
		// if there is no to-value, create one 15 minutes later
		Date resEnd = SimpleValidator.stringToDateTime(timeTo, new Date(
				resBegin.getTime() + (15 * 60000))); // add 15 minutes
		// in all cases, create a string from the validated date/time-values to
		// overwrite the incoming values (timefrom/timto) with the well
		// validated values:
		timeFrom = SimpleValidator.dateToDateTimeString(resBegin);
		timeTo = SimpleValidator.dateToDateTimeString(resEnd);
		if (resBegin.compareTo(resEnd) >= 0)
			validationErrors
					.add("invalid resbegin/resend constellation. resbegin '"
							+ new SimpleDateFormat(
									SimpleValidator.DATE_TIME_PATTERN)
									.format(resBegin)
							+ "' must be earlier than "
							+ new SimpleDateFormat(
									SimpleValidator.DATE_TIME_PATTERN)
									.format(resEnd));
		if (!SimpleValidator.notNull(longStart))
			validationErrors.add("invalid longstart '" + longStart + "'");
		if (!SimpleValidator.notNull(latStart))
			validationErrors.add("invalid latstart '" + latStart + "'");
		// resType enum
		String[] resTypeEnum = RES_TYPE_ENUM_STRING.split(",");
		if (!Arrays.asList(resTypeEnum).contains(resType))
			validationErrors.add("invalid restype '" + resType
					+ "', expecting " + RES_TYPE_ENUM_STRING);

		if (!validationErrors.isEmpty())
			throw new ValidationException(validationErrors);

		LOGGER.error("createReservation: validation okay");

		/*********************** vehicle availability client config ***********************/
		VehicleAvailabilityClient availabilityClient = VehicleAvailabilityClient
				.getInstance();
		if (availabilityClient == null)
			throw new ServiceFailedException(
					VehicleReservationService.class.getName(),
					VehicleAvailabilityClient.class.getName());
		availabilityClient.setAuthHeaders(headers);
		LOGGER.error("createReservation: availabilityClient okay");
		/*********************** query vehicle availability logic ***********************/
		String isAvailable = availabilityClient.getAvailability(eVehicleID,
				timeFrom, timeTo);

		if (isAvailable == null) {
			LOGGER.error("createReservation: isAvailable is null, throwing exception");
			throw new VehicleAvailabilityException(Integer.parseInt(eVehicleID));
		}
		if (isAvailable.length() <= 0) {
			LOGGER.error("createReservation: isAvailable length <= 0, throwing exception");
			throw new VehicleAvailabilityException(Integer.parseInt(eVehicleID));
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonFactory factory = mapper.getJsonFactory(); // since 2.1 use
		// mapper.getFactory()
		// instead
		JsonParser jp = factory.createJsonParser(isAvailable);
		JsonNode actualObj = mapper.readTree(jp);

		String result = "FAIL";
		if (!actualObj.isArray()) {
			LOGGER.error("createReservation: actualObj is not array, throwing exception");
			throw new ServiceFailedException(
					VehicleReservationService.class.getName(),
					VehicleAvailabilityClient.class.getName()
							+ " returns an unexpected format (expecting array).");
		}

		ArrayNode availabilityArray = (ArrayNode) actualObj;
		if (availabilityArray.size() > 0) {
			// return reservations which are conflicted 409
			// return availabilityArray.toString();
			// return Response.status(Response.Status.CONFLICT)
			// .entity(availabilityArray).build();
			LOGGER.error("createReservation: car not available");

			Reservation[] resultSet = mapper.readValue(availabilityArray,
					Reservation[].class);
			String resultSetStr = mapper.writeValueAsString(resultSet);
			throw new VehicleAvailabilityException(eVehicleID, resultSetStr);
		}

		/********* data store client config **************************/
		// get an instance of the client
		ReservationDataStoreClient reservationClient = ReservationDataStoreClient
				.getInstance();
		if (reservationClient == null) {
			LOGGER.error("getReservation: storage is null");
			throw new ServiceFailedException(
					VehicleReservationService.class.getName(),
					ReservationDataStoreClient.class.getName());
		}

		/********* [forward authentication headers] ***********/
		reservationClient.setAuthHeaders(headers);

		/** insert statement of the new reservation record **/
		String wifi_code = UUID.randomUUID().toString();
		result = reservationClient.createReservation(eVehicleID, userprofileID,
				timeFrom, timeTo, longStart, latStart, longEnd, latEnd,
				resType, resBegin, wifi_code, macaddress);

		if (!result.equals("OK")) {
			LOGGER.error("createReservation: storage inserted OK");
			throw new ServiceFailedException(
					VehicleReservationService.class.getName(),
					"Reservation insert failed using postFormData with "
							+ ReservationDataStoreClient.class.getName());
		}
		LOGGER.debug("createReservation: return new reservation:");
		// everything works fine. return the newly inserted object:
		Reservation res = reservationClient.getLastInsertedReservation(
				timeFrom, timeTo, wifi_code);
		res.generateToken();
		LOGGER.debug("createReservation: reservation is:{}", res.toString());
		// return res;
		// dsc: we build and return a json manually, because the auto
		// serialization
		// doesn't work for this project on the live server

		String json = mapper.writeValueAsString(res);
		return json;
	}

	// private String createEncodedToken(Date resBegin) throws IOException {
	//
	// TOTPGenerator totpgen = new SHA1TOTPGenerator();
	// String totpToken = totpgen.generateToken(resBegin, null);
	// String totpEncoded = Base64.encodeBase64String(totpToken.getBytes());
	// return totpEncoded;
	// }

	@GET
	@Path("/{reservationID}/segments")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSegments(
			@PathParam("reservationID") String reservationID,
			@Context HttpHeaders headers) throws IOException {

		/********* data store client config **************************/
		// get an instance of the client
		ReservationDataStoreClient reservationClient = ReservationDataStoreClient
				.getInstance();
		if (reservationClient == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
		/********* [forward authentication headers] ***********/
		reservationClient.setAuthHeaders(headers);
		/********************* logic *********************/
		Response result = reservationClient.getSegments(reservationID);
		return result;
	}

	// TODO check if the initial end time of the reservation is exceeded, create
	// additional reservation
	@POST
	@Path("/{reservationID}/segments")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addSegment(
			@PathParam("reservationID") String reservationID,
			@FormParam("restype") String resType,
			@FormParam("segbegin") String segBegin,
			@FormParam("segend") String segEnd, @Context HttpHeaders headers)
			throws ParseException {
		/********* data store client config **************************/
		// get an instance of the client
		ReservationDataStoreClient reservationClient = ReservationDataStoreClient
				.getInstance();
		if (reservationClient == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
		/********* [forward authentication headers] ***********/
		reservationClient.setAuthHeaders(headers);

		// TODO check input, resid, restype valid?
		// resolve problem with timestamp syntax error
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date segBeg = dateFormat.parse(segBegin);
		Date segFin = dateFormat.parse(segEnd);
		segBegin = dateFormat.format(segBeg);
		segEnd = dateFormat.format(segFin);

		String result = reservationClient.createSegment(reservationID, resType,
				segBegin, segEnd);
		// TODO return JSON
		return Response
				.status(200)
				.entity("posted a segment for res id " + reservationID
						+ " result from storage : " + result).build();
	}

}
