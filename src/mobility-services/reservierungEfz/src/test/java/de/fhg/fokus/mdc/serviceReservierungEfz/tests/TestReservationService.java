package de.fhg.fokus.mdc.serviceReservierungEfz.tests;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestReservationService {
	static String RESERVATION_URL = "http://193.175.133.248/service/reservierungEfz/reservations/";
	/**
	 * The logger of the class.
	 */
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Test
	@Ignore
	public void createReservationRequest() {
		String path = RESERVATION_URL;
		Client client = new Client();
		WebResource webResource = client.resource(path);
		// Make a POST request to ReservationService

		MultivaluedMapImpl map = new MultivaluedMapImpl();
		map.add("evehicleid", "ecar_00017");
		map.add("userid", "1");
		map.add("resbegin", "2015-03-18 08:30:00");
		map.add("resend", "2015-03-18 09:30:00");
		map.add("longstart", "13.54313");
		map.add("latstart", "52.42746");
		map.add("restype", "parking");
		map.add("macaddress", "12:34:56:78:9A:BC");

		ClientResponse response = webResource
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("accessToken", "39393939kkk000")
				.header("scope", "write").header("username", "developer")
				.post(ClientResponse.class, map);
		LOGGER.debug("response in test : " + response.getEntity(String.class));
		assertEquals(200, response.getClientResponseStatus().getStatusCode());
	}

	@Test
	@Ignore
	public void createReservationRequestIVI() {
		String path = RESERVATION_URL;
		Client client = new Client();
		WebResource webResource = client.resource(path);
		// Make a POST request to ReservationService

		MultivaluedMapImpl map = new MultivaluedMapImpl();
		map.add("evehicleid", "ecar_00023");
		map.add("userid", "1");
		map.add("resbegin", "2014-10-14 15:30:00");
		map.add("resend", "2014-10-14 16:30:00");
		map.add("longstart", "13.54313");
		map.add("latstart", "52.42746");
		map.add("restype", "parking");
		map.add("macaddress", "F0:6B:CA:CB:14:60");

		ClientResponse response = webResource
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("accessToken", "39393939kkk000")
				.header("scope", "write").header("username", "developer")
				.post(ClientResponse.class, map);
		System.out.println("response : " + response.getEntity(String.class));
		assertEquals(200, response.getClientResponseStatus().getStatusCode());
	}

	@Test
	public void createReservationRequestESK() {
		String path = RESERVATION_URL;
		Client client = new Client();
		WebResource webResource = client.resource(path);
		// Make a POST request to ReservationService

		MultivaluedMapImpl map = new MultivaluedMapImpl();
		map.add("evehicleid", "ecar_00012");
		map.add("userid", "1");
		map.add("resbegin", "2014-10-13 15:30:00");
		map.add("resend", "2014-10-13 16:30:00");
		map.add("longstart", "13.54313");
		map.add("latstart", "52.42746");
		map.add("restype", "parking");
		map.add("macaddress", "08:FC:88:38:CE:0C");

		ClientResponse response = webResource
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("accessToken", "39393939kkk000")
				.header("scope", "write").header("username", "developer")
				.post(ClientResponse.class, map);
		System.out.println("response : " + response.getEntity(String.class));
		assertEquals(200, response.getClientResponseStatus().getStatusCode());
	}
}
