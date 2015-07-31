package de.fhg.fokus.mdc.cruisingrange.services;

// imports
import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * The class implements the queries for multiple vehicles of the
 * AllgemeineEFzDaten service.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
@Path("/cruisingrange")
public class CruisingRange {

	/** The logger of the class. */
	private static Logger LOGGER = Logger.getLogger(CruisingRange.class.getName());

	/**
	 * The method realizes the query request for data of multiple vehicles.
	 * 
	 * @param query
	 *            the query to issue on the database.
	 * 
	 * @return a JSON String with the data for the electric vehicles.
	 * @throws IOException
	 *             an IO Exception in case of a corresponding fault.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getVehicleCruisingRange(@QueryParam("vehicleId") String vehicleId) throws IOException {

		return "alles supi";
	}
}
