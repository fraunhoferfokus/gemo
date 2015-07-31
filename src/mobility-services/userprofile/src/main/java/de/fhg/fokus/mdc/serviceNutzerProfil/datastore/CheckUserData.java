package de.fhg.fokus.mdc.serviceNutzerProfil.datastore;

// imports

/**
 * This class offers methods for checking the data to be stored in the datastore.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class CheckUserData {

	/** Singleton instance. */
	private static CheckUserData instance = null;
	
	/** Private constructor for the singleton pattern. */
	private CheckUserData () {}
	
	/**
	 * The method to get the singleton instance.
	 * 
	 * @return the singleton instance.
	 */
	public static CheckUserData getInstance () {
		if (instance == null) {
			instance = new CheckUserData ();
		}
		return instance;
	}
	
	
	/**
	 * The method checks the submitted user data.
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
	 * @return null if everything was okey, otherwise an error message.
	 */
	public String checkData(String vorname, String nachname, String nutzername,
			String passwort, String email, String geburtsdatum, String strasse,
			String hausnummer, String postalcode, String ort,
			String telefonnummer, String kontoinhaber, String kontonummer,
			String bankleitzahl, String praeferenzen, String fahrzeugtyp,
			String oEPNV_Affinitaet, String fuehrerschein_Klasse,
			String fuehrerschein_Datum, String fuehrerschein_Ort,
			String fuehrerschein_Nr, String sprache, String kreditkarten_Nr,
			String aGB_akzeptiert_Datum) {

		// the variable to return
		String toreturn = "";
		
		// check the parameter and compile an error message if required
		toreturn += checkFirstName(vorname);
		toreturn += checkLastName(nachname);
		toreturn += checkUserName(nutzername);
		toreturn += checkPassword(passwort);
		toreturn += checkEmail(email);
		toreturn += checkBirthDate(geburtsdatum);
		toreturn += checkStreet(strasse);
		toreturn += checkHouseNr(hausnummer);
		toreturn += checkPostalCode(postalcode);
		toreturn += checkLocation(ort);
		toreturn += checkPhoneNumber(telefonnummer);
		toreturn += checkBankAccountOwner(kontoinhaber);
		toreturn += checkBankAccountNumber(kontonummer);
		toreturn += checkBankCode(bankleitzahl);
		toreturn += checkPreferences(praeferenzen);
		toreturn += checkVehicleType(fahrzeugtyp);
		toreturn += checkPublicTransportAffinity(oEPNV_Affinitaet);
		toreturn += checkDrivingLicenseClass(fuehrerschein_Klasse);
		toreturn += checkDrivingLicenseDate(fuehrerschein_Datum);
		toreturn += checkDrivingLicenseLocation(fuehrerschein_Ort);
		toreturn += checkDrivingLicenseID(fuehrerschein_Nr);
		toreturn += checkLang(sprache);
		toreturn += checkCreditCardNumber(kreditkarten_Nr);
		toreturn += checkTermsAndConditionsDate(aGB_akzeptiert_Datum);
		
		if (toreturn.matches("^\\s*$")) {
			return null;
		}
		
		return toreturn;
	}

	/**
	 * The function checks the validity of the submitted username.
	 * @param nutzername the username to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkUserName(String nutzername) {
		// TODO Auto-generated method stub
		return "";
	}

	/**
	 * The function checks the date on which the "terms and conditions" were accepted.
	 * @param aGB_akzeptiert_Datum the date to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkTermsAndConditionsDate(String aGB_akzeptiert_Datum) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the credit card number.
	 * @param kreditkarten_Nr the credit card number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkCreditCardNumber(String kreditkarten_Nr) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the driving license ID.
	 * @param fuehrerschein_Nr the driving license ID to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkDrivingLicenseID(String fuehrerschein_Nr) {
		// TODO: implement fully.
		return "";

	}

	/**
	 * The function checks the credit card number.
	 * @param fuehrerschein_Ort the credit card number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkDrivingLicenseLocation(String fuehrerschein_Ort) {
		// TODO: implement fully.
		return "";

	}

	/**
	 * The function checks the credit card number.
	 * @param sprache the credit card number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkLang(String sprache) {
		// TODO: implement fully.
		return "";

	}

	/**
	 * The function checks the credit card number.
	 * @param fuehrerschein_Datum the credit card number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkDrivingLicenseDate(String fuehrerschein_Datum) {
		// TODO: implement fully.
		return "";

	}

	/**
	 * The function checks the credit card number.
	 * @param fuehrerschein_Klasse the credit card number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkDrivingLicenseClass(String fuehrerschein_Klasse) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the credit card number.
	 * @param praeferenzen the credit card number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkPreferences(String praeferenzen) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the credit card number.
	 * @param oEPNV_Affinitaet the credit card number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkPublicTransportAffinity(String oEPNV_Affinitaet) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the credit card number.
	 * @param fahrzeugtyp the credit card number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkVehicleType(String fahrzeugtyp) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the validity of the bank account owner.
	 * @param kontoinhaber the bank account owner to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkBankAccountOwner(String kontoinhaber) {
		// TODO: implement fully.
		return "";
	}
	
	/**
	 * The function checks the bank account number.
	 * @param kontoinhaber the bank account number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkBankAccountNumber(String kontonummer) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the credit card number.
	 * @param telefonnummer the credit card number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkPhoneNumber(String telefonnummer) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the submitted location where the customer lives.
	 * @param ort the submitted location.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkLocation(String ort) {
		// TODO: implement fully.
		return "";
	}
	
	
	/**
	 * The function checks the submitted bank code.
	 * @param bankleitzahl the bank code to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkBankCode(String postalcode) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the submitted postal code.
	 * @param postalcode the postal code to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkPostalCode(String postalcode) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the submitted house number.
	 * @param hausnummer the house number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkHouseNr(String hausnummer) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the submitted street.
	 * @param strasse the street to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkStreet(String strasse) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the the submitted birth date.
	 * @param geburtsdatum the birth date to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkBirthDate(String geburtsdatum) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the submitted email.
	 * @param kreditkarten_Nr email to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkEmail(String email) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the submitted password.
	 * @param passwort the password to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkPassword(String passwort) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the submitted last name.
	 * @param nachname the credit card number to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkLastName(String nachname) {
		// TODO: implement fully.
		return "";
	}

	/**
	 * The function checks the submitted first name.
	 * @param vorname the first name to check.
	 * 
	 * @return "" if okey, otherwise an error message.
	 */
	private String checkFirstName(String vorname) {
		// TODO: implement fully.
		return "";
	}
	
}
