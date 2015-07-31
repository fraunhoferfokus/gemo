package de.fhg.fokus.cm.webapp;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class RestHelper {

	private static final String user = "";
	private static final String pwd = "";

	public static WebResource getWebResource(String baseURI) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		if (!user.equals("") && !pwd.equals("")) {
			client.addFilter(new HTTPBasicAuthFilter(user, pwd));
		}
		WebResource service = client.resource(RestHelper.getBaseURI(baseURI));
		return service;
	}

	public static URI getBaseURI(String uri) {
		return UriBuilder.fromUri(uri).build();
	}
}
