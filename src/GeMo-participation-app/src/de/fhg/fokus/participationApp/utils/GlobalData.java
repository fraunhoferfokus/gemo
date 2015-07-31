package de.fhg.fokus.participationApp.utils;

import de.fhg.fokus.participationApp.ShowComplaintsListVehicleActivity;
import android.widget.ListView;

/**
 * The class contains global data for the application.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * 
 */
public class GlobalData {

	// TODO: make the values of all the fields configurable over the XML files
	// in the values directory

	/** The path to the participation service. */
	public static final String PATH_TO_PARTICIPATION_SERVICE = "https://193.175.133.248/service/partizipation";

	/** The path to complaints part. */
	public static final String COMPLAINTS = "/complaints";

	/** The eVehicleID string. */
	public static final String EVEHICLE_ID = "eVehicleID";

	/** The authentication string containing the user name and password. */
	public static final String AUTHENTICATION_STRING = "fokus:f0ku5";

	/** The message to notify that there are not complaints for this vehicle. */
	public static final String NO_COMPLAINTS_FOR_VEHICLE_STR = "Keine Mängel für dieses Fahrzeug";

	/**
	 * Constant for dispatching the functions in the communication thread to the
	 * Mobility Data Cloud.
	 */
	public static final Integer GET_COMPLAINTS_FOR_VEHICLE = 1;

	/**
	 * Constant for dispatching the functions in the communication thread to the
	 * Mobility Data Cloud.
	 */
	public static final Integer SUBMIT_COMPLAINT_FOR_VEHICLE = 2;

	/**
	 * The variable contains the message to show after a successful complaint
	 * submission.
	 */
	public static final String SUBMISSION_SUCCESSFUL_MESSAGE = "Eintrag erstellt. Bitte wählen Sie erneut die Anzeige der Mängelliste für das Fahrzeug, um die aktualisierte Liste zu sehen";

	/**
	 * The variable contains the message to show after a failed complaint
	 * submission.
	 */
	public static String SUBMISSION_FAILED_MESSAGE = "Eintrag konnte leider nicht erstellt werden";

	/**
	 * This is a global view list that is taken from the activity for showing
	 * the complaints list. This view list is used for dynamically updating the
	 * list of complaints after a new one has been added.
	 */
	public static ListView complaintsListView = null;

	/**
	 * This is a global reference to the activity for showing all the
	 * complaints. This reference is used for dynamically updating the list of
	 * complaints after a new one has been added.
	 */
	public static ShowComplaintsListVehicleActivity showComplaintsListVehicleActivity = null;

}
