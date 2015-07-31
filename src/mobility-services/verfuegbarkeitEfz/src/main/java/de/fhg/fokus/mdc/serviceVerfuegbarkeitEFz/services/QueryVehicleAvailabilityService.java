package de.fhg.fokus.mdc.serviceVerfuegbarkeitEFz.services;

// imports
import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.serviceVerfuegbarkeitEFz.clients.VehicleAvailabilityDataStoreClient;

//import java.util.logging.Level;
/**
 * The class implements the querying of the electric vehicle's availability.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
@Path("/queryAvailability")
public class QueryVehicleAvailabilityService {

	private static final String TIME_REGEX = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})((-|\\+)\\d{2}:\\d{2}|Z)?";
	/**
	 * The logger of the class.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
	 * The method realizes the query request for data of a particular electric
	 * vehicle.
	 * 
	 * @param eVehicleID
	 *            the Id of the electric vehicle to query.
	 * 
	 *            dates in "YYYY-MM-DDThh:mm:ssZ" (specified in schema) example:
	 *            2002-05-30T09:30:10+01:00
	 * 
	 *            regex
	 *            "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})((-|\\+)\\d{2}:\\d{2}|Z)?"
	 * 
	 * 
	 *            logische Regel für dauer überscheneidung: s1 e1 für
	 *            reservierung s2 & e2 für Abfragewert ( e2 >= s1 && e2 <= e1 )
	 *            || ( s2 >= s1 && s2 <= e1 )
	 * 
	 * 
	 * @return a JSON String with the availability of the electric vehicle in
	 *         question. Actually it returns the conflicted reservation
	 * @throws IOException
	 *             an IO Exception in case of a corresponding fault.
	 */
	@GET
	@Path("/vehicle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response queryVehicleData(
			@QueryParam("eVehicleID") String eVehicleID,
			@QueryParam("timeFrom") String timeFrom,
			@QueryParam("timeTo") String timeTo, @Context HttpHeaders headers)
			throws IOException {

		// check the input
		if (eVehicleID == null || eVehicleID.matches("^\\s*$")
				|| timeFrom == null || timeTo == null) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("{ \"Error\": \"bad eVehicleID: " + eVehicleID
							+ "\" }").build();
		}

		// get an instance of the client
		VehicleAvailabilityDataStoreClient availabilityClient = VehicleAvailabilityDataStoreClient
				.getInstance();
		if (availabilityClient == null) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{ \"Error\": \"unable to create storage client\" }")
					.build();
		}
		/********* [forward authentication headers] ***********/
		availabilityClient.setAuthHeaders(headers);

		// get the query results
		String toreturn = availabilityClient.getEFzAvailability(eVehicleID,
				timeFrom, timeTo);
		if (toreturn == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{ \"Error\": \"client returns no result\" }")
					.build();
		}

		return Response.ok(toreturn).build();
	}

	@GET
	@Path("/vehicles")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response queryVehiclesData(
			@QueryParam("eVehicleID") List<String> eVehicleIDs,
			@QueryParam("timeFrom") String timeFrom,
			@QueryParam("timeTo") String timeTo, @Context HttpHeaders headers)
			throws IOException {

		// check the input
		// TODO Vehicle IDs matchen und time matchen und diese verdammten scheiß
		// response types ändern!
		if (timeFrom == null || timeTo == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("{ \"Error\": \"no time  provided\" }").build();
		}

		// get an instance of the client
		VehicleAvailabilityDataStoreClient availabilityClient = VehicleAvailabilityDataStoreClient
				.getInstance();
		if (availabilityClient == null) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{ \"Error\": \"unable to create storage client\" }")
					.build();
		}
		/********* [forward authentication headers] ***********/
		availabilityClient.setAuthHeaders(headers);

		// get the query results
		String toreturn = availabilityClient.getMultipleEfzAvailability(
				eVehicleIDs, timeFrom, timeTo);
		if (toreturn == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{ \"Error\": \"client returns no result\" }")
					.build();
		}

		return Response.ok(toreturn).build();
	}
}
