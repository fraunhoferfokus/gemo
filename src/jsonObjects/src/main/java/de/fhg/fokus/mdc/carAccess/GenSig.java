package de.fhg.fokus.mdc.carAccess;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

class GenSig {

	public static void main(String[] args) {

		/* Generate an RSA signature */

		try {
			/*
			 * read priv from file
			 */
			FileInputStream keyfis = new FileInputStream(
					"C:\\Users\\izi\\Documents\\GeMo_FOKUS\\keys\\id_priv");
			byte[] encKey = new byte[keyfis.available()];
			keyfis.read(encKey);
			keyfis.close();

			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey priv = keyFactory.generatePrivate(privKeySpec);
			/*
			 * Create a Signature object and initialize it with the private key
			 */

			Signature dsa = Signature.getInstance("SHA1withRSA");

			dsa.initSign(priv);

			/* Update and sign the data */
			// TODO instead of reading from file, read the encrypted token
			// string as stream
			String name = "toBeSigned";
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(name);

			BufferedInputStream bufin = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			while (bufin.available() != 0) {
				len = bufin.read(buffer);
				dsa.update(buffer, 0, len);
			}
			;

			bufin.close();

			/*
			 * Now that all the data to be signed has been read in, generate a
			 * signature for it
			 */

			byte[] realSig = dsa.sign();

			/* Save the signature in a file */
			FileOutputStream sigfos = new FileOutputStream("sig");
			sigfos.write(realSig);
			sigfos.close();

		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
		}

	}
}