package de.fhg.fokus.mdc.carAccess.generate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class GenerateAESKey {
	public static void main(String[] args) throws NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException, IOException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		KeyGenerator generator = KeyGenerator.getInstance("AES", "BC");

		generator.init(256);

		// TODO save this key somewhere --- shared secret
		Key encryptionKey = generator.generateKey();
		FileOutputStream fos = new FileOutputStream(
				"C:\\Users\\izi\\Documents\\GeMo_FOKUS\\keys\\AESKey.txt");
		fos.write(encryptionKey.getEncoded());
		fos.close();
		System.out
				.println("key   : " + Utils.toHex(encryptionKey.getEncoded()));

	}
}
