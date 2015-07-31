/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhg.fokus.oauth.endpoints;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Properties;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import de.fhg.fokus.data.DataAccess;
import de.fhg.fokus.mdc.propertyProvider.Constants;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.utils.Util;

@ManagedBean
@Path("/authz")
public class AuthzEndpoint {
	@Inject
	DataAccess database;

	private static final String SERVICE_URL;

	static {
		Properties props = null;
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// configure return_to_url
		SERVICE_URL = props.getProperty(Constants.SERVICE_URL);
	}

	@Context
	ServletContext context;

	@GET
	public Response authorize(@Context HttpServletRequest request,
			@Context HttpServletResponse resp) throws URISyntaxException,
			OAuthSystemException, IOException {
		try {
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
			OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(
					new MD5Generator());

			// enforce scope
			// TODO scope should contain 'openid' because we use OpenID Connect
			String scope = oauthRequest.getParam(OAuth.OAUTH_SCOPE);
			if ("".equals(scope) || scope == null
					|| !Util.isValidScopeValue(scope)
					|| !database.isValidScope(scope)) {

				return Util.buildErrorResponse(
						HttpServletResponse.SC_BAD_REQUEST,
						OAuthError.CodeResponse.INVALID_SCOPE,
						"no scope or invalid scope provided");
			}

			// enforce valid client id
			if (!Util.isValidClientIDValue(oauthRequest.getClientId())) {
				return Util.buildErrorResponse(
						HttpServletResponse.SC_BAD_REQUEST,
						OAuthError.OAUTH_ERROR, "invalid client id");
			}
			// enforce valid state (if given)
			if (oauthRequest.getState() != null) {
				if (!Util.isValidStateValue(oauthRequest.getState())) {
					return Util.buildErrorResponse(
							HttpServletResponse.SC_BAD_REQUEST,
							OAuthError.OAUTH_ERROR, "invalid client id");
				}
			}

			// initiate OpenID
			if (request.getParameter("token") == null) {
				RequestDispatcher dispatcher = context
						.getRequestDispatcher("/");
				try {
					dispatcher.forward(request, resp);
					return null;
				} catch (ServletException e) {
					e.printStackTrace();
				}
			} else if (isValidToken(request.getParameter("username"),
					request.getParameter("token"))) {
				if ("reject".equals(request.getParameter("feedback"))) {

					return Util.buildErrorResponse(
							HttpServletResponse.SC_FORBIDDEN,
							OAuthError.CodeResponse.ACCESS_DENIED,
							"resource owner rejected your request.");
				}
				// build response according to response_type
				String responseType = oauthRequest
						.getParam(OAuth.OAUTH_RESPONSE_TYPE);

				OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
						.authorizationResponse(request,
								HttpServletResponse.SC_FOUND);

				if (responseType.equals(ResponseType.CODE.toString())) {
					final String authorizationCode = oauthIssuerImpl
							.authorizationCode();

					database.saveAuthCode(authorizationCode,
							oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID),
							oauthRequest.getRedirectURI(),
							oauthRequest.getParam(OAuth.OAUTH_SCOPE),
							request.getParameter("username"));
					System.out.println("Currently in CODE section");
					builder.setCode(authorizationCode);
					builder.setScope(oauthRequest.getParam("scope"));
				}
				if (responseType.equals(ResponseType.TOKEN.toString())) {
					System.out.println("Currently in TOKEN section");
					final String accessToken = oauthIssuerImpl.accessToken();

					builder.setAccessToken(accessToken);
					builder.setExpiresIn(3600l);
				}

				String redirectURI = oauthRequest.getRedirectURI();
				final OAuthResponse response = builder.location(redirectURI)
						.buildQueryMessage();
				URI url = new URI(response.getLocationUri());
				return Response.status(response.getResponseStatus())
						.location(url).build();
			} else {
				return null;
			}
		} catch (OAuthProblemException e) {
			final Response.ResponseBuilder responseBuilder = Response
					.status(HttpServletResponse.SC_FOUND);
			String redirectUri = e.getRedirectUri();

			if (OAuthUtils.isEmpty(redirectUri)) {
				throw new WebApplicationException(
						responseBuilder
								.entity("OAuth redirection URI needs to be provided by the client.")
								.build());
			}
			final OAuthResponse response = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_FOUND).error(e)
					.location(redirectUri).buildQueryMessage();
			final URI location = new URI(response.getLocationUri());
			return responseBuilder.location(location).build();
		}
		return null;
	}

	private Client hostIgnoringClient() {
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, null, null);
			DefaultClientConfig config = new DefaultClientConfig();
			Map<String, Object> properties = config.getProperties();
			HTTPSProperties httpsProperties = new HTTPSProperties(
					new HostnameVerifier() {
						@Override
						public boolean verify(String s, SSLSession sslSession) {
							return true;
						}
					}, sslcontext);
			properties.put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
					httpsProperties);
			config.getClasses().add(JacksonJsonProvider.class);
			return Client.create(config);
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isValidToken(String username, String token) {
		Client client = hostIgnoringClient();
		WebResource resource = client.resource(SERVICE_URL
				+ "openid/consumer/validate");
		// pass the header information and request the response
		ClientResponse response = resource.header("token", token)
				.header("username", username)
				.header("Authorization", "Basic Zm9rdXM6ZjBrdTU=")
				.post(ClientResponse.class);
		if (response.getStatus() == 200) {
			return true;
		} else {
			return false;
		}

	}
}