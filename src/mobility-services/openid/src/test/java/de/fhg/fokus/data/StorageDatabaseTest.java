package de.fhg.fokus.data;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import de.fhg.fokus.data.StorageDatabase;

public class StorageDatabaseTest {
	static Logger logger = Logger
			.getLogger(StorageDatabaseTest.class.getName());
	static StorageDatabase database;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);

	@BeforeClass
	public static void setUp() {
		database = new StorageDatabase();
		database.setSTORAGE_INSERT_PATH("http://localhost:8089/storage/insert");
		database.setSTORAGE_SEARCH_PATH("http://localhost:8089/storage/search");
		database.setSTORAGE_UPDATE_PATH("http://localhost:8089/storage/update");
	}

	@Test
	public void isValidRefreshTokenTest() {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("username", "asdfasdf2");
		jsonMap.put("valid_until", "1411660128763");
		jsonMap.put("scope", "res_r");
		jsonMap.put("refresh_token", "581b9ec5a4c44846601561f7db36834d");
		jsonMap.put("client_id", "8dh9cpkijndku0i1hbogeckdio");
		jsonMap.put("_id", "14");

		JSONArray arr = new JSONArray();
		arr.put(jsonMap);

		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(arr.toString())));

		boolean result = database.isValidRefreshToken(
				"8dh9cpkijndku0i1hbogeckdio",
				"581b9ec5a4c44846601561f7db36834d", "res_r");

		assertTrue(result);
	}

	@Test
	public void isValidRefreshTokenWithInvalidClientTest() {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("username", "asdfasdf2");
		jsonMap.put("valid_until", "1411660128763");
		jsonMap.put("scope", "res_r");
		jsonMap.put("refresh_token", "581b9ec5a4c44846601561f7db36834d");
		jsonMap.put("client_id", "8dh9cpkijndku0i1hbogeckdio");
		jsonMap.put("_id", "14");

		JSONArray arr = new JSONArray();
		arr.put(jsonMap);

		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(arr.toString())));

		// with an invalid client id method returns false
		boolean result = database.isValidRefreshToken("invalid_client",
				"581b9ec5a4c44846601561f7db36834d", "res_r");

		assertFalse(result);
	}

	@Test
	public void isValidRefreshTokenWithWronScopeTest() {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("username", "asdfasdf2");
		jsonMap.put("valid_until", "1411660128763");
		jsonMap.put("scope", "res_r");
		jsonMap.put("refresh_token", "581b9ec5a4c44846601561f7db36834d");
		jsonMap.put("client_id", "8dh9cpkijndku0i1hbogeckdio");
		jsonMap.put("_id", "14");

		JSONArray arr = new JSONArray();
		arr.put(jsonMap);

		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(arr.toString())));

		// same with wrong scope
		boolean result = database.isValidRefreshToken(
				"8dh9cpkijndku0i1hbogeckdio",
				"581b9ec5a4c44846601561f7db36834d", "res_w");

		assertFalse(result);

	}

	@Test
	public void isValidRefreshTokenWithEmptyReturnTest() {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("username", "asdfasdf2");
		jsonMap.put("valid_until", "1411660128763");
		jsonMap.put("scope", "res_r");
		jsonMap.put("refresh_token", "581b9ec5a4c44846601561f7db36834d");
		jsonMap.put("client_id", "8dh9cpkijndku0i1hbogeckdio");
		jsonMap.put("_id", "14");

		JSONArray arr = new JSONArray();
		arr.put(jsonMap);

		// test empty return
		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("[{}]")));

		boolean result = database.isValidRefreshToken(
				"8dh9cpkijndku0i1hbogeckdio",
				"581b9ec5a4c44846601561f7db36834d", "res_r");

		assertFalse(result);
	}

	@Test
	public void isAuthenticatedClientTest() {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("client_id", "8dh9cpkijndku0i1hbogeckdio");
		jsonMap.put("client_description", "asdf2");
		jsonMap.put("redirect_uri", "http://localhost:8080");
		jsonMap.put("client_name", "asdf");
		jsonMap.put("hashed_client_secret",
				"$2a$10$4AGAHaW52BZWvkxfz8Iu4.JB6CwpJasGU0MS2K7FjPwsCWu/H6O/C");
		jsonMap.put("client_url", "google.de");

		JSONArray arr = new JSONArray();
		arr.put(jsonMap);
		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(arr.toString())));

		boolean result = database.isAuthenticatedClient(
				"8dh9cpkijndku0i1hbogeckdio", "d3qh6i5gteunjp5qi7dsop34lt");

		assertTrue(result);

	}

	@Test
	public void isAuthenticatedClientWithWrongPWTest() {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("client_id", "8dh9cpkijndku0i1hbogeckdio");
		jsonMap.put("client_description", "asdf2");
		jsonMap.put("redirect_uri", "http://localhost:8080");
		jsonMap.put("client_name", "asdf");
		jsonMap.put("hashed_client_secret",
				"$2a$10$4AGAHaW52BZWvkxfz8Iu4.JB6CwpJasGU0MS2K7FjPwsCWu/H6O/C");
		jsonMap.put("client_url", "google.de");

		JSONArray arr = new JSONArray();
		arr.put(jsonMap);
		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(arr.toString())));

		boolean result = database.isAuthenticatedClient(
				"8dh9cpkijndku0i1hbogeckdio", "wrong_pw");

		assertFalse(result);
	}

	@Test
	public void isValidAccessTokenTest() {

		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("username", "asdfasdf2");
		jsonMap.put("valid_until", "1811660128763"); // valid_until lays in the
														// far future
		jsonMap.put("scope", "res_r");
		jsonMap.put("access_token", "f222a9a7d569d4e01a42a1fd77d70dd4");

		JSONArray arr = new JSONArray();
		arr.put(jsonMap);
		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(arr.toString())));

		boolean result = database.isValidAccessToken(
				"f222a9a7d569d4e01a42a1fd77d70dd4", "res_r", "asdfasdf2");

		assertTrue(result);

		// wrong scope
		result = database.isValidAccessToken(
				"f222a9a7d569d4e01a42a1fd77d70dd4", "res_w", "asdfasdf2");

		assertFalse(result);

		// wrong user name
		result = database.isValidAccessToken(
				"f222a9a7d569d4e01a42a1fd77d70dd4", "res_r", "asasdfasdf2");

		assertFalse(result);
	}

	@Test
	public void isValidAccessTokenWithWrongScopeTest() {

		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("username", "asdfasdf2");
		jsonMap.put("valid_until", "1811660128763"); // valid_until lays in the
		// far future
		jsonMap.put("scope", "res_r");
		jsonMap.put("access_token", "f222a9a7d569d4e01a42a1fd77d70dd4");

		JSONArray arr = new JSONArray();
		arr.put(jsonMap);
		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(arr.toString())));

		// wrong scope
		boolean result = database.isValidAccessToken(
				"f222a9a7d569d4e01a42a1fd77d70dd4", "res_w", "asdfasdf2");

		assertFalse(result);
	}

	@Test
	public void isValidAccessTokenWithWrongUsernameTest() {

		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("username", "asdfasdf2");
		jsonMap.put("valid_until", "1811660128763"); // valid_until lays in the
		// far future
		jsonMap.put("scope", "res_r");
		jsonMap.put("access_token", "f222a9a7d569d4e01a42a1fd77d70dd4");

		JSONArray arr = new JSONArray();
		arr.put(jsonMap);
		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(arr.toString())));

		// wrong user name
		boolean result = database.isValidAccessToken(
				"f222a9a7d569d4e01a42a1fd77d70dd4", "res_r", "asasdfasdf2");

		assertFalse(result);
	}

	@Test
	public void isValidAuthCodeTest() {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("username", "asdfasdf2");
		jsonMap.put("scope", "res_r res_w");
		jsonMap.put("_id", "55");
		jsonMap.put("redirect_uri", "http://localhost:8080/openid/redirect");
		jsonMap.put("auth_code", "80c4f9ade44a408d76dec4cdb738959b");
		jsonMap.put("valid_until", "1895849744152");
		jsonMap.put("client_id", "8dh9cpkijndku0i1hbogeckdio");

		JSONArray arr = new JSONArray();
		arr.put(jsonMap);

		stubFor(post(urlMatching("/storage/update.*")).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type",
						"application/json")));

		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(arr.toString())));

		boolean result = database.isValidAuthCode(
				"80c4f9ade44a408d76dec4cdb738959b",
				"8dh9cpkijndku0i1hbogeckdio",
				"http://localhost:8080/openid/redirect", "res_r res_w");

		assertTrue(result);

		// wrong scope
		result = database.isValidAuthCode("80c4f9ade44a408d76dec4cdb738959b",
				"8dh9cpkijndku0i1hbogeckdio",
				"http://localhost:8080/openid/redirect", "res_R");

		assertFalse(result);

		// wrong client_id name
		result = database.isValidAuthCode("80c4f9ade44a408d76dec4cdb738959b",
				"wrong_client_id", "http://localhost:8080/openid/redirect",
				"res_r res_w");

		assertFalse(result);

		// wrong redirect_uri
		result = database.isValidAuthCode("80c4f9ade44a408d76dec4cdb738959b",
				"8dh9cpkijndku0i1hbogeckdio", "redirect_is_wrong",
				"res_r res_w");

		assertFalse(result);
	}

	@Test
	/**
	 * @see Database.isValidClientId()
	 */
	public void isValidClientIdTest() {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("client_id", "8dh9cpkijndku0i1hbogeckdio");
		jsonMap.put("client_description", "asdf2");
		jsonMap.put("redirect_uri", "http://localhost:8080");
		jsonMap.put("client_name", "asdf");
		jsonMap.put("hashed_client_secret",
				"$2a$10$4AGAHaW52BZWvkxfz8Iu4.JB6CwpJasGU0MS2K7FjPwsCWu/H6O/C");
		jsonMap.put("client_url", "google.de");

		JSONArray arr = new JSONArray();
		arr.put(jsonMap);

		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(arr.toString())));

		boolean result = database.isValidClientId("8dh9cpkijndku0i1hbogeckdio");

		assertTrue(result);

		arr = new JSONArray();

		stubFor(get(urlMatching("/storage/search.*")).willReturn(
				aResponse().withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(arr.toString())));
		result = database.isValidClientId("wrong_client_id");

		assertFalse(result);
	}
}
