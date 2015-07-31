package de.fhg.fokus.mdc.serviceAllgemeineEFzDaten.datastore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.clients.DataStoreClient;

/**
 * 
 * @author izi
 */

public class CarInfoDataStoreClient extends DataStoreClient {

	/** The singleton instances of the class. */
	private static CarInfoDataStoreClient instance = null;
	/**
	 * The logger of the class.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CarInfoDataStoreClient.class.getClass());

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static CarInfoDataStoreClient getInstance() {
		if (instance == null) {
			instance = new CarInfoDataStoreClient();
		}
		return instance;
	}

	/**
	 * is an implementation of DataStoreclient
	 */
	private CarInfoDataStoreClient() {
		super();
	}

	@Override
	public void defineTableName() {
		// TODO table = props.getProperty(EVEHICLES_TABLE);
		table = "e_vehicles";
	}

	/**
	 * The method obtains the details for an electric vehicle from the data
	 * store.
	 * 
	 * @param sqlString
	 *            the SQL string fetching data regarding an electric vehicle
	 *            with a particular ID.
	 * 
	 * @return the details in the form of a JSON String.
	 */
	public String getEFzDetailsById(String efzId) {

		// the variable to return
		String toreturn = null;
		// compile the sql query string
		String sqlString = "select * from " + table + " where id=" + "\'"
				+ efzId + "'";
		toreturn = select(sqlString);
		if (toreturn == null) {
			return "";
		}

		return toreturn;
	}

	/**
	 * The method obtains data for multiple vehicles from the storage.
	 * 
	 * @param sqlString
	 *            the SQL string fetching data regarding multiple vehicles.
	 * 
	 * @return the data in the form of a JSON String.
	 */
	public String sendEFZQuery(String sqlString) {

		// the variable to return
		String toreturn = null;

		toreturn = select(sqlString);
		if (toreturn == null) {
			return "";
		}

		return toreturn;
	}

}