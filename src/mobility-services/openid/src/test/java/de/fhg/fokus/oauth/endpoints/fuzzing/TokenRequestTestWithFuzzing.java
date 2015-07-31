package de.fhg.fokus.oauth.endpoints.fuzzing;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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
import de.fraunhofer.fokus.fuzzing.fuzzino.heuristics.generators.string.BadHostnamesGenerator;
import de.fraunhofer.fokus.fuzzing.fuzzino.heuristics.generators.string.SQLInjectionsGenerator;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.RequestFactory;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.StringEncoding;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.StringSpecification;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.StringType;

@RunWith(Parameterized.class)
@Category(IntegrationTest.class)
public class TokenRequestTestWithFuzzing {

	@Parameters
	public static Collection<Object[]> data() {
		StringSpecification stringSpecification = RequestFactory.INSTANCE
				.createStringSpecification();
		stringSpecification.setEncoding(StringEncoding.ASCII);
		stringSpecification.setType(StringType.STRING);
		stringSpecification.setIgnoreLengths(true);

		StringSpecification sqlSpecification = RequestFactory.INSTANCE
				.createStringSpecification();
		sqlSpecification.setEncoding(StringEncoding.ASCII);
		sqlSpecification.setType(StringType.SQL);
		sqlSpecification.setIgnoreLengths(true);

		List<String> values = new ArrayList<String>();
		List<Object[]> toReturn = new ArrayList<Object[]>();

		BadHostnamesGenerator badHostnameGenerator = StringGeneratorFactory.INSTANCE
				.createBadHostnames(stringSpecification, 0);
		// create instance of fuzzing heuristic for all bad strings
		AllBadStringsGenerator allBadStringsGenerator = StringGeneratorFactory.INSTANCE
				.createAllBadStrings(stringSpecification, 0);

		SQLInjectionsGenerator sqlInjectionsGenerator = StringGeneratorFactory.INSTANCE
				.createSqlInjections(sqlSpecification, 0);

		Iterator<FuzzedValue<String>> badStringIter = allBadStringsGenerator
				.iterator();
		Iterator<FuzzedValue<String>> badHostnameIter = badHostnameGenerator
				.iterator();
		Iterator<FuzzedValue<String>> sqlInIterator = sqlInjectionsGenerator
				.iterator();
		for (int testCases = 0; testCases < 100; testCases++) {
			for (int parameters = 0; parameters < 6; parameters++) {
				if (parameters != 2) {
					if (sqlInIterator.hasNext()) {
						values.add(sqlInIterator.next().getValue()
								.replaceAll("\\\\x00", "" + ((char) 0)));
					} else {
						values.add(badStringIter.next().getValue()
								.replaceAll("\\\\x00", "" + ((char) 0)));

					}
				} else {
					// add bad hostname
					values.add(badHostnameIter.next().getValue()
							.replaceAll("\\\\x00", "" + ((char) 0)));
				}
			}

			toReturn.add(values.toArray());
			values.clear();
		}

		return toReturn;
	}

	private String _grant_type;
	private String _client_id;
	private String _redirect_url;
	private String _client_secret;
	private String _code;
	private String _scope;

	public TokenRequestTestWithFuzzing(String client_id, String client_secret,
			String redirect_url, String grant_type, String code, String scope) {
		_client_id = client_id;
		_client_secret = client_secret;
		_redirect_url = redirect_url;
		_grant_type = grant_type;
		_code = code;
		_scope = scope;

	}

	@Test
	public void testWithAllfuzzed() {
		given().contentType("application/x-www-form-urlencoded")
				.param("grant_type", _grant_type)
				.param("redirect_uri", _redirect_url)
				.param("client_id", _client_id)
				.param("client_secret", _client_secret).param("code", _code)
				.param("scope", _scope).expect().statusCode(400)
				.contentType("application/json")
				.body("error", is(notNullValue()))
				.post("http://localhost:8080/openid/token");
	}

	@Test
	public void testWithGrantTypeHardcoded() {
		given().contentType("application/x-www-form-urlencoded")
				.param("grant_type", "authorization_code")
				.param("redirect_uri", _redirect_url)
				.param("client_id", _client_id)
				.param("client_secret", _client_secret).param("code", _code)
				.param("scope", _scope).expect().statusCode(400)
				.contentType("application/json")
				.body("error", is(notNullValue()))
				.post("http://localhost:8080/openid/token");
	}

	@Test
	public void testWithTypeAndClientIDHardcoded() {
		given().contentType("application/x-www-form-urlencoded")
				.param("grant_type", "authorization_code")
				.param("redirect_uri", _redirect_url)
				.param("client_id", "8dh9cpkijndku0i1hbogeckdio")
				.param("client_secret", _client_secret).param("code", _code)
				.param("scope", _scope).expect().statusCode(400)
				.contentType("application/json")
				.body("error", is(notNullValue()))
				.post("http://localhost:8080/openid/token");
	}

	@Test
	public void testWithTypeAndValidClientHardcoded() {
		given().contentType("application/x-www-form-urlencoded")
				.param("grant_type", "authorization_code")
				.param("redirect_uri", _redirect_url)
				.param("client_id", "8dh9cpkijndku0i1hbogeckdio")
				.param("client_secret", "d3qh6i5gteunjp5qi7dsop34lt")
				.param("code", _code).param("scope", _scope).expect()
				.statusCode(400).contentType("application/json")
				.body("error", is(notNullValue()))
				.post("http://localhost:8080/openid/token");
	}
}
