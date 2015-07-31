package de.fhg.fokus.mdc.serviceReservierungEfz.totp;

import java.io.IOException;
import java.util.Date;

public class SHA1TOTPGenerator extends TOTPGenerator {

	private static final String hashAlgorithm = "HmacSHA1";

	public SHA1TOTPGenerator() throws IOException {
		this.seedQuery = "select * from seeds where seedtype=" + "\'"
				+ "byte20" + "\'";
		fetchSeed();
	}

	@Override
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
		// TODO option to set length of key
		String key = keygen.getSaltedHashKey(MessageDigestAlgorithm.SHA_1);
		return key;
	}
}
