package de.fhg.fokus.oauth;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.fokus.data.DataAccess;
import de.fhg.fokus.utils.Util;

public class TokenValidationTest {
	DataAccess database;

	@Test
	public void tokenNotExpiredYet() {

		long validuntil = 1464872435000L;

		boolean isExpired = Util.isExpired(validuntil);
		Assert.assertFalse(isExpired);
	}

	// @Test
	// public void tokenScopeok() {
	//
	// boolean isValidScope = Database.isValidScope("reservation",
	// "reservation");
	//
	// Assert.assertTrue(isValidScope);
	// }

	// @Test
	public void tokenNameOk() {

		String username = "developer";
		boolean isValidname = username.equals("developer");
		Assert.assertTrue(isValidname);
	}

	// @Test
	public void databasecomparisonOk() {
		String accessToken = "39393939kkk000";
		String scope = "reservation";
		String username = "developer";
		boolean isvalidToken = database.isValidAccessToken(accessToken, scope,
				username);
		Assert.assertTrue(isvalidToken);
	}
}
