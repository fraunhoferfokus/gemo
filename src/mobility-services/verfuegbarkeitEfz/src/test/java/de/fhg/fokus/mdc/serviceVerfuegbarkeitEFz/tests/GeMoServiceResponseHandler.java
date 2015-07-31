package de.fhg.fokus.mdc.serviceVerfuegbarkeitEFz.tests;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

public class GeMoServiceResponseHandler implements HttpRequestHandler {

	private static Map<String, GeMoServiceResponse> responses = new HashMap<String, GeMoServiceResponse>();
	static {

		// construct response list
		List<GeMoServiceResponse> responseList = new LinkedList<GeMoServiceResponse>();

		// responses for GeMo RESTful Services tests
		responseList.add(new GeMoServiceResponse(
				"/storage/search?query=sqlstring", "GET", "vehicleStatus.json",
				200));
		URI uri;
		try {
			uri = new URI(null, null,
					"select status from efzavailability where efzid=\'6666'",
					null);
			String uriToRespond = uri.toString();
			// System.out.println("url to respond to : " + uriToRespond);
			responseList.add(new GeMoServiceResponse("/storage/search?query="
					+ uriToRespond, "GET", "vehicleStatus.json", 200));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// or String request = uri.toString();
		responseList.add(new GeMoServiceResponse("/storage/update", "POST",
				"okPost", 200));
		responseList.add(new GeMoServiceResponse("/storage/update/fail",
				"POST", "failPost", 200));

		// transform list to map
		for (GeMoServiceResponse mockResponse : responseList) {
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
		System.out.println("request string " + request.toString() + "\n");
		// System.out.println("auth header"
		// + request.getHeaders("Authorization").toString());

		HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
		String str = EntityUtils.toString(entity);
		System.out.println("entity : " + str);
		// find the correct reply
		GeMoServiceResponse mockResponse = responses.get(method + ":"
				+ requestUri);

		if (mockResponse != null && mockResponse.checkIfRequestMatches(request)) {
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
