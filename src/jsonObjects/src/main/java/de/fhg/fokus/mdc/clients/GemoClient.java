package de.fhg.fokus.mdc.clients;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.mdc.utils.auth.HeaderHelper;

public class GemoClient {

	/** The url string. */
	protected String url = null;

	/** local REST template. */
	protected RestTemplate restTemplate = null;

	protected static Properties props = null;

	protected final Logger log = LoggerFactory.getLogger(getClass());

	public org.springframework.http.HttpHeaders authHeaders;

	HeaderHelper headerHelper = new HeaderHelper();

	public void setAuthHeaders(javax.ws.rs.core.HttpHeaders authHeaders) {

		this.authHeaders = headerHelper
				.convertjaxrsToSpringHeaders(authHeaders);
	}

	static {
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
