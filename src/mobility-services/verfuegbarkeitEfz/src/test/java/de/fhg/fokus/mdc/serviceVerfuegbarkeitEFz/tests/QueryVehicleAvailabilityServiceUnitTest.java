//package de.fhg.fokus.mdc.serviceVerfuegbarkeitEFz.tests;
//
//import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_AVAILABILITY_EFZ;
//import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_AVAILABILITY_EFZ_QUERY;
//import static de.fhg.fokus.mdc.propertyProvider.Constants.STORAGE_URI;
//import static org.junit.Assert.assertEquals;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.StringWriter;
//import java.util.List;
//import java.util.Properties;
//
//import org.apache.commons.io.IOUtils;
//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import de.fhg.fokus.mdc.jsonObjects.VehicleState;
//import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
//import de.fhg.fokus.mdc.serviceVerfuegbarkeitEFz.clients.OldDataStoreClient;
//TODO update tests to use the new client
///**
// * The class tests the QueryVehicleAvailabilityService by mocking the return
// * value to DataStoreClient's getEfzAvailability method invocation
// * 
// * @author Ilke Zilci, ilke.zilci@fokus.fraunhofer.de
// */
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(OldDataStoreClient.class)
//public class QueryVehicleAvailabilityServiceUnitTest {
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
//		pathToDataStore = props.getProperty(STORAGE_URI);
//	}
//
//	@Test
//	public void testWithMockDataStoreClient() {
//		// List<VehicleState> vehicleStates = null;
//		// // arrange what QueryVehicleAvailabilityService receives from
//		// // DataStoreClient
//		// OldDataStoreClient mockDataClient = PowerMockito
//		// .mock(OldDataStoreClient.class);
//		// PowerMockito.mockStatic(OldDataStoreClient.class);
//		//
//		// Mockito.when(OldDataStoreClient.getInstance(pathToDataStore)).thenReturn(
//		// mockDataClient);
//		// try {
//		// Mockito.when(
//		// mockDataClient
//		// .getEFzAvailability("/search?query="
//		// +
//		// "select * from e_vehicle_reservation where eVehicleID=\'ecar_00023\' and (( \'2012-07-03 13:00:00\' >= resbegin  and  \'2012-07-03 13:00:00\' <= resend ) or (  \'2012-07-03 12:00:00\' >= resbegin  and  \'2012-07-03 12:00:00\' <= resend  ) or ( \'2012-07-03 12:00:00\' < resbegin and \'2012-07-03 13:00:00\' > resend ) )"))
//		// .thenReturn(populateJsonResponse());
//		// } catch (IOException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// }
//		// String jsonResponse = null;
//		// QueryVehicleAvailabilityService qvas = new
//		// QueryVehicleAvailabilityService();
//		// // qvas.setDataStoreClient(mockDataClient);
//		//
//		// try {
//		// String eVehicleID = "ecar_00023";
//		// String timeFrom = "2012-07-03 12:00:00";
//		// String timeTo = "2012-07-03 13:00:00";
//		// jsonResponse = (String) qvas.queryVehicleData(eVehicleID, timeFrom,
//		// timeTo, headers).getEntity();
//		// // if qvas makes the query on another url, the mock condition above
//		// // should be edited with the new parameter
//		// vehicleStates = parseVehicleStates(jsonResponse);
//		// } catch (IOException e1) {
//		// // TODO Auto-generated catch block
//		// e1.printStackTrace();
//		// }
//		//
//		// // assertEquals(
//		// // "somestring",
//		// // qvas.getDataStoreClient()
//		// // .getEFzAvailability(
//		// // "/search?query= "
//		// // + "select status from efzavailability where efzid=\'6666'"));
//		//
//		// assertEquals("busy", vehicleStates.get(0).status);
//	}
//
//	public String populateJsonResponse() throws IOException {
//		String responseBody = "";
//		InputStream fileStream = Thread.currentThread().getContextClassLoader()
//				.getResourceAsStream("http_replies/vehicleStatus.json");
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
//	@Test
//	public void testJsonObjectFromFile() throws IOException {
//		String res;
//
//		ObjectMapper mapper = new ObjectMapper();
//		List<VehicleState> vehicleStates = null;
//		res = populateJsonResponse();
//		vehicleStates = mapper.readValue(res,
//				new TypeReference<List<VehicleState>>() {
//				});
//
//		assertEquals("busy", vehicleStates.get(0).status);
//	}
//
// }
