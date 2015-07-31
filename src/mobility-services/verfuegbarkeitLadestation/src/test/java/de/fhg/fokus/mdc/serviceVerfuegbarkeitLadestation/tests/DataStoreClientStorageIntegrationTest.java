package de.fhg.fokus.mdc.serviceVerfuegbarkeitLadestation.tests;

import com.sun.jersey.test.framework.JerseyTest;

public class DataStoreClientStorageIntegrationTest extends JerseyTest {
	/**
	 * <pre>
	 * 
	 * private static LocalTestServer server = null;
	 * private static String serverUrl = null;
	 * private static Properties props = null;
	 * private static final String pathToDataStore;
	 * private static final String pathToAvailability;
	 * // private static final String pathToQueryAvailability;
	 * private static final String pathToUpdateAvailability;
	 * 
	 * static {
	 * 	try {
	 * 		props = PropertyProvider.getInstance().loadProperties();
	 * 	} catch (IOException e) {
	 * 		// TODO Auto-generated catch block
	 * 		e.printStackTrace();
	 * 	}
	 * 	pathToAvailability = props
	 * 			.getProperty(SERVICE_URI_AVAILABILITY_CHARGING_POINT);
	 * 	// pathToQueryAvailability = props
	 * 	// .getProperty(SERVICE_URI_AVAILABILITY_CHARGING_POINT_QUERY);
	 * 	pathToUpdateAvailability = props
	 * 			.getProperty(SERVICE_URI_AVAILABILITY_EFZ_UPDATE);
	 * 	pathToDataStore = props.getProperty(STORAGE_URI);
	 * }
	 * 
	 * &#064;BeforeClass
	 * public static void start() throws Exception {
	 * 	// server = new LocalTestServer(null, null);
	 * 	// server.register(&quot;/storage/*&quot;, new GeMoServiceResponseHandler());
	 * 	// server.start();
	 * 	//
	 * 	// // report how to access the server
	 * 	// serverUrl = &quot;http://&quot; + server.getServiceAddress().getHostName() +
	 * 	// &quot;:&quot;
	 * 	// + server.getServiceAddress().getPort();
	 * 	// System.out.println(&quot;LocalTestServer available at &quot; + serverUrl);
	 * 
	 * }
	 * 
	 * &#064;Override
	 * protected TestContainerFactory getTestContainerFactory() {
	 * 	return new ExternalTestContainerFactory();
	 * }
	 * 
	 * &#064;Override
	 * public WebAppDescriptor configure() {
	 * 	return new WebAppDescriptor.Builder()
	 * 			.initParam(WebComponent.RESOURCE_CONFIG_CLASS,
	 * 					ClassNamesResourceConfig.class.getName())
	 * 			.initParam(
	 * 					ClassNamesResourceConfig.PROPERTY_CLASSNAMES,
	 * 					UpdateStationAvailabilityService.class.getName() + &quot;;&quot;
	 * 							+ DataStoreClient.class.getName()).build();
	 * }
	 * 
	 * // @Test
	 * public void testLSUpdateHttpRequestSuccess() {
	 * 	DataStoreClient dsc = DataStoreClient.getInstance(serverUrl + &quot;/storage&quot;);
	 * 	String efzidExisting = &quot;6666&quot;;
	 * 	String statusValid = &quot;free&quot;;
	 * 	String response = dsc.updateChargingStationAvailability(&quot;/update&quot;,
	 * 			efzidExisting, statusValid);
	 * 	System.out.println(response);
	 * 	assertEquals(&quot;OK&quot;, response);
	 * 
	 * }
	 * 
	 * &#064;Test
	 * public void testUpdateAvailabilityFail() {
	 * 	// prepare request to vehicleAvailabilityService
	 * 	WebResource webResource = client().resource(pathToAvailability);
	 * 	String lsid = &quot;6666&quot;;
	 * 	String status = &quot;busy&quot;;
	 * 
	 * 	MultivaluedMap formData = new MultivaluedMapImpl();
	 * 	formData.add(&quot;lsidd&quot;, lsid);
	 * 	formData.add(&quot;status&quot;, status);
	 * 	formData.add(&quot;tableName&quot;, &quot;lsavailability&quot;);
	 * 	ClientResponse response = webResource.path(pathToUpdateAvailability)
	 * 			.type(MediaType.APPLICATION_FORM_URLENCODED)
	 * 			.post(ClientResponse.class, formData);
	 * 	String textEntity = response.getEntity(String.class);
	 * 	String expected = &quot;FAIL- Mandatory parameters do not match the required format or null&quot;;
	 * 	assertEquals(expected, textEntity);
	 * }
	 * 
	 * &#064;Test
	 * public void testQueryAvailabilitySuccess() {
	 * 	// prepare request to vehicleAvailabilityService
	 * 	WebResource webResource = client().resource(pathToAvailability);
	 * 	String lsid = &quot;6666&quot;;
	 * 	String status = &quot;busy&quot;;
	 * 
	 * 	MultivaluedMap formData = new MultivaluedMapImpl();
	 * 	formData.add(&quot;lsid&quot;, lsid);
	 * 	formData.add(&quot;status&quot;, status);
	 * 	formData.add(&quot;tableName&quot;, &quot;lsavailability&quot;);
	 * 	ClientResponse response = webResource.path(pathToUpdateAvailability)
	 * 			.type(MediaType.APPLICATION_FORM_URLENCODED)
	 * 			.post(ClientResponse.class, formData);
	 * 	String textEntity = response.getEntity(String.class);
	 * 	String expected = &quot;OK&quot;;
	 * 	assertEquals(expected, textEntity);
	 * }
	 * 
	 * &#064;Test
	 * public void testQueryAvailabilityFailWrongStatus() {
	 * 	// prepare request to vehicleAvailabilityService
	 * 	WebResource webResource = client().resource(pathToAvailability);
	 * 	String lsid = &quot;6666&quot;;
	 * 	String status = &quot;busyy&quot;;
	 * 
	 * 	MultivaluedMap formData = new MultivaluedMapImpl();
	 * 	formData.add(&quot;lsid&quot;, lsid);
	 * 	formData.add(&quot;status&quot;, status);
	 * 	formData.add(&quot;tableName&quot;, &quot;lsavailability&quot;);
	 * 	ClientResponse response = webResource.path(pathToUpdateAvailability)
	 * 			.type(MediaType.APPLICATION_FORM_URLENCODED)
	 * 			.post(ClientResponse.class, formData);
	 * 	String textEntity = response.getEntity(String.class);
	 * 	String expected = &quot;FAIL- Status must be free or busy&quot;;
	 * 	assertEquals(expected, textEntity);
	 * }
	 * 
	 * // @Test
	 * public void testQueryAvailabilityFailStorage() {
	 * 	// prepare request to vehicleAvailabilityService
	 * 	WebResource webResource = client().resource(pathToAvailability);
	 * 	String lsid = &quot;6666&quot;;
	 * 	String status = &quot;busyy&quot;;
	 * 
	 * 	MultivaluedMap formData = new MultivaluedMapImpl();
	 * 	formData.add(&quot;lsid&quot;, lsid);
	 * 	formData.add(&quot;status&quot;, status);
	 * 	formData.add(&quot;tableName&quot;, &quot;lsavailabilityy&quot;);
	 * 	ClientResponse response = webResource.path(pathToUpdateAvailability)
	 * 			.type(MediaType.APPLICATION_FORM_URLENCODED)
	 * 			.post(ClientResponse.class, formData);
	 * 	String textEntity = response.getEntity(String.class);
	 * 	String expected = &quot;FAIL- Storage returned FAIL&quot;;
	 * 	assertEquals(expected, textEntity);
	 * }
	 * </pre>
	 */
}
