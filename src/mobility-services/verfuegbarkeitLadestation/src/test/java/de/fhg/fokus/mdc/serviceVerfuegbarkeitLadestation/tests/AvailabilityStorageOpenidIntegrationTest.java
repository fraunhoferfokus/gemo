package de.fhg.fokus.mdc.serviceVerfuegbarkeitLadestation.tests;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class AvailabilityStorageOpenidIntegrationTest {
	static Client CLIENT;
	static WebResource RESOURCE;

	@BeforeClass
	public static void setUp() {
		CLIENT = Client.create();
		final String PATH = "http://localhost:8080/verfuegbarkeitLadestation/queryAvailability";
		RESOURCE = CLIENT.resource(PATH);
	}

	@Test
	public void tokenAndRequestOk() {
		String token = "39393939kkk000";
		String scope = "reservation";
		String username = "developer";
		String from = "2065-10-25 20:05:00.0";
		String to = "2065-10-25 21:00:00.0";

		ClientResponse response = RESOURCE.path("/station")
				.queryParam("chargingstationid", "2")
				.queryParam("resBegin", from).queryParam("resEnd", to)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.header("access_token", token).header("scope", scope)
				.header("username", username).get(ClientResponse.class);
		Assert.assertEquals(200, response.getStatus());
	}
}
