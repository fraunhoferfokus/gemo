package de.fhg.fokus.mdc.opnv.services;

import java.io.IOException;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.sun.jersey.server.wadl.WadlApplicationContext;

import de.fhg.fokus.mdc.opnv.schema.ResC;

public interface QueryRouteService {

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public abstract ResC getRouteA2B(
			@QueryParam("startLongitude") int longitude,
			@QueryParam("startLatitude") int latitude,
			@QueryParam("destLongitude") int destinationLongitude,
			@QueryParam("destLatitude") int destinationLatitude,
			@QueryParam("startTime") String startTime,
			@Context WadlApplicationContext wadlContext)
			throws JsonGenerationException, JsonMappingException, IOException;

}