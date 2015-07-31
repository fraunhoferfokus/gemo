package de.fhg.fokus.odp.portal.managedatasets.utils;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

/**
 * Authentication Request Filter for the upload and auto deploy 
 * service  
 * @author dsc
 *
 */
public class AuthHeadersRequestFilter implements ClientRequestFilter {

	/*
	 * TODO: exclude them to a central unit, like jsonObjects util classes
	 */
	public static final String HEADER_FIELD_ACCESSTOKEN = "accessToken"; 
	public static final String HEADER_FIELD_SCOPE = "scope";
	public static final String HEADER_FIELD_USERNAME = "username";
	private final String accessToken;
	private final String scope;
	private final String username;

    public AuthHeadersRequestFilter(String accessToken, String scope, String username) {
    	this.accessToken = accessToken;
    	this.scope = scope;
    	this.username = username;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders().add(HEADER_FIELD_ACCESSTOKEN, accessToken);
        requestContext.getHeaders().add(HEADER_FIELD_SCOPE, scope);
        requestContext.getHeaders().add(HEADER_FIELD_USERNAME, username);
    }

}
