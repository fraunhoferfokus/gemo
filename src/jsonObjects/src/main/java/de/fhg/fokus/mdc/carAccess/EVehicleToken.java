package de.fhg.fokus.mdc.carAccess;

import static de.fhg.fokus.mdc.propertyProvider.Constants.CARACCESS_PRIVATE_PATH;
import static de.fhg.fokus.mdc.propertyProvider.Constants.CARACCESS_SHAREDSECRET_PATH;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.jsonObjects.Reservation;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;

public class EVehicleToken {
	@JsonProperty
	byte[] carAccessToken;
	@JsonProperty
	byte[] signatureToken;
	// byte[] publicKeyOfCarEncoded;
	@JsonProperty
	byte[] initializationVector = new byte[] { 0x00, 0x00, 0x00, 0x01, 0x04,
			0x05, 0x06, 0x07, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };

	String privateKeyPath;
	String sharedSecretPath;

	protected static Properties props = null;
	/**
	 * The logger of the class.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public EVehicleToken(Reservation res) {
		privateKeyPath = props.getProperty(CARACCESS_PRIVATE_PATH);
		sharedSecretPath = props.getProperty(CARACCESS_SHAREDSECRET_PATH);
		carAccessToken = generateEncodedToken(res);
		signatureToken = sign(carAccessToken);

	}

	static {
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] sign(byte[] carAccessToken2) {
		/* Generate an EC signature */

		try {
			/*
			 * read priv from file
			 */
			LOGGER.debug("loading private key from path: " + privateKeyPath);
			FileInputStream keyfis = new FileInputStream(privateKeyPath);
			byte[] encKey = new byte[keyfis.available()];
			keyfis.read(encKey);
			keyfis.close();
			LOGGER.debug("private key byte array length: " + encKey.length);
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);

			KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
			PrivateKey priv = keyFactory.generatePrivate(privKeySpec);
			// ECGenParameterSpec kpgparams = new
			// ECGenParameterSpec("SECP160R1");

			/*
			 * Create a Signature object and initialize it with the private key
			 */
			Provider[] providers = Security.getProviders();

			Signature rsa = Signature.getInstance("SHA512WITHECDSA", "BC");

			rsa.initSign(priv);

			/* Update and sign the data */
			rsa.update(carAccessToken2);

			/*
			 * Now that all the data to be signed has been read in, generate a
			 * signature for it
			 */

			byte[] realSig = rsa.sign();
			LOGGER.debug("signature byte array length: " + realSig.length);
			return realSig;

		} catch (Exception e) {
			LOGGER.error("Caught exception " + e.toString());
		}
		return null;
	}

	public EVehicleToken() {

	}

	public byte[] generateEncodedToken(Reservation res) {
		// put the car access token together
		byte[] token = generateTokenBytes(res);

		try {
			// encrypt with the shared secret
			token = encryptSymmetric(token);

		} catch (Exception e) {

			e.printStackTrace();
		}
		LOGGER.debug("encrypted car access token byte array length : "
				+ token.length);
		return token;
	}

	public byte[] generateTokenBytes(Reservation reservation) {
		long resBeginEpoch = 0L;
		short resPeriodInMinutes = 30;
		int rangeOfFunctions = 1;
		String macAddress = reservation.getMacAddress();
		if (macAddress == null) {
			macAddress = "1A:F4:2A:F9:CE:1A";
		}
		int userId = Integer.parseInt(reservation.getUserID());
		String eVehileIDTrimmed = reservation.geteVehicleID().replace("ecar_",
				"");
		int eVehicleId = Integer.parseInt(eVehileIDTrimmed);

		ByteBuffer bb = ByteBuffer.allocate(28);
		Date resBegin = new Date();
		resBeginEpoch = dateToUnixTime(resBegin);
		// largest int is 2147483647
		// corresponding GMT date for that is 19 Jan 2038.
		// so casting to int should be ok for a while
		int resBeginInt = (int) resBeginEpoch;
		bb.putInt(resBeginInt);
		bb.putShort(resPeriodInMinutes);
		bb.putInt(rangeOfFunctions);
		bb.putInt(userId);
		bb.putInt(eVehicleId);
		byte[] macinbytes = MACAddress.valueOf(macAddress).toBytes();
		bb.put(macinbytes);
		byte[] tokenBytes = bb.array();
		int checksum = calculateCRC32(tokenBytes);
		bb.putInt(checksum);
		return tokenBytes;
	}

	private int calculateCRC32(byte[] bytes) {
		Checksum checksum = new CRC32();
		// update the current checksum with the specified array of bytes
		checksum.update(bytes, 0, bytes.length);
		// get the current checksum value
		long checksumValue = checksum.getValue();
		int checksumint = (int) checksumValue;
		return checksumint;
	}

	public long dateToUnixTime(Date resBegin) {
		long unixTimeInMiliseconds = resBegin.getTime();
		long unixTimeInSeconds = TimeUnit.MILLISECONDS
				.toSeconds(unixTimeInMiliseconds);
		return unixTimeInSeconds;
	}

	// public byte[] getValue() {
	// return carAccessToken;
	// }

	private byte[] encryptSymmetric(byte[] input) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		// TODO use different ivbytes each time if you are going to use same key

		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
		LOGGER.debug("input : " + new String(input));

		byte[] keybyte = new byte[32];
		LOGGER.debug("loading shared secret from: " + sharedSecretPath);
		FileInputStream fin = new FileInputStream(sharedSecretPath);
		fin.read(keybyte);
		SecretKey encryptionKey = new SecretKeySpec(keybyte, 0, 32, "AES");

		// encryption pass
		cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, new IvParameterSpec(
				initializationVector));

		byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

		// why are these two steps and not directly doFinal? to know the length?
		// int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
		// ctLength += cipher.doFinal(cipherText, ctLength);
		cipherText = cipher.doFinal(input);

		fin.close();
		LOGGER.debug("encrypted : " + new String(cipherText));
		return cipherText;

	}

	// this was just for checking the length
	@Deprecated
	public int generateTokenPartRes(Reservation reservation) {
		long resBeginEpoch = 0L;
		short resPeriodInMinutes = 30;
		int rangeOfFunctions = 1;
		String macAddress = "14:F4:2A:E9:CE:1A";
		int userId = Integer.parseInt(reservation.getUserID());
		String eVehileIDTrimmed = reservation.geteVehicleID().replace("ecar_",
				"");
		int eVehicleId = Integer.parseInt(eVehileIDTrimmed);

		ByteBuffer bb = ByteBuffer.allocate(28);
		Date resBegin = new Date();
		resBeginEpoch = dateToUnixTime(resBegin);
		// largest int is 2147483647
		// corresponding GMT date for that is 19 Jan 2038.
		// so casting to int should be ok for a while
		int resBeginInt = (int) resBeginEpoch;
		bb.putInt(resBeginInt);
		bb.putShort(resPeriodInMinutes);
		bb.putInt(rangeOfFunctions);
		bb.putInt(userId);
		bb.putInt(eVehicleId);
		byte[] macinbytes = MACAddress.valueOf(macAddress).toBytes();
		bb.put(macinbytes);
		byte[] tokenBytes = bb.array();

		int checksum = calculateCRC32(tokenBytes);
		bb.putInt(checksum);
		return tokenBytes.length;
	}

}
