package de.fhg.fokus.mdc.servicePositionEFz.services;

// imports
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.clients.CarPositionDataStoreClient;

//import java.util.logging.Level;
/**
 * The class implements the querying of the electric vehicle's position.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
@Path("/queryPosition")
public class QueryVehiclePositionService {

	/**
	 * The logger of the class.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(QueryVehiclePositionService.class.getClass());

	/**
	 * The method realizes the query request for data of a particular electric
	 * vehicle.
	 * 
	 * @param eVehicleId
	 *            the Id of the electric vehicle to query.
	 * 
	 * @return a JSON String with the availability of the electric vehicle in
	 *         question.
	 * @throws IOException
	 *             an IO Exception in case of a corresponding fault.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String queryData(@QueryParam("eVehicleID") String eVehicleId,
			@Context HttpHeaders headers) throws IOException {

		String result = "FAIL";

		try {
			CarPositionDataStoreClient positionClient = CarPositionDataStoreClient
					.getInstance();
			positionClient.setAuthHeaders(headers);
			result = positionClient.getEFzPositionById(eVehicleId);
		} catch (Exception e) {
			LOGGER.error("Error while getting vehicle position", e);
		}

		return result;
	}
}
