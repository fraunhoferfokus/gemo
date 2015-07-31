package de.fhg.fokus.mdc.payments.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Simple Exception Class for Not Implemented Request 501
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class NotImplementedException extends WebApplicationException {
	private static final long serialVersionUID = 1L;

	public NotImplementedException() {
		this("Not implemented (yet)");
	}

	private NotImplementedException(String message) {
		super(Response.status(501).entity(message).build());
	}
}