package de.fhg.fokus.mdc.utils.auth;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

public class HeaderHelper {

	private final Logger log = LoggerFactory.getLogger(getClass());

	public HttpEntity<String> generateEntityForGet(HttpHeaders headers) {

		// set headers for the GET request
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		return requestEntity;

	}

	public HttpEntity<MultiValueMap<String, Object>> generateEntityForPostObject(
			HttpHeaders headers, MultiValueMap<String, Object> map) {
		// prepare authentication and media type headers

		// set headers for the POST request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
				map, headers);

		return requestEntity;
	}

	public HttpEntity<MultiValueMap<String, String>> generateEntityForPost(
			HttpHeaders headers, MultiValueMap<String, String> map) {
		// prepare authentication and media type headers

		// set headers for the POST request
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
				map, headers);

		return requestEntity;
	}

	public String extractUsernameFromHeaders(
			javax.ws.rs.core.HttpHeaders authHeaders) {
		List<String> names = new ArrayList<String>();
		names = getUsernameHeaders(authHeaders);
		String username = null;
		try {
			username = names.get(0);
		} catch (Exception e) {
			log.error("no username");
		}
		return username;

	}

	public HttpHeaders convertjaxrsToSpringHeaders(
			javax.ws.rs.core.HttpHeaders authHeaders) {
		org.springframework.http.HttpHeaders springAuthHeaders = new org.springframework.http.HttpHeaders();
		// Get the auth token passed in HTTP header parameters
		List<String> access = new ArrayList<String>();
		List<String> scopes = new ArrayList<String>();
		List<String> names = new ArrayList<String>();

		if (authHeaders.getRequestHeaders().containsKey(
				AuthConstants.HEADER_FIELD_ACCESSTOKEN))
			access = authHeaders.getRequestHeaders().get(
					AuthConstants.HEADER_FIELD_ACCESSTOKEN);

		if (authHeaders.getRequestHeaders().containsKey(
				AuthConstants.HEADER_FIELD_SCOPE))
			scopes = authHeaders.getRequestHeaders().get(
					AuthConstants.HEADER_FIELD_SCOPE);

		names = getUsernameHeaders(authHeaders);

		final String token;
		final String scope;
		final String username;

		try {
			token = access.get(0);
			scope = scopes.get(0);
			username = names.get(0);
			log.debug("helper converting jaxrs headers to spring. token: "
					+ token);
			springAuthHeaders
					.set(AuthConstants.HEADER_FIELD_ACCESSTOKEN, token);
			springAuthHeaders.set(AuthConstants.HEADER_FIELD_SCOPE, scope);
			springAuthHeaders
					.set(AuthConstants.HEADER_FIELD_USERNAME, username);
		} catch (Exception e) {
			log.debug("no token, scope,username");
			// removed the unauthorized exception because turning off the filter
			// required editing the code that way
			// throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		return springAuthHeaders;
	}

	private List<String> getUsernameHeaders(
			javax.ws.rs.core.HttpHeaders authHeaders) {
		List<String> names = new ArrayList<String>();

		if (authHeaders.getRequestHeaders().containsKey(
				AuthConstants.HEADER_FIELD_USERNAME))
			names = authHeaders.getRequestHeaders().get(
					AuthConstants.HEADER_FIELD_USERNAME);
		return names;
	}

	public Builder setHeadersJersey(javax.ws.rs.core.HttpHeaders authHeaders,
			WebResource service) {
		// Get the auth token passed in HTTP header parameters
		List<String> access = new ArrayList<String>();
		List<String> scopes = new ArrayList<String>();
		List<String> names = new ArrayList<String>();

		if (authHeaders.getRequestHeaders().containsKey(
				AuthConstants.HEADER_FIELD_ACCESSTOKEN))
			access = authHeaders.getRequestHeaders().get(
					AuthConstants.HEADER_FIELD_ACCESSTOKEN);

		if (authHeaders.getRequestHeaders().containsKey(
				AuthConstants.HEADER_FIELD_SCOPE))
			scopes = authHeaders.getRequestHeaders().get(
					AuthConstants.HEADER_FIELD_SCOPE);

		names = getUsernameHeaders(authHeaders);

		final String token;
		final String scope;
		final String username;
		try {
			token = access.get(0);
			scope = scopes.get(0);
			username = names.get(0);
			return service
					.header(AuthConstants.HEADER_FIELD_ACCESSTOKEN, token)
					.header(AuthConstants.HEADER_FIELD_SCOPE, scope)
					.header(AuthConstants.HEADER_FIELD_USERNAME, username);
		} catch (Exception e) {
			log.debug("no token, scope,username");
			// removed the unauthorized exception because turning off the filter
			// required editing the code that way
			// throw new WebApplicationException(Status.UNAUTHORIZED);
		}
		return service.header(AuthConstants.HEADER_FIELD_ACCESSTOKEN, "")
				.header(AuthConstants.HEADER_FIELD_SCOPE, "")
				.header(AuthConstants.HEADER_FIELD_USERNAME, "");
	}

}
