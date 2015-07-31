package de.fhg.fokus.mdc.exceptions;

import javax.ws.rs.core.Response;

/**
 * Exception to signal errors from dependent services
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class ServiceFailedException extends ExpectedBusinessException {
	private static final long serialVersionUID = 1L;

	public ServiceFailedException(String dependentServiceName,
			String serviceNameThatFailed) {
		this(
				dependentServiceName
						+ ": The following dependent service didn't work correctly or delivered unexpected results: "
						+ serviceNameThatFailed);
	}

	public ServiceFailedException(String message) {
		super(ServiceFailedException.class,
				Response.Status.INTERNAL_SERVER_ERROR, message);
	}

}
