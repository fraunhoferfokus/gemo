package de.fhg.fokus.mdc.carAccess.generate;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.fhg.fokus.mdc.carAccess.EVehicleToken;

public class AESKeyGeneratorExample {

	/**
	 * Basic example using the KeyGenerator class and showing how to create a
	 * SecretKeySpec from an encoded key.
	 */
	public static void main(String[] args) throws Exception {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		EVehicleToken token = new EVehicleToken();
		byte[] input = token.generateTokenBytes(null);

		byte[] ivBytes = new byte[] { 0x00, 0x00, 0x00, 0x01, 0x04, 0x05, 0x06,
				0x07, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };

		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
		KeyGenerator generator = KeyGenerator.getInstance("AES", "BC");

		generator.init(192);

		// TODO save this key somewhere
		Key encryptionKey = generator.generateKey();

		System.out
				.println("key   : " + Utils.toHex(encryptionKey.getEncoded()));
		System.out.println("input : " + new String(input));

		// encryption pass
		cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, new IvParameterSpec(
				ivBytes));

		byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

		// why are these two steps and not directly doFinal? to know the length?
		// int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
		// ctLength += cipher.doFinal(cipherText, ctLength);
		cipherText = cipher.doFinal(input);
		System.out.println("encrypted : " + new String(cipherText));
		// TODO return cipherText;

		// decryption pass
		Key decryptionKey = new SecretKeySpec(encryptionKey.getEncoded(),
				encryptionKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, decryptionKey, new IvParameterSpec(
				ivBytes));
		byte[] plainText = cipher.doFinal(cipherText);
		// byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
		// int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
		// ptLength += cipher.doFinal(plainText, ptLength);
		System.out.println("plain : " + new String(plainText));
	}
}
