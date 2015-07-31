package de.fhg.fokus.mdc.jsonObjects.tests;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.zip.CRC32;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.fokus.mdc.carAccess.MACAddress;
import de.fhg.fokus.mdc.jsonObjects.Reservation;

public class EVehicleTokenTest {

	@Test
	public void testcreateTokenForReservation() throws JsonGenerationException,
			JsonMappingException, IOException {

		Reservation res = new Reservation();
		res.seteVehicleID("ecar_0024");
		res.setResBegin(new Date());
		res.setUserID("1");
		res.setResType("tour_private");
		String macAddress = "12:34:56:78:9A:BC";
		res.setMacAddress(macAddress);
		res.generateToken();
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String json = mapper.writeValueAsString(res);
		System.out.println(json);
		InputStream fileStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("reservation_example");
		Reservation resDes = mapper.readValue(fileStream, Reservation.class);
		System.out.println(resDes.geteVehicleID());
	}

	@Test
	@Ignore
	public void testStringtoMAC() {
		String macAddress = "14:F4:2A:E9:CE:1A";
		ByteBuffer bb = ByteBuffer.allocate(6);
		byte[] macinbytes = MACAddress.valueOf(macAddress).toBytes();
		bb.put(macinbytes);
		byte[] tokenBytes = bb.array();
		assertEquals(6, tokenBytes.length);
	}

	@Test
	@Ignore
	public void testCRCtoBytes() {
		String toBeEncoded = new String(
				"The quick brown fox jumps over the lazy dog");
		CRC32 myCRC = new CRC32();
		myCRC.update(toBeEncoded.getBytes());
		long value = myCRC.getValue();
		System.out.println("long to binary string : "
				+ Long.toBinaryString(value) + "\n");
		ByteBuffer bb = ByteBuffer.allocate(4);
		int valueInt = (int) value;
		bb.putInt(valueInt);
		System.out.println("integer to binary string : "
				+ Integer.toBinaryString(valueInt));
		byte[] tokenBytes = bb.array();
		assertEquals(4, tokenBytes.length);

		assertEquals(Long.toBinaryString(value),
				Integer.toBinaryString(valueInt));

	}

}
