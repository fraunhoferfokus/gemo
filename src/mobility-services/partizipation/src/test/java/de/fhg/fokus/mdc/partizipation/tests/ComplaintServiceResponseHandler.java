package de.fhg.fokus.mdc.partizipation.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;

public class ComplaintServiceResponseHandler implements HttpRequestHandler {

	private static Map<String, ComplaintServiceResponse> responses = new HashMap<String, ComplaintServiceResponse>();
	static {

		// construct response list
		List<ComplaintServiceResponse> responseList = new LinkedList<ComplaintServiceResponse>();

		responseList.add(new ComplaintServiceResponse(
				"/fixmystadt-service/cm/complaints/1", "GET", "complaint.xml",
				200));
		responseList.add(new ComplaintServiceResponse(
				"/fixmystadt-service/cm/complaints", "POST",
				"complaintCreated", 200));
		// transform list to map
		for (ComplaintServiceResponse mockResponse : responseList) {
			responses.put(
					mockResponse.getExpectedMethod() + ":"
							+ mockResponse.getExpectedUri(), mockResponse);
		}

	}

	@Override
	public void handle(HttpRequest request, HttpResponse response,
			HttpContext context) throws HttpException, IOException {
		System.out.println("\n\n handle in handler ");
		// pass to appropriate method based on request Uri
		String requestUri = request.getRequestLine().getUri();
		String method = request.getRequestLine().getMethod().toUpperCase();

		System.out.println("auth header"
				+ request.getHeaders("Authorization").toString());

		HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
		String str = EntityUtils.toString(entity);
		System.out.println("request entity : \n" + str);
		// find the correct reply
		ComplaintServiceResponse mockResponse = responses.get(method + ":"
				+ requestUri);

		if (mockResponse != null && mockResponse.checkIfRequestMatches(request)) {
			System.out.println("responding to method: "
					+ mockResponse.getExpectedMethod());
			System.out.println("responding to uri: "
					+ mockResponse.getExpectedUri());
			mockResponse.populateHttpResponse(response);
		} else {
			// all other requests return NotFound
			handleNotFound(request, response, context);
		}

	}

	/**
	 * Sends an 404 reply.
	 */
	private void handleNotFound(HttpRequest request, HttpResponse response,
			HttpContext context) throws HttpException, IOException {

		response.setStatusCode(HttpStatus.SC_NOT_FOUND);
		StringEntity entity = new StringEntity("Not Found", ContentType.create(
				"text/html", "UTF-8"));
		response.setEntity(entity);

	}

}
