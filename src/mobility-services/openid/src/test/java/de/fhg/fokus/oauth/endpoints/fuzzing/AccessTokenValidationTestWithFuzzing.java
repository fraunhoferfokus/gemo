package de.fhg.fokus.oauth.endpoints.fuzzing;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assume.assumeTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.fhg.fokus.IntegrationTest;
import de.fraunhofer.fokus.fuzzing.fuzzino.FuzzedValue;
import de.fraunhofer.fokus.fuzzing.fuzzino.heuristics.generators.StringGeneratorFactory;
import de.fraunhofer.fokus.fuzzing.fuzzino.heuristics.generators.string.AllBadStringsGenerator;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.RequestFactory;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.StringEncoding;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.StringSpecification;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.StringType;

@RunWith(Parameterized.class)
@Category(IntegrationTest.class)
public class AccessTokenValidationTestWithFuzzing {

	@Parameters
	public static Collection<Object[]> data() {
		StringSpecification stringSpecification = RequestFactory.INSTANCE
				.createStringSpecification();
		stringSpecification.setEncoding(StringEncoding.ASCII);
		stringSpecification.setType(StringType.STRING);
		stringSpecification.setIgnoreLengths(true);

		List<String> values = new ArrayList<String>();
		List<Object[]> toReturn = new ArrayList<Object[]>();

		// create instance of fuzzing heuristic for all bad strings
		AllBadStringsGenerator allBadStringsGenerator = StringGeneratorFactory.INSTANCE
				.createAllBadStrings(stringSpecification, 0);

		Iterator<FuzzedValue<String>> badStringIter = allBadStringsGenerator
				.iterator();
		for (int testCases = 0; testCases < 100; testCases++) {
			for (int parameters = 0; parameters < 3; parameters++) {
				values.add(badStringIter.next().getValue()
						.replaceAll("\\\\x00", "" + ((char) 0)));
			}

			toReturn.add(values.toArray());
			values.clear();
		}

		return toReturn;
	}

	private String _access_token;
	private String _username;
	private String _scope;

	public AccessTokenValidationTestWithFuzzing(String access_token,
			String username, String scope) {
		_access_token = access_token;
		_scope = scope;
		_username = username;

	}

	@Test
	public void testWithAllfuzzed() {
		assumeTrue(!_access_token.contains("\0"));
		assumeTrue(!_scope.contains("\0"));
		assumeTrue(!_username.contains("\0"));
		assumeTrue(_access_token.length() < 5000);
		assumeTrue(_scope.length() < 5000);
		assumeTrue(_username.length() < 5000);

		given().header("access_token", _access_token)
				.header("username", _username).header("scope", _scope).expect()
				.statusCode(403).contentType("application/json")
				.body("error", is(notNullValue()))
				.post("http://localhost:8080/openid/token/validate");
	}

	@Test
	public void testWithUserNameHardcoded() {
		assumeTrue(!_access_token.contains("\0"));
		assumeTrue(!_scope.contains("\0"));
		assumeTrue(!_username.contains("\0"));
		assumeTrue(_access_token.length() < 5000);
		assumeTrue(_scope.length() < 5000);
		assumeTrue(_username.length() < 5000);

		given().header("access_token", _access_token)
				.header("username", "asdfasdf2").header("scope", _scope)
				.expect().statusCode(403).contentType("application/json")
				.body("error", is(notNullValue()))
				.post("http://localhost:8080/openid/token/validate");
	}

	@Test
	public void testWithUserNameAndScopeHardcoded() {
		assumeTrue(!_access_token.contains("\0"));
		assumeTrue(!_scope.contains("\0"));
		assumeTrue(!_username.contains("\0"));
		assumeTrue(_access_token.length() < 5000);
		assumeTrue(_scope.length() < 5000);
		assumeTrue(_username.length() < 5000);

		given().header("access_token", _access_token)
				.header("username", "asdfasdf2").header("scope", "read")
				.expect().statusCode(403).contentType("application/json")
				.body("error", is(notNullValue()))
				.post("http://localhost:8080/openid/token/validate");
	}
}