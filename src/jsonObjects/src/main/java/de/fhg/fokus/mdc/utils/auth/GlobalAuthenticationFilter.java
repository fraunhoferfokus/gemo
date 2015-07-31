package de.fhg.fokus.mdc.utils.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import de.fhg.fokus.mdc.utils.auth.clients.TokenValidatorClient;

/**
 * Jersey HTTP Basic Auth filter
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 */
public class GlobalAuthenticationFilter implements ContainerRequestFilter {

	TokenValidatorClient validatorClient = new TokenValidatorClient();
	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Apply the filter : check input request, validate or not with user auth
	 * 
	 * @param request
	 *            The request from Tomcat server
	 */
	public ContainerRequest filter(ContainerRequest request)
			throws WebApplicationException {

		// read method: GET, POST, PUT, DELETE, ...
		String method = request.getMethod();
		// read path: myresource/get/56bCA for example
		String path = request.getPath(true);

		MultivaluedMap<String, String> params = request
				.getQueryParameters(true);
		List<String> queryParamValues = new ArrayList<String>();
		String query = null;

		if (params.containsKey("query")) {
			queryParamValues = params.get("query");
		}

		if (!queryParamValues.isEmpty()) {
			query = queryParamValues.get(0);
		}

		// We do allow wadl to be retrieved
		if (method.equals("GET")
				&& (path.equals("application.wadl") || path
						.equals("application.wadl/xsd0.xsd"))) {
			return request;
		}

		log.debug("the path: " + path);
		log.debug("the query" + query);
		// We allow scope validation requests (temporary)
		if ((query != null) && (path != null)) {
			if (method.equals("GET") && path.equals("search/private")) {
				if (query.matches("select \\* from scopes where(.*)")
						|| query.matches("select \\* from services where(.*)"))
					return request;
			}
		}

		// Get the auth token passed in HTTP header parameters
		final String token = request
				.getHeaderValue(AuthConstants.HEADER_FIELD_ACCESSTOKEN);
		// Get the scope of the request
		final String scope = request
				.getHeaderValue(AuthConstants.HEADER_FIELD_SCOPE);
		// Get the username of the request
		final String username = request
				.getHeaderValue(AuthConstants.HEADER_FIELD_USERNAME);

		log.debug("##############path of base uri##############\n"
				+ request.getBaseUri().getPath());
		String serviceName = request.getBaseUri().getPath();
		serviceName = serviceName.replace("/", "");

		try {
			validatorClient.validateParams(token, scope, username, serviceName);
		} catch (JsonParseException e) {

			e.printStackTrace();
		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return request;
	}
}