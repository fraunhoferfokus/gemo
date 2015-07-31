package de.fhg.fokus.mdc.partizipation.services;
import java.io.IOException;
import java.net.HttpURLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class NoHostCheckSimpleClientHttpRequestFactory extends SimpleClientHttpRequestFactory  {
	
	private final HostnameVerifier verifier;
	/**
	 * A host name verifier for accepting all certificates.
	 */
	

	   public  NoHostCheckSimpleClientHttpRequestFactory(HostnameVerifier verifier) {
	      this.verifier = verifier;
	   }

	   @Override
	   protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
	      if (connection instanceof HttpsURLConnection) {
	         ((HttpsURLConnection) connection).setHostnameVerifier(verifier);
	      }
	      super.prepareConnection(connection, httpMethod);
	   }
}
