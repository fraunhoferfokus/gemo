package de.fhg.fokus.mdc.carAccess.generate;

import static de.fhg.fokus.mdc.propertyProvider.Constants.CARACCESS_PRIVATE_PATH;
import static de.fhg.fokus.mdc.propertyProvider.Constants.CARACCESS_PUBLIC_PATH;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Properties;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;

import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;

public class GenerateKeyPair {
	public static void main(String[] args) throws IOException {

		final Properties props = PropertyProvider.getInstance()
				.loadProperties();

		String privateKeyPath = props.getProperty(CARACCESS_PRIVATE_PATH);

		String publicKeyPath = props.getProperty(CARACCESS_PUBLIC_PATH);
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		try {

			/* Generate a key pair */

			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECParameterSpec ecSpec = ECNamedCurveTable
					.getParameterSpec("SECP160R1");

			keyGen.initialize(160, random);

			// keyGen.initialize(ecSpec, random);
			KeyPair pair = keyGen.generateKeyPair();
			PrivateKey priv = pair.getPrivate();
			PublicKey pub = pair.getPublic();

			/* Save the private key in a file */
			byte[] keyp = priv.getEncoded();

			System.out.println("algo : " + priv.getAlgorithm()
					+ ", private key byte array length: " + keyp.length);
			FileOutputStream keypfos = new FileOutputStream(privateKeyPath);
			keypfos.write(keyp);
			keypfos.close();

			/* Save the public key in a file */
			byte[] key = pub.getEncoded();
			FileOutputStream keyfos = new FileOutputStream(publicKeyPath);
			keyfos.write(key);
			keyfos.close();

		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
		}
	}
}