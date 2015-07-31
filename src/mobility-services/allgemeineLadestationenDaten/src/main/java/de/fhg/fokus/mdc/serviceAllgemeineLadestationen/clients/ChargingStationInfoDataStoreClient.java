package de.fhg.fokus.mdc.serviceAllgemeineLadestationen.clients;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.fhg.fokus.mdc.clients.DataStoreClient;
import de.fhg.fokus.mdc.utils.GeoObject;
import de.fhg.fokus.mdc.utils.GeoUtils;

public class ChargingStationInfoDataStoreClient extends DataStoreClient {
	/** The singleton instances of the class. */
	private static ChargingStationInfoDataStoreClient instance = null;

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static ChargingStationInfoDataStoreClient getInstance() {
		if (instance == null) {
			instance = new ChargingStationInfoDataStoreClient();
		}
		return instance;
	}

	/**
	 * is an implementation of DataStoreclient
	 */
	private ChargingStationInfoDataStoreClient() {
		super();
	}

	@Override
	public void defineTableName() {
		// TODO table = props.getProperty(CHARGING_STATIONS_TABLE);
		table = "charging_stations";
	}

	/**
	 * The method obtains the details of a charging station from the data store.
	 * 
	 * @param sqlString
	 *            the SQL string fetching data regarding a charging station with
	 *            a particular ID.
	 * 
	 * @return the details in the form of a JSON String.
	 */
	public String getLSDetails(String chargingStationId) {

		// the variable to return
		String toreturn = null;
		String sqlString = "select * from " + table + " where _id=" + "\'"
				+ chargingStationId + "'";
		;
		toreturn = select(sqlString);
		if (toreturn == null) {
			return "";
		}

		return toreturn;
	}

	public String getAllChargingStations() {
		String sqlString = "select * from " + table;
		String toreturn = select(sqlString);
		return toreturn;
	}

	public String getAllChargingStationsPosition() {
		String sqlString = "SELECT _id, lon, lat FROM " + table;
		String toreturn = select(sqlString);
		return toreturn;
	}

	public String getPositionById(String chargingStationId) {
		String sqlString = "select lon, lat from " + table + " where _id="
				+ "\'" + chargingStationId + "'";
		String position = select(sqlString);
		return position;
	}

	public String getChargingStationsInRange(double mylat, double mylon,
			int radius) {
		String allChargingStations = getAllChargingStationsPosition();
		try {

			GeoObject userPosition = new GeoObject();
			userPosition.setLatitude(Double.valueOf(mylat));
			userPosition.setLongitude(Double.valueOf(mylon));

			Object object = JSONValue.parse(allChargingStations);
			JSONArray jsonArray = (JSONArray) object;
			JSONArray resultJSONArray = new JSONArray();

			for (int i = 0; i < jsonArray.size(); i++) {
				Object objectStation = jsonArray.get(i);
				JSONObject Station = (JSONObject) objectStation;
				double distance = GeoUtils.calculateDistance(
						userPosition,
						new GeoObject(Double.valueOf((String) Station
								.get("lon")), Double.valueOf((String) Station
								.get("lat"))));
				if (distance <= radius) {
					resultJSONArray.add(objectStation);
				}
			}

			return resultJSONArray.toJSONString();
		} catch (NullPointerException ex) {
			// LOGGER.error("Error while finding stations in range", ex);
			return "FAIL, Error while finding stations in range: " + ex;
		}
	}
}
