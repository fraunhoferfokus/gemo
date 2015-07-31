package de.fhg.fokus.oauth.endpoints;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import de.fhg.fokus.data.DataAccess;
import de.fhg.fokus.utils.MyHttpServletRequest;
import de.fhg.fokus.utils.Util;

/**
 *
 *
 *
 */
@Path("/token")
/*
 * TODO generate ID token, too (uses JWT) TODO use:
 * https://bitbucket.org/nimbusds/nimbus-jose-jwt/wiki/Home page contains
 * example, should be sufficient
 */
@ManagedBean
public class TokenEndpoint {
	@Inject
	DataAccess database;

	public static final String INVALID_CLIENT_DESCRIPTION = "Client authentication failed (e.g., unknown client, no client authentication included, or unsupported authentication method).";

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response authorize(MultivaluedMap<String, String> params)
			throws OAuthSystemException {
		try {
			// dirty fix to use request and MultivaluedMap together
			MyHttpServletRequest request = new MyHttpServletRequest(params);
			OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(
					new MD5Generator());

			if (!Util.isValidGrantTypeValue(oauthRequest.getGrantType())) {

				return Util.buildErrorResponse(
						HttpServletResponse.SC_BAD_REQUEST,
						OAuthError.TokenResponse.INVALID_GRANT,
						"invalid grant provided");

			}

			if (!Util.isValidScopeValue(oauthRequest
					.getParam(OAuth.OAUTH_SCOPE))
					|| !database.isValidScope(oauthRequest
							.getParam(OAuth.OAUTH_SCOPE))) {
				return Util.buildErrorResponse(
						HttpServletResponse.SC_BAD_REQUEST,
						OAuthError.TokenResponse.INVALID_SCOPE,
						"no scope or invalid scope provided");
			}

			// check if clientid is valid
			if (!Util.isValidClientIDValue(oauthRequest.getClientId())
					|| !database.isValidClientId(oauthRequest.getClientId())) {
				// return buildInvalidClientIdResponse();
				return Util.buildErrorResponse(
						HttpServletResponse.SC_BAD_REQUEST,
						OAuthError.TokenResponse.INVALID_CLIENT,
						INVALID_CLIENT_DESCRIPTION);
			}

			// check if client_secret is valid
			if (!Util.isValidClientSecretValue(oauthRequest.getClientSecret())
					|| !database.isAuthenticatedClient(
							oauthRequest.getClientId(),
							oauthRequest.getClientSecret())) {
				return Util.buildErrorResponse(
						HttpServletResponse.SC_BAD_REQUEST,
						OAuthError.TokenResponse.INVALID_CLIENT,
						INVALID_CLIENT_DESCRIPTION);
			}

			String refreshToken = null;
			String username = null;
			// do checking for different grant types
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.AUTHORIZATION_CODE.toString())) {
				if (!Util.isValidCodeValue(oauthRequest.getCode())
						|| !database.isValidAuthCode(
								oauthRequest.getParam(OAuth.OAUTH_CODE),
								oauthRequest.getClientId(),
								oauthRequest.getRedirectURI(),
								oauthRequest.getParam(OAuth.OAUTH_SCOPE))) {
					return Util.buildErrorResponse(
							HttpServletResponse.SC_BAD_REQUEST,
							OAuthError.TokenResponse.INVALID_GRANT,
							"invalid authorization code");
				} else {
					// valid authcode
					// invalidate it
					database.invalidateAuthCode(oauthRequest
							.getParam(OAuth.OAUTH_CODE));
					username = database.getUserNameForAuthCode(oauthRequest
							.getParam(OAuth.OAUTH_CODE));
					refreshToken = oauthIssuerImpl.refreshToken();
					database.saveRefreshToken(refreshToken,
							oauthRequest.getClientId(),
							oauthRequest.getParam(OAuth.OAUTH_SCOPE), username);

				}
			} else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.REFRESH_TOKEN.toString())) {
				// check if user supplied refresh token is valid
				if (!Util.isValidRefreshTokenValue(oauthRequest
						.getRefreshToken())
						|| !database.isValidRefreshToken(oauthRequest
								.getClientId(), oauthRequest
								.getParam(OAuth.OAUTH_REFRESH_TOKEN),
								oauthRequest.getParam(OAuth.OAUTH_SCOPE))) {
					return Util.buildErrorResponse(
							HttpServletResponse.SC_BAD_REQUEST,
							OAuthError.TokenResponse.INVALID_GRANT,
							"invalid or expired refresh token");
				} else {
					// valid refresh token
					username = database.getUserNameForRefreshToken(oauthRequest
							.getParam(OAuth.OAUTH_REFRESH_TOKEN));
				}
			} else {
				OAuthResponse response = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(
								OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE)
						.setErrorDescription("grant type not supported")
						.buildJSONMessage();
				return Response.status(response.getResponseStatus())
						.entity(response.getBody()).build();
			}

			final String accessToken = oauthIssuerImpl.accessToken();
			database.saveAccessToken(accessToken,
					oauthRequest.getParam(OAuth.OAUTH_SCOPE), username);

			// if authcode, send refreshToken, too
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.AUTHORIZATION_CODE.toString())) {
				OAuthResponse response = OAuthASResponse
						.tokenResponse(HttpServletResponse.SC_OK)
						.setAccessToken(accessToken).setExpiresIn("3600")
						.setRefreshToken(refreshToken)
						.setTokenType(OAuth.DEFAULT_TOKEN_TYPE.toString())
						.setScope(oauthRequest.getParam(OAuth.OAUTH_SCOPE))
						.setParam("username", username).buildJSONMessage();
				return Response.status(response.getResponseStatus())
						.entity(response.getBody()).build();
			} else {
				OAuthResponse response = OAuthASResponse
						.tokenResponse(HttpServletResponse.SC_OK)
						.setAccessToken(accessToken).setExpiresIn("3600")
						.setScope(oauthRequest.getParam(OAuth.OAUTH_SCOPE))
						.setParam("username", username).buildJSONMessage();
				return Response.status(response.getResponseStatus())
						.entity(response.getBody()).build();
			}

		} catch (OAuthProblemException e) {
			OAuthResponse res = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
					.buildJSONMessage();
			return Response.status(res.getResponseStatus())
					.entity(res.getBody()).build();
		}
	}

	@POST
	@Produces("application/json")
	@Path("/validate")
	public Response validate(@HeaderParam("access_token") String accessToken,
			@HeaderParam("scope") String scope,
			@HeaderParam("username") String username) {

		OAuthResponse response;

		try {
			if (database.isValidAccessToken(accessToken, scope, username)) {
				response = OAuthASResponse.status(HttpServletResponse.SC_OK)
						.buildJSONMessage();
				return Response.status(response.getResponseStatus())
						.entity(response.getBody()).build();
			} else {
				return Util.buildErrorResponse(
						HttpServletResponse.SC_FORBIDDEN,
						OAuthError.CodeResponse.ACCESS_DENIED, "invalid token");
			}
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		}
		return null;
	}
}