package de.fhg.fokus.mdc.opnv.services;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.spi.container.servlet.WebComponent;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.external.ExternalTestContainerFactory;

import de.fhg.fokus.mdc.opnv.services.impl.OPNVClient;
import de.fhg.fokus.mdc.opnv.services.impl.QueryRouteServiceImpl;

public class QueryRouteServiceTest extends JerseyTest {

	@Override
	public WebAppDescriptor configure() {
		return new WebAppDescriptor.Builder()
				.initParam(WebComponent.RESOURCE_CONFIG_CLASS,
						ClassNamesResourceConfig.class.getName())
				.initParam(
						ClassNamesResourceConfig.PROPERTY_CLASSNAMES,
						QueryRouteServiceImpl.class.getName() + ";"
								+ OPNVClient.class.getName()).build();
	}

	@Override
	protected TestContainerFactory getTestContainerFactory() {
		return new ExternalTestContainerFactory();
	}

	@Test
	public void testgetRouteJSONPublicTransportServiceHttpRequestSuccess() {
		String expectedText = "sth";
		String pathToParticipation = "http://localhost:8080/opnv";
		WebResource webResource = client().resource(pathToParticipation);
		// Make a GET request to ParticipationService
		ClientResponse response = webResource.path("/route")
				.queryParam("startLatitude", "52505147")
				.queryParam("startLongitude", "13303477")
				.queryParam("destLatitude", "52390930")
				.queryParam("destLongitude", "13067169")
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		String textEntity = response.getEntity(String.class);
		int expected = 200;

		assertEquals(expected, response.getClientResponseStatus()
				.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getType().toString());
		assertEquals(expectedText, textEntity);
	}

	@Test
	public void testgetRouteXMLPublicTransportServiceHttpRequestSuccess() {

		String pathToParticipation = "http://localhost:8080/opnv";
		WebResource webResource = client().resource(pathToParticipation);
		// Make a GET request to ParticipationService
		ClientResponse response = webResource.path("/route")
				.queryParam("startLatitude", "52505147")
				.queryParam("startLongitude", "13303477")
				.queryParam("destLatitude", "52390930")
				.queryParam("destLongitude", "13067169")
				.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);

		String textEntity = response.getEntity(String.class);
		int expectedStatus = 200;
		assertEquals(MediaType.APPLICATION_XML, response.getType().toString());
		assertEquals(expectedStatus, response.getClientResponseStatus()
				.getStatusCode());
		// assertEquals(expected, textEntity);
	}
}
