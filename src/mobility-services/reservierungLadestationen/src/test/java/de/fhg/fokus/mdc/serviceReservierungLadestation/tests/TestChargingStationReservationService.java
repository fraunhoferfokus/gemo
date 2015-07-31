package de.fhg.fokus.mdc.serviceReservierungLadestation.tests;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestChargingStationReservationService {
	@Test
	public void createReservationRequest() {
		String path = "http://localhost:8080/reservierungLadestation/reservation";
		Client client = new Client();
		WebResource webResource = client.resource(path);
		// Make a POST request to ReservationService

		MultivaluedMapImpl map = new MultivaluedMapImpl();
		map.add("chargingstationid", "2");
		// map.add("userid", "1");
		map.add("resbegin", "2066-03-18 14:05:00");
		map.add("resend", "2066-03-18 15:00:00");

		ClientResponse response = webResource
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("accessToken", "39393939kkk000")
				.header("scope", "write").header("username", "developer")
				.post(ClientResponse.class, map);
		System.out.println("response : " + response.getEntity(String.class));
		assertEquals(200, response.getClientResponseStatus().getStatusCode());
	}
}
