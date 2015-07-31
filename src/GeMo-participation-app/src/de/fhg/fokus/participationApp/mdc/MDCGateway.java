package de.fhg.fokus.participationApp.mdc;

// imports
import java.util.HashMap;

import de.fhg.fokus.participationApp.ShowComplaintsListVehicleActivity;
import de.fhg.fokus.participationApp.utils.GlobalData;

import android.content.Context;
import android.widget.ListView;

/**
 * The class consitutes the methods to access the data cloud.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class MDCGateway {

	/** The test vehicle ID. */
	public static final String testEvehicleID = "ecar_00001";

	/** The instance for the singleton pattern. */
	private static MDCGateway instance = null;

	/**
	 * Local instance for storing the cloud based data for the vehicle
	 * complaints.
	 */
	private VehicleComplaintsData vComplaintsData = null;

	// Just for fun arrays --> TODO: remove
	String testTitlesForChargingStation[] = { "Ladestation beschmutzt",
			"Kein Strom", "Gefährliche Gegend", "Farbe fällt ab", "Geruch",
			"Batterie Ladeprobleme", "Komische Gräusche" };

	String testDescriptionsForChargingStation[] = { "Ladestation beschmutzt",
			"Kein Strom", "Gefährliche Gegend", "Farbe fällt ab", "Geruch",
			"Batterie Ladeprobleme", "Komische Gräusche" };

	String testTagsForChargingStation[] = { "Verschmutzung_der_Ladestation",
			"Kein_Strom", "Gefährliche_Gegend", "Farbe", "Komischer_Geruch",
			"Ladeprobleme", "Probleme_beim_Ladevorgang" };

	String testCategoryForChargingStation[] = { "Verschmutzung", "Strom",
			"Gegend", "Farbe", "Geruch", "Ladeprobleme", "Ladevorgang" };

	/** Constructor for the singleton pattern. */
	private MDCGateway() {
	}

	/**
	 * The getInstance method for the singleton pattern.
	 * 
	 * @return the singleton instance.
	 */
	public static MDCGateway getInstance() {
		if (instance == null) {
			instance = new MDCGateway();
		}
		return instance;
	}

	/**
	 * The method submits a complaint to the participation service of the cloud.
	 * 
	 * @param context
	 *            the application context.
	 * 
	 * @param title
	 *            the title of the complaint.
	 * @param description
	 *            the description of the complaint.
	 * @param tags
	 *            the tags of the complaint.
	 * @param categories
	 *            the categories of the complaint.
	 * @param latitude
	 *            the latitude of the complaint.
	 * @param longitude
	 *            the longitude of the complaint.
	 * @param countryCode
	 *            the countryCode of the complaint.
	 * @param city
	 *            the city of the complaint.
	 * @param street
	 *            the street of the complaint.
	 * @param houseNo
	 *            the house number of the complaint.
	 * @param postalCode
	 *            the postal code.
	 * 
	 * @return true if everything has gone fine, false otherwise.
	 */
	@SuppressWarnings("unchecked")
	public boolean submitComplaint(Context context, String title,
			String description, String tags, String categories,
			String latitude, String longitude, String countryCode, String city,
			String street, String houseNo, String postalCode) {

		// get the complaints from the cloud in a thread
		new MDCThread().execute(GlobalData.SUBMIT_COMPLAINT_FOR_VEHICLE, title,
				description, tags, testEvehicleID, context);

		// System.out.println("GeMo: title:" + title);
		// System.out.println("GeMo: description:" + description);
		// System.out.println("GeMo: tags:" + tags);
		// System.out.println("GeMo: categories:" + categories);
		// System.out.println("GeMo: latitude:" + latitude);
		// System.out.println("GeMo: longitude:" + longitude);
		// System.out.println("GeMo: countryCode:" + countryCode);
		// System.out.println("GeMo: city:" + city);
		// System.out.println("GeMo: street:" + street);
		// System.out.println("GeMo: houseNo:" + houseNo);
		// System.out.println("GeMo: postalCode:" + postalCode);

		return true;
	}

	/**
	 * The method returns the titles of the complaints related to a vehicle.
	 * 
	 * @param complaintsListView
	 *            the list view to update as soon as all the complaints for the
	 *            vehicle were obtained from the mobility data cloud.
	 * 
	 * @param showComplaintsListVehicleActivity
	 *            the activity fir showing the complaints for a vehicle.
	 * 
	 * @return an array with the complaints
	 */
	@SuppressWarnings("unchecked")
	public void getVehicleComplaints(ListView complaintsListView,
			ShowComplaintsListVehicleActivity showComplaintsListVehicleActivity) {
		
		// reset the complaints data
		vComplaintsData = null;
		vComplaintsData = new VehicleComplaintsData();
		
		if (complaintsListView == null
				|| showComplaintsListVehicleActivity == null) {
			return;
		}

		// get the complaints from the cloud in a thread
		new MDCThread().execute(GlobalData.GET_COMPLAINTS_FOR_VEHICLE,
				complaintsListView, vComplaintsData,
				showComplaintsListVehicleActivity);
	}

	/**
	 * The function returns the details for the various vehicle complaints.
	 * 
	 * @param position
	 *            the position of the complaint in the internal list.
	 * 
	 * @return the details as hash map.
	 */
	public HashMap<String, String> getDetailsForVehicleComplaint(int position) {

		HashMap<String, String> toreturn = new HashMap<String, String>();

		if (vComplaintsData.titlesForVehicle.size() <= position
				|| vComplaintsData.descriptionsForVehicle.size() <= position
				|| vComplaintsData.tagsForVehicle.size() <= position
				|| vComplaintsData.categoryForVehicle.size() <= position) {
			return toreturn;
		}

		toreturn.put("title", vComplaintsData.titlesForVehicle.get(position));
		toreturn.put("description",
				vComplaintsData.descriptionsForVehicle.get(position));
		toreturn.put("tags", vComplaintsData.tagsForVehicle.get(position));
		toreturn.put("category",
				vComplaintsData.categoryForVehicle.get(position));

		return toreturn;
	}

	/**
	 * The method returns the titles of the complaints related to a charging
	 * station.
	 * 
	 * @return an array with the complaints
	 */
	public String[] getChargingStationComplaints() {
		// TODO: implement
		String[] toreturn = testTitlesForChargingStation;

		return toreturn;
	}

	/**
	 * The function returns the details for the various vehicle complaints.
	 * 
	 * @param position
	 *            the position of the complaint in the internal list.
	 * 
	 * @return the details as hash map.
	 */
	public HashMap<String, String> getDetailsForChargingStationComplaint(
			int position) {
		// TODO: implement fully

		HashMap<String, String> toreturn = new HashMap<String, String>();
		toreturn.put("title", testTitlesForChargingStation[position]);
		toreturn.put("description",
				testDescriptionsForChargingStation[position]);
		toreturn.put("tags", testTagsForChargingStation[position]);
		toreturn.put("category", testCategoryForChargingStation[position]);

		return toreturn;
	}

}
