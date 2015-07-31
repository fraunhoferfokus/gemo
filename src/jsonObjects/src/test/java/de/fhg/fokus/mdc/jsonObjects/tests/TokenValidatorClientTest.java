package de.fhg.fokus.mdc.jsonObjects.tests;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.fokus.mdc.utils.auth.clients.TokenValidatorClient;

public class TokenValidatorClientTest {
	static TokenValidatorClient validatorClient;

	@BeforeClass
	public static void init() {
		validatorClient = new TokenValidatorClient();
	}

	// @Test(expected = WebApplicationException.class)
	// public void validatorreturnsforbidden() throws JsonParseException,
	// JsonMappingException, IOException {
	// String token = "token0303030";
	// String scope = "reservation";
	// String username = "Admin";
	// String servicename = "verfuegbarkeitLadestation";
	// validatorClient.validateParams(token, scope, username, servicename);
	// // Assert.assertFalse(isValid);
	// }
	//
	// @Test
	// public void removeSlashes() {
	// String servicename = "/verfuegbarkeitLadestation/";
	// System.out.println(servicename);
	// servicename = servicename.replace("/", "");
	// System.out.println(servicename);
	// Assert.assertEquals("verfuegbarkeitLadestation", servicename);
	// }

	@Test
	public void queryMatches() {
		String Str = new String("select * from scopes where name='read'");
		String query = "select * from scopes where";

		System.out.print("Return Value :");
		System.out.println(Str.matches("(.*)scopes(.*)"));

		System.out.print("Return Value :");
		System.out.println(Str.matches("select \\* from scopes where(.*)"));

		System.out.print("Return Value :");
		System.out.println(query.matches("select \\* from scopes where(.*)"));

		assertTrue(query.matches("select \\* from scopes where(.*)"));
	}

	// public void tokenWrongForbidden() {
	// String token = "a38383uuu333";
	// String scope = "reservation";
	// String username = "Admin";
	// validatorClient.validateParams(token, scope, username);
	//
	// // Assert.assertTrue(isValid);
	// }
	//
	// @Test(expected = WebApplicationException.class)
	// public void nullTokenReturnsFalse() {
	// String token = null;
	// String scope = "reservation";
	// String username = "Admin";
	// validatorClient.validateParams(token, scope, username);
	// // Assert.assertFalse(isValid);
	// }
	//
	// @Test(expected = WebApplicationException.class)
	// public void nullScopeReturnsFalse() {
	// String token = null;
	// String scope = null;
	// String username = null;
	// validatorClient.validateParams(token, scope, username);
	// // Assert.assertFalse(isValid);
	// }
	//
	// @Test(expected = WebApplicationException.class)
	// public void nullUsernameReturnsFalse() {
	// String token = "a38383uuu333";
	// String scope = "reservation";
	// String username = null;
	// validatorClient.validateParams(token, scope, username);
	// // Assert.assertFalse(isValid);
	// }

}
