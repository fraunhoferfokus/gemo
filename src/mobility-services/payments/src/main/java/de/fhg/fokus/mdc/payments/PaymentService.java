package de.fhg.fokus.mdc.payments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import de.fhg.fokus.mdc.clients.PaymentDataStoreClient;
import de.fhg.fokus.mdc.clients.ReservationWebClient;
import de.fhg.fokus.mdc.clients.UserprofileDataStoreClient;
import de.fhg.fokus.mdc.exceptions.ServiceFailedException;
import de.fhg.fokus.mdc.exceptions.ValidationException;
import de.fhg.fokus.mdc.jsonObjects.Payment;
import de.fhg.fokus.mdc.jsonObjects.ReservationSegment;
import de.fhg.fokus.mdc.jsonObjects.Userprofile;
import de.fhg.fokus.mdc.payments.exceptions.PaymentAlreadyExistsException;
import de.fhg.fokus.mdc.payments.exceptions.PaymentAlreadyPaidException;
import de.fhg.fokus.mdc.payments.exceptions.PaymentMustBeConfirmedException;
import de.fhg.fokus.mdc.payments.exceptions.PaymentNotFoundException;
import de.fhg.fokus.mdc.payments.lib.Consts;
import de.fhg.fokus.mdc.payments.managers.PaymentManager;
import de.fhg.fokus.mdc.utils.SimpleValidator;

/**
 * This class implements the payment service.
 * 
 * @author Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de
 */
@Path("/")
public class PaymentService {

	/**
	 * GET "payments?userprofileID=1" returns all payments of the user as
	 * List<Payment>
	 * 
	 * username in headers mapped to userprofileID
	 * 
	 * @param userprofileID
	 *            The user system id
	 * @see Vehicle Rerservation Service
	 */
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Payment> getPayments(@Context HttpHeaders headers)
			throws IOException {

		Integer userprofileID = getUserID(headers);

		/********* [ deny first - validation ] ********/

		// simple validation check of the incoming parameters
		List<String> validationErrors = new ArrayList<String>();

		if (!SimpleValidator.notNull(userprofileID))
			validationErrors.add(Consts.NO_VALID_USERID + "'" + userprofileID
					+ "'");

		if (!validationErrors.isEmpty())
			throw new ValidationException(validationErrors);

		/********* [forward authentication headers] ***********/
		PaymentDataStoreClient.getInstance().setAuthHeaders(headers);

		/********* [ logic ] ********/
		return PaymentDataStoreClient.getInstance().getPayments(userprofileID);
	}

	private Integer getUserID(HttpHeaders headers) throws JsonParseException,
			JsonMappingException, IOException {
		// ---------------map username to userid
		UserprofileDataStoreClient.getInstance().setAuthHeaders(headers);
		// TODO change this, setting headers twice is horrible
		UserprofileDataStoreClient.getInstance().setAuthHeadersJersey(headers);
		Integer userprofileID = UserprofileDataStoreClient.getInstance()
				.getUseridByUsername();
		// ---------------map username to userid end
		return userprofileID;
	}

	/**
	 * GET "payments/{id}?userprofileID=x" returns the payment with {id} for
	 * user with id = x
	 * 
	 * @param userprofileID
	 * @param id
	 * @return Payment
	 * @throws PaymentNotFoundException
	 * @throws IOException
	 */
	@GET
	@Path("{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Payment getPayment(@PathParam("id") Integer id,

	@Context HttpHeaders headers) throws PaymentNotFoundException, IOException {
		Integer userprofileID = getUserID(headers);
		/********* [ deny first - validation ] ********/

		// simple validation check of the incoming parameters
		List<String> validationErrors = new ArrayList<String>();

		if (!SimpleValidator.notNull(id))
			validationErrors.add(Consts.NO_VALID_PAYMENTID + "'" + id + "'");
		if (!SimpleValidator.notNull(userprofileID))
			validationErrors.add(Consts.NO_VALID_USERID + "'" + userprofileID
					+ "'");

		if (!validationErrors.isEmpty())
			throw new ValidationException(validationErrors);

		/********* [forward authentication headers] ***********/
		PaymentDataStoreClient.getInstance().setAuthHeaders(headers);

		/********* [ logic ] ********/

		// delegate to the storage data store client (direct db request)
		Payment expectedPayment = PaymentDataStoreClient.getInstance()
				.getPayment(id, userprofileID);

		// business exception
		if (expectedPayment == null)
			throw new PaymentNotFoundException(id, userprofileID);

		return expectedPayment;
	}

	/**
	 * POST "payments/" creates always a new payment based on the current user
	 * session and reservation. The user himself can trigger the creation or
	 * another service if the user wants to finish his current session.
	 * 
	 * @see PUT to confirm a payment
	 * @param userprofileID
	 *            The user system id
	 * @param reservationSequenceID
	 *            the group id (sequence id) of all current reservations
	 * @return
	 * @throws IOException
	 * @throws ServiceFailedException
	 * @throws PaymentAlreadyExistsException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Payment createPayment(
			@FormParam(Consts.PARAM_RESERVATIONSEQUENCEID) Integer reservationSequenceID,
			@Context HttpHeaders headers) throws PaymentAlreadyExistsException,
			ServiceFailedException, IOException {

		Integer userprofileID = getUserID(headers);

		/********* [ deny first - validation ] ********/

		// simple validation check of the incoming parameters
		List<String> validationErrors = new ArrayList<String>();
		if (!SimpleValidator.notNull(userprofileID))
			validationErrors.add(Consts.NO_VALID_USERID + "'" + userprofileID
					+ "'");
		if (!SimpleValidator.notNull(reservationSequenceID))
			validationErrors.add(Consts.NO_VALID_RESERVATIONSEQUENCEID + "'"
					+ reservationSequenceID + "'");
		if (!validationErrors.isEmpty())
			throw new ValidationException(validationErrors);

		/********* [forward authentication headers] ***********/
		ReservationWebClient.getInstance().setAuthHeadersJersey(headers);
		PaymentDataStoreClient.getInstance().setAuthHeaders(headers);

		/********* [ logic ] ********/

		// read all necessary objects from external services
		// get the userprofile
		Userprofile userprofile = UserprofileDataStoreClient.getInstance()
				.requestUserByID(userprofileID);

		// get the segment list from reservation service
		// TODO change to use ReservationDataStoreClient since it is easier to
		// forward scopes that way

		List<ReservationSegment> reservations = ReservationWebClient
				.getInstance()
				.requestReservationsSegmentsByReservationSequenceID(
						reservationSequenceID);

		return PaymentManager.getInstance().generatePayment(userprofile,
				reservationSequenceID, reservations);
	}

	/**
	 * PUT "payments/{id}?confirmed=true&userprofileID=x" confirms a payment of
	 * a user
	 * 
	 * @param id
	 *            the existing payment id to update the item using PUT
	 * @param confirmed
	 *            Boolean, true if the user has confirmed
	 * @param userprofileID
	 *            ID of the user profile
	 * 
	 */

	@PUT
	@Path("{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void confirmPayment(@PathParam("id") Integer id,
			@QueryParam(Consts.PARAM_CONFIRMED) Boolean confirmed,
			@Context HttpHeaders headers) throws JsonParseException,
			JsonMappingException, IOException {
		Integer userprofileID = getUserID(headers);
		/********* [forward authentication headers] ***********/
		PaymentDataStoreClient.getInstance().setAuthHeaders(headers);

		// a bill is mostly read-only ;-) so we only support updates to confirm
		Payment p = PaymentDataStoreClient.getInstance().getPayment(id,
				userprofileID);

		/********* [ business logic ] ********/

		// updates only if paid == false
		if (p.getPaid())
			throw new PaymentAlreadyPaidException(p);

		// the user must confirm to be able to pay
		if (!confirmed/* payment.getConfirmed() */)
			throw new PaymentMustBeConfirmedException();

		// now we can be sure to have a valid payment. trigger payment now:
		PaymentManager.getInstance().triggerPayment(p);

	}

	/**
	 * DELETE "payments/{id}" deletes an existing payment by id
	 * 
	 * @param id
	 *            the resource _id
	 * @param userprofileID
	 *            ID of the userprofile
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@DELETE
	// Path("{id}") uses @PathParam("id") int id
	@Consumes(MediaType.TEXT_PLAIN)
	public void deletePayment(@QueryParam(Consts.PARAM_ID) int paymentID,
			@Context HttpHeaders headers) throws JsonParseException,
			JsonMappingException, IOException {
		Integer userprofileID = getUserID(headers);
		/********* [forward authentication headers] ***********/
		PaymentDataStoreClient.getInstance().setAuthHeaders(headers);
		// grab the instance
		Payment payment = PaymentDataStoreClient.getInstance().getPayment(
				paymentID, userprofileID);
		PaymentDataStoreClient.getInstance().deletePayment(payment);
	}
}
