package de.fhg.fokus.mdc.serviceNutzerProfil.services;

// imports
import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import de.fhg.fokus.mdc.serviceNutzerProfil.datastore.UserProfileDataStoreClient;

/**
 * The class implements the service for updating details of a user profile.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * @author izi
 */
@Path("/update_user_details")
public class UpdateUserDetailsService {

	/** The logger of the class. */
	private static Logger log = Logger.getLogger(UpdateUserDetailsService.class
			.getName());

	/**
	 * The method realizes the update of the user details.
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
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateUserProfile(@FormParam("Vorname") String Vorname,
			@FormParam("Nachname") String Nachname,
			@FormParam("Nutzername") String Nutzername,
			@FormParam("Passwort") String Passwort,
			@FormParam("Email") String Email,
			@FormParam("Geburtsdatum") String Geburtsdatum,
			@FormParam("Strasse") String Strasse,
			@FormParam("Hausnummer") String Hausnummer,
			@FormParam("PLZ") String PLZ, @FormParam("Ort") String Ort,
			@FormParam("Telefonnummer") String Telefonnummer,
			@FormParam("Kontoinhaber") String Kontoinhaber,
			@FormParam("Kontonummer") String Kontonummer,
			@FormParam("Bankleitzahl") String Bankleitzahl,
			@FormParam("Praeferenzen") String Praeferenzen,
			@FormParam("Fahrzeugtyp") String Fahrzeugtyp,
			@FormParam("OEPNV_Affinitaet") String OEPNV_Affinitaet,
			@FormParam("Fuehrerschein_Klasse") String Fuehrerschein_Klasse,
			@FormParam("Fuehrerschein_Datum") String Fuehrerschein_Datum,
			@FormParam("Fuehrerschein_Ort") String Fuehrerschein_Ort,
			@FormParam("Fuehrerschein_Nr") String Fuehrerschein_Nr,
			@FormParam("Sprache") String Sprache,
			@FormParam("Kreditkarten_Nr") String Kreditkarten_Nr,
			@FormParam("AGB_akzeptiert_Datum") String AGB_akzeptiert_Datum,
			@Context HttpHeaders headers) throws IOException {

		UserProfileDataStoreClient userClient = UserProfileDataStoreClient
				.getInstance();
		userClient.setAuthHeaders(headers);

		// add the new user
		String response = userClient.updateUserDetails(Vorname, Nachname,
				Nutzername, Passwort, Email, Geburtsdatum, Strasse, Hausnummer,
				PLZ, Ort, Telefonnummer, Kontoinhaber, Kontonummer,
				Bankleitzahl, Praeferenzen, Fahrzeugtyp, OEPNV_Affinitaet,
				Fuehrerschein_Klasse, Fuehrerschein_Datum, Fuehrerschein_Ort,
				Fuehrerschein_Nr, Sprache, Kreditkarten_Nr,
				AGB_akzeptiert_Datum);

		// respond according to the returned value
		if (response == null || response.matches("^\\s*$")) {
			return "OK";
		}

		// return back the error response
		return response;
	}
}
