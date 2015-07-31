package de.fhg.fokus.mdc.serviceRegistrierung.services;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class contains key functions that support the registration process.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class RegistrationFunctions {

	/** The singleton instance. */
	private static RegistrationFunctions instance = null;

	/**
	 * Constructor for the singleton instance.
	 */
	private RegistrationFunctions() {
	}

	/**
	 * The function to get the singleton instance.
	 * 
	 * @return the singleton instance.
	 */
	public static RegistrationFunctions getInstance() {
		if (instance == null) {
			instance = new RegistrationFunctions();
		}

		return instance;
	}

	/**
	 * The function returns an authorization token.
	 * 
	 * @return the authorization token.
	 */
	public String getAuthorizationToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
}
