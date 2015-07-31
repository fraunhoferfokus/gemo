package de.fhg.fokus.mdc.servicePositionEFz.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//imports
/**
 * The class implements the methods to update and query the data storage for the
 * availability of an electric vehicle.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class OldDataStoreClient {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	/**
	 * The instance for the singleton pattern.
	 */
	private static OldDataStoreClient instance = null;
	/**
	 * The url string.
	 */
	private String url = null;
	/**
	 * The local REST template.
	 */
	private RestTemplate restTemplate = null;

	/**
	 * Function to deliver the singleton instance.
	 * 
	 * @param url
	 *            the url of the REST service.
	 * 
	 * @return the pre-configured data store client.
	 */
	public static OldDataStoreClient getInstance(String url) {
		if (instance == null) {
			instance = new OldDataStoreClient(url);
		}

		return instance;
	}

	/**
	 * Constructor for the singleton pattern.
	 * 
	 * @param url
	 *            the url of the REST service.
	 */
	private OldDataStoreClient(String url) {
		this.url = url;
		restTemplate = new RestTemplate();
	}

	/**
	 * The method obtains the latest available position of an electric vehicle.
	 * 
	 * @param sqlString
	 *            the SQL string fetching data regarding an electric vehicle
	 *            with a particular ID.
	 * 
	 * @return the details in the form of a JSON String.
	 */
	public String getEFzPosition(String sqlString) {

		String toreturn = restTemplate.getForObject(url + sqlString,
				String.class);
		if (toreturn == null) {
			return "";
		}

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
	public String updateElectricVehiclePosition(String path, String efzid,
			double lon, double lat) {

		// prepare the post parameter
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("where", "evehicleid=\'" + efzid + "\'");
		map.add("lon", String.valueOf(lon));
		map.add("lat", String.valueOf(lat));

		// TODO: make the table names configurable in all services
		map.add("tableName", "e_vehicle_positions");

		// issue the post request
		String toreturn = restTemplate.postForObject(url + path, map,
				String.class);
		if (toreturn == null) {
			toreturn = "FAIL";
		}

		return toreturn;
	}
}