package de.fhg.fokus.mdc.serviceNutzerProfil.services;

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

import de.fhg.fokus.mdc.serviceNutzerProfil.datastore.UserProfileDataStoreClient;

/**
 * The class implements the service for getting the details of a user profile.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * @author izi
 */
@Path("/get_user_details")
public class GetUserDetailsService {

	/** The logger of the class. */
	private static Logger log = Logger.getLogger(GetUserDetailsService.class
			.getName());

	/**
	 * The method obtains the details for a particular user.
	 * 
	 * @param username
	 *            the name of the user to check for.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String query(@QueryParam("username") String username,
			@Context HttpHeaders headers) throws IOException {
		// check the parameter
		if (username == null || username.matches("^\\s*$")) {
			return "FAILED: NO USERNAME SPECIFIED";
		}

		UserProfileDataStoreClient userClient = UserProfileDataStoreClient
				.getInstance();
		userClient.setAuthHeaders(headers);
		String userDetails = userClient.getUserDetails(username);

		if (userDetails == null || userDetails.matches("^\\s*$")) {
			return "NO USER DETAILS FOUND";
		}
		return userDetails;
	}
}
