package de.fhg.fokus.mdc.servicePositionEFz.services;

// imports
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
@Path("/queryRange")
public class QueryVehiclesInRange {

	/**
	 * The logger of the class.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(QueryVehiclesInRange.class.getClass());

	/**
	 * Gets all vehicles within the given range around the given position.
	 * 
	 * @param lon
	 *            longitude
	 * @param lat
	 *            latitude
	 * @param radius
	 *            radius around long and lat in meters
	 * @return list of all found vehicles as json
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getVehiclesInRange(@QueryParam("lon") double lon,
			@QueryParam("lat") double lat, @QueryParam("radius") int radius,
			@Context HttpHeaders headers) {

		String result = "FAIL";
		CarPositionDataStoreClient positionClient = CarPositionDataStoreClient
				.getInstance();
		positionClient.setAuthHeaders(headers);
		result = positionClient.getEfzsInRange(lat, lon, radius);
		return result;
	}
}
