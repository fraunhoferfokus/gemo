package de.fhg.fokus.mdc.partizipation.tests;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.spi.container.servlet.WebComponent;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.external.ExternalTestContainerFactory;

import de.fhg.fokus.mdc.partizipation.clients.AnliegenManagementClient;
import de.fhg.fokus.mdc.partizipation.services.ParticipationService;

public class ParticipationServiceTest extends JerseyTest {
	@Override
	public WebAppDescriptor configure() {
		return new WebAppDescriptor.Builder()
				.initParam(WebComponent.RESOURCE_CONFIG_CLASS,
						ClassNamesResourceConfig.class.getName())
				.initParam(
						ClassNamesResourceConfig.PROPERTY_CLASSNAMES,
						ParticipationService.class.getName() + ";"
								+ AnliegenManagementClient.class.getName())
				.build();
	}

	@Override
	protected TestContainerFactory getTestContainerFactory() {
		return new ExternalTestContainerFactory();
	}

	// @Test
	public void testgetComplaintForEFZParticipationServiceHttpRequestSuccess() {

		String pathToParticipation = "http://localhost:8080/partizipation";
		WebResource webResource = client().resource(pathToParticipation);
		// Make a GET request to ParticipationService
		ClientResponse response = webResource.path("/complaints")
				.queryParam("eVehicleID", "ecar_00001")
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		// String textEntity = response.getEntity(String.class);
		int expected = 200;

		assertEquals(expected, response.getClientResponseStatus()
				.getStatusCode());
		// assertEquals(expected, textEntity);
	}

	@Test
	public void testaddComplaintParticipationServiceHttpRequestSuccess() {

		String pathToParticipation = "http://localhost:8080/partizipation";
		WebResource webResource = client().resource(pathToParticipation);
		// Make a POST request to ParticipationService

		MultivaluedMapImpl map = new MultivaluedMapImpl();
		map.add("title", "new complaint");
		map.add("description", "new complaint");

		map.add("eVehicleID", "ecar_00024");
		map.add("tags", "müll,abfall");
		map.add("latitude", "52.2525");
		map.add("longitude", "14.5758");

		ClientResponse response = webResource.path("/complaints")
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.post(ClientResponse.class, map);
		// String textEntity = response.getEntity(String.class);
		int expected = 201;

		assertEquals(expected, response.getClientResponseStatus()
				.getStatusCode());

	}

	// @Test
	public void testgetComplaintForGeoParticipationServiceHttpRequestSuccess() {

		String pathToParticipation = "http://localhost:8080/partizipation";
		WebResource webResource = client().resource(pathToParticipation);
		// Make a GET request to ParticipationService
		ClientResponse response = webResource.path("/complaints")
				.queryParam("latitude", "52.2525")
				.queryParam("longitude", "14.5758")
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		// String textEntity = response.getEntity(String.class);
		int expected = 200;

		assertEquals(expected, response.getClientResponseStatus()
				.getStatusCode());
		// assertEquals(expected, textEntity);
	}

	// @Test
	public void testgetComplaintForGeoParticipationServiceHttpRequestFail() {

		String pathToParticipation = "http://localhost:8080/partizipation";
		WebResource webResource = client().resource(pathToParticipation);
		// Make a GET request to ParticipationService
		ClientResponse response = webResource.path("/complaints")
				.queryParam("latitude", "11.2525")
				.queryParam("longitude", "111.57581")
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		// String textEntity = response.getEntity(String.class);
		int expected = 400;

		assertEquals(expected, response.getClientResponseStatus()
				.getStatusCode());
		// assertEquals(expected, textEntity);
	}

	// @Test
	public void checkregex() {
		String latlon = "11.0,111.0";
		latlon.matches("^[+-]?\\d+\\.?\\d*,?[+-]?\\d+\\.?\\d*$");
	}

	// fixmycity developer told that addcomplaint as json method was never used,
	// it is not a priority to fix it.
	// public void testaddComplaintJSONParticipationServiceHttpRequestSuccess()
	// throws JsonGenerationException, JsonMappingException, IOException {
	//
	// ComplaintRequest c = new ComplaintRequest();
	//
	// Address a = new Address();
	// a.setCity("Berlin");
	// a.setCountryCode("DE");
	// a.setHouseNo("2");
	// a.setPostalCode("10000");
	// a.setStreet("Strasse");
	// Geolocation g = new Geolocation();
	// Double latitude = 52.2525;
	// Double longitude = 14.57581;
	// g.setLatitude(latitude);
	// g.setLongitude(longitude);
	// c.setAddress(a);
	// c.setGeolocation(g);
	// c.setTags("abfall,strasse");
	// c.setDescription("wann wird endlich?");
	// c.setTitle("yet another complaint");
	// ObjectMapper mapper = new ObjectMapper();
	// String jsonString = mapper.writeValueAsString(c);
	//
	// }

	// //@Test
	// public void
	// testgetComplaintForEFZPathParamParticipationServiceHttpRequestSuccess() {
	//
	// String pathToParticipation = "http://localhost:8080/partizipation";
	// WebResource webResource = client().resource(pathToParticipation);
	// // Make a GET request to ParticipationService
	// ClientResponse response = webResource.path("/complaints").path("/1")
	// .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
	//
	// String textEntity = response.getEntity(String.class);
	// int expected = 200;
	//
	// assertEquals(expected, response.getClientResponseStatus()
	// .getStatusCode());
	// // assertEquals(expected, textEntity);
	// }

	// see into interceptor to change response code if I make a GET request on
	// that path without sending any parameters it creates an entry?
	// @Test
	// public void
	// testgetComplaintForEFZPathParamParticipationServiceHttpRequestFail() {
	//
	// String pathToParticipation = "http://localhost:8080/partizipation";
	// WebResource webResource = client().resource(pathToParticipation);
	// // Make a GET request to ParticipationService
	// ClientResponse response = webResource.path("/complaints")
	// .path("/999999").accept(MediaType.APPLICATION_JSON)
	// .get(ClientResponse.class);
	//
	// String textEntity = response.getEntity(String.class);
	// int expected = 404;
	//
	// assertEquals(expected, response.getClientResponseStatus()
	// .getStatusCode());
	// // assertEquals(expected, textEntity);
	// }

}
