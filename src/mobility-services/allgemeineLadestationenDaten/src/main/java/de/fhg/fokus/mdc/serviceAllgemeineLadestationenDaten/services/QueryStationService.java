package de.fhg.fokus.mdc.serviceAllgemeineLadestationenDaten.services;

// imports
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.serviceAllgemeineLadestationen.clients.ChargingStationInfoDataStoreClient;

//import java.util.logging.Level;

/**
 * The class implements the AllgemeineLadestationenDaten service.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 */
@Path("/query")
public class QueryStationService {

	/**
	 * The logger of the class.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(QueryStationService.class.getClass());

	/**
	 * The method realizes the query request for data of a particular charging
	 * station.
	 * 
	 * @info: Danilo said: this is a duplicated method of positionLadestation
	 *        service :-( but awesome ^^
	 * 
	 * @param LSId
	 *            , optional, default 0 the Id of the charging station to query.
	 * @param mylon
	 *            , optional, default 0 longitude (only takes effect with
	 *            "mylat", can be combined with radius)
	 * @param mylat
	 *            , optional, default 0 latitude (only takes effect with
	 *            "mylon", can be combined with radius)
	 * @param radius
	 *            , optional, default 5000 radius around mylong and mylat in
	 *            meters
	 * 
	 * @return a JSON String with the data for the charging station in question
	 *         or a list of all found vehicles as json
	 * 
	 * @example of combination
	 * 
	 *          <pre>
	 * 			<code>
	 * 				scenarios:
	 * 				1.) you want a single dataset by id: just pass the target id
	 *  			2.) you want all stations in your area (5000m radius by default): 
	 *  				just pass your position using mylat and mylong
	 *  			3.) you want all stations in a specific area of e.g. 7777 meters:
	 *  				use 2.) and define additionally a radius of 7777
	 * 			</code>
	 * </pre>
	 * 
	 * @throws IOException
	 *             an IO Exception in case of a corresponding fault.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String queryData(@DefaultValue("0") @QueryParam("LSId") int LSId,
			@DefaultValue("0") @QueryParam("mylon") double mylon,
			@DefaultValue("0") @QueryParam("mylat") double mylat,
			@DefaultValue("5000") @QueryParam("radius") int radius,
			@Context HttpHeaders headers) throws IOException {

		// get an instance of the client
		ChargingStationInfoDataStoreClient chargingInfoClient = ChargingStationInfoDataStoreClient
				.getInstance();
		if (chargingInfoClient == null) {
			return "";
		}
		/********* [forward authentication headers] ***********/
		chargingInfoClient.setAuthHeaders(headers);

		// scenarios
		if (LSId != 0) {// check scenario 1 (exactly one result or null)
			String toreturn = chargingInfoClient.getLSDetails(String
					.valueOf(LSId));
			return (toreturn == null) ? "" : toreturn;
		}

		// other scenarios (more than one result)~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// quick validation
		if (mylon == 0 || mylat == 0)
			return "FAIL, invalid position data mylon=" + mylon + " AND mylat="
					+ mylat;

		String chargingStationsInRange = chargingInfoClient
				.getChargingStationsInRange(mylat, mylon, radius);

		return chargingStationsInRange;
	}
}
