package de.fhg.fokus.mdc.clients;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_RESERVATION_TABLE;

import java.io.IOException;
import java.util.Date;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import de.fhg.fokus.mdc.jsonObjects.Reservation;

public class ReservationDataStoreClient extends DataStoreClient {
	/** The singleton instances of the class. */
	private static ReservationDataStoreClient instance = null;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private final String reservationDetailTableName;

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static ReservationDataStoreClient getInstance() {
		if (instance == null) {
			instance = new ReservationDataStoreClient();
		}
		return instance;
	}

	/**
	 * is an implementation of DataStoreclient
	 */
	private ReservationDataStoreClient() {
		super();
		reservationDetailTableName = "e_vehicle_reservation_details";

	}

	@Override
	public void defineTableName() {
		table = props.getProperty(SERVICE_RESERVATION_TABLE);
	}

	/**
	 * finds a payment instance by id
	 * 
	 * @param id
	 * @param userprofileID
	 * @return Payment
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Reservation getReservation(Integer id) throws JsonParseException,
			JsonMappingException, IOException {
		// fire a select
		String query = "select * from " + table + " where _id" + " =\'" + id
				+ "\'";
		LOGGER.error("getReservation query to storage search:", query);
		String res = select(query);

		// The response is an array, we must grab the first
		ObjectMapper mapper = new ObjectMapper();
		Reservation[] resultSet = mapper.readValue(res, Reservation[].class);
		return (resultSet.length == 0) ? null : resultSet[0];
	}

	/**
	 * this method is very first hack and workaround until the storage service
	 * supports inserts with json as return type of the last inserted object.
	 * 
	 * TODO: delete this method and replace it with the right storage
	 * functionality
	 * 
	 * @param path
	 *            (get path for the storge)
	 * @param map
	 *            (for intelligent where clause getting the max/last inserted
	 *            row)
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Reservation getLastInsertedReservation(String resBegin,
			String resEnd, String wifi) throws JsonParseException,
			JsonMappingException, IOException {

		// storage hates this native postgres sql xD but it should work
		String query = "select * from " + table + " where _id = "
				+ "  ( select max(_id) from " + table + " where "
				+ "        resbegin = to_timestamp('" + resBegin
				+ "', 'YYYY-MM-DD HH24:MI:SS')::timestamp "
				+ "    and resend = to_timestamp('" + resEnd
				+ "', 'YYYY-MM-DD HH24:MI:SS')::timestamp "
				+ "    and wifi_code = '" + wifi + "')";

		// fire a primitive select
		query = "select * from " + table + " where _id = "
				+ "  (select max(_id) from " + table + ")";
		String res = select(query);

		// The response is an array, we must grab the first
		ObjectMapper mapper = new ObjectMapper();
		Reservation[] resultSet = mapper.readValue(res, Reservation[].class);
		return (resultSet.length == 0) ? null : resultSet[0];
	}

	// TODO return array of segments or null and seperate the jaxrs response
	// code from parsing code

	public Response getSegments(String reservationID)
			throws JsonParseException, IOException {
		String result = null;
		String query = "select * from " + reservationDetailTableName
				+ " where reservationid=\'" + reservationID + "\'";
		result = select(query);

		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory();

		JsonParser jp = factory.createJsonParser(result);
		JsonNode actualObj = mapper.readTree(jp);
		if (actualObj.isArray()) {
			ArrayNode availabilityArray = (ArrayNode) actualObj;
			if (availabilityArray.size() > 0) {
				return Response.status(200).entity(result).build();
			} else if (availabilityArray.size() == 0) {
				return Response
						.status(404)
						.entity("{\"error\":\"No segments for the given reservation id\"}")
						.build();
			}
		}
		return Response.status(500).build();
	}

	public String createReservation(String eVehicleID, Integer userID,
			String timeFrom, String timeTo, String longStart, String latStart,
			String longEnd, String latEnd, String resType, Date resBegin,
			String wifi_code, String macaddress) throws IOException {

		String result = "FAIL";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		// insert params in map
		map.add("tableName", table);
		map.add("eVehicleID", eVehicleID);
		map.add("userID", String.valueOf(userID));
		map.add("resBegin", timeFrom);
		map.add("resEnd", timeTo);
		map.add("longStart", longStart);
		map.add("latStart", latStart);
		map.add("longEnd", longEnd);
		map.add("latEnd", latEnd);
		map.add("wifi_code", wifi_code);
		map.add("resType", resType);
		map.add("macaddress", macaddress);

		// send insert statement to storage
		result = insert(map);
		return result;
	}

	public String createSegment(String reservationID, String resType,
			String segBegin, String segEnd) {
		String result = null;
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		// insert params in map
		map.add("tableName", reservationDetailTableName);
		map.add("reservationID", reservationID);
		map.add("reservationtype", resType);
		map.add("segBegin", segBegin);
		map.add("segEnd", segEnd);
		result = insert(map);
		return result;
	}

}