package de.fhg.fokus.mdc.clients;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import de.fhg.fokus.mdc.jsonObjects.ReservationSegment;
import de.fhg.fokus.mdc.payments.exceptions.ReservationSegmentsNotFoundException;
import de.fhg.fokus.mdc.propertyProvider.Constants;

public class ReservationWebClient extends GemoClient {

	private HttpHeaders authHeadersJersey;
	// ---------------------[ Constructor ]---------------------------

	/** The singleton instances of the class. */
	private static ReservationWebClient instance = null;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static ReservationWebClient getInstance() {
		if (instance == null) {
			instance = new ReservationWebClient();
		}
		return instance;
	}

	private ReservationWebClient() {
		super();
		url = props.getProperty(Constants.SERVICE_URI_RESERVATION_EFZ);
	}

	public void setAuthHeadersJersey(HttpHeaders headers) {
		this.authHeadersJersey = headers;

	}

	// ----------------[ business/interface logic ]----------------------

	/**
	 * grabs the reservation segments by reservation ID
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public List<ReservationSegment> requestReservationsSegmentsByReservationSequenceID(
			Integer reservationSequenceID) throws JsonParseException,
			JsonMappingException, IOException {

		List<ReservationSegment> theList = new ArrayList<ReservationSegment>();

		try {

			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getFeatures().put(
					JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
			String uri = url + "/reservations/" + reservationSequenceID
					+ "/segments";
			Client client = Client.create(clientConfig);
			// changed to below version to add headers
			// List<ReservationSegment> expectedReservationSegments = client
			// .resource(uri).get(
			// new GenericType<List<ReservationSegment>>() {
			// });

			WebResource service = client.resource(uri);
			Builder builder = headerHelper.setHeadersJersey(authHeadersJersey,
					service);

			List<ReservationSegment> expectedReservationSegments = builder
					.get(new GenericType<List<ReservationSegment>>() {
					});
			theList = expectedReservationSegments;
		} catch (UniformInterfaceException uie) {
			if (uie.getResponse().getStatus() == ClientResponse.Status.NOT_FOUND
					.getStatusCode())
				throw new ReservationSegmentsNotFoundException(
						reservationSequenceID);
		}

		return theList;
	}
}
