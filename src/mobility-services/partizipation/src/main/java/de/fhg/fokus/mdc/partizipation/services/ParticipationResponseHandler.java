package de.fhg.fokus.mdc.partizipation.services;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

public class ParticipationResponseHandler implements ResponseErrorHandler {
	private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
	/** The logger of the class. */
	private static Logger log = Logger.getLogger(ParticipationService.class
			.getName());

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return errorHandler.hasError(response);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		log.info("Response from fixmycity service : "
				+ response.getStatusCode() + response.getStatusText());

		// try {
		//
		// errorHandler.handleError(response);
		//
		// } catch (RestClientException scx) {
		// throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
		// "No results returned for the query");
		// }
	}

}
