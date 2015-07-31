package de.fhg.fokus.mdc.clients;

//no need for this, moved the method to ChargingStationInfoDataStoreClient
public class ChargingStationPositonDataStoreClient extends DataStoreClient {

	/** The singleton instances of the class. */
	private static ChargingStationPositonDataStoreClient instance = null;

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static ChargingStationPositonDataStoreClient getInstance() {
		if (instance == null) {
			instance = new ChargingStationPositonDataStoreClient();
		}
		return instance;
	}

	private ChargingStationPositonDataStoreClient() {
		super();
	}

	@Override
	public void defineTableName() {
		table = "charging_stations";
	}

	public String getPositionById(String chargingStationId) {
		String sqlString = "select lon, lat from " + table + " where _id="
				+ "\'" + chargingStationId + "'";
		String position = select(sqlString);
		return position;
	}
}
