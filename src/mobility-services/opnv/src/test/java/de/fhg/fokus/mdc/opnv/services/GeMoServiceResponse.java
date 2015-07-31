package de.fhg.fokus.mdc.opnv.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class GeMoServiceResponse {
	// private fields
	private final String expectedUri;
	private final String expectedMethod;
	private final String responseFile;
	private final int responseStatus;

	/**
	 * Creates a new mock response, by reading the given file and getting its
	 * contents as a string.
	 * 
	 * @param expectedUri
	 *            the URL to reply to, incl. query parameters
	 * @param expectedMethod
	 *            the expected method, i.e. GET, POST, etc. as a string
	 * @param responseFile
	 *            the file to open, in order to get the reply that will be sent.
	 * @param responseStatus
	 *            the status to return when replying to the request
	 */
	public GeMoServiceResponse(String expectedUrl, String expectedMethod, String responseFile, int responseStatus) {

		this.expectedUri = expectedUrl;
		this.expectedMethod = expectedMethod.toUpperCase();
		this.responseStatus = responseStatus;
		this.responseFile = responseFile;

	}

	/**
	 * Checks if the request matches the expected input (authentication key and
	 * HTTP method).
	 * 
	 * @param request
	 *            the HTTP request
	 * @return true if everything matches, false otherwise
	 */
	public boolean checkIfRequestMatches(HttpRequest request) {

		String givenUri = request.getRequestLine().getUri();
		String givenMethod = request.getRequestLine().getMethod().toUpperCase();

		if (givenUri.equals(expectedUri) == false) {
			return false;
		}
		if (givenMethod.equals(expectedMethod) == false) {
			return false;
		}

		// all tests passed
		return true;
	}

	/** checks if parameters are correct **/

	/**
	 * Fills the response with the data that should be grabbed.
	 */
	public void populateHttpResponse(HttpResponse response) throws IOException {

		String responseBody = "";
		if (responseFile.isEmpty() == false) {
			// String filePath = FileUtils.combine("http_replies",
			// responseFile);
			String filePath = "http_replies/" + responseFile;

			ClassLoader classLoader = GeMoServiceResponse.class.getClassLoader();
			InputStream fileStream = classLoader.getResourceAsStream(filePath);

			StringWriter writer = new StringWriter();
			IOUtils.copy(fileStream, writer, "utf-8");

			responseBody = writer.toString();
			writer.close();
			fileStream.close();
		}
		response.setStatusCode(responseStatus);

		StringEntity entity = new StringEntity(responseBody, ContentType.create("application/json", "UTF-8"));
		response.setEntity(entity);

	}

	// accessors

	/**
	 * Returns the expected URI.
	 */
	public String getExpectedUri() {
		return expectedUri;
	}

	/**
	 * Returns the expected method, in upper case.
	 */
	public String getExpectedMethod() {
		return expectedMethod;
	}

	/**
	 * Returns the filename of the file that holds the response body.
	 */
	public String getResponseFile() {
		return responseFile;
	}

	/**
	 * Returns the status code that will be returned.
	 */
	public int getResponseStatus() {
		return responseStatus;
	}

}
