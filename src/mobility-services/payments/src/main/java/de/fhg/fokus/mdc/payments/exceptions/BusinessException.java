package de.fhg.fokus.mdc.payments.exceptions;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class BusinessException extends WebApplicationException {
	private static final long serialVersionUID = 1L;
	private static final String exception = "Expected business exception occurred: ";

	/**
	 * throws a business exception with the specific Response Status HTTP Code
	 * 
	 * @param message
	 * @param httpStatusCode
	 *            (Response.Status.X)
	 */
	public BusinessException(String message, Response.Status httpStatusCode) {
		this(exception, message, httpStatusCode);
	}

	/**
	 * throws a business exception (single message/error) with Default Response
	 * Status HTTP Code BAD_REQUEST (400)
	 * 
	 * @param message
	 */
	public BusinessException(String message) {
		this(exception, message, Response.Status.BAD_REQUEST);
	}

	// intern: constructor for normal message based exceptions
	private BusinessException(String internalMessage, String externalMessage,
			Response.Status httpStatusCode) {
		super(Response.status(httpStatusCode)
				.entity(internalMessage + "\n" + externalMessage).build());
	}

	/**
	 * throws a business exception (multiple messages/errors) with Default
	 * Response Status HTTP Code BAD_REQUEST (400) as JSON
	 * 
	 * @param messages
	 *            as List<String>
	 */
	public BusinessException(List<String> messages) {
		this(exception, messages, Response.Status.BAD_REQUEST,
				MediaType.APPLICATION_JSON);
	}

	/**
	 * throws a business exception (multiple messages/errors) with specific
	 * Response Status HTTP Code and Mediatype
	 * 
	 * @param messages
	 *            as List<String>
	 * @param httpStatusCode
	 *            like Response.Status.BAD_REQUEST
	 * @param mediaType
	 *            like MediaType.APPLICATION_JSON
	 */
	public BusinessException(List<String> messages,
			Response.Status httpStatusCode, String mediaType) {
		this(exception, messages, httpStatusCode, mediaType);
	}

	// intern: constructor for List exceptions
	private BusinessException(String internalMessage,
			List<String> externalMessages, Response.Status httpStatusCode,
			String mediaType) {
		super(Response.status(httpStatusCode).type(mediaType)
				.entity(buildGenericEntity(externalMessages, internalMessage))
				.build());
	}

	// --------------------[ helper Methods ]--------------------------------

	// intern: helper for List Exceptions with internal messages at the
	// beginning
	private static Object buildGenericEntity(List<String> externalMessages,
			String internalMessage) {
		// add internal Message at the beginning of the list:
		externalMessages.add(0, internalMessage);
		return new GenericEntity<List<String>>(externalMessages) {
		};
	}
}
