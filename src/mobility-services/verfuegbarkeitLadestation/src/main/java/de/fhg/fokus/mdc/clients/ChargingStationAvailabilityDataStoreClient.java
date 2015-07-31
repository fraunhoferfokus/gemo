package de.fhg.fokus.mdc.clients;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import de.fhg.fokus.mdc.clients.DataStoreClient;
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

public class ChargingStationAvailabilityDataStoreClient extends DataStoreClient {

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
	private static ChargingStationAvailabilityDataStoreClient instance = null;

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static ChargingStationAvailabilityDataStoreClient getInstance() {
		if (instance == null) {
			instance = new ChargingStationAvailabilityDataStoreClient();
		}
		return instance;
	}

	/**
	 * is an implementation of DataStoreclient
	 */
	public ChargingStationAvailabilityDataStoreClient() {
		super();
	}

	// ---------------------[ Super Class Methods]----------------------

	public void defineTableName() {
		table = props.getProperty(Constants.CHARGING_STATION_RESERVATION_TABLE);
	}

	// ----------------[ business/interface logic ]----------------------

	/**
	 * <pre>
	 * Danilo says: the magic here is to get all possible constellations that matches for overlapping reservations:
	 * 
	 * keys:
	 * 			  B 	= 	value of parameter Date resbegin
	 * 			  E 	= 	value of parameter Date resend
	 * 			  b 	= 	value of column "resbegin" in the storage table
	 * 			  e 	= 	value of column "resend" in the storage table
	 * 			  X_____Y = a time span between X and Y (or from X to Y) 
	 * 
	 * constellations we wanna find (overlapping time spans):
	 * 1.) 	|	  B_____E     |   2.)  |	  B_____E     |    <--- our time span to check against:..
	 *      |  b___________e  |        | b_______e        |    <--- ...the value (time span) in the database
	 *      (   B<e and E>b   )        (    B<e and E>b   )    <--- the expression to get that match
	 *      
	 * 3.)  |  B_____E        |   4.)  |	 B_____E      |   
	 *      |     b________e  |        |     b_____e      |
	 *      (   B<e and E>b   )        (   B<e and E>b    )
	 *      
	 * all other constellations are not overlapping elements!
	 * So it' easy now. the expression is:
	 * 
	 *        B<e and E>b       <=>      e>B and b<E     (same expression)
	 *  
	 * sql: where resend > B and resbegin < E
	 * </pre>
	 */
	public List<ChargingStationReservation> getOverlappingReservations(
			int chargingstationID, Date resbegin, Date resend)
			throws JsonParseException, JsonMappingException, IOException {

		// fire a select
		String res = select("select * from " + table + " where resend >" + " '"
				+ SimpleValidator.dateToDateTimeString(resbegin) + "'"
				+ " and resbegin < '"
				+ SimpleValidator.dateToDateTimeString(resend) + "'"
				+ " and chargingstationID = " + chargingstationID
				+ " order by resbegin");
		// the result contains all reservations that affects the passed time
		// span ordered by resbegin
		ObjectMapper mapper = new ObjectMapper();
		ChargingStationReservation[] resultSet = mapper.readValue(res,
				ChargingStationReservation[].class);
		return Arrays.asList(resultSet);
	}

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

		String res = select("select * from "
				+ table
				+ " where "
				+ " resbegin >= '"
				+ SimpleValidator
						.dateToDateTimeString(chargingStationReservation
								.getResbegin()) + "'");

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