package de.fhg.fokus.mdc.serviceVerfuegbarkeitEFz.clients;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import de.fhg.fokus.mdc.clients.DataStoreClient;

public class VehicleAvailabilityDataStoreClient extends DataStoreClient {

	/** The singleton instances of the class. */
	private static VehicleAvailabilityDataStoreClient instance = null;

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static VehicleAvailabilityDataStoreClient getInstance() {
		if (instance == null) {
			instance = new VehicleAvailabilityDataStoreClient();
		}
		return instance;
	}

	/**
	 * is an implementation of DataStoreclient
	 */
	private VehicleAvailabilityDataStoreClient() {
		super();
	}

	@Override
	public void defineTableName() {
		table = "e_vehicle_reservation";
	}

	/**
	 * The method obtains the current availability of an electric vehicle.
	 * 
	 * @param sqlString
	 *            the SQL string fetching data regarding an electric vehicle
	 *            with a particular ID.
	 * 
	 * @return the details in the form of a JSON String.
	 */
	public String getEFzAvailability(String eVehicleID, String timeFrom,
			String timeTo) {
		// the variable to return
		String toreturn = null;
		// TODO modify regex for db accepted format

		// if (timeFrom.matches(TIME_REGEX) && timeTo.matches(TIME_REGEX)) {
		// return "";
		// }

		// ( '2012-07-03 12:00:00' >= resbegin) or ( '2012-07-03 12:00:00' >=
		// resend)
		String sqlBaseString = "select * from " + table + " where";

		// compile the sql query string
		String availabilityQuery = sqlBaseString + " eVehicleID=\'"
				+ eVehicleID + "' and (( \'" + timeTo
				+ "' >= resbegin  and  \'" + timeTo + "' <= resend ) or (  \'"
				+ timeFrom + "' >= resbegin  and  \'" + timeFrom
				+ "' <= resend  ) or ( \'" + timeFrom + "' < resbegin and \'"
				+ timeTo + "' > resend ) )";

		toreturn = select(availabilityQuery);
		if (toreturn == null) {
			return "";
		}

		return toreturn;
	}

	public String getMultipleEfzAvailability(List<String> eVehicleIDs,
			String timeFrom, String timeTo) {
		String sqlBaseString = "select * from " + table + " where ";
		String multipleAvailabilityQuery = sqlBaseString;
		// the variable to return
		String toreturn = null;
		for (String vehicleID : eVehicleIDs) {
			if (vehicleID == null || vehicleID.matches("^\\s*$")) {
				throw new WebApplicationException();
			} else {
				multipleAvailabilityQuery += "eVehicleID=\'" + vehicleID
						+ "' and (( \'" + timeTo + "' >= resbegin  and  \'"
						+ timeTo + "' <= resend ) or (  \'" + timeFrom
						+ "' >= resbegin  and  \'" + timeFrom
						+ "' <= resend  ) or ( \'" + timeFrom
						+ "' < resbegin and \'" + timeTo + "' > resend ) ) or ";

			}
		}

		// compile the sql query string
		StringBuilder b = new StringBuilder(multipleAvailabilityQuery);
		b.replace(multipleAvailabilityQuery.lastIndexOf(" or"),
				multipleAvailabilityQuery.lastIndexOf(" or") + 3, "");
		multipleAvailabilityQuery = b.toString();

		toreturn = select(multipleAvailabilityQuery);
		if (toreturn == null) {
			return "";
		}

		return toreturn;

	}

	/**
	 * The method issues the update of the availability status of an electric
	 * vehicle.
	 * 
	 * @param path
	 *            the path to use.
	 * 
	 * @param efzid
	 *            the id of the electric vehicle.
	 * 
	 * @param status
	 *            the new status to set.
	 * 
	 * @return the response from the data store, in general "OK" or "FAIL".
	 */
	@Deprecated
	public String updateElectricVehicleAvailability(String efzid, String status) {

		// the to string to return
		String toreturn = "OK";

		// prepare the post parameter
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("where", "efzid=\'" + efzid + "\'");
		map.add("status", status);
		map.add("tableName", "efzavailability");

		// issue the post request
		toreturn = update(map);
		if (toreturn == null) {
			toreturn = "FAIL";
		}

		return toreturn;
	}

}
