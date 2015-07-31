package de.fhg.fokus.mdc.clients;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import de.fhg.fokus.mdc.exceptions.ServiceFailedException;
import de.fhg.fokus.mdc.propertyProvider.Constants;
import de.fhg.fokus.mdc.serviceReservierungLadestation.services.lib.Consts;
import de.fhg.fokus.mdc.utils.SimpleValidator;

/**
 * Basic Web-Client for the communication with the availability service
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class ChargingStationAvailabilityClient extends GemoClient {

	private final String query;
	private HttpHeaders authHeadersJersey;
	// ---------------------[ Constructor ]---------------------------

	/** The singleton instances of the class. */
	private static ChargingStationAvailabilityClient instance = null;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static ChargingStationAvailabilityClient getInstance() {
		if (instance == null) {
			instance = new ChargingStationAvailabilityClient();
		}
		return instance;
	}

	private ChargingStationAvailabilityClient() {
		super();
		url = props
				.getProperty(Constants.SERVICE_URI_AVAILABILITY_CHARGING_POINT);
		restTemplate = new RestTemplate();
		query = props
				.getProperty(Constants.SERVICE_URI_AVAILABILITY_CHARGING_POINT_QUERY);

	}

	// ----------------[ business/interface logic ]----------------------
	/**
	 * return true if available
	 * 
	 * @param chargingstationID
	 * @param resbegin
	 * @param resend
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public boolean requestChargingStationAvailability(int chargingstationID,
			Date resbegin, Date resend) throws JsonParseException,
			JsonMappingException, IOException {
		try {
			// configure the client
			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getFeatures().put(
					JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

			// build the uri
			String uri = url + query + "/station";
			// build the client
			Client client = Client.create(clientConfig);
			WebResource service = client
					.resource(uri)
					.queryParam(Consts.PARAM_CHARGINGSTATIONID,
							"" + chargingstationID)
					.queryParam(
							Consts.PARAM_RESBEGIN,
							new SimpleDateFormat(
									SimpleValidator.DATE_TIME_PATTERN)
									.format(resbegin))
					.queryParam(
							Consts.PARAM_RESEND,
							new SimpleDateFormat(
									SimpleValidator.DATE_TIME_PATTERN)
									.format(resend));
			// Ilke says: or use the spring method below and override spring's
			// responsehandler
			// pass the header information
			Builder builder = headerHelper.setHeadersJersey(authHeadersJersey,
					service);
			// request the response
			ClientResponse response = builder.get(ClientResponse.class);
			// analyse the response
			if (response.getStatus() == 401)
				throw new WebApplicationException(Status.UNAUTHORIZED);
			if (response.getStatus() == 403)
				throw new WebApplicationException(Status.FORBIDDEN);
			return (Response.Status.OK.getStatusCode() == response.getStatus());
		} catch (UniformInterfaceException uie) {
			throw new ServiceFailedException(url);
		}
	}

	public void setAuthHeadersJersey(HttpHeaders headers) {
		this.authHeadersJersey = headers;

	}

	@Deprecated
	public boolean requestChargingStationAvailabilitySpring(
			int chargingstationID, Date resbegin, Date resend) {
		if (authHeaders == null) {
			org.springframework.http.HttpHeaders authHeaders = new org.springframework.http.HttpHeaders();
			this.authHeaders = authHeaders;
		}
		authHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		String path = query + "/station";
		String requestUrl = this.url
				+ path
				+ "?"
				+ Consts.PARAM_CHARGINGSTATIONID
				+ "="
				+ chargingstationID
				+ "&"
				+ Consts.PARAM_RESBEGIN
				+ "="
				+ new SimpleDateFormat(SimpleValidator.DATE_TIME_PATTERN)
						.format(resbegin)
				+ "&"
				+ Consts.PARAM_RESEND
				+ "="
				+ new SimpleDateFormat(SimpleValidator.DATE_TIME_PATTERN)
						.format(resend);
		HttpEntity<String> requestEntity = headerHelper
				.generateEntityForGet(authHeaders);
		ResponseEntity<String> response = restTemplate.exchange(requestUrl,
				HttpMethod.GET, requestEntity, String.class);
		return (response.getStatusCode().compareTo(HttpStatus.OK) == 0);
	}

}