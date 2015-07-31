package de.fhg.fokus.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class DirectDatabaseTest {
	static Logger logger = Logger.getLogger(DirectDatabaseTest.class.getName());
	static DirectDatabase database;

	@BeforeClass
	public static void setUp() {
		database = new DirectDatabase();
	}

	@Test
	public void isValidRefreshTokenTest() {
		boolean result = database.isValidRefreshToken(
				"8dh9cpkijndku0i1hbogeckdio",
				"581b9ec5a4c44846601561f7db36834d", "read");

		assertTrue(result);
	}

	@Test
	public void isValidRefreshTokenWithInvalidClientTest() {
		// with an invalid client id method returns false
		boolean result = database.isValidRefreshToken("invalid_client",
				"581b9ec5a4c44846601561f7db36834d", "read");

		assertFalse(result);
	}

	@Test
	public void isValidRefreshTokenWithWrongScopeTest() {
		// same with wrong scope
		boolean result = database.isValidRefreshToken(
				"8dh9cpkijndku0i1hbogeckdio",
				"581b9ec5a4c44846601561f7db36834d", "res_w");

		assertFalse(result);

	}

	@Test
	public void isAuthenticatedClientTest() {
		boolean result = database.isAuthenticatedClient(
				"8dh9cpkijndku0i1hbogeckdio", "d3qh6i5gteunjp5qi7dsop34lt");

		assertTrue(result);

	}

	@Test
	public void isAuthenticatedClientWithWrongPWTest() {
		boolean result = database.isAuthenticatedClient(
				"8dh9cpkijndku0i1hbogeckdio", "wrong_pw");

		assertFalse(result);
	}

	@Test
	public void isValidAccessTokenTest() {

		boolean result = database.isValidAccessToken(
				"f222a9a7d569d4e01a42a1fd77d70dd4", "read", "asdfasdf2");

		assertTrue(result);
	}

	@Test
	public void isValidAccessTokenWithWrongScopeTest() {
		// wrong scope
		boolean result = database.isValidAccessToken(
				"f222a9a7d569d4e01a42a1fd77d70dd4", "res_w", "asdfasdf2");

		assertFalse(result);
	}

	@Test
	public void isValidAccessTokenWithWrongUsernameTest() {
		// wrong user name
		boolean result = database.isValidAccessToken(
				"f222a9a7d569d4e01a42a1fd77d70dd4", "read", "asasdfasdf2");

		assertFalse(result);
	}

	@Test
	public void isValidAuthCodeTest() {
		boolean result = database.isValidAuthCode(
				"80c4f9ade44a408d76dec4cdb738959b",
				"8dh9cpkijndku0i1hbogeckdio",
				"http://localhost:8080/openid/redirect", "read write");

		assertTrue(result);
	}

	@Test
	public void isValidAuthCodeWithWrongScopeTest() {
		// wrong scope
		boolean result = database.isValidAuthCode(
				"80c4f9ade44a408d76dec4cdb738959b",
				"8dh9cpkijndku0i1hbogeckdio",
				"http://localhost:8080/openid/redirect", "res_R");

		assertFalse(result);

	}

	@Test
	public void isValidAuthCodeWithWrongClientIDTest() {
		// wrong client_id name
		boolean result = database.isValidAuthCode(
				"80c4f9ade44a408d76dec4cdb738959b", "wrong_client_id",
				"http://localhost:8080/openid/redirect", "read write");

		assertFalse(result);
	}

	@Test
	public void isValidAuthCodeWithWrongRedirectTest() {
		// wrong redirect_uri
		boolean result = database
				.isValidAuthCode("80c4f9ade44a408d76dec4cdb738959b",
						"8dh9cpkijndku0i1hbogeckdio", "redirect_is_wrong",
						"read write");

		assertFalse(result);
	}

	@Test
	/**
	 * @see Database.isValidClientId()
	 */
	public void isValidClientIdTest() {
		boolean result = database.isValidClientId("8dh9cpkijndku0i1hbogeckdio");

		assertTrue(result);

	}

	@Test
	/**
	 * @see Database.isValidClientId()
	 */
	public void isValidClientIdWithWrongClientIDTest() {
		// wrong client_id
		boolean result = database.isValidClientId("wrong_client_id");

		assertFalse(result);
	}

	@Test
	public void isValidScopeTest() {
		String givenScope = "read write";
		String dbScope = "write read";
		boolean result = database.isValidScope(givenScope, dbScope);

		assertTrue(result);
	}

	@Test
	public void isValidScopeSubsetTest() {
		String givenScope = "read";
		String dbScope = "write read";
		boolean result = database.isValidScope(givenScope, dbScope);

		assertTrue(result);
	}

	@Test
	public void isValidScopeWithGivenContainsMore() {
		String givenScope = "read write test";
		String dbScope = "write read";
		boolean result = database.isValidScope(givenScope, dbScope);

		assertFalse(result);
	}

	@Test
	public void getDescriptionForScope() {
		String givenScope = "read write";
		Map<String, String> descriptions = database
				.getDescriptionForScope(givenScope);

		assertTrue(descriptions.size() == 2);
		assertTrue(descriptions.containsKey("read"));
		assertTrue(descriptions.containsKey("write"));
	}

	@Test
	public void getDescriptionForScopeWithWrongScope() {
		String givenScope = "wrong!!";
		Map<String, String> descriptions = database
				.getDescriptionForScope(givenScope);

		assertTrue(descriptions.size() == 0);
		assertFalse(descriptions.containsKey("wrong!!"));

	}
}
