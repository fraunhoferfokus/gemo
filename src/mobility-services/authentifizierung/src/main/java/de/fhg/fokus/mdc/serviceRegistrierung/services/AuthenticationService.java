package de.fhg.fokus.mdc.serviceRegistrierung.services;

// imports

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_SEARCH;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_SEARCH_PARAMETER;
import static de.fhg.fokus.mdc.propertyProvider.Constants.STORAGE_URI;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_USERPROFILE;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_USERPROFILE_ADD_USER;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;



/**
 * The class implements the Authentifizierung service.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
@Path("/authenticate")
public class AuthenticationService {

	/** The logger of the class. */
	private static Logger log = Logger.getLogger(AuthenticationService.class
			.getName());

	/** The path to the data store. */
	private static final String pathToDataStore;
	private static final String pathToSearch;
	private static final String qParam;

	/** The paths to the user profile service. */
	private static final String pathToUserProfile;
	private static final String addUserStr;

	private static Properties props = null;

	static {
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pathToDataStore = props.getProperty(STORAGE_URI);
		pathToSearch = props.getProperty(SERVICE_URI_STORAGE_SEARCH);
		qParam = props.getProperty(SERVICE_URI_STORAGE_SEARCH_PARAMETER);

		pathToUserProfile = props.getProperty(SERVICE_URI_USERPROFILE);
		addUserStr =  props.getProperty(SERVICE_URI_USERPROFILE_ADD_USER);
	}

	/**
	 * The method realizes the registration of a new user.
	 * 
	 * @param Nutzername
	 *            the user name.
	 * @param Passwort
	 *            the password.
	 * 
	 * @return a String representing the authentication token.
	 * @throws IOException
	 *             an IO Exception in case of a corresponding fault.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String registerUser(
			@QueryParam("Nutzername") String Nutzername,
			@QueryParam("Passwort") String Passwort)
			throws IOException {


		// get the authorization token
		String authorizationToken = AuthenticationFunctions.getInstance()
				.getAuthorizationToken();

		return authorizationToken;
	}
}
