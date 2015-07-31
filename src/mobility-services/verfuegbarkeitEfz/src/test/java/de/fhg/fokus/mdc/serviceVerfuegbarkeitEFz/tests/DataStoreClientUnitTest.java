//package de.fhg.fokus.mdc.serviceVerfuegbarkeitEFz.tests;
//
//import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_AVAILABILITY_EFZ;
//import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_AVAILABILITY_EFZ_QUERY;
//import static de.fhg.fokus.mdc.propertyProvider.Constants.STORAGE_URI;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.mock;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.StringWriter;
//import java.util.List;
//import java.util.Properties;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.http.localserver.LocalTestServer;
//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import de.fhg.fokus.mdc.jsonObjects.VehicleState;
//import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
//TODO update tests to use the new client
//public class DataStoreClientUnitTest {
//	private static LocalTestServer server = null;
//	private static String serverUrl = null;
//	private static Properties props = null;
//	private static final String pathToDataStore;
//	private static final String pathToAvailability;
//	private static final String pathToQueryAvailability;
//
//	static {
//		try {
//			props = PropertyProvider.getInstance().loadProperties();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		pathToAvailability = props.getProperty(SERVICE_URI_AVAILABILITY_EFZ);
//		pathToQueryAvailability = props
//				.getProperty(SERVICE_URI_AVAILABILITY_EFZ_QUERY);
//
//		pathToDataStore = props.getProperty(STORAGE_URI);
//	}
//
//	@BeforeClass
//	public static void setUp() throws Exception {
//		server = new LocalTestServer(null, null);
//		server.register("/storage/*", new GeMoServiceResponseHandler());
//		server.start();
//
//		// report how to access the server
//		serverUrl = "http://" + server.getServiceAddress().getHostName() + ":"
//				+ server.getServiceAddress().getPort();
//		System.out.println("LocalTestServer available at " + serverUrl);
//
//	}
//
//	/*
//	 * Tests if DataStoreClient makes a valid GET Request together with
//	 * RestTemplate to query availability of a vehicle
//	 */
//	// @Test
//	public void testVehicleFetchHttpRequest() {
//
//	}
//
//	/*
//	 * Tests if QueryVehicleAvailabilityService makes a valid call to
//	 * DataStoreClient to query availability of a vehicle make an instance of
//	 * queryvehicleavailability with parameters and call queryData returning
//	 * response from localhttpserver
//	 */
//	// @Test
//	// public void testVehicleQueryDataSuccess() {
//	// QueryVehicleAvailabilityService qvas= new
//	// QueryVehicleAvailabilityService();
//	//
//	// String response = qvas
//	// .getEFzAvailability("/search?query=select status from efzavailability where efzid=\'6666'");
//	// System.out.println(response);
//	// List<VehicleState> vehicleStates = parseVehicleStates(response);
//	// assertEquals("busy", vehicleStates.get(0).status);
//
//	// compare json objects alternatively like this if the order of objects
//	// shouldnt matter
//	// this calls the equals method of hashmaps which orders them and calls
//	// equals recursively
//	// assertArrayEquals(mapper.readValue(expectedJson,
//	// new TypeReference<HashMap<String, Object>>() {
//	// }), mapper.readValue(actualJson,
//	// new TypeReference<HashMap<String, Object>>() {
//	// }));
//	// }
//
//	public List<VehicleState> parseVehicleStates(String res) {
//		ObjectMapper mapper = new ObjectMapper();
//		List<VehicleState> vehicleStates = null;
//		try {
//			vehicleStates = mapper.readValue(res,
//					new TypeReference<List<VehicleState>>() {
//					});
//		} catch (JsonGenerationException e) {
//
//			e.printStackTrace();
//
//		} catch (JsonMappingException e) {
//
//			e.printStackTrace();
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		}
//		return vehicleStates;
//	}
//
//	/*
//	 * Tests if DataStoreClient makes a valid POST Request together with
//	 * RestTemplate to update availability of a vehicle
//	 */
//	@Test
//	public void testVehicleUpdateHttpRequestSuccess() {
//		OldDataStoreClient dsc = OldDataStoreClient.getInstance(serverUrl
//				+ "/storage");
//		String efzidExisting = "6666";
//		String statusValid = "free";
//		String response = dsc.updateElectricVehicleAvailability("/update",
//				efzidExisting, statusValid);
//		System.out.println(response);
//		assertEquals("OK", response);
//
//	}
//
//	/*
//	 * Tests if DataStoreClient makes a valid POST Request together with
//	 * RestTemplate to update availability of a vehicle
//	 */
//	// @Test
//	public void testVehicleUpdateHttpRequestFail() {
//		OldDataStoreClient dsc = OldDataStoreClient.getInstance(serverUrl
//				+ "/storage");
//		String efzidExisting = "6666";
//		String statusValid = "free";
//		String response = dsc.updateElectricVehicleAvailability("/update/fail",
//				efzidExisting, statusValid);
//		System.out.println(response);
//		assertEquals("FAIL", response);
//	}
//
//	public String populateJsonResponse() throws IOException {
//		String responseBody = "";
//		InputStream fileStream = Thread.currentThread().getContextClassLoader()
//				.getResourceAsStream("vehicleStatus.json");
//		StringWriter writer = new StringWriter();
//		IOUtils.copy(fileStream, writer, "utf-8");
//
//		responseBody = writer.toString();
//		writer.close();
//		fileStream.close();
//
//		return responseBody;
//	}
//
//	@Test
//	public void populateJsonResponseTest() throws IOException {
//		String responseBody = "";
//		InputStream fileStream = Thread.currentThread().getContextClassLoader()
//				.getResourceAsStream("http_replies/vehicleStatus.json");
//
//		StringWriter writer = new StringWriter();
//		IOUtils.copy(fileStream, writer, "utf-8");
//
//		responseBody = writer.toString();
//		writer.close();
//		fileStream.close();
//
//		assertEquals("file found", responseBody);
//
//	}
//
//	public List<VehicleState> jsonObjectFromFile() {
//		String res;
//
//		ObjectMapper mapper = new ObjectMapper();
//		List<VehicleState> vehicleStates = null;
//		try {
//			res = populateJsonResponse();
//			vehicleStates = mapper.readValue(res,
//					new TypeReference<List<VehicleState>>() {
//					});
//		} catch (JsonGenerationException e) {
//
//			e.printStackTrace();
//
//		} catch (JsonMappingException e) {
//
//			e.printStackTrace();
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		}
//		return vehicleStates;
//	}
//
//	// @Test
//	/* Tests DataStoreClient using a RestTemplate Mock. */
//	public void testVehicleFetchesSuccess() {
//		OldDataStoreClient dsc = OldDataStoreClient
//				.getInstance(pathToDataStore);
//		// arrange what DataStoreClient receives from storage
//		RestTemplate restTemplateMock = mock(RestTemplate.class);
//		try {
//			Mockito.when(
//					restTemplateMock
//							.getForObject(
//									pathToDataStore
//											+ "select status from efzavailability where efzid=\'6666'",
//									String.class)).thenReturn(
//					populateJsonResponse());
//		} catch (RestClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dsc.setRestTemplate(restTemplateMock);
//
//		// act
//		String response = dsc
//				.getEFzAvailability("select status from efzavailability where efzid=\'6666'");
//
//		// parse response
//		ObjectMapper mapper = new ObjectMapper();
//		List<VehicleState> actualVehicleStates = null;
//		// get expected vehicle states from test resource
//		List<VehicleState> expectedVehicleStates = jsonObjectFromFile();
//		try {
//			actualVehicleStates = mapper.readValue(response,
//					new TypeReference<List<VehicleState>>() {
//					});
//		} catch (JsonGenerationException e) {
//
//			e.printStackTrace();
//
//		} catch (JsonMappingException e) {
//
//			e.printStackTrace();
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		}
//
//		// compare the objects
//		assertEquals(expectedVehicleStates.get(0).status,
//				actualVehicleStates.get(0).status);
//
//	}
//
//	@AfterClass
//	public static void stopServer() throws Exception {
//		server.stop();
//
//	}
// }