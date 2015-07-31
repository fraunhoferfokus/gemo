package de.fhg.fokus.mdc.serviceReservierungEfz.totp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Key4TOTPGenerator {

	/**
	 * The logger of the class.
	 */
	private final Logger keyLogger = LoggerFactory.getLogger(getClass());

	public String getSaltedHashKey(String mode) {
		String passwordToHash = "password";
		String salt;
		try {
			salt = getSalt();
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException("");
		}

		String securePassword = get_SHA_Key(passwordToHash, salt, mode);

		return securePassword;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {

		Key4TOTPGenerator ktotp = new Key4TOTPGenerator();
		ktotp.getSaltedHashKey(MessageDigestAlgorithm.SHA_1);
		ktotp.getSaltedHashKey(MessageDigestAlgorithm.SHA_256);
		ktotp.getSaltedHashKey(MessageDigestAlgorithm.SHA_512);
	}

	private String get_SHA_Key(String passwordToHash, String salt, String algo) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algo);
			md.update(salt.getBytes());
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			generatedPassword = sb.toString();
			keyLogger.debug(generatedPassword);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return generatedPassword;
	}

	// Add salt
	private String getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}
}
