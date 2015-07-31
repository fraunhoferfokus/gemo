package de.fhg.fokus.mdc.partizipation.services;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class NullHostnameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String hostname, SSLSession session) {
		 return true;
	}

}
