package de.fhg.fokus.mdc.serviceRegistrierung.services;

// imports

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_SEARCH;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_SEARCH_PARAMETER;
import static de.fhg.fokus.mdc.propertyProvider.Constants.STORAGE_URI;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_USERPROFILE;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_USERPROFILE_ADD_USER;

import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.mdc.serviceRegistrierung.userprofile.UserProfileClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Example call in a browser:
 * http://localhost:8080/Registrierung/register?Vorname=Benjamin&Nachname=Dittwald&Nutzername=bdi&Passwort=pwd&Email=benjamin.dittwald@fokus.fraunhofer.de&Geburtsdatum=01.01.1901&Strasse=test-strasse&Hausnummer=38&PLZ=13187&Ort=Berlin&Telefonnummer=123456&Kontoinhaber=Benjamin%20Dittwald&Kontonummer=1111111&Bankleitzahl=111111&Praeferenzen=12&Fahrzeugtyp=van&OEPNV_Affinitaet=4&Fuehrerschein_Klasse=A&Fuehrerschein_Datum=01.01.01&Fuehrerschein_Ort=Berlin&Fuehrerschein_Nr=11111&Sprache=DE&Kreditkarten_Nr=12345&AGB_akzeptiert_Datum=01.01.01
 */

/**
 * The class implements the Registrierung service.
 *
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
@Path("/register")
public class RegistrationService {

	/** The logger of the class. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationService.class.getClass());

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
			LOGGER.error("Error on loading properties", e);
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
	 * @param Vorname
	 *            the first name.
	 * @param Nachname
	 *            the last name.
	 * @param Nutzername
	 *            the user name.
	 * @param Passwort
	 *            the password.
	 * @param Email
	 *            the email address.
	 * @param Geburtsdatum
	 *            the birthday.
	 * @param Strasse
	 *            the street.
	 * @param Hausnummer
	 *            the house number.
	 * @param PLZ
	 *            the postal code.
	 * @param Ort
	 *            the location.
	 * @param Telefonnummer
	 *            the phone number.
	 * @param Kontoinhaber
	 *            the bank account owner.
	 * @param Kontonummer
	 *            the bank account.
	 * @param Bankleitzahl
	 *            the bank code number.
	 * @param Praeferenzen
	 *            the preferences.
	 * @param Fahrzeugtyp
	 *            the type of preferred vehicle.
	 * @param OEPNV_Affinitaet
	 *            a rating for the affinity to use the public transport.
	 * @param Fuehrerschein_Klasse
	 *            the class of the driving license.
	 * @param Fuehrerschein_Datum
	 *            the date at which the driving license was issued.
	 * @param Fuehrerschein_Ort
	 *            the location at which the driving license was issued.
	 * @param Fuehrerschein_Nr
	 *            the ID of the driving license.
	 * @param Sprache
	 *            the language in which the user would like to use the system.
	 * @param Kreditkarten_Nr
	 *            the credit card number.
	 * @param AGB_akzeptiert_Datum
	 *            the date at the which the "terms and conditions" were
	 *            accepted.
	 *
	 * @return a String representing the authentication token.
	 * @throws IOException
	 *             an IO Exception in case of a corresponding fault.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String registerUser(@QueryParam("Vorname") String Vorname,
			@QueryParam("Nachname") String Nachname,
			@QueryParam("Nutzername") String Nutzername,
			@QueryParam("Passwort") String Passwort,
			@QueryParam("Email") String Email,
			@QueryParam("Geburtsdatum") String Geburtsdatum,
			@QueryParam("Strasse") String Strasse,
			@QueryParam("Hausnummer") String Hausnummer,
			@QueryParam("PLZ") String PLZ, @QueryParam("Ort") String Ort,
			@QueryParam("Telefonnummer") String Telefonnummer,
			@QueryParam("Kontoinhaber") String Kontoinhaber,
			@QueryParam("Kontonummer") String Kontonummer,
			@QueryParam("Bankleitzahl") String Bankleitzahl,
			@QueryParam("Praeferenzen") String Praeferenzen,
			@QueryParam("Fahrzeugtyp") String Fahrzeugtyp,
			@QueryParam("OEPNV_Affinitaet") String OEPNV_Affinitaet,
			@QueryParam("Fuehrerschein_Klasse") String Fuehrerschein_Klasse,
			@QueryParam("Fuehrerschein_Datum") String Fuehrerschein_Datum,
			@QueryParam("Fuehrerschein_Ort") String Fuehrerschein_Ort,
			@QueryParam("Fuehrerschein_Nr") String Fuehrerschein_Nr,
			@QueryParam("Sprache") String Sprache,
			@QueryParam("Kreditkarten_Nr") String Kreditkarten_Nr,
			@QueryParam("AGB_akzeptiert_Datum") String AGB_akzeptiert_Datum)
			throws IOException {

		// System.out.println("Nachname:" + Nachname);
		// System.out.println("Nutzername:" + Nutzername);
		// System.out.println("Passwort:" + Passwort);
		// System.out.println("Email:" + Email);
		// System.out.println("Geburtsdatum:" + Geburtsdatum);
		// System.out.println("Strasse:" + Strasse);
		// System.out.println("Hausnummer:" + Hausnummer);
		// System.out.println("PLZ:" + PLZ);
		// System.out.println("Ort:" + Ort);
		// System.out.println("Telefonnummer:" + Telefonnummer);
		// System.out.println("Kontoinhaber:" + Kontoinhaber);
		// System.out.println("Kontonummer:" + Kontonummer);
		// System.out.println("Bankleitzahl:" + Bankleitzahl);
		// System.out.println("Praeferenzen:" + Praeferenzen);
		// System.out.println("Fahrzeugtyp:" + Fahrzeugtyp);
		// System.out.println("OEPNV_Affinitaet:" + OEPNV_Affinitaet);
		// System.out.println("Fuehrerschein_Klasse:" + Fuehrerschein_Klasse);
		// System.out.println("Fuehrerschein_Datum:" + Fuehrerschein_Datum);
		// System.out.println("Fuehrerschein_Ort:" + Fuehrerschein_Ort);
		// System.out.println("Fuehrerschein_Nr:" + Fuehrerschein_Nr);
		// System.out.println("Sprache:" + Sprache);
		// System.out.println("Kreditkarten_Nr:" + Kreditkarten_Nr);
		// System.out.println("AGB_akzeptiert_Datum:" + AGB_akzeptiert_Datum);

		// trigger the method for adding the new user
		String registrationResult = UserProfileClient.getInstance(
				pathToUserProfile, addUserStr).addNewUser(Vorname,
				Nachname, Nutzername, Passwort, Email, Geburtsdatum, Strasse,
				Hausnummer, PLZ, Ort, Telefonnummer, Kontoinhaber, Kontonummer,
				Bankleitzahl, Praeferenzen, Fahrzeugtyp, OEPNV_Affinitaet,
				Fuehrerschein_Klasse, Fuehrerschein_Datum, Fuehrerschein_Ort,
				Fuehrerschein_Nr, Sprache, Kreditkarten_Nr,
				AGB_akzeptiert_Datum);

		// return an error message in case there is one
		if (registrationResult != null) {
			return registrationResult;
		}

		// get the authorization token
		String authorizationToken = RegistrationFunctions.getInstance()
				.getAuthorizationToken();

		return authorizationToken;
	}
}
