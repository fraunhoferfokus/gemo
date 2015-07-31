package de.fhg.fokus.mdc.clients;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_AVAILABILITY_EFZ;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_AVAILABILITY_EFZ_METHOD_VEHICLE;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_AVAILABILITY_EFZ_METHOD_VEHICLES;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_AVAILABILITY_EFZ_QUERY;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class VehicleAvailabilityClient extends GemoClient {
	// ---------------------[ Constructor ]---------------------------

	/** The singleton instances of the class. */
	private static VehicleAvailabilityClient instance = null;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private final String pathToQueryAvailability;
	private final String vehicleMethod;
	private final String vehiclesMethod;

	/**
	 * The query parameter for vehicleAvailability.
	 */
	private final String aParam;

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static VehicleAvailabilityClient getInstance() {
		if (instance == null) {
			instance = new VehicleAvailabilityClient();
		}
		return instance;
	}

	private VehicleAvailabilityClient() {
		super();
		url = props.getProperty(SERVICE_URI_AVAILABILITY_EFZ);
		restTemplate = new RestTemplate();
		pathToQueryAvailability = props
				.getProperty(SERVICE_URI_AVAILABILITY_EFZ_QUERY);
		// TODO repair
		// aParam = props
		// .getProperty(SERVICE_URI_AVAILABILITY_EFZ_QUERY_PARAMETER);
		aParam = "?eVehicleID=";
		vehicleMethod = props
				.getProperty(SERVICE_URI_AVAILABILITY_EFZ_METHOD_VEHICLE);
		vehiclesMethod = props
				.getProperty(SERVICE_URI_AVAILABILITY_EFZ_METHOD_VEHICLES);
	}

	public String getAvailability(String eVehicleID, String timeFrom,
			String timeTo) throws URISyntaxException,
			UnsupportedEncodingException {

		// the variable to return
		String toreturn = null;
		if (authHeaders == null) {
			org.springframework.http.HttpHeaders authHeaders = new org.springframework.http.HttpHeaders();
			this.authHeaders = authHeaders;
		}
		authHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		String path = pathToQueryAvailability + vehicleMethod;

		// String requestUrl = this.url
		// + path
		// + "?eVehicleID={eVehicleID}&timeFrom={timeFrom}&timeTo={timeTo}";
		// HttpEntity<String> requestEntity = headerHelper
		// .generateEntityForGet(authHeaders);
		// ResponseEntity<String> response = restTemplate.exchange(requestUrl,
		// HttpMethod.GET, requestEntity, String.class, eVehicleID,
		// timeFrom, timeTo);
		// the above solution gives not enough parameters in the second call
		// with the same parameters, workaround with uri encoding the parameters
		// manually

		String requestUrl = this.url + path + "?eVehicleID=" + eVehicleID
				+ "&timeFrom=" + timeFrom + "&timeTo=" + timeTo;
		HttpEntity<String> requestEntity = headerHelper
				.generateEntityForGet(authHeaders);
		ResponseEntity<String> response = restTemplate.exchange(requestUrl,
				HttpMethod.GET, requestEntity, String.class);
		toreturn = response.getBody();
		if (toreturn == null) {
			return "";
		}
		return toreturn;
	}

}