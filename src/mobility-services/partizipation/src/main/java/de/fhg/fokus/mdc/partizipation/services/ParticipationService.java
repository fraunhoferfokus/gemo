package de.fhg.fokus.mdc.partizipation.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.http.ResponseEntity;

import de.fhg.fokus.mdc.partizipation.clients.AnliegenManagementClient;
import de.fhg.fokus.mdc.partizipation.clients.ParticipationDataStoreClient;
import de.fhg.fokus.mdc.partizipation.model.Category;

/**
 * The class implements the participation service.
 * 
 * @author Begum Ilke Zilci, ilke.zilci@fokus.fraunhofer.de
 */
@Path("/complaints")
public class ParticipationService {
	/** The logger of the class. */
	private static Logger log = Logger.getLogger(ParticipationService.class
			.getName());

	private Response checkInput(String lat, String lon, String type) {
		if (type == null || type.matches("^\\s*$")) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("{\"message\":\"Missing information, add parameter type=charging_station or type=e_vehicle to the form\"}")
					.build();
		}

		if ((lat != null && lon == null) || (lat == null && lon != null)) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("{\"message\":\"For geolocation both coordinates needed\"}")
					.build();
		} else if (lat != null && lon != null) {
			String latlon = lat + "," + lon;
			if (!latlon.matches("^[+-]?\\d+\\.?\\d*,?[+-]?\\d+\\.?\\d*$"))
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("{\"message\":\"Invalid geolocation input\"}")
						.build();
		}
		return Response.status(Response.Status.OK).build();
	}

	/*
	 * each category in fixmycityDB represents an eVehicle or a charging station
	 * from gemoDB mapping for gemoDB.evehicleID-fixmycityDB.categoryid stored
	 * in e_vehicle_damage table mapping for
	 * gemoDB.charger_id-fixmycityDB.categoryid stored in charger_damage table
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addComplaint(@FormParam("title") String title,
			@FormParam("description") String description,
			@FormParam("tags") String tags,
			@FormParam("eVehicleID") String eVehicleID,
			@FormParam("latitude") String lat,
			@FormParam("longitude") String lon, @FormParam("type") String type,
			// @FormParam("countryCode") String countryCode,
			// @FormParam("city") String city, @FormParam("street") String
			// street,
			// @FormParam("houseNo") String houseNo,
			// @FormParam("postalCode") String postalCode,
			@Context HttpHeaders headers) throws JsonParseException,
			JsonMappingException, IOException {

		String fmcIdOfGemoEntity = null;
		// check input
		Response preResponse = checkInput(lat, lon, type);
		if (!(preResponse.getStatus() == 200)) {
			return preResponse;
		}

		// get an instance of the client
		ParticipationDataStoreClient participationClient = ParticipationDataStoreClient
				.getInstance();
		if (participationClient == null) {
			return Response.status(500).build();
		}
		/********* [forward authentication headers] ***********/
		participationClient.setAuthHeaders(headers);

		// get an instance of the client
		AnliegenManagementClient fixmycityClient = AnliegenManagementClient
				.getInstance();
		if (fixmycityClient == null) {
			return Response.status(500).build();
		}
		/********* [forward authentication headers] ***********/
		fixmycityClient.setAuthHeaders(headers);

		/******************* logic *****************************/
		fmcIdOfGemoEntity = participationClient.convertGemoToFMCByType(
				eVehicleID, type);
		if (fmcIdOfGemoEntity == null) {
			// make an cm/categories POST request to fixmycity
			log.info("No mapping found for eVehicleID " + eVehicleID
					+ " sending POST request to fixmycity/cm/categories");
			Category category = fixmycityClient.createFMCCategory(eVehicleID);
			// create should make a
			fmcIdOfGemoEntity = Long.toString(category.getId());
			String storageResponse = participationClient.getInstance()
					.createGemoFMCMapping(eVehicleID, fmcIdOfGemoEntity, type);
		}

		// Complaint complaint = fixmycityClient
		// .createFMCComplaint(title, description, tags, lat, lon,
		// fmcIdOfGemoEntity);

		String complaint = fixmycityClient.createFMCComplaintStr(title,
				description, tags, lat, lon, fmcIdOfGemoEntity);
		return Response.status(200).entity(complaint).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	/* GET request to fixmycity complaints/{id} where id is complaint's id */
	public Response getComplaintJSONById(@PathParam("id") String complaintId,
			@Context HttpHeaders headers) throws URISyntaxException {
		// get an instance of the client
		AnliegenManagementClient fixmycityClient = AnliegenManagementClient
				.getInstance();
		if (fixmycityClient == null) {
			return Response.status(500).build();
		}
		/********* [forward authentication headers] ***********/
		fixmycityClient.setAuthHeaders(headers);

		ResponseEntity<String> str = fixmycityClient
				.getComplaintByIdAsJSON(complaintId);
		return Response.status(200).entity(str.getBody()).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	/* GET request with optional query parameters */
	public Response getComplaintJSONByParams(
			@QueryParam("eVehicleID") String gemoEntityID,
			@DefaultValue("0") @QueryParam("offset") String offset,
			@DefaultValue("20") @QueryParam("limit") String limit,
			@QueryParam("latitude") String lat,
			@QueryParam("longitude") String lon,
			@DefaultValue("2") @QueryParam("radius") String radius,
			@QueryParam("damageCode") String damageCodeP,
			@QueryParam("type") String type, @Context HttpHeaders headers)
			throws URISyntaxException, JsonProcessingException, IOException {

		String query;
		// check input
		Response preResponse = checkInput(lat, lon, type);
		if (!(preResponse.getStatus() == 200)) {
			return preResponse;
		}

		/************************* Configure Clients Begin ***********************/
		ParticipationDataStoreClient participationClient = ParticipationDataStoreClient
				.getInstance();
		if (participationClient == null) {
			return Response.status(500).build();
		}
		/********* [forward authentication headers] ***********/
		participationClient.setAuthHeaders(headers);

		AnliegenManagementClient fixmycityClient = AnliegenManagementClient
				.getInstance();
		if (fixmycityClient == null) {
			return Response.status(500).build();
		}

		/********* [forward authentication headers] ***********/
		fixmycityClient.setAuthHeaders(headers);
		/************************* Configure Clients End ***********************/

		if (gemoEntityID == null || gemoEntityID.matches("^\\s*$")) {
			// TODO return all complaints of charging_stations or of e_vehicles
			// accordingly
			log.info("returning all relevant complaints with query: ");
			return participationClient.queryConcernsByType(type);
		} else {
			// TODO implement queryConcernsByType for a fmcIdOfGemoEntity in
			// participationDataStoreClient
			String fmcIdOfGemoEntity = null;
			// map efzid to fixmycity.categoryid
			fmcIdOfGemoEntity = participationClient.convertGemoToFMCByType(
					gemoEntityID, type);

			if (fmcIdOfGemoEntity == null) {
				return Response.status(404)
						.entity("{\"message\":\"Concern profile not found\"}")
						.type(MediaType.APPLICATION_JSON).build();
			}

			query = fixmycityClient.buildConcernQuery(offset, limit, lat, lon,
					radius, damageCodeP, fmcIdOfGemoEntity);
			return fixmycityClient.queryConcernsByFmcId(query);
		}
	}

	// ask which statusids are possible
	// TODO get all statuses

	// @POST
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	// public String addComplaintJSON() {
	// String s = null;
	// return s;
	// }
	//
	// @Path("/{complaintid}/photos")
	// @POST
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// public String addComplaintPhoto(@HeaderParam("userid") String userId,
	// @PathParam("complaintid") Long complaintId,
	// @FormDataParam(value = "file") FormDataMultiPart formData) {
	// String response = null;
	//
	// return response;
	// }
}
