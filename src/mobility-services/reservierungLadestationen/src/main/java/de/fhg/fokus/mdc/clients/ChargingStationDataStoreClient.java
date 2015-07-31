package de.fhg.fokus.mdc.clients;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import de.fhg.fokus.mdc.exceptions.ServiceFailedException;
import de.fhg.fokus.mdc.jsonObjects.ChargingStationReservation;
import de.fhg.fokus.mdc.propertyProvider.Constants;
import de.fhg.fokus.mdc.utils.SimpleValidator;

/**
 * Storage-Client to work with charging stations
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */

public class ChargingStationDataStoreClient extends DataStoreClient {

	/** The table fields for a charing station. */
	private static final String COL_ID = "_id";
	private static final String COL_CHARGINGSTATIONID = "chargingstationID";
	private static final String COL_USERID = "userID";
	private static final String COL_RESBEGIN = "resBegin";
	private static final String COL_RESEND = "resEnd";
	private static final String COL_ACCESSCODE = "accesscode";

	public static final String PARAM_TABLENAME = "tableName";
	public static final String PARAM_WHERE = "where";

	public static final String STORAGE_RESPONSE_FAIL = "FAIL";
	public static final String STORAGE_RESPONSE_OK = "OK";

	/** The singleton instances of the class. */
	private static ChargingStationDataStoreClient instance = null;

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static ChargingStationDataStoreClient getInstance() {
		if (instance == null) {
			instance = new ChargingStationDataStoreClient();
		}
		return instance;
	}

	/**
	 * is an implementation of DataStoreclient
	 */
	public ChargingStationDataStoreClient() {
		super();
	}

	// ---------------------[ Super Class Methods]----------------------

	public void defineTableName() {
		table = props.getProperty(Constants.CHARGING_STATION_RESERVATION_TABLE);
	}

	// ----------------[ business/interface logic ]----------------------

	/**
	 * returns a reservation instance by id
	 * 
	 * @param id
	 * @param userID
	 * @return ChargingStationReservation
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public ChargingStationReservation getChargingStationReservation(Integer id,
			Integer userID) throws JsonParseException, JsonMappingException,
			IOException {
		// fire a select
		String res = select("select * from " + table + " where " + COL_ID
				+ " =\'" + id + "\'" + " and " + COL_USERID + "=" + userID);
		// The response is an array, we must grab the first
		ObjectMapper mapper = new ObjectMapper();
		ChargingStationReservation[] resultSet = mapper.readValue(res,
				ChargingStationReservation[].class);
		return (resultSet.length == 0) ? null : resultSet[0];
	}

	/**
	 * returns all ChargingStationReservations of a user
	 * 
	 * @param userID
	 * @return list of ChargingStationReservation
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public List<ChargingStationReservation> getChargingStationReservations(
			Integer userID) throws JsonParseException, JsonMappingException,
			IOException {
		// fire a select
		String res = select("select * from " + table + " where " + COL_USERID
				+ " =\'" + userID + "\'");
		// The response is an array, we must grab the first
		ObjectMapper mapper = new ObjectMapper();
		ChargingStationReservation[] resultSet = mapper.readValue(res,
				ChargingStationReservation[].class);
		return Arrays.asList(resultSet);
	}

	/**
	 * creates a new ChargingStationReservation in the storage
	 * 
	 * @param ChargingStationReservation
	 *            the new instance to persist
	 * @return the inserted instance with the updated object
	 * @throws IOException
	 */
	public ChargingStationReservation insertChargingStationReservation(
			ChargingStationReservation chargingStationReservation)
			throws ServiceFailedException, IOException {

		// field mapping for a new chargingStationReservation
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add(PARAM_TABLENAME, table);
		map = mapObjectFields(map, chargingStationReservation);

		// fire an insert and analyze the response
		if (insert(map).equals(STORAGE_RESPONSE_FAIL))
			throw new ServiceFailedException(
					"Storage: ChargingStationReservation insert failed.");

		// correct insert, so update the new instance
		return getLastInsertedReservation(chargingStationReservation);
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
	 * @param resBegin
	 *            the timestamp of the passed instance to persist
	 * @param resEnd
	 *            the timestamp of the passed instance to persist
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */

	public ChargingStationReservation getLastInsertedReservation(
			ChargingStationReservation chargingStationReservation)
			throws JsonParseException, JsonMappingException, IOException {

		String res = select("select * from " + table + " where " +

		// fire a primitive select
		// String res = select("select * from " + table + " where _id = "
		// + "  ( select max(_id) from " + table + " where "
		// // + "        resbegin = to_timestamp('"
		// + SimpleValidator
		// .dateToDateTimeString(chargingStationReservation
		// .getResbegin())
		// + "', 'YYYY-MM-DD HH24:MI:SS')::timestamp "
		// + "    and resend = to_timestamp('"
		// + SimpleValidator
		// .dateToDateTimeString(chargingStationReservation
		// .getResend())
		// + "', 'YYYY-MM-DD HH24:MI:SS')::timestamp "
				"        resbegin >= '"
				+ SimpleValidator
						.dateToDateTimeString(chargingStationReservation
								.getResbegin()) + "'");

		// String sqlQueryStr = sqlStr + " eVehicleID=\'" + eVehicleID
		// + "' and (( \'" + timeTo + "' >= resbegin  and  \'" + timeTo
		// + "' <= resend ) or (  \'" + timeFrom
		// + "' >= resbegin  and  \'" + timeFrom
		// + "' <= resend  ) or ( \'" + timeFrom + "' < resbegin and \'"
		// + timeTo + "' > resend ) )";
		// The response is an array, we must grab the first
		ObjectMapper mapper = new ObjectMapper();
		ChargingStationReservation[] resultSet = mapper.readValue(res,
				ChargingStationReservation[].class);
		return (resultSet.length == 0) ? null : resultSet[0];
	}

	/**
	 * updates a reservation in the storage
	 * 
	 * @param ChargingStationReservation
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void updateChargingStationReservation(
			ChargingStationReservation chargingStationReservation)
			throws JsonParseException, JsonMappingException, IOException {

		// field mapping for the update statement via parameters
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add(PARAM_TABLENAME, table);
		map.add(PARAM_WHERE,
				COL_ID + "=\'" + chargingStationReservation.getId() + "\'");
		map = mapObjectFields(map, chargingStationReservation);

		// fire an insert and analyze the response
		if (update(map).equals(STORAGE_RESPONSE_FAIL))
			throw new ServiceFailedException(
					"Storage: ChargingStationReservation operation failed.");
	}

	// ---------------------[ Helper Methods]----------------------

	/**
	 * helper method to set/map/convert the object fields
	 * 
	 * @param map
	 *            as MultiValueMap<String, String>
	 * @param csr
	 *            as ChargingStationReservation
	 * @return
	 */
	private MultiValueMap<String, String> mapObjectFields(
			MultiValueMap<String, String> map, ChargingStationReservation csr) {
		map.add(COL_CHARGINGSTATIONID, csr.getChargingstationid().toString());
		map.add(COL_USERID, csr.getUserid().toString());
		map.add(COL_RESBEGIN,
				SimpleValidator.dateToDateTimeString(csr.getResbegin())
						.toString());
		map.add(COL_RESEND,
				SimpleValidator.dateToDateTimeString(csr.getResend())
						.toString());
		map.add(COL_ACCESSCODE, csr.getAccesscode().toString());
		return map;
	}
}