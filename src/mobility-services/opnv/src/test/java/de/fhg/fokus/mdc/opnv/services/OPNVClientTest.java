package de.fhg.fokus.mdc.opnv.services;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.localserver.LocalTestServer;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import de.fhg.fokus.mdc.opnv.schema.ConReq;
import de.fhg.fokus.mdc.opnv.schema.LocValReq;
import de.fhg.fokus.mdc.opnv.schema.LocationType;
import de.fhg.fokus.mdc.opnv.schema.Prod;
import de.fhg.fokus.mdc.opnv.schema.RFlags;
import de.fhg.fokus.mdc.opnv.schema.ReqC;
import de.fhg.fokus.mdc.opnv.schema.ReqLocType;
import de.fhg.fokus.mdc.opnv.schema.ReqTType;
import de.fhg.fokus.mdc.opnv.schema.RequestLocationType;
import de.fhg.fokus.mdc.opnv.schema.ResC;
import de.fhg.fokus.mdc.opnv.schema.StartViaType;
import de.fhg.fokus.mdc.opnv.schema.StationType;
import de.fhg.fokus.mdc.opnv.services.impl.OPNVClient;

/**
 * Unit test for simple App.
 */
public class OPNVClientTest

{
	private static LocalTestServer server = null;
	private static String serverUrl = null;

	@BeforeClass
	public static void setUp() throws Exception {
		// TODO mock up hafas responses
		server = new LocalTestServer(null, null);
		server.register("/opnv/*", new GeMoServiceResponseHandler());
		server.start();

		// report how to access the server
		serverUrl = "http://" + server.getServiceAddress().getHostName() + ":"
				+ server.getServiceAddress().getPort();
		System.out.println("LocalTestServer available at " + serverUrl);

	}

	@Test
	public void testQueryRouteByCoordHttpRequestSuccess()
			throws JsonGenerationException, JsonMappingException, IOException {
		OPNVClient opnvc = OPNVClient
				.getInstance("http://demo.hafas.de/bin/pub/vbb-fahrinfo/relaunch2011/extxml.exe/");
		String accessID = "f779c8315fcf3959a518d6dc94cd491c";
		// fokus: 52.5259683, 13.3141459
		// alex:52.523206, 13.410836
		Integer latitude = 52505147;
		Integer longitude = 13303477;
		Integer destinationLatitude = 52390930;
		Integer destinationLongitude = 13067169;

		ConReq connectionRequest = new ConReq();
		StartViaType startVia = new StartViaType();

		LocationType startCoord = new LocationType();
		RequestLocationType destinationLoc = new RequestLocationType();
		LocationType destinationCoord = new LocationType();
		ReqC reqc = new ReqC();

		reqc.setVer("1.1");
		reqc.setLang("DE");
		reqc.setAccessId(accessID);
		// prod selects means of transportation, there are are 16 types, to
		// select all options, all digits set to 1 here.
		reqc.setProd("1111111111111111");

		startCoord.setX(longitude);
		startCoord.setY(latitude);
		startVia.setCoord(startCoord);

		Prod prod = new Prod();
		prod.setProd("1111111111111111");
		startVia.setProd(prod);
		connectionRequest.setStart(startVia);

		destinationCoord.setX(destinationLongitude);
		destinationCoord.setY(destinationLatitude);
		destinationLoc.setCoord(destinationCoord);
		connectionRequest.setDest(destinationLoc);

		RFlags rflags = new RFlags();
		rflags.setB(2);
		rflags.setF(2);
		connectionRequest.setRFlags(rflags);

		ReqTType requestTime = new ReqTType();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		requestTime.setDate(dateFormat.format(date));
		requestTime.setTime(timeFormat.format(date));
		connectionRequest.setReqT(requestTime);
		reqc.setConReq(connectionRequest);

		ObjectMapper mapper = new ObjectMapper();
		ResponseEntity<ResC> response = opnvc.getRouteXML(reqc);
		String src = mapper.writeValueAsString(response.getBody());
		// System.out.println(response.getBody().getConSectionList()
		// .getConSection().get(0).getDeparture().getBasicStop()
		// .getStation().getName());
		// assertEquals(200, src);
		assertEquals(200, response.getStatusCode().value());
	}

	@Test
	public void testQueryRouteByStationIdHttpRequestSuccess()
			throws JsonGenerationException, JsonMappingException, IOException {
		OPNVClient opnvc = OPNVClient
				.getInstance("http://demo.hafas.de/bin/pub/vbb-fahrinfo/relaunch2011/extxml.exe/");
		String accessID = "f779c8315fcf3959a518d6dc94cd491c";
		// fokus: 52.5259683, 13.3141459
		// alex:52.523206, 13.410836
		ReqC reqc = new ReqC();

		reqc.setVer("1.1");
		reqc.setLang("DE");
		reqc.setAccessId(accessID);
		reqc.setProd("1111111111111111");
		ConReq connectionRequest = new ConReq();

		StationType startStation = new StationType();
		startStation.setExternalId("009230999#86");
		StartViaType startVia = new StartViaType();
		startVia.setStation(startStation);

		// prod selects means of transportation, there are are 16 types, to
		// select all options, all digits set to 1 here.

		Prod prod = new Prod();
		prod.setProd("1111111111111111");
		startVia.setProd(prod);
		connectionRequest.setStart(startVia);

		RequestLocationType destinationLoc = new RequestLocationType();
		StationType destStation = new StationType();
		destStation.setExternalId("009024101#86");
		destinationLoc.setStation(destStation);
		connectionRequest.setDest(destinationLoc);

		RFlags rflags = new RFlags();
		rflags.setB(2);
		rflags.setF(2);
		connectionRequest.setRFlags(rflags);

		ReqTType requestTime = new ReqTType();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		requestTime.setDate(dateFormat.format(date));
		requestTime.setTime(timeFormat.format(date));
		connectionRequest.setReqT(requestTime);
		reqc.setConReq(connectionRequest);
		// add the other parameters map.add("categoryId",categoryId);
		ObjectMapper mapper = new ObjectMapper();
		ResponseEntity<ResC> response = opnvc.getRouteXML(reqc);
		String src = mapper.writeValueAsString(response.getBody());

		assertEquals(200, response.getStatusCode().value());

	}

	// available according to VBB-API documentation, but it never worked,
	// question to VBB not answered
	// @Test
	public void testLocationValidationRequestSuccess()
			throws JsonGenerationException, JsonMappingException, IOException {
		OPNVClient opnvc = OPNVClient
				.getInstance("http://demo.hafas.de/bin/pub/vbb-fahrinfo/relaunch2011/extxml.exe/");
		String accessID = "f779c8315fcf3959a518d6dc94cd491c";
		ReqC reqc = new ReqC();

		reqc.setVer("1.1");
		reqc.setLang("DE");
		reqc.setAccessId(accessID);
		reqc.setProd("1111111111111111");

		LocValReq lvr = new LocValReq();
		lvr.setId("001");
		lvr.setMaxNr(20);
		lvr.setSMode(1);
		ReqLocType requestlocation = new ReqLocType();
		requestlocation.setMatch("bahnhof");
		requestlocation.setType("ST");
		// requestlocation.setValue(value);
		lvr.setReqLoc(requestlocation);
		reqc.setLocValReq(lvr);

		ObjectMapper mapper = new ObjectMapper();
		ResponseEntity<ResC> response = opnvc.getRouteXML(reqc);
		String src = mapper.writeValueAsString(response.getBody());

		assertEquals("sth", src);
	}

	// available according to VBB-API documentation, but it never worked,
	// question to VBB not answered
	// @Test
	public void testLocationValidationRequestLocal()
			throws JsonGenerationException, JsonMappingException, IOException {
		OPNVClient opnvc = OPNVClient
				.getInstance("http://demo.hafas.de/bin/pub/vbb-fahrinfo/relaunch2011/extxml.exe/");
		String accessID = "f779c8315fcf3959a518d6dc94cd491c";
		ReqC reqc = new ReqC();

		reqc.setVer("1.1");
		reqc.setLang("DE");
		reqc.setAccessId(accessID);
		reqc.setProd("1111111111111111");

		LocValReq lvr = new LocValReq();
		lvr.setId("001");
		lvr.setMaxNr(20);
		lvr.setSMode(1);
		ReqLocType requestlocation = new ReqLocType();
		requestlocation.setMatch("bahnhof");
		requestlocation.setType("ST");
		// requestlocation.setValue(value);
		lvr.setReqLoc(requestlocation);
		reqc.setLocValReq(lvr);

		ObjectMapper mapper = new ObjectMapper();
		ResponseEntity<ResC> response = opnvc.getRouteXML(reqc);
		String src = mapper.writeValueAsString(response.getBody());

		assertEquals("sth", src);
	}
}
