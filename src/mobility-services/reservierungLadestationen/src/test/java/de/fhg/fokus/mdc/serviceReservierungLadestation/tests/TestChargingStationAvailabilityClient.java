package de.fhg.fokus.mdc.serviceReservierungLadestation.tests;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

import de.fhg.fokus.mdc.clients.ChargingStationAvailabilityClient;
import de.fhg.fokus.mdc.utils.SimpleValidator;

public class TestChargingStationAvailabilityClient {
	// @Test
	public void testGetAvailability() throws URISyntaxException,
			UnsupportedEncodingException {
		// get availability
		ChargingStationAvailabilityClient availabilityClient = ChargingStationAvailabilityClient
				.getInstance();

		int stationID = 1;
		String resbegin = "2065-10-25 03:00:00";
		String resend = "2065-10-25 08:00:00";

		org.springframework.http.HttpHeaders authHeaders = new org.springframework.http.HttpHeaders();
		authHeaders.add("accessToken", "39393939kkk000");
		authHeaders.add("scope", "write");
		authHeaders.add("username", "developer");
		availabilityClient.authHeaders = authHeaders;
		/*********************** query charging station availability logic ***********************/
		boolean isAvailable = availabilityClient
				.requestChargingStationAvailabilitySpring(stationID,
						SimpleValidator.stringToDateTime(resbegin),
						SimpleValidator.stringToDateTime(resend));
		System.out.println(isAvailable);
	}

	// @Test
	public void testGetAvailabilityNot() throws URISyntaxException,
			UnsupportedEncodingException {
		// get availability
		ChargingStationAvailabilityClient availabilityClient = ChargingStationAvailabilityClient
				.getInstance();

		int stationID = 1;
		String resbegin = "2013-07-03 17:00:00";
		String resend = "2013-07-03 18:00:00";

		org.springframework.http.HttpHeaders authHeaders = new org.springframework.http.HttpHeaders();
		authHeaders.add("accessToken", "39393939kkk000");
		authHeaders.add("scope", "write");
		authHeaders.add("username", "developer");
		availabilityClient.authHeaders = authHeaders;
		/*********************** query charging station availability logic ***********************/
		boolean isAvailable = availabilityClient
				.requestChargingStationAvailabilitySpring(stationID,
						SimpleValidator.stringToDateTime(resbegin),
						SimpleValidator.stringToDateTime(resend));
		System.out.println(isAvailable);
	}

	@Test
	public void testGetAvailabilityJersey() throws URISyntaxException,
			JsonParseException, JsonMappingException, IOException {
		// get availability
		ChargingStationAvailabilityClient availabilityClient = ChargingStationAvailabilityClient
				.getInstance();

		int stationID = 2;
		String resbegin = "2066-03-18 11:00:00";
		String resend = "2066-03-18 12:00:00";

		/*********************** query charging station availability logic ***********************/
		boolean isAvailable = availabilityClient
				.requestChargingStationAvailability(stationID,
						SimpleValidator.stringToDateTime(resbegin),
						SimpleValidator.stringToDateTime(resend));
		System.out.println(isAvailable);
	}
}
