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
 * The class implements the updating of an electric vehicle availability.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
@Path("/updatePosition")
public class UpdateVehiclePositionService {

	/** The logger of the class. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UpdateVehiclePositionService.class.getClass());

	/**
	 * This is the GET method for updating the position status.
	 * 
	 * @param efzid
	 *            the id of the electric vehicle to update.
	 * @param lon
	 *            the new latitude.
	 * @param lat
	 *            the lat
	 * @return the String to return over HTTP - "OK" if everything was alright,
	 *         "FAIL" otherwise.
	 * @throws IOException
	 *             an IO exception if any.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePosition(@QueryParam("eVehicleID") String efzid,
			@QueryParam("lon") double lon, @QueryParam("lat") double lat,
			@Context HttpHeaders headers) throws IOException {

		// check the input parameters
		if (efzid == null || efzid.matches("^\\s*$")) {
			return "FAIL";
		}

		CarPositionDataStoreClient positionClient = CarPositionDataStoreClient
				.getInstance();
		positionClient.setAuthHeaders(headers);

		// invoke the update method
		String res = positionClient.updateElectricVehiclePosition(efzid, lon,
				lat);
		if (res.contains("FAIL") || res.matches("^\\s*$")) {
			return "FAIL";
		}

		return "OK";
	}

}
