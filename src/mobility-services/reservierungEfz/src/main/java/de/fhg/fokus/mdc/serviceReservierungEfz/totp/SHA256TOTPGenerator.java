package de.fhg.fokus.mdc.serviceReservierungEfz.totp;

import java.io.IOException;
import java.util.Date;

public class SHA256TOTPGenerator extends TOTPGenerator {
	private static final String hashAlgorithm = "HmacSHA256";

	public SHA256TOTPGenerator() throws IOException {
		this.seedQuery = "select * from seeds where seedtype" + "\'" + "byte32"
				+ "\'";
		fetchSeed();
	}

	public String generateToken(Date resBegin, String numberOfDigits) {
		// convert reservierungsbegin to long
		long timeFrom = this.dateToUnixTime(resBegin);
		// calculate T
		String steps = this.calculateTimeStepsDelta(timeFrom);
		String totpToken = TOTP.generateTOTP(seed, steps, "8", hashAlgorithm);
		return totpToken;
	}

	// use this method if key is not static any more
	private String generateSeed() {
		Key4TOTPGenerator keygen = new Key4TOTPGenerator();
		String key = keygen.getSaltedHashKey(MessageDigestAlgorithm.SHA_256);
		return key;
	}
}
