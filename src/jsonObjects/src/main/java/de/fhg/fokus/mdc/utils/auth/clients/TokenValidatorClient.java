package de.fhg.fokus.mdc.utils.auth.clients;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TokenValidatorClient {
	Client c = Client.create();
	// TODO get from properties
	static final String TOKEN_PATH = "http://localhost:8080/openid/token/validate";

	// curl -X POST -H "access_token:" -H "scope:reservation" -H
	// "username:developer" http://localhost:8080/openid/token/validate
	public void validateParams(final String token, final String scope,
			final String username, String serviceName)
			throws JsonParseException, JsonMappingException, IOException {
		if (token == null || token.equals("") || scope == null
				|| scope.equals("") || username == null || username.equals("")) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		// check if scope exists for service
		ServiceScopeDataStoreClient dsclient = new ServiceScopeDataStoreClient();
		boolean isScopeValid = dsclient
				.checkScopeforService(scope, serviceName);
		if (!isScopeValid)
			throw new WebApplicationException(Status.FORBIDDEN);
		// ask openid if credentials are valid
		WebResource r = c.resource(TOKEN_PATH);
		ClientResponse response = r.accept(MediaType.APPLICATION_JSON_TYPE)
				.header("access_token", token).header("scope", scope)
				.header("username", username).post(ClientResponse.class);

		if (response.getStatus() == 200)
			return;

		if (response.getStatus() == 403)
			throw new WebApplicationException(Status.FORBIDDEN);

	}

}
