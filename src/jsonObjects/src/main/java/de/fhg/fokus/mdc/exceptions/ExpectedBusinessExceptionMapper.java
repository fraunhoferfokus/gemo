package de.fhg.fokus.mdc.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//Versuche alle Businessexceptions zu fangen und als Nicht-Exceptions ordentlich zurück zu geben
@Provider
public class ExpectedBusinessExceptionMapper implements
		ExceptionMapper<ExpectedBusinessException> {
	public Response toResponse(ExpectedBusinessException bex) {
		return Response.status(200).entity(bex.getMessage()).type("text/plain")
				.build();
	}
}
