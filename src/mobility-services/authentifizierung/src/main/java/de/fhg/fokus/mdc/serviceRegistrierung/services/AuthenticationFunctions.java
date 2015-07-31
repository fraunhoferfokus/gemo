package de.fhg.fokus.mdc.serviceRegistrierung.services;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class contains key functions that support the registration process.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class AuthenticationFunctions {

	/** The singleton instance. */
	private static AuthenticationFunctions instance = null;

	/**
	 * Constructor for the singleton instance.
	 */
	private AuthenticationFunctions() {
	}

	/**
	 * The function to get the singleton instance.
	 * 
	 * @return the singleton instance.
	 */
	public static AuthenticationFunctions getInstance() {
		if (instance == null) {
			instance = new AuthenticationFunctions();
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
