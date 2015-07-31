package de.fhg.fokus.mdc.payments.managers;

// imports
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import bsh.EvalError;
import bsh.Interpreter;
import de.fhg.fokus.mdc.clients.PaymentDataStoreClient;
import de.fhg.fokus.mdc.exceptions.ServiceFailedException;
import de.fhg.fokus.mdc.jsonObjects.Payment;
import de.fhg.fokus.mdc.jsonObjects.ReservationSegment;
import de.fhg.fokus.mdc.jsonObjects.Userprofile;
import de.fhg.fokus.mdc.payments.exceptions.PaymentAlreadyExistsException;
import de.fhg.fokus.mdc.payments.exceptions.ReservationMustContainSegmentsException;
import de.fhg.fokus.mdc.payments.exceptions.ReservationSegmentTypeUndefinedException;

/**
 * The class offers diverse functions for realizing the billing.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * @author Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de
 * 
 */
public class PaymentManager {

	/** The path to the current billing code for electric vehicles. */
	private final static String billingCodeElectricVehicle = "efz_billing.bsh";

	/**
	 * The path to the current beanshell for electric vehicles to calculate a
	 * price of a route segment per time and route type "drive".
	 */
	private final static String BeanshellEVRouteSegmentPerTimeAndDrive = "ev_routesegement_time_drive.bsh";

	/**
	 * The path to the current beanshell for electric vehicles to calculate a
	 * price of a route segment per time and route type "park".
	 */
	private final static String BeanshellEVRouteSegmentPerTimeAndPark = "ev_routesegement_time_park.bsh";

	/** The path to the current billing code for charging stations. */
	private final static String billingCodeChargingStation = "ls_billing.bsh";

	/** The singleton instances of the class. */
	private static PaymentManager instance = null;

	/** The table for managing the users. */
	private static String usersTable = null;

	/** the price catalog */
	public static final float BILLSERVICE_PRICE_PER_MINUTE_DRIVE = 0.29f; // deprecated
	public static final float BILLSERVICE_PRICE_PER_MINUTE_PARK = 0.19f;// deprecated

	public static final float BILLSERVICE_PRICE_PER_MINUTE_MAINTENANCE = 0.01f;
	public static final float BILLSERVICE_PRICE_PER_MINUTE_IDLE = 0.00f;
	public static final float BILLSERVICE_PRICE_PER_MINUTE_TOUR_BUSINESS = 0.30f;
	public static final float BILLSERVICE_PRICE_PER_MINUTE_TOUR_PRIVATE = 0.25f;
	public static final float BILLSERVICE_PRICE_PER_MINUTE_TOUR_PARKING = 0.10f;

	/** Constructor for the singleton pattern. */
	private PaymentManager() {
	}

	/**
	 * The get-instance method of the singleton pattern.
	 * 
	 * @return the belonging singleton instance.
	 */
	public static PaymentManager getInstance() {
		if (instance == null) {
			instance = new PaymentManager();
		}
		return instance;
	}

	/**
	 * generator (factory) with db access (storage web client)
	 * 
	 * @param userprofile
	 * @param reservationSequenceID
	 * @param reservations
	 * @return
	 * @throws PaymentAlreadyExistsException
	 * @throws ServiceFailedException
	 * @throws IOException
	 */
	public Payment generatePayment(Userprofile userprofile,
			Integer reservationSequenceID,
			List<ReservationSegment> reservationSegments)
			throws PaymentAlreadyExistsException,
			ServiceFailedException, IOException {

		// business validation
		if (reservationSegments.size() <= 0)
			throw new ReservationMustContainSegmentsException(
					reservationSequenceID);

		// create a new instance
		Payment payment = new Payment(userprofile, reservationSequenceID,
				reservationSegments);

		// calculate items and price
		payment = calculatePayment(payment);

		// persist new instance and update with generated PK
		payment = PaymentDataStoreClient.getInstance().insertPayment(payment);

		return payment;
	}

	/**
	 * triggers the payment
	 * 
	 * @param payment
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void triggerPayment(Payment payment) throws JsonParseException,
			JsonMappingException, IOException {
		// triggers the payment
		payment.pay();
		// update instance
		PaymentDataStoreClient.getInstance().updatePayment(payment);
	}

	// --------------------[ helper methods ]---------------------------

	/**
	 * collects all items and calculates the total price
	 * 
	 * @param payment
	 *            the calculated payment
	 * @return
	 */
	private Payment calculatePayment(Payment payment) {

		final List<ReservationSegment> segments = payment
				.getReservationSegments();
		String itemsText = "";
		float totalPrice = 0f;

		for (int i = 0; i < segments.size(); i++) {
			ReservationSegment segment = segments.get(i);
			float price = calculatePriceByRouteSegment(segment);

			// calculate duration
			DateTime begin = new DateTime(segment.getSegbegin());
			DateTime end = new DateTime(segment.getSegend());
			Duration duration = new Duration(begin, end);
			long minutes = duration.getStandardMinutes();

			// get the local currency information
			Currency cLoc = Currency.getInstance(new Locale("de", "DE"));
			final String cSymbol = cLoc.getSymbol(); // not compatible with our
														// postgres encoding?

			// build item informaion string
			itemsText += (i + 1) + ".) " + segment.getReservationtype() + " ("
					+ minutes + " min) =  " + price + " " + cLoc + " \n";
			totalPrice += price;
		}
		payment.setTotalPrice(totalPrice);
		payment.setItems(itemsText);
		return payment;
	}

	/**
	 * The function checks on whether a string constitutes a number.
	 * 
	 * @param str
	 *            the string to check.
	 * 
	 * @return true if the string is a number, otherwise false.
	 */
	private boolean isNumber(String str) {
		if (str == null || str.matches("^\\s*$")) {
			return false;
		}

		try {
			Integer.parseInt(str);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * The function calculates the requested bill.
	 * 
	 * @param type
	 *            the type of bill to calculate - efz (electric vehicle) or ls
	 *            (charging station).
	 * @param units
	 *            the units for the calculation.
	 * 
	 * @return the value of the bill or -1 in case of problems.
	 * @deprecated
	 */
	public int calculateBill(String type, String units) {

		// the parameters are not being checked since they must
		// have been previously analyzed upon having been received
		// by the main service method

		// the variable to return
		int toreturn = 0;

		// the interpreter
		Interpreter i = new Interpreter();

		// the variable for the billing code
		String billingCode = null;

		if (type.trim().equalsIgnoreCase("ls")) {
			// in case the billing code for charging stations is required

			// get the billing code for charging stations
			billingCode = readBillingFile(billingCodeChargingStation);

		} else if (type.trim().equalsIgnoreCase("efz")) {
			// in case the billing code for electric vehicles is required

			// get the billing code for electric vehicles
			billingCode = readBillingFile(billingCodeElectricVehicle);

		} else {
			return -1;
		}

		// check finally the billing code
		if (billingCode == null) {
			return -1;
		}

		try {
			// Set variables
			i.set("units", Integer.parseInt(units));

			// execute the billing code
			i.eval(billingCode);
			toreturn = (Integer) i.get("result");
		} catch (EvalError e) {
			e.printStackTrace();
		}

		return toreturn;
	}

	/**
	 * This function calculates and retrns a price for a route segment by time
	 * and route type
	 * 
	 * @return the price of the bill or -1 in case of problems.
	 */
	private float calculatePriceByRouteSegment(ReservationSegment segment) {

		/** the beanshell file for the route type */
		String billingShellCode = "";

		/** the variable to return */
		float segmentPrice = 0;

		/** the route type **/
		String routeType = segment.getReservationtype();

		/*
		 * the following types are possible (at the moment):
		 */

		// take the shell scripts for each type (stuff for later)
		// if (routeType.equalsIgnoreCase("drive")) {
		// billingShellCode =
		// readBillingFile(BeanshellEVRouteSegmentPerTimeAndDrive);
		// } else if (routeType.equalsIgnoreCase("park")) {
		// billingShellCode =
		// readBillingFile(BeanshellEVRouteSegmentPerTimeAndPark);
		// }
		// // the interpreter
		// Interpreter i = new Interpreter();
		// try {
		// // Set variables
		// i.set("minutes", minutes);
		// // execute the billing shell code
		// i.eval(billingShellCode);
		// segmentPrice = (Float) i.get("result");
		// } catch (EvalError e) {
		// e.printStackTrace();
		// }

		// -------[ logic ]---------------------

		DateTime begin = new DateTime(segment.getSegbegin());
		DateTime end = new DateTime(segment.getSegend());
		Duration duration = new Duration(begin, end);
		long minutes = duration.getStandardMinutes();

		// TODO: must start and end with idle.
		// maintenance,idle,tour_business,tour_private,parking .......

		if (segment.getReservationtype().equals("maintenance")) {
			segmentPrice = BILLSERVICE_PRICE_PER_MINUTE_MAINTENANCE * minutes;
		} else if (segment.getReservationtype().equals("idle")) {
			// nothing happens
		} else if (segment.getReservationtype().equals("parking")) {
			segmentPrice = BILLSERVICE_PRICE_PER_MINUTE_TOUR_PARKING * minutes;
		} else if (segment.getReservationtype().equals("tour_private")) {
			segmentPrice = BILLSERVICE_PRICE_PER_MINUTE_TOUR_PRIVATE * minutes;
		} else if (segment.getReservationtype().equals("tour_business")) {
			segmentPrice = BILLSERVICE_PRICE_PER_MINUTE_TOUR_BUSINESS * minutes;
		} else {
			throw new ReservationSegmentTypeUndefinedException(segment);
		}

		// round (half up)
		BigDecimal roundedPrice = new BigDecimal(String.valueOf(segmentPrice))
				.setScale(2, BigDecimal.ROUND_HALF_UP);

		segmentPrice = roundedPrice.floatValue();

		return segmentPrice;
	}

	/**
	 * The function imports the code for the billing function from a file.
	 * 
	 * @param resourcePath
	 *            the resource path to the billing code.
	 * 
	 * @return the billing code or null if something has gone wrong.
	 */
	private String readBillingFile(String resourcePath) {
		// the parameter is not being checked since it should be an internal
		// constant

		// read in the whole file from the resource path
		StringBuffer fileData = new StringBuffer();
		char[] buf = new char[1024];
		int numRead = 0;
		try {

			// read the bytes of the file in a cycle

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream(
							resourcePath)));
			while ((numRead = reader.read(buf)) != -1) {
				// read in the bytes and append them to the string buffer from
				// above
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
			}
			// close he stream
			reader.close();
		} catch (IOException e) {
			// handle IO exceptions
			e.printStackTrace();
			return null;
		}

		// return a string extracted from the string buffer
		return fileData.toString();

	}

}
