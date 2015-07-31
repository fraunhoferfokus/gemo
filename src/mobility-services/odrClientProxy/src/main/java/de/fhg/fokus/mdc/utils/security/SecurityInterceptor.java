package de.fhg.fokus.mdc.utils.security;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interceptor verify the access permissions for a user based on username
 * and passowrd provided in request
 * */
@Provider
public class SecurityInterceptor implements
		javax.ws.rs.container.ContainerRequestFilter {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void filter(ContainerRequestContext requestContext) {
		Request request = requestContext.getRequest();
		String method = request.getMethod();
		System.out.println("request method: " + method);

		String path = requestContext.getUriInfo().getPath();
		URI absPath = requestContext.getUriInfo().getAbsolutePath();
		MultivaluedMap<String, String> queryParams = requestContext
				.getUriInfo().getQueryParameters();

		// We do allow wadl to be retrieved
		if (method.equals("GET")
				&& (path.equals("application.wadl") || path
						.equals("application.wadl/xsd0.xsd"))) {
			return;
		}

		// Get the auth token passed in HTTP header parameters
		List<String> access = new ArrayList<String>();
		List<String> scopes = new ArrayList<String>();
		List<String> names = new ArrayList<String>();

		if (requestContext.getHeaders().containsKey("accessToken"))
			access = requestContext.getHeaders().get("accessToken");

		if (requestContext.getHeaders().containsKey("scope"))
			scopes = requestContext.getHeaders().get("scope");

		if (requestContext.getHeaders().containsKey("username"))
			names = requestContext.getHeaders().get("username");

		final String token;
		final String scope;
		final String username;
		try {
			token = access.get(0);
			scope = scopes.get(0);
			username = names.get(0);
		} catch (Exception e) {
			log.debug("no token, scope,username");
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		// if (token == null || token.equals("") || scope == null
		// || scope.equals("") || username == null || username.equals("")) {
		// requestContext.abortWith(UNAUTHORIZED);
		// return;
		// }
		return;
	}
}