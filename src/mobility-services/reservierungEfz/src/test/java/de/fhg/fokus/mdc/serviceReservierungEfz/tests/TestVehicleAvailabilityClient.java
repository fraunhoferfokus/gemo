package de.fhg.fokus.mdc.serviceReservierungEfz.tests;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.junit.Test;

import de.fhg.fokus.mdc.clients.VehicleAvailabilityClient;
import de.fhg.fokus.mdc.exceptions.ServiceFailedException;
import de.fhg.fokus.mdc.serviceReservierungEfz.services.VehicleReservationService;

public class TestVehicleAvailabilityClient {

	@Test
	public void testGetAvailability() throws URISyntaxException,
			UnsupportedEncodingException {
		// get availability
		VehicleAvailabilityClient availabilityClient = VehicleAvailabilityClient
				.getInstance();
		if (availabilityClient == null)
			throw new ServiceFailedException(
					VehicleReservationService.class.getName(),
					VehicleAvailabilityClient.class.getName());

		String eVehicleID = "ecar_00024";
		String timeFrom = "2012-07-03 13:00:00";
		String timeTo = "2012-07-03 15:00:00";

		org.springframework.http.HttpHeaders authHeaders = new org.springframework.http.HttpHeaders();
		authHeaders.add("accessToken", "39393939kkk000");
		authHeaders.add("scope", "write");
		authHeaders.add("username", "developer");
		availabilityClient.authHeaders = authHeaders;
		/*********************** query vehicle availability logic ***********************/
		String isAvailable = availabilityClient.getAvailability(eVehicleID,
				timeFrom, timeTo);
		System.out.println(isAvailable);
	}
}
