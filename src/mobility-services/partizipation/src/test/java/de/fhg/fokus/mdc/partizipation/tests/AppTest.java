//package de.fhg.fokus.mdc.partizipation.tests;
//
//import static org.junit.Assert.assertEquals;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.StringWriter;
//import java.net.URISyntaxException;
//
//import javax.xml.transform.Source;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.http.localserver.LocalTestServer;
//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//import de.fhg.fokus.mdc.partizipation.model.ComplaintRequest;
//import de.fhg.fokus.mdc.partizipation.model.Geolocation;
//import de.fhg.fokus.mdc.partizipation.services.AnliegenManagementClient;
//
//public class AppTest {
//	private static LocalTestServer server = null;
//	private static String serverUrl = null;
//
//	@BeforeClass
//	public static void start() throws Exception {
//		server = new LocalTestServer(null, null);
//		server.register("/fixmystadt-service/cm/*",
//				new ComplaintServiceResponseHandler());
//		server.start();
//
//		// report how to access the server
//		serverUrl = "http://" + server.getServiceAddress().getHostName() + ":"
//				+ server.getServiceAddress().getPort();
//		System.out.println("LocalTestServer available at " + serverUrl);
//
//	}
//
//	// @Test
//	public void testgetComplaintHttpRequestSuccess() {
//		AnliegenManagementClient amc = AnliegenManagementClient
//				.getInstance(serverUrl + "/fixmystadt-service/cm");
//		String cid = "1";
//		Source response = amc.getComplaintXML("/complaints" + "/" + cid);
//		// System.out.println(response.toString());
//		assertEquals("OK", response);
//
//	}
//
//	//@Test
////	public void testgetComplaintJSONHttpRequestSuccess()
////			throws URISyntaxException {
////		AnliegenManagementClient amc = AnliegenManagementClient
////				.getInstance("http://govmashups.fokus.fraunhofer.de"
////						+ "/fixmystadt-service/cm");
////		String cid = "9999999";
//////		ResponseEntity<String> response = amc.getComplaintJSON("/complaints"
//////				+ "/" + cid);
////		assertEquals("NOT_FOUND", response.getStatusCode().name());
////
////	}
//
//	// @Test
//	public void testaddComplaintFormAnliegenManagementClientHttpRequestSuccess() {
//		AnliegenManagementClient amc = AnliegenManagementClient
//				.getInstance("http://govmashups.fokus.fraunhofer.de"
//						+ "/fixmystadt-service/cm");
//		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//		map.add("title", "new complaint");
//		map.add("description",
//				"wann wird endlich der Dreck von unseren Straßen gefegt?");
//
//		map.add("categoryId", "1");
//		map.add("tags", "müll,abfall");
//		map.add("latitude", "52.2525");
//		map.add("longitude", "14.57581");
//
//		// add the other parameters map.add("categoryId",categoryId);
//		ResponseEntity<String> response = amc.postFormData("/complaints", map);
//
//		assertEquals(200, response.getStatusCode().value());
//
//	}
//
//	//@Test
//	public void testaddComplaintJSONAnliegenManagementClientHttpRequestSuccess()
//			throws JsonGenerationException, JsonMappingException, IOException {
//		// AnliegenManagementClient amc = AnliegenManagementClient
//		// .getInstance("govmashups.fokus.fraunhofer.de" +
//		// "/fixmystadt-service/cm");
//		AnliegenManagementClient amc = AnliegenManagementClient
//				.getInstance("http://govmashups.fokus.fraunhofer.de"
//						+ "/fixmystadt-service/cm");
//		ComplaintRequest c = new ComplaintRequest();
//		c.setStatusId("1");
//		c.setCategoryId("1");
//		// Address a = new Address();
//		// a.setCity("Berlin");
//		// a.setCountryCode("DE");
//		// a.setHouseNo("2");
//		// a.setPostalCode("10000");
//		// a.setStreet("Strasse");
//		Geolocation g = new Geolocation();
//		Double latitude = 52.2525;
//		Double longitude = 14.57581;
//		g.setLatitude(latitude);
//		g.setLongitude(longitude);
//		// c.setAddress(a);
//		c.setGeolocation(g);
//		c.setTags("abfall,strasse");
//		c.setDescription("wann wird endlich?");
//		c.setTitle("yet another complaint");
//
//		// no need to convet POJO to JSON, springs mappingjackson... does that.
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonString = mapper.writeValueAsString(c);
//		
//		String response = amc.addComplaint("/complaints", c);
//		assertEquals(true, response);
//		//assertEquals(true, response.contains("title"));
//
//	}
//
//	// @Test
//	public void populateXMLResponseTest() throws IOException {
//		String responseBody = "";
//		InputStream fileStream = Thread.currentThread().getContextClassLoader()
//				.getResourceAsStream("http_replies/complaint.xml");
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
//	// public String populateXMLResponse() throws IOException {
//	// String responseBody = "";
//	// InputStream fileStream = Thread.currentThread().getContextClassLoader()
//	// .getResourceAsStream("http_replies/complaint.xml");
//	//
//	// StringWriter writer = new StringWriter();
//	// IOUtils.copy(fileStream, writer, "utf-8");
//	//
//	// responseBody = writer.toString();
//	// writer.close();
//	// fileStream.close();
//	//
//	// return responseBody;
//	// }
//
//}
