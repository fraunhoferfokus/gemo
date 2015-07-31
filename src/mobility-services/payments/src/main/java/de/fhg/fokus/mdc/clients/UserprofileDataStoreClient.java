package de.fhg.fokus.mdc.clients;

import static de.fhg.fokus.mdc.propertyProvider.Constants.USERS_TABLE;

import java.io.IOException;

import javax.ws.rs.core.HttpHeaders;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import de.fhg.fokus.mdc.jsonObjects.Userprofile;

/**
 * Storage-Client to receive all user information
 * 
 * @info better would be a rest confirm service request (no direct storage
 *       access)
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class UserprofileDataStoreClient extends DataStoreClient {

	/** The singleton instances of the class. */
	private static UserprofileDataStoreClient instance = null;
	private HttpHeaders authHeadersJersey;

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static UserprofileDataStoreClient getInstance() {
		if (instance == null) {
			instance = new UserprofileDataStoreClient();
		}
		return instance;
	}

	/**
	 * is an implementation of DataStoreclient
	 */
	public UserprofileDataStoreClient() {
		super();
	}

	// ---------------------[ Super Class Methods]----------------------

	public void defineTableName() {
		table = props.getProperty(USERS_TABLE);
	}

	// TODO move this to gemoclient or
	public void setAuthHeadersJersey(HttpHeaders headers) {
		this.authHeadersJersey = headers;

	}

	/**
	 * grabs the user object by ID
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Userprofile requestUserByID(Integer userID)
			throws JsonParseException, JsonMappingException, IOException {

		String userString = select("select * from " + table + " where _id=\'"
				+ userID + "\'");

		ObjectMapper mapper = new ObjectMapper();

		Userprofile[] resultSet = mapper.readValue(userString,
				Userprofile[].class);

		// TODO: expect exactly one result, throw exception if not
		Userprofile user = resultSet[0];

		return user;
	}

	/**
	 * grabs the user object by ID
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Userprofile requestUserByUsername(String username)
			throws JsonParseException, JsonMappingException, IOException {

		String userString = select("select * from " + table
				+ " where username=\'" + username + "\'");

		ObjectMapper mapper = new ObjectMapper();

		Userprofile[] resultSet = mapper.readValue(userString,
				Userprofile[].class);

		// TODO: expect exactly one result, throw exception if not
		Userprofile user = resultSet[0];

		return user;
	}

	public Integer getUseridByUsername() throws JsonParseException,
			JsonMappingException, IOException {
		String username = headerHelper
				.extractUsernameFromHeaders(authHeadersJersey);
		Userprofile user = requestUserByUsername(username);
		Integer userid = user.get_id();

		return userid;
	}

}
