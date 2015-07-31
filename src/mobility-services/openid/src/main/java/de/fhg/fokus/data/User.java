package de.fhg.fokus.data;

public class User {
	// private static final String base_openid =
	// "http://localhost:8080/openid/user/";
	private String firstName;
	private String lastName;
	private String username;
	private String hashed_pw;
	private String email;
	private String dob;
	private String street;
	private String housenr;
	private String postalcode;
	private String location;
	private String phonenumber;
	private String bankaccountowner;
	private String bankaccountnumber;
	private String bankcode;
	private String preferences;
	private String vehicletype;
	private String publictransportaffinity;
	private String drivinglicenseclass;
	private String drivinglicensedate;
	private String drivinglicenselocation;
	private String drivinglicenseid;
	private String lang;
	private String creditcardnumber;
	private String termsandconditionsdate;
	private String fullName;
	private String openid;

	public User(String firstName, String lastName, String username,
			String hashed_pw, String email, String dob, String street,
			String housenr, String postalcode, String location,
			String phonenumber, String bankaccountowner,
			String bankaccountnumber, String bankcode, String preferences,
			String vehicletype, String publictransportaffinity,
			String drivinglicenseclass, String drivinglicensedate,
			String drivinglicenselocation, String drivinglicenseid,
			String lang, String creditcardnumber, String termsandconditionsdate) {

		setFirstName(firstName);
		setLastName(lastName);
		setFullName(firstName + " " + lastName);
		setUsername(username);
		setHashed_pw(hashed_pw);
		setEmailAddress(email);
		setDateOfBirth(dob);
		setStreet(street);
		setHousenr(housenr);
		setPostalcode(postalcode);
		setLocation(location);
		setPhonenumber(phonenumber);
		setBankaccountowner(bankaccountowner);
		setBankaccountnumber(bankaccountnumber);
		setBankcode(bankcode);
		setPreferences(preferences);
		setVehicletype(vehicletype);
		setPublictransportaffinity(publictransportaffinity);
		setDrivinglicenseclass(drivinglicenseclass);
		setDrivinglicensedate(drivinglicensedate);
		setDrivinglicenselocation(drivinglicenselocation);
		setDrivinglicenseid(drivinglicenseid);
		setLang(lang);
		setCreditcardnumber(creditcardnumber);
		setTermsandconditionsdate(termsandconditionsdate);
	}

	public User(String username) {
		setUsername(username);
	}

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setHashed_pw(String hashed_pw) {
		this.hashed_pw = hashed_pw;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHousenr() {
		return housenr;
	}

	public void setHousenr(String housenr) {
		this.housenr = housenr;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getBankaccountowner() {
		return bankaccountowner;
	}

	public void setBankaccountowner(String bankaccountowner) {
		this.bankaccountowner = bankaccountowner;
	}

	public String getBankaccountnumber() {
		return bankaccountnumber;
	}

	public void setBankaccountnumber(String bankaccountnumber) {
		this.bankaccountnumber = bankaccountnumber;
	}

	public String getPreferences() {
		return preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public String getVehicletype() {
		return vehicletype;
	}

	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}

	public String getPublictransportaffinity() {
		return publictransportaffinity;
	}

	public void setPublictransportaffinity(String publictransportaffinity) {
		this.publictransportaffinity = publictransportaffinity;
	}

	public String getDrivinglicenseclass() {
		return drivinglicenseclass;
	}

	public void setDrivinglicenseclass(String drivinglicenseclass) {
		this.drivinglicenseclass = drivinglicenseclass;
	}

	public String getDrivinglicensedate() {
		return drivinglicensedate;
	}

	public void setDrivinglicensedate(String drivinglicensedate) {
		this.drivinglicensedate = drivinglicensedate;
	}

	public String getDrivinglicenselocation() {
		return drivinglicenselocation;
	}

	public void setDrivinglicenselocation(String drivinglicenselocation) {
		this.drivinglicenselocation = drivinglicenselocation;
	}

	public String getDrivinglicenseid() {
		return drivinglicenseid;
	}

	public void setDrivinglicenseid(String drivinglicenseid) {
		this.drivinglicenseid = drivinglicenseid;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCreditcardnumber() {
		return creditcardnumber;
	}

	public void setCreditcardnumber(String creditcardnumber) {
		this.creditcardnumber = creditcardnumber;
	}

	public String getTermsandconditionsdate() {
		return termsandconditionsdate;
	}

	public void setTermsandconditionsdate(String termsandconditionsdate) {
		this.termsandconditionsdate = termsandconditionsdate;
	}

	// public static String getBaseOpenid() {
	// return base_openid;
	// }

	public void setDateOfBirth(String time) {
		this.dob = time;
	}

	public void setEmailAddress(String string) {
		this.email = string;
	}

	public void setFullName(String string) {
		this.fullName = string;
	}

	public void setOpenId(String userSelectedId) {
		this.openid = userSelectedId;

	}

	public String getFullName() {
		return this.fullName;
	}

	public String getDateOfBirth() {
		return this.dob;
	}

	public String getEmailAddress() {
		return this.email;
	}

	public String getOpenid() {
		return this.openid;
	}

	public String getHashedPassword() {
		return this.hashed_pw;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
