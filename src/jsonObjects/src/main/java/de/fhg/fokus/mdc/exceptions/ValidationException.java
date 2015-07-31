package de.fhg.fokus.mdc.exceptions;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Simple Validation Exception Class for Bad Request 400
 * 
 * @example <pre>
 * <code>
 * 		// simple validation check (deny first-strategy) using the SimpleValidator-utils
 * 		List<String> validationErrors = new ArrayList<String>();
 * 
 * 		if (!SimpleValidator.notNull(YOURVALUE1))
 * 			validationErrors.add("YOUR VALUE "+ YOURVALUE1 + " IS WRONG!");
 * 		if (!SimpleValidator.notNull(YOURVALUE2))
 * 			validationErrors.add("YOUR VALUE "+ YOURVALUE2 + " IS WRONG!");
 * 
 * 		if (!validationErrors.isEmpty())
 * 			throw new ValidationException(validationErrors);
 * </code>
 * </pre>
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class ValidationException extends ExpectedBusinessException {

	private static final long serialVersionUID = 1658206122006937L;
	@SuppressWarnings("unused")
	private List<String> errors;

	/**
	 * @examples see class description
	 * @param validationErrors
	 */
	public ValidationException(List<String> validationErrors) {
		this(validationErrors.toString());
		this.errors = validationErrors;
	}

	/**
	 * please use this constructor if you want to throw validation exceptions
	 * with the reason of a bad request (maybe missing parameters)
	 * 
	 * @param message
	 */
	public ValidationException(String message) {
		super(ValidationException.class, Response.Status.BAD_REQUEST, message);
	}

	/**
	 * please use this constructor if you want to define your own http status
	 * code- This is useful for correct structured requests but expected errors
	 * on the application layer (business exceptions) that maybe could throw a
	 * CONFLICT (409) or sth. else
	 * 
	 * @param message
	 */
	public ValidationException(String message, Status httpStatus) {
		super(ValidationException.class, httpStatus, message);
	}
}
