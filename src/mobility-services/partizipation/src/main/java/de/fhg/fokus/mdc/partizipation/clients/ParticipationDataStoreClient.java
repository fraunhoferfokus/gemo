package de.fhg.fokus.mdc.partizipation.clients;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import de.fhg.fokus.mdc.clients.DataStoreClient;
import de.fhg.fokus.mdc.jsonObjects.GeMoFMCMapping;

public class ParticipationDataStoreClient extends DataStoreClient {
	/** The singleton instances of the class. */
	private static ParticipationDataStoreClient instance = null;
	private final Logger log = LoggerFactory.getLogger(getClass());

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static ParticipationDataStoreClient getInstance() {
		if (instance == null) {
			instance = new ParticipationDataStoreClient();
		}
		return instance;
	}

	/**
	 * is an implementation of DataStoreclient
	 */
	private ParticipationDataStoreClient() {
		super();
		// define second table charger_damage
	}

	@Override
	public void defineTableName() {
		// TODO table = props.getProperty(EVEHICLES_TABLE);
		table = "e_vehicle_damage";
	}

	/**
	 * The method obtains the id to access damage profile of an electric
	 * vehicle.
	 * 
	 * @param sqlString
	 *            the SQL string fetching data regarding an electric vehicle
	 *            with a particular ID.
	 * 
	 * @return the details in the form of a JSON String.
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */

	public GeMoFMCMapping convertGemoToFMCById(String sqlString)
			throws JsonParseException, JsonMappingException, IOException {

		String storageResponse = null;

		storageResponse = select(sqlString);
		if (storageResponse == null) {
			return new GeMoFMCMapping();
		}

		// The response is an array, we must grab the first
		ObjectMapper mapper = new ObjectMapper();
		GeMoFMCMapping[] resultSet = mapper.readValue(storageResponse,
				GeMoFMCMapping[].class);
		return (resultSet.length == 0) ? null : resultSet[0];

	}

	public String createGemoFMCMapping(String eVehicleID, String efzDamagesId,
			String type) {
		MultiValueMap<String, String> mapToStore = new LinkedMultiValueMap<String, String>();
		if (type.equals("e_vehicle")) {
			mapToStore.add("tableName", "e_vehicle_damage");
			mapToStore.add("gemoentityid", eVehicleID);
			mapToStore.add("categoryId", efzDamagesId);
		} else if (type.equals("charging_station")) {
			mapToStore.add("tableName", "charger_damage");
			mapToStore.add("gemoentityid", eVehicleID);
			mapToStore.add("categoryId", efzDamagesId);
		}
		// TODO change the client to ParticipationDataStoreClient
		String str = insert(mapToStore);
		log.info("sending POST request to storage/insert : "
				+ mapToStore.values());

		log.info("storage response:" + str);

		return str;
	}

	private List<GeMoFMCMapping> queryGemoFMCMappingsByType(String type)
			throws JsonParseException, JsonMappingException, IOException {
		String query = "select * from ";
		List<GeMoFMCMapping> gemoFMCMappingList = new ArrayList<GeMoFMCMapping>();
		if (type.equals("charging_station")) {
			query += "charger_damage";
		} else if (type.equals("e_vehicle")) {
			query += "e_vehicle_damage";
		}
		String storageResponse = select(query);
		// The response is an array of GemoFMCMapping
		ObjectMapper mapper = new ObjectMapper();
		GeMoFMCMapping[] gemofmcmappings = mapper.readValue(storageResponse,
				GeMoFMCMapping[].class);
		gemoFMCMappingList = Arrays.asList(gemofmcmappings);
		// make a list of ids from the list
		// resultSet
		return gemoFMCMappingList;
	}

	public Response queryConcernsByType(String type) throws JsonParseException,
			JsonMappingException, IOException, URISyntaxException {
		// the mappings for all e_vehicles or for all charging_stations
		List<GeMoFMCMapping> gemoFMCmappings = queryGemoFMCMappingsByType(type);
		List<String> fmcIdsOfGemoEntities = new ArrayList<String>();
		if (!gemoFMCmappings.isEmpty()) {
			for (GeMoFMCMapping gfm : gemoFMCmappings) {
				fmcIdsOfGemoEntities.add(gfm.getCategoryId());
			}
		}

		List<String> concerns = new ArrayList<String>();
		if (!fmcIdsOfGemoEntities.isEmpty()) {
			for (String fmcId : fmcIdsOfGemoEntities) {
				String query = AnliegenManagementClient.getInstance()
						.buildConcernQuery("0", "20", null, null, "2", null,
								fmcId);
				Response response = AnliegenManagementClient.getInstance()
						.queryConcernsByFmcId(query);
				concerns.add(response.getEntity().toString());
			}
		}

		return Response.status(200).entity(concerns.toString())
				.type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * asks storage service the gemo fixmycity mapping: gets gemoEntityId ,
	 * returns categoryid
	 * */
	public String convertGemoToFMCByType(String eVehicleID, String type)
			throws JsonParseException, JsonMappingException, IOException {
		String efzDamagesId = null;
		/** The SQL string for picking the data from storage */
		String sqlQueryStr = "select categoryid from ";

		if (type.equals("charging_station")) {
			sqlQueryStr += "charger_damage where gemoentityid=";
		} else if (type.equals("e_vehicle")) {
			sqlQueryStr += "e_vehicle_damage where gemoentityid=";
		}

		sqlQueryStr += "\'" + eVehicleID + "'";
		GeMoFMCMapping gemoFMCmapping = convertGemoToFMCById(sqlQueryStr);
		if (gemoFMCmapping == null) {
			return null;
		} else {
			efzDamagesId = gemoFMCmapping.getCategoryId();
		}
		return efzDamagesId;
	}

}
