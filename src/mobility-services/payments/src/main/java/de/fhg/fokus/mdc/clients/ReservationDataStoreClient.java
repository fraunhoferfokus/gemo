package de.fhg.fokus.mdc.clients;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_RESERVATION_TABLE;

import java.util.ArrayList;
import java.util.List;

import de.fhg.fokus.mdc.clients.DataStoreClient;
import de.fhg.fokus.mdc.payments.models.Reservation;

/**
 * Storage-Client to receive all reservations
 * 
 * @info better would be a rest confirm service request (no direct storage
 *       access)
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class ReservationDataStoreClient extends DataStoreClient {

	/** The singleton instances of the class. */
	private static ReservationDataStoreClient instance = null;

	// ---------------------[ Constructor ]---------------------------

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static ReservationDataStoreClient getInstance() {
		if (instance == null) {
			instance = new ReservationDataStoreClient();
		}
		return instance;
	}

	/**
	 * is an implementation of DataStoreclient
	 */
	public ReservationDataStoreClient() {
		super();
	}

	// ---------------------[ Super Class Methods]----------------------

	public void defineTableName() {
		table = props.getProperty(SERVICE_RESERVATION_TABLE);
	}

	// ----------------[ business/interface logic ]----------------------

	/**
	 * grabs the reservation instance by ID
	 */
	public List<Reservation> requestReservationsBySequenceID(
			Integer reservationSequenceID) {
		List<Reservation> theList = new ArrayList<Reservation>();
		// String reservationDetails = requestStorage("select * from " +
		// reservationTable
		// + " where _id=\'" + reservationID + "\'");

		return theList;
	}

}
