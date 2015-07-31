package de.fhg.fokus.mdc.clients;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import de.fhg.fokus.mdc.utils.GeoObject;
import de.fhg.fokus.mdc.utils.GeoUtils;

public class CarPositionDataStoreClient extends DataStoreClient {
	/** The singleton instances of the class. */
	private static CarPositionDataStoreClient instance = null;
	/**
	 * The logger of the class.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CarPositionDataStoreClient.class.getClass());

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static CarPositionDataStoreClient getInstance() {
		if (instance == null) {
			instance = new CarPositionDataStoreClient();
		}
		return instance;
	}

	private CarPositionDataStoreClient() {
		super();
	}

	@Override
	public void defineTableName() {
		// TODO get this from properties
		table = "e_vehicle_positions";
	}

	public String getEFzPositionById(String efzId) {

		// the variable to return
		String toreturn = null;
		// compile the sql query string
		String sqlString = "select lon, lat from " + table
				+ " where evehicleid=" + "\'" + efzId + "'";
		toreturn = select(sqlString);
		if (toreturn == null) {
			return "";
		}

		return toreturn;
	}

	public String getEfzsInRange(double lat, double lon, int radius) {
		String result = "FAIL";
		try {
			String allEVehicles = getAllEVehiclesPosition();
			GeoObject userPosition = new GeoObject();
			userPosition.setLatitude(Double.valueOf(lat));
			userPosition.setLongitude(Double.valueOf(lon));

			Object object = JSONValue.parse(allEVehicles);
			JSONArray jsonArray = (JSONArray) object;
			JSONArray resultJSONArray = new JSONArray();

			for (int i = 0; i < jsonArray.size(); i++) {
				Object objectVehicle = jsonArray.get(i);
				JSONObject vehicle = (JSONObject) objectVehicle;
				double distance = GeoUtils.calculateDistance(
						userPosition,
						new GeoObject(Double.valueOf((String) vehicle
								.get("lon")), Double.valueOf((String) vehicle
								.get("lat"))));
				if (distance <= radius) {
					resultJSONArray.add(objectVehicle);
				}
			}
			result = resultJSONArray.toJSONString();
		} catch (NullPointerException ex) {
			LOGGER.error("Error while finding vehicles in range", ex);
		}

		return result;

	}

	public String getAllEVehiclesPosition() {
		String sqlString = "SELECT evehicleid, lon, lat FROM " + table;
		String toreturn = select(sqlString);
		return toreturn;
	}

	/**
	 * The method issues the update of the position of an electric vehicle.
	 * 
	 * @param path
	 *            the path to use.
	 * 
	 * @param efzid
	 *            the id of the electric vehicle.
	 * 
	 * @param lon
	 *            the longitude.
	 * 
	 * @param lat
	 *            the latitude.
	 * 
	 * @return the response from the data store, in general "OK" or "FAIL".
	 */
	public String updateElectricVehiclePosition(String efzid, double lon,
			double lat) {

		// prepare the post parameter
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("where", "evehicleid=\'" + efzid + "\'");
		map.add("lon", String.valueOf(lon));
		map.add("lat", String.valueOf(lat));

		map.add("tableName", "e_vehicle_positions");

		// issue the post request
		String toreturn = update(map);
		if (toreturn == null) {
			toreturn = "FAIL";
		}

		return toreturn;
	}
}
