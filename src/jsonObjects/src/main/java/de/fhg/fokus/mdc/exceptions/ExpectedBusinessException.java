package de.fhg.fokus.mdc.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Class for Expected Business Exceptions on the server. This class generates a
 * serialized BusinessException that contains all passed attributes (message,
 * httpStatus...)
 * 
 * A java client will catch unexpected WebExceptions for example of type
 * UniformInterfaceException. The clients must deserialize the response body by
 * using the normal factory methods, for example: <code>
 * 	uniformInterfaceException.getResponse().getEntity(ExpectedBusinessException
 * 		.BusinessException.class)
 * </code>
 * 
 * @see examples in the class constructors to find out how to inherit from
 *      ExpectedBusinessException.
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class ExpectedBusinessException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public BusinessException getBusinessException() {
		return (BusinessException) this.getResponse().getEntity();
	}

	/**
	 * generates an expected business exception with the specific Response
	 * Status (HTTP Status Code) and exception message. All subclasses must send
	 * its class types (Class<?>) as exceptionClassTypeToSerialize. This is the
	 * most simple way next to an adultly Exception Wrapper Class.
	 * 
	 * <pre>
	 *  <code>
	 *  // simplest form for a specific exception subclass that calls the super constructor of 
	 *  // ExpectedBusinessException:
	 * 
	 *  //Expected Business Exception for payments that must be confirmed first:
	 * 	@SuppressWarnings("serial")
	 * 	public class PaymentMustBeConfirmedException extends ExpectedBusinessException {
	 * 		public PaymentMustBeConfirmedException() {
	 * 			super(
	 * 				PaymentMustBeConfirmedException.class, //send specific class path (so we don't need a wrapper)
	 * 				"Aborted: payment must be confirmed first."
	 * 			);
	 * 		}
	 * 	}
	 *  </code>
	 * </pre>
	 * 
	 * @param exceptionClassTypeToSerialize
	 *            (yourClass).class
	 * @param message
	 * @param httpStatusCode
	 *            (Response.Status.X)
	 */
	public ExpectedBusinessException(Class<?> exceptionClassTypeToSerialize,
			Response.Status httpStatusCode, String message) {
		super(
				Response.status(httpStatusCode)
						.entity(new ExpectedBusinessException.BusinessException(
								exceptionClassTypeToSerialize, httpStatusCode,
								message)).build());
	}

	/**
	 * generates a simple expected business exception Defaults: HTTP Status Code
	 * = BAD_REQUEST (400)
	 * 
	 * @param exceptionClassTypeToSerialize
	 * @param message
	 */
	public ExpectedBusinessException(Class<?> exceptionClassTypeToSerialize,
			String message) {
		super(Response
				.status(Response.Status.BAD_REQUEST)
				.entity(new ExpectedBusinessException.BusinessException(
						exceptionClassTypeToSerialize,
						Response.Status.BAD_REQUEST, message)).build());
	}

	/**
	 * Do not use this constructor directly
	 * 
	 * @param message
	 */
	public ExpectedBusinessException(String message) {
		super(Response.status(Response.Status.NOT_ACCEPTABLE).entity(message)
				.build());
	}

	// --------------------[ helper Methods/Classes ]------------------------

	/**
	 * Inner class for serialized exception types that a client can consume. It
	 * can be deserialized using the following lines of code:
	 * 
	 * <pre>
	 * // deserialization:
	 * ExpectedBusinessException.BusinessException businessException = e.getResponse()
	 * 		.getEntity(ExpectedBusinessException.BusinessException.class);
	 * // usage:
	 * // businessException.getType();
	 * // businessException.getMessage();
	 * // businessException.getHttpStatus();
	 * </pre>
	 * 
	 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
	 * 
	 */
	@XmlRootElement(name = "BusinessException")
	public static class BusinessException {

		@JsonProperty("type")
		private String type;

		@JsonProperty("message")
		private String message;

		@JsonProperty("httpStatus")
		private Response.Status httpStatus;

		public BusinessException() {
			// empty constructor for deserialization
		}

		public BusinessException(Class<?> exceptionType,
				Response.Status httpStatus, String message) {
			this.setType(exceptionType.getName());
			this.setHttpStatus(httpStatus);
			this.setMessage(message);
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message
		 *            the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type
		 *            the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @return the httpStatus
		 */
		public Response.Status getHttpStatus() {
			return httpStatus;
		}

		/**
		 * @param httpStatus
		 *            the httpStatus to set
		 */
		public void setHttpStatus(Response.Status httpStatus) {
			this.httpStatus = httpStatus;
		}
	}

	@Override
	public String toString() {
		BusinessException e = (BusinessException) this.getResponse()
				.getEntity();
		return "type: \"" + e.getType() + "\",message: \"" + e.getMessage()
				+ "\",httpStatus: \"" + e.getHttpStatus().toString() + "\"";
	}
}
