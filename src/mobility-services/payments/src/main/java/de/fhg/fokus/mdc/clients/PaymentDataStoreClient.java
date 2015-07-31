package de.fhg.fokus.mdc.clients;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import de.fhg.fokus.mdc.clients.DataStoreClient;
import de.fhg.fokus.mdc.exceptions.ServiceFailedException;
import de.fhg.fokus.mdc.jsonObjects.Payment;
import de.fhg.fokus.mdc.payments.exceptions.PaymentAlreadyExistsException;
import de.fhg.fokus.mdc.payments.lib.Consts;
import de.fhg.fokus.mdc.propertyProvider.Constants;
import de.fhg.fokus.mdc.utils.SimpleValidator;

/**
 * Storage-Client to work with payments
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */

public class PaymentDataStoreClient extends DataStoreClient {

	/** The table fields for a payment. */
	private static final String COL_ID = "_id";
	private static final String COL_USERPROFILEID = "userprofileid";
	private static final String COL_RESERVATIONSEQUENCEID = "reservationsequenceid";
	private static final String COL_CONFIRMED = "confirmed";
	private static final String COL_ITEMS = "items";
	private static final String COL_TOTALPRICE = "totalprice";
	private static final String COL_CREATEDAT = "createdat";
	private static final String COL_PAID = "paid";
	private static final String COL_PAIDAT = "paidat";
	private static final String COL_DELETED = "deleted";

	/** The singleton instances of the class. */
	private static PaymentDataStoreClient instance = null;

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static PaymentDataStoreClient getInstance() {
		if (instance == null) {
			instance = new PaymentDataStoreClient();
		}
		return instance;
	}

	/**
	 * is an implementation of DataStoreclient
	 */
	public PaymentDataStoreClient() {
		super();
	}

	// ---------------------[ Super Class Methods]----------------------

	public void defineTableName() {
		table = props.getProperty(Constants.PAYMENT_TABLE);
	}

	// ----------------[ business/interface logic ]----------------------

	/**
	 * check if a payment for the current reservation sequence does already
	 * exist
	 * 
	 * @param reservationSequenceID
	 * @return true if it does already exist
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Boolean paymentExist(Integer reservationSequenceID)
			throws JsonParseException, JsonMappingException, IOException {
		// fire a select
		String res = select("select count(*) from " + table + " where "
				+ COL_RESERVATIONSEQUENCEID + " =\'" + reservationSequenceID
				+ "\'" + " and " + COL_DELETED + "=\'false\'");
		// TODO: create shiny stuff with count(*)
		// expected result from storage: [{"count":0}]
		// there is no elegant way to parse or decode this :-/
		return (!res.trim().endsWith(":0}]"));
	}

	/**
	 * check if a payment does already exist by id
	 * 
	 * @param id
	 * @return true if it does already exist
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Boolean paymentExistById(Integer id) throws JsonParseException,
			JsonMappingException, IOException {
		// fire a select
		String res = select("select count(*) from " + table + " where "
				+ COL_ID + " =\'" + id + "\'" + " and " + COL_DELETED
				+ "=\'false\'");
		// TODO: create shiny stuff with count(*)
		// expected result from storage: [{"count":0}]
		// there is no elegant way to parse or decode this :-/
		return (!res.trim().endsWith(":0}]"));
	}

	/**
	 * finds a payment instance by id
	 * 
	 * @param id
	 * @param userprofileID
	 * @return Payment
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Payment getPayment(Integer id, Integer userprofileID)
			throws JsonParseException, JsonMappingException, IOException {
		// fire a select
		String res = select("select * from " + table + " where " + COL_ID
				+ " =\'" + id + "\'" + " and " + COL_USERPROFILEID + "="
				+ userprofileID + " and " + COL_DELETED + "=\'false\'");

		// The response is an array, we must grab the first
		ObjectMapper mapper = new ObjectMapper();
		Payment[] resultSet = mapper.readValue(res, Payment[].class);
		return (resultSet.length == 0) ? null : resultSet[0];
	}

	/**
	 * returns all payments
	 * 
	 * @param userprofileID
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public List<Payment> getPayments(Integer userprofileID)
			throws JsonParseException, JsonMappingException, IOException {
		// fire a select
		String res = select("select * from " + table + " where "
				+ COL_USERPROFILEID + " =\'" + userprofileID + "\'" + " and "
				+ COL_DELETED + "=\'false\'");// order by " + COL_ID);
		// The response is an array, we must grab the first
		ObjectMapper mapper = new ObjectMapper();
		Payment[] resultSet = mapper.readValue(res, Payment[].class);
		return Arrays.asList(resultSet);
	}

	/**
	 * finds a payment instance by sequence id
	 * 
	 * @param reservationSequenceID
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public Payment getPaymentByReservationSequenceId(
			Integer reservationSequenceID) throws JsonParseException,
			JsonMappingException, IOException {
		// fire a select
		String res = select("select * from " + table + " where "
				+ COL_RESERVATIONSEQUENCEID + " =\'" + reservationSequenceID
				+ "\'" + " and " + COL_DELETED + "=\'false\'");
		// The response is an array, we must grab the first
		ObjectMapper mapper = new ObjectMapper();
		Payment[] resultSet = mapper.readValue(res, Payment[].class);
		return (resultSet.length == 0) ? null : resultSet[0];
	}

	/**
	 * creates a new Payment in the storage
	 * 
	 * @param payment
	 *            the payment instance to persist
	 * @return the inserted payment with the updated object
	 * @throws IOException
	 */
	public Payment insertPayment(Payment payment)
			throws PaymentAlreadyExistsException, ServiceFailedException,
			IOException {

		if (paymentExist(payment.getReservationSequenceID()))
			throw new PaymentAlreadyExistsException();

		// field mapping for a new payment
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add(Consts.PARAM_TABLENAME, table);
		map = mapObjectFields(map, payment);

		// fire an insert and analyze the response
		if (insert(map).equals(Consts.STORAGE_RESPONSE_FAIL))
			throw new ServiceFailedException("Storage: Payment insert failed.");

		// correct insert, so update the new instance
		return getPaymentByReservationSequenceId(payment
				.getReservationSequenceID());
	}

	/**
	 * updates a payment in the storage
	 * 
	 * @param payment
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void updatePayment(Payment payment) throws JsonParseException,
			JsonMappingException, IOException {

		// field mapping for the update statement via parameters
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add(Consts.PARAM_TABLENAME, table);
		map.add(Consts.PARAM_WHERE, COL_ID + "=\'" + payment.getId() + "\'");
		map = mapObjectFields(map, payment);

		// fire an insert and analyze the response
		if (update(map).equals(Consts.STORAGE_RESPONSE_FAIL))
			throw new ServiceFailedException(
					"Storage: Payment operation failed.");
	}

	/**
	 * deletes a payment by id
	 * 
	 * @param id
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * 
	 */
	public void deletePayment(Payment payment) throws JsonParseException,
			JsonMappingException, IOException {
		// delete is an update with delete flag
		payment.setDeleted(true);
		updatePayment(payment);
	}

	// ---------------------[ Helper Methods]----------------------

	/**
	 * helper method to set/map/convert the object fields
	 * 
	 * @param map
	 *            as MultiValueMap<String, String>
	 * @param p
	 *            as Payment
	 * @return
	 */
	private MultiValueMap<String, String> mapObjectFields(
			MultiValueMap<String, String> map, Payment p) {
		map.add(COL_USERPROFILEID, p.getUserprofileID().toString());
		map.add(COL_RESERVATIONSEQUENCEID, p.getReservationSequenceID()
				.toString());
		map.add(COL_CONFIRMED, p.getConfirmed().toString());
		map.add(COL_ITEMS, p.getItems());
		map.add(COL_TOTALPRICE, String.valueOf(p.getTotalPrice()));
		map.add(COL_CREATEDAT,
				SimpleValidator.dateToDateTimeString(p.getCreatedAt())
						.toString());
		map.add(COL_PAID, p.getPaid().toString());
		map.add(COL_PAIDAT, SimpleValidator.dateToDateTimeString(p.getPaidAt())
				.toString());
		map.add(COL_DELETED, p.getDeleted().toString());
		return map;
	}
}
