package de.fhg.fokus.mdc.serviceNutzerProfil.datastore;

import static de.fhg.fokus.mdc.propertyProvider.Constants.USERS_TABLE;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import de.fhg.fokus.mdc.clients.DataStoreClient;

public class UserProfileDataStoreClient extends DataStoreClient {

	private static UserProfileDataStoreClient instance = null;

	public static UserProfileDataStoreClient getInstance() {
		if (instance == null) {
			instance = new UserProfileDataStoreClient();
		}
		return instance;
	}

	private UserProfileDataStoreClient() {
		super();
	}

	@Override
	public void defineTableName() {
		table = props.getProperty(USERS_TABLE);

	}

	public String getUserDetails(String username) {

		String userDetails = select("select * from " + table
				+ " where username=\'" + username + "\'");
		return userDetails;
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

		// invoke the function
		String errorMessage = CheckUserData.getInstance().checkData(vorname,
				nachname, nutzername, passwort, email, geburtsdatum, strasse,
				hausnummer, pLZ, ort, telefonnummer, kontoinhaber, kontonummer,
				bankleitzahl, praeferenzen, fahrzeugtyp, oEPNV_Affinitaet,
				fuehrerschein_Klasse, fuehrerschein_Datum, fuehrerschein_Ort,
				fuehrerschein_Nr, sprache, kreditkarten_Nr,
				aGB_akzeptiert_Datum);

		// return the error message in case of wrong data
		if (errorMessage != null) {
			return errorMessage;
		}

		// check if the user already exists
		String userData = select("select count(*) from " + table
				+ " where username=\'" + nutzername + "\'");

		if (!userData.trim().endsWith(":0}]")) {
			return "USER ALREADY EXISTS";
		}

		// the to string to return
		String toreturn = "OK";

		// prepare the post parameter
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("tableName", table);

		map.add("FirstName", vorname);
		map.add("LastName", nachname);
		map.add("UserName", nutzername);
		map.add("Password", passwort);
		map.add("Email", email);
		map.add("BirthDate", geburtsdatum);
		map.add("Street", strasse);
		map.add("HouseNr", hausnummer);
		map.add("PostalCode", pLZ);
		map.add("Location", ort);
		map.add("PhoneNumber", telefonnummer);
		map.add("BankAccountOwner", kontoinhaber);
		map.add("BankAccountNumber", kontonummer);
		map.add("BankCode", bankleitzahl);
		map.add("Preferences", praeferenzen);
		map.add("VehicleType", fahrzeugtyp);
		map.add("PublicTransportAffinity", oEPNV_Affinitaet);
		map.add("DrivingLicenseClass", fuehrerschein_Klasse);
		map.add("DrivingLicenseDate", fuehrerschein_Datum);
		map.add("DrivingLicenseLocation", fuehrerschein_Ort);
		map.add("DrivingLicenseID", fuehrerschein_Nr);
		map.add("Lang", sprache);
		map.add("CreditCardNumber", kreditkarten_Nr);
		map.add("TermsAndConditionsDate", aGB_akzeptiert_Datum);

		// issue the post request
		toreturn = insert(map);
		if (toreturn == null) {
			toreturn = "FAIL";
		}

		return toreturn;
	}

	/**
	 * The function updates the user details in the datastore.
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
	public String updateUserDetails(String vorname, String nachname,
			String nutzername, String passwort, String email,
			String geburtsdatum, String strasse, String hausnummer, String pLZ,
			String ort, String telefonnummer, String kontoinhaber,
			String kontonummer, String bankleitzahl, String praeferenzen,
			String fahrzeugtyp, String oEPNV_Affinitaet,
			String fuehrerschein_Klasse, String fuehrerschein_Datum,
			String fuehrerschein_Ort, String fuehrerschein_Nr, String sprache,
			String kreditkarten_Nr, String aGB_akzeptiert_Datum) {

		// invoke the function
		String errorMessage = CheckUserData.getInstance().checkData(vorname,
				nachname, nutzername, passwort, email, geburtsdatum, strasse,
				hausnummer, pLZ, ort, telefonnummer, kontoinhaber, kontonummer,
				bankleitzahl, praeferenzen, fahrzeugtyp, oEPNV_Affinitaet,
				fuehrerschein_Klasse, fuehrerschein_Datum, fuehrerschein_Ort,
				fuehrerschein_Nr, sprache, kreditkarten_Nr,
				aGB_akzeptiert_Datum);

		// return the error message in case of wrong data
		if (errorMessage != null) {
			return errorMessage;
		}

		// the to string to return
		String toreturn = "OK";

		// move on to add the user

		// prepare the post parameter
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("tableName", table);
		map.add("where", "UserName=\'" + nutzername + "\'");

		if (vorname != null) {
			map.add("FirstName", vorname);
		}
		if (nachname != null) {
			map.add("LastName", nachname);
		}

		if (nutzername != null) {
			map.add("UserName", nutzername);
		}

		if (passwort != null) {
			map.add("Password", passwort);
		}

		if (email != null) {
			map.add("Email", email);
		}

		if (geburtsdatum != null) {
			map.add("BirthDate", geburtsdatum);
		}

		if (strasse != null) {
			map.add("Street", strasse);
		}

		if (hausnummer != null) {
			map.add("HouseNr", hausnummer);
		}

		if (pLZ != null) {
			map.add("PostalCode", pLZ);
		}

		if (ort != null) {
			map.add("Location", ort);
		}

		if (telefonnummer != null) {
			map.add("PhoneNumber", telefonnummer);
		}

		if (kontoinhaber != null) {
			map.add("BankAccountOwner", kontoinhaber);
		}

		if (kontonummer != null) {
			map.add("BankAccountNumber", kontonummer);
		}

		if (bankleitzahl != null) {
			map.add("BankCode", bankleitzahl);
		}

		if (bankleitzahl != null) {
			map.add("BankCode", bankleitzahl);
		}

		if (praeferenzen != null) {
			map.add("Preferences", praeferenzen);
		}

		if (fahrzeugtyp != null) {
			map.add("VehicleType", fahrzeugtyp);
		}

		if (oEPNV_Affinitaet != null) {
			map.add("PublicTransportAffinity", oEPNV_Affinitaet);
		}

		if (fuehrerschein_Klasse != null) {
			map.add("DrivingLicenseClass", fuehrerschein_Klasse);
		}

		if (fuehrerschein_Datum != null) {
			map.add("DrivingLicenseDate", fuehrerschein_Datum);
		}

		if (fuehrerschein_Ort != null) {
			map.add("DrivingLicenseLocation", fuehrerschein_Ort);
		}

		if (fuehrerschein_Nr != null) {
			map.add("DrivingLicenseID", fuehrerschein_Nr);
		}

		if (sprache != null) {
			map.add("Lang", sprache);
		}

		if (kreditkarten_Nr != null) {
			map.add("CreditCardNumber", kreditkarten_Nr);
		}

		if (aGB_akzeptiert_Datum != null) {
			map.add("TermsAndConditionsDate", aGB_akzeptiert_Datum);
		}

		// issue the post request
		toreturn = update(map);
		if (toreturn == null) {
			toreturn = "FAIL";
		}

		return null;
	}

}
