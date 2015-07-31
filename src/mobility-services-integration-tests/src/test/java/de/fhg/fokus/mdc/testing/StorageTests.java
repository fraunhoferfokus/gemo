package de.fhg.fokus.mdc.testing;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonNode;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.fhg.fokus.mdc.propertyProvider.Constants;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.mdc.testing.rest.Storage;

public class StorageTests {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private DefaultHttpClient httpClient;
	private String storageBasicPath;
	private Storage storage;
	private ApacheHttpClient4Engine engine;
	private ResteasyClient client;

	@BeforeClass
	public void init() throws Exception {
		try {
			Properties props = PropertyProvider.getInstance().loadProperties();
			storageBasicPath = props.getProperty(Constants.STORAGE_URI);
			LOGGER.debug("basicPath >> {}", storageBasicPath);

			httpClient = getSecuredHttpClient(new DefaultHttpClient());

			String userName = props
					.getProperty(Constants.SERVICE_ACCESS_TO_FIXMYCITY_USERNAME);
			String password = props
					.getProperty(Constants.SERVICE_ACCESS_TO_FIXMYCITY_PASSWORD);
			LOGGER.debug("credentials >> {}:{}", userName, "hiddenPassword");
			Credentials credentials = new UsernamePasswordCredentials(userName,
					password);
			httpClient.getCredentialsProvider().setCredentials(
					org.apache.http.auth.AuthScope.ANY, credentials);
			engine = new ApacheHttpClient4Engine(httpClient);
			client = new ResteasyClientBuilder().httpEngine(engine).build();
			WebTarget target = client.target(storageBasicPath);
			ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
			storage = rtarget.proxy(Storage.class);
		} catch (IOException e) {
			LOGGER.error("Error while loading properties", e);
		}
	}

	// Thx to https://gist.github.com/michaelsd/2028163:
	// For testing reasons one can add a X509TrustManager to a HttpClient to
	// avoid the SSLPeerUnverifiedException
	// References:
	// http://javaskeleton.blogspot.com/2010/07/avoiding-peer-not-authenticated-with.html
	// http://en.wikibooks.org/wiki/Programming:WebObjects/Web_Services/How_to_Trust_Any_SSL_Certificate
	// Made some customizations to these guides. Works with Java 7 (no
	// NullPointerException when calling init() of SSLContext) and does not use
	// any deprecated functions of HttpClient, SSLContext...
	private DefaultHttpClient getSecuredHttpClient(HttpClient httpClient)
			throws Exception {
		final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return _AcceptedIssuers;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}
			};
			ctx.init(null, new TrustManager[] { tm }, new SecureRandom());
			SSLSocketFactory ssf = new SSLSocketFactory(ctx,
					SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", 443, ssf));
			return new DefaultHttpClient(ccm, httpClient.getParams());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Tests responses from storage tables: charging_stations efzreservation
	 * e_vehicle_damage e_vehicles
	 */
	@Test
	public void testSearchQuery() {
		// TODO Some thoughts about whether it is good to have queries hardcoded
		// or not?
		final String SELECT_ALL_FROM_USERS = "SELECT * FROM users";
		LOGGER.debug(SELECT_ALL_FROM_USERS);
		JsonNode users = storage.getSearchResult(SELECT_ALL_FROM_USERS);
		LOGGER.debug(" << {}", oneEntry(users.toString()));
		assertTrue(users.isArray(), "isArray");
		assertTrue(users.size() > 0, "not empty");
		assertTrue(users.get(0).has("housenr"), "has \"housenr\"");

		final String SELECT_ALL_FROM_E_VEHICLE_POSITIONS = "SELECT * FROM e_vehicle_positions";
		LOGGER.debug(SELECT_ALL_FROM_E_VEHICLE_POSITIONS);
		JsonNode e_vehicle_positions = storage
				.getSearchResult(SELECT_ALL_FROM_E_VEHICLE_POSITIONS);
		LOGGER.debug(" << {}", oneEntry(e_vehicle_positions.toString()));
		assertTrue(e_vehicle_positions.isArray(), "isArray");
		assertTrue(e_vehicle_positions.size() > 0, "not empty");
		assertTrue(e_vehicle_positions.get(0).has("evehicleid"),
				"has \"evehicleid\"");

		final String SELECT_ALL_FROM_E_CHARGING_STATIONS = "SELECT * FROM charging_stations";
		LOGGER.debug(SELECT_ALL_FROM_E_CHARGING_STATIONS);
		JsonNode charging_stations = storage
				.getSearchResult(SELECT_ALL_FROM_E_CHARGING_STATIONS);
		LOGGER.debug(" << {}", oneEntry(charging_stations.toString()));
		assertTrue(charging_stations.isArray(), "isArray");
		assertTrue(charging_stations.size() > 0, "not empty");
		assertTrue(charging_stations.get(0).has("address"), "has \"address\"");

		final String SELECT_ALL_FROM_E_VEHICLE_DAMAGE = "SELECT * FROM e_vehicle_damage";
		LOGGER.debug(SELECT_ALL_FROM_E_VEHICLE_DAMAGE);
		JsonNode e_vehicle_damage = storage
				.getSearchResult(SELECT_ALL_FROM_E_VEHICLE_DAMAGE);
		LOGGER.debug(" << {}", oneEntry(e_vehicle_damage.toString()));
		assertTrue(e_vehicle_damage.isArray(), "isArray");
		assertTrue(e_vehicle_damage.size() > 0, "not empty");
		assertTrue(e_vehicle_damage.get(0).has("evehicleid"),
				"has \"evehicleid\"");

		final String SELECT_ALL_FROM_E_VEHICLES = "SELECT * FROM e_vehicles";
		LOGGER.debug(SELECT_ALL_FROM_E_VEHICLES);
		JsonNode e_vehicles = storage
				.getSearchResult(SELECT_ALL_FROM_E_VEHICLES);
		LOGGER.debug(" << {}", oneEntry(e_vehicles.toString()));
		assertTrue(e_vehicles.isArray(), "isArray");
		assertTrue(e_vehicles.size() > 0, "not empty");
		assertTrue(e_vehicles.get(0).has("type"), "has \"type\"");
	}

	@Test
	public void testPostUpload() {
		String tableName = "my_test_table";
		String testFileStr = "" + "myFirstAttribute;mySecondAttribute"
				+ "\nsome;values" + "\nanother;row\n";
		MultipartFormDataOutput multipartFormDataOutput = new MultipartFormDataOutput();
		multipartFormDataOutput.addFormData("tableName", tableName,
				MediaType.TEXT_PLAIN_TYPE);
		multipartFormDataOutput.addFormData("file", testFileStr.getBytes(),
				MediaType.APPLICATION_OCTET_STREAM_TYPE, "my_test_table.csv");
		Response response = storage.postUpload(multipartFormDataOutput);

		int status = response.getStatus();
		LOGGER.debug("status " + status);
		assertTrue(status == 200, "status == 200");
		String successResponseString = "Table my_test_table created...";
		String responseString = response.readEntity(String.class);
		LOGGER.debug(responseString);
		assertTrue(responseString.equals(successResponseString),
				"responseString.equals(" + successResponseString + ")");
	}

	// @Test
	// public void testUpdate() {
	//
	// String newLon = "13.32764";
	// String newLat = "52.5044";
	// String eVehicleId = "ecar_00001";
	//
	// JsonNode eVehiclePosOld = storage
	// .getSearchResult("select * e_vehicle_positions WHERE evehicleid = '"
	// + eVehicleId + "'");
	//
	// MultiValueMap<String, String> map = new LinkedMultiValueMap<String,
	// String>();
	// map.add("where", "evehicleid=\'" + eVehicleId + "\'");
	// map.add("lon", String.valueOf(newLon));
	// map.add("lat", String.valueOf(newLat));
	// map.add("tableName", "e_vehicle_positions");
	//
	// Assert.assertEquals(storage.getUpdateResults(map,
	// "e_vehicle_positions", "evehicleid=\'" + eVehicleId + "\'"),
	// "OK");
	//
	// JsonNode eVehiclePosNew = storage
	// .getSearchResult("select * e_vehicle_positions WHERE evehicleid = '"
	// + eVehicleId + "'");
	//
	// }

	private String oneEntry(String myPotentialJsonString) {
		int closingBracePosition = myPotentialJsonString.indexOf("}");
		if (closingBracePosition != -1) {
			return myPotentialJsonString.substring(0, ++closingBracePosition)
					+ " [..] ";
		} else
			return limit(myPotentialJsonString);
	}

	private String limit(String myString) {
		final int MAX_CHARS = 120;
		return limitToMaxChars(myString, MAX_CHARS);
	}

	private String limitToMaxChars(String myString, int maxChars) {
		int maxLength = (myString.length() < maxChars) ? myString.length()
				: maxChars;
		return myString.substring(0, maxLength);
	}
}
