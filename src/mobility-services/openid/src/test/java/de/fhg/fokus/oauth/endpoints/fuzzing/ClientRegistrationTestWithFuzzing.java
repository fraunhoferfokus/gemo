package de.fhg.fokus.oauth.endpoints.fuzzing;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assume.assumeTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.fhg.fokus.IntegrationTest;
import de.fraunhofer.fokus.fuzzing.fuzzino.FuzzedValue;
import de.fraunhofer.fokus.fuzzing.fuzzino.heuristics.generators.StringGeneratorFactory;
import de.fraunhofer.fokus.fuzzing.fuzzino.heuristics.generators.string.BadHostnamesGenerator;
import de.fraunhofer.fokus.fuzzing.fuzzino.heuristics.generators.string.BadLongStringsGenerator;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.RequestFactory;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.StringEncoding;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.StringSpecification;
import de.fraunhofer.fokus.fuzzing.fuzzino.request.java.StringType;

@RunWith(Parameterized.class)
@Category(IntegrationTest.class)
public class ClientRegistrationTestWithFuzzing {

	@Parameters
	public static Collection<Object[]> data() {
		StringSpecification stringSpecification = RequestFactory.INSTANCE
				.createStringSpecification();
		stringSpecification.setEncoding(StringEncoding.ASCII);
		stringSpecification.setType(StringType.STRING);
		stringSpecification.setIgnoreLengths(true);

		List<String> values = new ArrayList<String>();
		List<Object[]> toReturn = new ArrayList<Object[]>();

		BadHostnamesGenerator badHostnameGenerator = StringGeneratorFactory.INSTANCE
				.createBadHostnames(stringSpecification, 0);
		// create instance of fuzzing heuristic for bad long strings
		BadLongStringsGenerator badLongStringsGenerator = StringGeneratorFactory.INSTANCE
				.createBadLongStrings(stringSpecification, 0);

		Iterator<FuzzedValue<String>> iter = badLongStringsGenerator.iterator();
		Iterator<FuzzedValue<String>> iter2 = badHostnameGenerator.iterator();
		for (int testCases = 0; testCases < 100; testCases++) {
			for (int parameters = 0; parameters < 5; parameters++) {
				if (parameters != 2 && parameters != 4) {
					values.add(iter.next().getValue()
							.replaceAll("\\\\x00", "" + ((char) 0)));
				} else {
					// add bad hostname
					values.add(iter2.next().getValue()
							.replaceAll("\\\\x00", "" + ((char) 0)));
				}
			}

			toReturn.add(values.toArray());
			values.clear();
		}

		return toReturn;
	}

	private String _type;
	private String _client_name;
	private String _redirect_url;
	private String _client_description;
	private String _client_url;

	public ClientRegistrationTestWithFuzzing(String type, String client_name,
			String redirect_url, String client_description, String client_url) {
		_type = type;
		_client_name = client_name;
		_client_description = client_description;
		_client_url = client_url;
		_redirect_url = redirect_url;

	}

	@Test
	public void testWithAllfuzzed() {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("type", _type);
		jsonMap.put("client_name", _client_name);
		jsonMap.put("client_description", _client_description);
		jsonMap.put("client_url", _client_url);
		jsonMap.put("redirect_url", _redirect_url);

		JSONObject json = new JSONObject(jsonMap);

		given().contentType("application/json").body(json.toString()).expect()
				.statusCode(400).contentType("application/json")
				.body("error", is(notNullValue()))
				.post("http://localhost:8080/openid/register");
	}

	@Test
	public void testWithTypeHardcoded() {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("type", "push");
		jsonMap.put("client_name", _client_name);
		jsonMap.put("client_description", _client_description);
		jsonMap.put("client_url", _client_url);
		jsonMap.put("redirect_url", _redirect_url);

		JSONObject json = new JSONObject(jsonMap);

		given().contentType("application/json").body(json.toString()).expect()
				.statusCode(400).contentType("application/json")
				.body("error", is(notNullValue()))
				.post("http://localhost:8080/openid/register");
	}

	@Test
	public void testWithTypeAndUrlsHardcoded() {
		assumeTrue(_client_name.length() > 50);
		assumeTrue(_client_description.length() > 200);

		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put("type", "push");
		jsonMap.put("redirect_url", "http://localhost:8080/openid/redirect");
		jsonMap.put("client_url", "http://www.google.com/");
		jsonMap.put("client_name", _client_name);
		jsonMap.put("client_description", _client_description);

		JSONObject json = new JSONObject(jsonMap);

		given().contentType("application/json").body(json.toString()).expect()
				.statusCode(400).contentType("application/json")
				.body("error", is(notNullValue()))
				.post("http://localhost:8080/openid/register");
	}
}
