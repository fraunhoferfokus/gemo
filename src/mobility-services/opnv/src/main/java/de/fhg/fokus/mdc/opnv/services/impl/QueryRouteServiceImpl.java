package de.fhg.fokus.mdc.opnv.services.impl;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_OPNV;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_OPNV_PARAM_ACCESS;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.http.ResponseEntity;

import com.sun.jersey.server.wadl.WadlApplicationContext;

import de.fhg.fokus.mdc.opnv.schema.ConReq;
import de.fhg.fokus.mdc.opnv.schema.LocationType;
import de.fhg.fokus.mdc.opnv.schema.Prod;
import de.fhg.fokus.mdc.opnv.schema.RFlags;
import de.fhg.fokus.mdc.opnv.schema.ReqC;
import de.fhg.fokus.mdc.opnv.schema.ReqTType;
import de.fhg.fokus.mdc.opnv.schema.RequestLocationType;
import de.fhg.fokus.mdc.opnv.schema.ResC;
import de.fhg.fokus.mdc.opnv.schema.StartViaType;
import de.fhg.fokus.mdc.opnv.services.QueryRouteService;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.mdc.utils.SimpleValidator;

/**
 * Hello world!
 * 
 */
@Path("/route")
public class QueryRouteServiceImpl implements QueryRouteService {
	private final String pathToOPNV;
	private static Properties props = null;
	private final String accessID;
	private OPNVClient client;

	public QueryRouteServiceImpl() {

		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {

			e.printStackTrace();
		}
		pathToOPNV = props.getProperty(SERVICE_URI_OPNV);
		accessID = props.getProperty(SERVICE_URI_OPNV_PARAM_ACCESS);
		client = OPNVClient.getInstance(pathToOPNV);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.fhg.fokus.mdc.opnv.services.QueryRouteService#getRouteA2B(int,
	 * int, int, int)
	 */
	@Override
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ResC getRouteA2B(@QueryParam("startLongitude") int longitude,
			@QueryParam("startLatitude") int latitude,
			@QueryParam("destLongitude") int destinationLongitude,
			@QueryParam("destLatitude") int destinationLatitude,
			@QueryParam("startTime") String startTime,
			@Context WadlApplicationContext wadlContext)
			throws JsonGenerationException, JsonMappingException, IOException {
		// get request with anliegenmgmt client
		// Source complaint = client.getComplaint(path + "/" + id);
		// return resulting xml

		// System.out.println("wadl on? " +
		// wadlContext.isWadlGenerationEnabled());
		String src = null;
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
		// if no start time given fallback date is the current time
		Date date = SimpleValidator.stringToDateTime(startTime, new Date());
		requestTime.setDate(dateFormat.format(date));
		requestTime.setTime(timeFormat.format(date));
		connectionRequest.setReqT(requestTime);
		reqc.setConReq(connectionRequest);

		// try without gisparams , in the schema they are not optional but in
		// the example in the doc they are not there.
		// GISParameters gisparams= new GISParameters();

		// return connection body configure jersey to return xml or json based
		// on the accept header of the request
		// ObjectMapper mapper = new ObjectMapper();
		ResponseEntity<ResC> connectionResponse = client.getRouteXML(reqc);

		// src = mapper.writeValueAsString(connection.getBody());

		return connectionResponse.getBody();
	}

}
