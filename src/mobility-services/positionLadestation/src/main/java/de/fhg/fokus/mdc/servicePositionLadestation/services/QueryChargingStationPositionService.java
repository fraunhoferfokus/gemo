package de.fhg.fokus.mdc.servicePositionLadestation.services;

// imports
import java.io.IOException;

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

import de.fhg.fokus.mdc.serviceAllgemeineLadestationen.clients.ChargingStationInfoDataStoreClient;

/**
 * The class implements the PositionLadestation service.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * @author tsc
 */
@Path("/query")
public class QueryChargingStationPositionService {

	/** The logger of the class. */
	private static final Logger log = LoggerFactory
			.getLogger(QueryChargingStationPositionService.class.getName());

	/**
	 * The method realizes the query request for data of a particular station.
	 * 
	 * @param eFZId
	 *            the Id of the electric vehicle.
	 * 
	 * @return a JSON String with the data for the electric vehicle.
	 * @throws IOException
	 *             an IO Exception in case of a corresponding fault.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response queryData(@QueryParam("LSId") String LSId,
			@Context HttpHeaders headers) throws IOException {

		// check the input
		if (LSId == null || LSId.matches("^\\s*$")) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error\": \"charging station id null\"}")
					.build();
		}

		// get an instance of the client
		ChargingStationInfoDataStoreClient chargingInfoClient = ChargingStationInfoDataStoreClient
				.getInstance();
		if (chargingInfoClient == null) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error\": \"Error while finding vehicles in range\"}")
					.build();
		}
		/********* [forward authentication headers] ***********/
		chargingInfoClient.setAuthHeaders(headers);

		String position = chargingInfoClient.getPositionById(LSId);

		return Response.ok().entity(position).build();
	}

	/**
	 * Gets all stations within the given range around the given position.
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
	@Path("/proximity")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getChargingInRange(@QueryParam("lon") double lon,
			@QueryParam("lat") double lat, @QueryParam("radius") int radius,
			@Context HttpHeaders headers) {

		// get an instance of the client
		ChargingStationInfoDataStoreClient chargingInfoClient = ChargingStationInfoDataStoreClient
				.getInstance();
		if (chargingInfoClient == null) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error\": \"Error while finding vehicles in range\"}")
					.build();
		}
		/********* [forward authentication headers] ***********/
		chargingInfoClient.setAuthHeaders(headers);
		String str = chargingInfoClient.getChargingStationsInRange(lat, lon,
				radius);

		return Response.ok().entity(str).build();
	}
}
