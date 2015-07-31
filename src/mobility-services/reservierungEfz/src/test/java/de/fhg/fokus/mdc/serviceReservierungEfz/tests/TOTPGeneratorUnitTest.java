package de.fhg.fokus.mdc.serviceReservierungEfz.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.fokus.mdc.serviceReservierungEfz.services.VehicleReservationService;
import de.fhg.fokus.mdc.serviceReservierungEfz.totp.SHA1TOTPGenerator;
import de.fhg.fokus.mdc.serviceReservierungEfz.totp.TOTPGenerator;
import de.fhg.fokus.mdc.utils.SimpleValidator;

public class TOTPGeneratorUnitTest {

	@Test
	public void testUTCDatetoUnixTime() throws ParseException {

		// in "yyyy-MM-dd HH:mm:ss" pattern
		String timeFrom = "2005-03-18 01:58:31";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		// setting UTC to tell string is in UTC
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date resBegin = sdf.parse(timeFrom);
		System.out.println("date :" + resBegin.toString());
		long unixTimeInMiliseconds = resBegin.getTime();

		long unixTimeInSeconds = TimeUnit.MILLISECONDS
				.toSeconds(unixTimeInMiliseconds);
		long testTime = 1111111111L;
		assertEquals(testTime, unixTimeInSeconds);
	}

	@Test
	public void testUTCPlus1DatetoUnixTime() throws ParseException {
		// in "yyyy-MM-dd HH:mm:ss" pattern
		String timeFrom = "2005-03-18 02:58:31";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		// not setting timezone, defaults to system time zone
		Date resBegin = sdf.parse(timeFrom);
		System.out.println("date :" + resBegin.toString());
		long unixTimeInMiliseconds = resBegin.getTime();

		long unixTimeInSeconds = TimeUnit.MILLISECONDS
				.toSeconds(unixTimeInMiliseconds);
		long testTime = 1111111111L;
		assertEquals(testTime, unixTimeInSeconds);
	}

	@Test
	@Ignore
	public void testTOTPGeneration() throws IOException {
		TOTPGenerator totpgen = new SHA1TOTPGenerator();
		String timeFrom = "2005-03-18 01:58:31";
		Date resBegin = SimpleValidator.stringToDateTime(timeFrom, new Date());
		long unixTimeInMiliseconds = resBegin.getTime();
		long unixTimeInSeconds = TimeUnit.MILLISECONDS
				.toSeconds(unixTimeInMiliseconds);
		String totpToken = totpgen.generateToken(resBegin, null);
		String expected = "30330957";
		// assertEquals(expected, totpToken);
	}

	@Test
	public void testReservationServiceSendsToken() {
		VehicleReservationService vrs = new VehicleReservationService();

	}

	@Test
	@Ignore
	public void testUnixTimeToDateString() {
		long testTime = 1111111111L;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		String utcTime = df.format(new Date(testTime * 1000));

		assertEquals("2005-03-18 01:58:31", utcTime);
	}

}
