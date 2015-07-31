package de.fhg.fokus.oauth.endpoints;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.ext.dynamicreg.server.request.JSONHttpServletRequestWrapper;
import org.apache.oltu.oauth2.ext.dynamicreg.server.request.OAuthServerRegistrationRequest;
import org.apache.oltu.oauth2.ext.dynamicreg.server.response.OAuthServerRegistrationResponse;

import de.fhg.fokus.data.DataAccess;

@ManagedBean
@Path("/register")
public class ClientRegistrationEndpoint {
	@Inject
	DataAccess database;
	static final SecureRandom random = new SecureRandom();

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response register(@Context HttpServletRequest request)
			throws OAuthSystemException {

		OAuthServerRegistrationRequest oauthRequest = null;
		try {
			oauthRequest = new OAuthServerRegistrationRequest(
					new JSONHttpServletRequestWrapper(request));
			oauthRequest.discover();
			String client_name = oauthRequest.getClientName();
			String client_url = oauthRequest.getClientUrl();
			String client_description = oauthRequest.getClientDescription();
			String redirect_uri = oauthRequest.getRedirectURI();

			// test input values
			new URL(redirect_uri);
			new URL(client_url);

			if (client_description.length() > 200 || client_name.length() > 50) {
				throw new IllegalArgumentException();
			}

			String client_id = new BigInteger(130, random).toString(32);
			String client_secret = new BigInteger(130, random).toString(32);

			/*
			 * TODO there is no check, if client already registered!! serious
			 * issue, which leads to client impersonation
			 */

			database.saveClient(client_id, client_secret, client_name,
					client_url, client_description, redirect_uri);

			OAuthResponse response = OAuthServerRegistrationResponse
					.status(HttpServletResponse.SC_OK).setClientId(client_id)
					.setClientSecret(client_secret)
					.setIssuedAt(String.valueOf(System.currentTimeMillis()))
					.setExpiresIn("-1").buildJSONMessage();
			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();

		} catch (OAuthProblemException e) {
			OAuthResponse response = OAuthServerRegistrationResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
					.buildJSONMessage();
			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();
		} catch (MalformedURLException ex) {
			OAuthResponse response = OAuthServerRegistrationResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
					.setError("invalid URL provided").buildJSONMessage();
			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();
		} catch (IllegalArgumentException ex) {
			OAuthResponse response = OAuthServerRegistrationResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
					.setError("invalid client information").buildJSONMessage();
			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();
		}

	}
}