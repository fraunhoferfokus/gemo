package de.fhg.fokus.mdc.serviceReservierungEfz.totp;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import de.fhg.fokus.mdc.clients.SeedDataStoreClient;
import de.fhg.fokus.mdc.jsonObjects.TotpSeed;

public abstract class TOTPGenerator {
	private TOTP totp;

	long T0 = 0;
	long X = 30;
	String steps = "0";
	public final static String RESERVATION_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	protected String seedQuery;
	protected String seed;

	protected void fetchSeed() throws JsonParseException, JsonMappingException,
			IOException {
		SeedDataStoreClient storeClient = SeedDataStoreClient.getInstance();
		String storageResponse = storeClient.getByQuery(seedQuery);

		// The response is an array, we must grab the first
		ObjectMapper mapper = new ObjectMapper();
		TotpSeed[] resultSet = mapper.readValue(storageResponse,
				TotpSeed[].class);
		if (resultSet.length != 0)
			seed = resultSet[0].getSeed();

	}

	public long dateToUnixTime(Date resBegin) {
		long unixTimeInMiliseconds = resBegin.getTime();
		long unixTimeInSeconds = TimeUnit.MILLISECONDS
				.toSeconds(unixTimeInMiliseconds);
		return unixTimeInSeconds;
	}

	private String unixTimetoUTCString(long testTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		String utcTime = df.format(new Date(testTime * 1000));
		return utcTime;
	}

	// calculate T
	// the original : T = (Current Unix time - T0) / X
	// gemo : T = (reservationFrom Unix time - T0) / X
	protected String calculateTimeStepsDelta(long targetTime) {
		long T = (targetTime - T0) / X;
		steps = Long.toHexString(T).toUpperCase();
		// pad T with 0s on the left side till 16 digits
		while (steps.length() < 16)
			steps = "0" + steps;
		return steps;

	}

	abstract public String generateToken(Date resBegin, String numberOfDigits);

}
