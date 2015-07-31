package de.fhg.fokus.mdc.serviceRegistrierung.userprofile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//imports
import org.springframework.web.client.RestTemplate;

/**
 * The class implements the methods to access the user profile service.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class UserProfileClient {

	/** The instance for the singleton pattern. */
	private static UserProfileClient instance = null;

	/** The url string. */
	private String url = null;

	/** Url substring for addin a new user. */
	private String addUserStr = null;

	/** The local REST template. */
	private RestTemplate restTemplate = null;

	/**
	 * Function to deliver the singleton instance.
	 * 
	 * @param url
	 *            the url of the REST service.
	 * @param searchStr
	 *            url substring for adding a user.
	 * @return the pre-configured data store client.
	 */
	public static UserProfileClient getInstance(String url, String addUserStr) {
		if (instance == null) {
			instance = new UserProfileClient(url, addUserStr);
		}

		return instance;
	}

	/**
	 * Function to deliver the singleton instance.
	 * 
	 * @param url
	 *            the url of the REST service.
	 * @param searchStr
	 *            url substring for adding a user.
	 * @return the pre-configured data store client.
	 */
	private UserProfileClient(String url, String addUserStr) {
		this.url = url;
		this.addUserStr = addUserStr;

		restTemplate = new RestTemplate();
	}

	/**
	 * The function adds a new user to the database.
	 * 
	 * @param vorname
	 *            the first name.
	 * @param nachname
	 *            the last name.
	 * @param nutzername
	 *            the user name.
	 * @param passwort
	 *            the password.
	 * @param email
	 *            the email address.
	 * @param geburtsdatum
	 *            the birthday.
	 * @param strasse
	 *            the street.
	 * @param hausnummer
	 *            the house number.
	 * @param postalcode
	 *            the postalcode.
	 * @param ort
	 *            the location.
	 * @param telefonnummer
	 *            the phone number.
	 * @param kontoinhaber
	 *            the bank account owner.
	 * @param kontonummer
	 *            the bank account.
	 * @param bankleitzahl
	 *            the bank code number.
	 * @param praeferenzen
	 *            the preferences.
	 * @param fahrzeugtyp
	 *            the type of preferred vehicle.
	 * @param oEPNV_Affinitaet
	 *            a rating for the affinity to use the public transport.
	 * @param fuehrerschein_Klasse
	 *            the class of the driving license.
	 * @param fuehrerschein_Datum
	 *            the date at which the driving license was issued.
	 * @param fuehrerschein_Ort
	 *            the location at which the driving license was issued.
	 * @param fuehrerschein_Nr
	 *            the ID of the driving license.
	 * @param sprache
	 *            the language in which the user would like to use the system.
	 * @param kreditkarten_Nr
	 *            the credit card number.
	 * @param aGB_akzeptiert_Datum
	 *            the date at the which the "terms and conditions" were
	 *            accepted.
	 * @return null in case of a success, otherwise an error message.
	 */
	public String addNewUser(String vorname, String nachname,
			String nutzername, String passwort, String email,
			String geburtsdatum, String strasse, String hausnummer, String pLZ,
			String ort, String telefonnummer, String kontoinhaber,
			String kontonummer, String bankleitzahl, String praeferenzen,
			String fahrzeugtyp, String oEPNV_Affinitaet,
			String fuehrerschein_Klasse, String fuehrerschein_Datum,
			String fuehrerschein_Ort, String fuehrerschein_Nr, String sprache,
			String kreditkarten_Nr, String aGB_akzeptiert_Datum) {

		// move on to add the user
		String requestPath = "";
		try {
			// prepare the request path
			requestPath += "Vorname=" + URLEncoder.encode(vorname, "UTF-8");
			requestPath += "&Nachname=" + URLEncoder.encode(nachname, "UTF-8");
			requestPath += "&Nutzername="
					+ URLEncoder.encode(nutzername, "UTF-8");
			requestPath += "&Passwort=" + URLEncoder.encode(passwort, "UTF-8");
			requestPath += "&Email=" + URLEncoder.encode(email, "UTF-8");
			requestPath += "&Geburtsdatum="
					+ URLEncoder.encode(geburtsdatum, "UTF-8");
			requestPath += "&Strasse=" + URLEncoder.encode(strasse, "UTF-8");
			requestPath += "&Hausnummer="
					+ URLEncoder.encode(hausnummer, "UTF-8");
			requestPath += "&PLZ=" + URLEncoder.encode(pLZ, "UTF-8");
			requestPath += "&Ort=" + URLEncoder.encode(ort, "UTF-8");
			requestPath += "&Telefonnummer="
					+ URLEncoder.encode(telefonnummer, "UTF-8");
			requestPath += "&Kontoinhaber="
					+ URLEncoder.encode(kontoinhaber, "UTF-8");
			requestPath += "&Kontonummer="
					+ URLEncoder.encode(kontonummer, "UTF-8");
			requestPath += "&Bankleitzahl="
					+ URLEncoder.encode(bankleitzahl, "UTF-8");
			requestPath += "&Praeferenzen="
					+ URLEncoder.encode(praeferenzen, "UTF-8");
			requestPath += "&Fahrzeugtyp="
					+ URLEncoder.encode(fahrzeugtyp, "UTF-8");
			requestPath += "&OEPNV_Affinitaet="
					+ URLEncoder.encode(oEPNV_Affinitaet, "UTF-8");
			requestPath += "&Fuehrerschein_Klasse="
					+ URLEncoder.encode(fuehrerschein_Klasse, "UTF-8");
			requestPath += "&Fuehrerschein_Datum="
					+ URLEncoder.encode(fuehrerschein_Datum, "UTF-8");
			requestPath += "&Fuehrerschein_Ort="
					+ URLEncoder.encode(fuehrerschein_Ort, "UTF-8");
			requestPath += "&Fuehrerschein_Nr="
					+ URLEncoder.encode(fuehrerschein_Nr, "UTF-8");
			requestPath += "&Sprache=" + URLEncoder.encode(sprache, "UTF-8");
			requestPath += "&Kreditkarten_Nr="
					+ URLEncoder.encode(kreditkarten_Nr, "UTF-8");
			requestPath += "&AGB_akzeptiert_Datum="
					+ URLEncoder.encode(aGB_akzeptiert_Datum, "UTF-8");

			requestPath = url + addUserStr + "?" + requestPath;
		} catch (UnsupportedEncodingException e) {
			return null;
		}

		System.out.println("Request Path: " + requestPath);
		String result = restTemplate.getForObject(requestPath, String.class);
		if (!result.matches("^\\s*OK\\s*$")) {
			return result;
		}

		return null;
	}
}