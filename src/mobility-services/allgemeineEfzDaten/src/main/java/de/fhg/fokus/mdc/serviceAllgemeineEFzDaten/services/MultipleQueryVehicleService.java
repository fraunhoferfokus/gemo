package de.fhg.fokus.mdc.serviceAllgemeineEFzDaten.services;

// imports
import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import de.fhg.fokus.mdc.serviceAllgemeineEFzDaten.datastore.CarInfoDataStoreClient;

/**
 * The class implements the queries for multiple vehicles of the
 * AllgemeineEFzDaten service.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * @author edit izi
 */
@Path("/multiple_vehicles_query")
public class MultipleQueryVehicleService {

	/** The logger of the class. */
	private static Logger log = Logger
			.getLogger(MultipleQueryVehicleService.class.getName());

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
	public String multipleVehiclesQuery(@QueryParam("query") String query,
			@Context HttpHeaders headers) throws IOException {

		// check the input
		if (query == null || query.matches("^\\s*$")) {
			return "";
		}

		// get an instance of the client
		CarInfoDataStoreClient carInfoClient = CarInfoDataStoreClient
				.getInstance();
		if (carInfoClient == null) {
			return "";
		}
		/********* [forward authentication headers] ***********/
		carInfoClient.setAuthHeaders(headers);

		// get the query results
		String toreturn = carInfoClient.sendEFZQuery(query);
		if (toreturn == null) {
			return "";
		}

		return toreturn;

	}
}
