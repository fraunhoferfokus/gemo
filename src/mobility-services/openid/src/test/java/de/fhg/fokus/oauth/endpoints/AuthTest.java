package de.fhg.fokus.oauth.endpoints;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import junit.framework.Assert;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.experimental.categories.Category;

import de.fhg.fokus.IntegrationTest;

@Category(IntegrationTest.class)
public class AuthTest {

	private String url = new String("http://localhost:8080/openid");
	private Client client = JerseyClientBuilder.newClient();

	// @Test
	public void authorizationRequest() {
		try {
			Response response = makeAuthCodeRequest();
			System.out.println(response.getEntity().toString());
			Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());

			String authCode = getAuthCode(response);
			Assert.assertNotNull(authCode);
			System.out.println("Authcode:" + authCode);
		} catch (OAuthSystemException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (JSONException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	// @Test
	public void authCodeTokenRequest() throws OAuthSystemException {
		try {
			Response response = makeAuthCodeRequest();
			Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());

			String authCode = getAuthCode(response);
			Assert.assertNotNull(authCode);
			OAuthAccessTokenResponse oauthResponse = makeTokenRequestWithAuthCode(authCode);
			assertNotNull(oauthResponse.getAccessToken());
			assertNotNull(oauthResponse.getExpiresIn());
			System.out.println("Access Token: "
					+ oauthResponse.getAccessToken());
			System.out.println("Expires in: " + oauthResponse.getExpiresIn());
		} catch (OAuthSystemException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (JSONException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (OAuthProblemException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	// @Test
	public void endToEndWithAuthCode() {
		try {
			Response response = makeAuthCodeRequest();
			Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());

			String authCode = getAuthCode(response);
			Assert.assertNotNull(authCode);

			OAuthAccessTokenResponse oauthResponse = makeTokenRequestWithAuthCode(authCode);
			String accessToken = oauthResponse.getAccessToken();

			URL restUrl = new URL(url.toString() + "/resource");
			WebTarget target = client.target(restUrl.toURI());
			String entity = target.request(MediaType.TEXT_HTML)
					.header("Authorization", "Bearer " + accessToken)
					.get(String.class);
		} catch (MalformedURLException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (OAuthSystemException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (URISyntaxException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (JSONException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (OAuthProblemException ex) {
			Logger.getLogger(AuthTest.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	private Response makeAuthCodeRequest() throws OAuthSystemException,
			URISyntaxException {
		OAuthClientRequest request = OAuthClientRequest
				.authorizationLocation(url.toString() + "/authz")
				.setClientId("8dh9cpkijndku0i1hbogeckdio")
				.setRedirectURI(url.toString() + "/redirect")
				.setResponseType(ResponseType.CODE.toString())
				.setState("state").setScope("read write")
				.setParameter("username", "asdfasdf2")
				.setParameter("token", "token1").buildQueryMessage();
		System.out.println(request.getLocationUri());
		WebTarget target = client.target(new URI(request.getLocationUri()));

		System.out.println("1");
		Builder builder = target.request();
		System.out.println("2");
		Response response = builder.get();
		System.out.println("3");
		return response;
	}

	private String getAuthCode(Response response) throws JSONException {
		JSONObject obj = new JSONObject((String) response.getEntity());
		JSONObject qp = obj.getJSONObject("queryParameters");
		String authCode = null;
		if (qp != null) {
			authCode = qp.getString("code");
		}

		return authCode;
	}

	private OAuthAccessTokenResponse makeTokenRequestWithAuthCode(
			String authCode) throws OAuthProblemException, OAuthSystemException {
		OAuthClientRequest request = OAuthClientRequest
				.tokenLocation(url.toString() + "/token")
				.setClientId("8dh9cpkijndku0i1hbogeckdio")
				.setClientSecret("d3qh6i5gteunjp5qi7dsop34lt")
				.setGrantType(GrantType.AUTHORIZATION_CODE).setCode(authCode)
				.setRedirectURI(url.toString() + "/redirect")
				.setParameter("username", "asdfasdf2")
				.setParameter("token", "token1").buildBodyMessage();
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
		OAuthAccessTokenResponse oauthResponse = oAuthClient
				.accessToken(request);
		return oauthResponse;
	}
}