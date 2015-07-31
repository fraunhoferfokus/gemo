package de.fhg.fokus.mdc.payments.lib;

/**
 * common constants for the service
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */

public class Consts {

	/** fields, Query/FormParams */
	public static final String PARAM_ID = "id";
	public static final String PARAM_USERPROFILEID = "userprofileID";
	public static final String PARAM_RESERVATIONSEQUENCEID = "reservationSequenceID";
	public static final String PARAM_CONFIRMED = "confirmed";
	public static final String PARAM_TABLENAME = "tableName";
	public static final String PARAM_WHERE = "where";

	/** responses **/
	public static final String STORAGE_RESPONSE_FAIL = "FAIL";
	public static final String STORAGE_RESPONSE_OK = "OK";

	/** validation */
	public static final String NO_VALID_USERID = "NO VALID USER ID : userprofileID=";
	public static final String NO_VALID_PAYMENTID = "NO VALID PAYMENT ID : id=";
	public static final String NO_VALID_RESERVATIONSEQUENCEID = "NO VALID RESERVATION SEQUENCE ID : reservationSequenceID=";

	/** reservation service query params **/
	public static final String PARAM_RESERVATION_EVEHICLEID = "eVehicleID";
	public static final String PARAM_RESERVATION_USERID = "userID";
	public static final String PARAM_RESERVATION_TIMEFROM = "timeFrom";
	public static final String PARAM_RESERVATION_TIMETO = "timeTo";
	public static final String PARAM_RESERVATION_LONGSTART = "longStart";
	public static final String PARAM_RESERVATION_LATSTART = "latStart";
	public static final String PARAM_RESERVATION_LONGEND = "longEnd";
	public static final String PARAM_RESERVATION_LATEND = "latEnd";
	public static final String PARAM_RESERVATION_TYPE = "type";
	public static final String PARAM_RESERVATION_SEQUENCEID = "sequenceID";

}
