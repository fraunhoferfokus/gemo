package de.fhg.fokus.participationApp.mdc;

// imports
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;

import de.fhg.fokus.participationApp.R;
import de.fhg.fokus.participationApp.ShowComplaintsListVehicleActivity;
import de.fhg.fokus.participationApp.utils.GlobalData;
import de.fhg.fokus.participationApp.utils.ParticipationAppUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * The class implements the thread to communicate with the MDC.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * 
 */
@SuppressWarnings("rawtypes")
public class MDCThread extends AsyncTask {

	/** The local reference to the complaints list view. */
	private ListView complaintsListView = null;

	/**
	 * The local reference to the view for showing the complaints for a vehicle.
	 */
	private ShowComplaintsListVehicleActivity showComplaintsListVehicleActivity = null;

	/** The field to keep the service response. */
	private String serviceResponse = "";

	/** The local reference to the vehicle complaints data. */
	private VehicleComplaintsData vComplaintsData = null;

	/**
	 * The field is meant to keep the function which was requested from the
	 * Thread instance.
	 */
	private Integer function = null;

	/**
	 * The variable is meant to keep the response code of the POST call for
	 * submiting complaints.
	 */
	private int responseCode = -1;

	/** The application context to use for showing the hosts. */
	private Context context = null;

	/**
	 * A host name verifier for accepting all certificates.
	 */
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

		/**
		 * Method overriding.
		 */
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * The method implies trusting every server by ignoring certificates.
	 * 
	 */
	private static void trustAllHosts() {
		/**
		 * Create a trust manager that does not validate certificate chains.
		 */
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			@SuppressWarnings(value = { "all" })
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@SuppressWarnings(value = { "all" })
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
					throws java.security.cert.CertificateException {
			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
					throws java.security.cert.CertificateException {
			}
		} };

		// set the new trust managed that accepts all servers
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The method executed by the thread in the background,
	 */
	@Override
	protected Object doInBackground(Object... arg) {

		// get the list view to update
		function = (Integer) arg[0];

		// dispatch the requested function
		if (function == GlobalData.GET_COMPLAINTS_FOR_VEHICLE) {
			// in case the complaints for a vehicle should be obtained

			// pick the parameters
			complaintsListView = (ListView) arg[1];
			vComplaintsData = (VehicleComplaintsData) arg[2];
			showComplaintsListVehicleActivity = (ShowComplaintsListVehicleActivity) arg[3];

			getComplaintsForVehicle();

		} else if (function == GlobalData.SUBMIT_COMPLAINT_FOR_VEHICLE) {
			// in case a complaint for a vehicle should be submitted

			// pick the parameters for submitting the complaint
			String title = (String) arg[1];
			String description = (String) arg[2];
			String tags = (String) arg[3];
			String eVehicleID = (String) arg[4];
			context = (Context) arg[5];

			// submit the complaint
			submitComplaintForVehicle(title, description, tags, eVehicleID);
		}

		return null;
	}

	/**
	 * The function submits a complaint for a vehicle.
	 * 
	 * @param eVehicleID
	 *            the vehicle ID.
	 * @param tags
	 *            the tags.
	 * @param description
	 *            the description.
	 * @param title
	 *            the title.
	 */
	private void submitComplaintForVehicle(String title, String description,
			String tags, String eVehicleID) {

		// check the input parameters
		if (title == null || eVehicleID == null) {
			return;
		}

		try {
			// prepare the encoded authentication string
			String encoding = new String(
					Base64.encodeBase64(GlobalData.AUTHENTICATION_STRING
							.getBytes()));

			// prepare the URL
			URL purl = new URL(GlobalData.PATH_TO_PARTICIPATION_SERVICE
					+ GlobalData.COMPLAINTS);

			// setup the possibility to ignore the certificate
			trustAllHosts();

			// open the connection
			HttpsURLConnection yc = (HttpsURLConnection) purl.openConnection();

			// set the request method
			yc.setRequestMethod("POST");

			// prepare the URL parameters
			String urlPars = "title=" + URLEncoder.encode(title, "UTF-8") + "&";
			if (description == null) {
				urlPars += "description=" + URLEncoder.encode("", "UTF-8")
						+ "&";
			} else {
				urlPars += "description="
						+ URLEncoder.encode(description, "UTF-8") + "&";
			}

			if (tags == null) {
				urlPars += "tags=" + URLEncoder.encode("", "UTF-8") + "&";
			} else {
				urlPars += "tags=" + URLEncoder.encode(tags, "UTF-8") + "&";
			}

			urlPars += "eVehicleID=" + URLEncoder.encode(eVehicleID, "UTF-8")
					+ "&";
			// set some dummy latitude and longtitude
			urlPars += "latitude=" + URLEncoder.encode("00.00", "UTF-8") + "&";
			urlPars += "longitude=" + URLEncoder.encode("00.00", "UTF-8");

			// urlPars += "eVehicleID=" + eVehicleID + "&";
			// urlPars += "latitude=&";
			// urlPars += "longitude=";

			// ignore the certificate
			yc.setHostnameVerifier(DO_NOT_VERIFY);

			// set the authorization
			yc.setRequestProperty("Authorization", "Basic " + encoding);

			yc.setRequestProperty("Content-Length",
					"" + Integer.toString(urlPars.getBytes().length));

			// set the content type
			yc.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			yc.setRequestProperty("Charset", "UTF-8");
			// No caching, we want the real thing.
			yc.setUseCaches(false);

			// Send post request
			yc.setDoOutput(true);
			yc.setDoInput(true);

			// input the url parameters
			DataOutputStream wr = new DataOutputStream(yc.getOutputStream());
			wr.writeBytes(urlPars);
			wr.flush();
			wr.close();

			// get the response code
			responseCode = yc.getResponseCode();

			yc.disconnect();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * The method obtains all the complaints for a vehicle.
	 */
	private void getComplaintsForVehicle() {

		try {

			// prepare the encoded authentication string
			String encoding = new String(
					Base64.encodeBase64(GlobalData.AUTHENTICATION_STRING
							.getBytes()));

			// prepare the URL
			URL purl = new URL(GlobalData.PATH_TO_PARTICIPATION_SERVICE
					+ GlobalData.COMPLAINTS + "?" + GlobalData.EVEHICLE_ID
					+ "=" + MDCGateway.testEvehicleID + "&"
					+ "offset=0&limit=100");

			// setup the possibility to ignore the certificate
			trustAllHosts();

			// open the connection
			HttpsURLConnection yc = (HttpsURLConnection) purl.openConnection();

			// ignore the certificate
			yc.setHostnameVerifier(DO_NOT_VERIFY);

			// set the authorization
			yc.setRequestProperty("Authorization", "Basic " + encoding);

			// get the belonging stream reader
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));

			// get the response from the service
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				serviceResponse += inputLine;
			}
			in.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** The method that is executed on the UI thread at the end. */
	@Override
	protected void onPostExecute(Object result) {

		if (function == GlobalData.GET_COMPLAINTS_FOR_VEHICLE) {
			// in case the list of complaints was required

			// call the function for updating the complaints list view
			updateComplaintsListView(complaintsListView, serviceResponse,
					showComplaintsListVehicleActivity);

			// extract the properties of the complaints
			extractComplaintsProperties(serviceResponse, vComplaintsData);
		} else if (function == GlobalData.SUBMIT_COMPLAINT_FOR_VEHICLE) {
			// in case the submit complaint for vehicle was requested

			// the message to indicate the result
			String message = "";

			if (responseCode == 201) {
				// in case of a successful submission
				message = GlobalData.SUBMISSION_SUCCESSFUL_MESSAGE;
			} else {
				// otherwise
				message = GlobalData.SUBMISSION_FAILED_MESSAGE;
			}

			// show the toast with the message
			if (context != null) {
				CharSequence text = message;
				int duration = Toast.LENGTH_LONG*2;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}

		}
	}

	/**
	 * The function fills the arrays with the properties of the complaints.
	 * 
	 * @param jsonStr
	 *            the JSON string to process.
	 * @param vComplaintsData
	 *            the reference to the vehicle complaints data.
	 * 
	 */
	private void extractComplaintsProperties(String jsonStr,
			VehicleComplaintsData vComplaintsData) {
		ParticipationAppUtils.extractComplaintsProperties(serviceResponse,
				vComplaintsData);

	}

	/**
	 * The function updates the complaints list view for a vehicle based on the
	 * response of
	 * 
	 * @param complaintsListView
	 *            the complaints view to update.
	 * 
	 * @param serviceResponse
	 *            the service response as a JSON string.
	 * 
	 * @param showComplaintsListVehicleActivity
	 *            the show-complaints-activity from which the thread was
	 *            invoked.
	 */
	private void updateComplaintsListView(ListView complaintsListView,
			String serviceResponse,
			ShowComplaintsListVehicleActivity showComplaintsListVehicleActivity) {

		// Create and populate a List of planet names.
		String[] complaints = ParticipationAppUtils
				.getComplaintsFromJSONString(serviceResponse);

		// check whether there are any complaints and return a corresponding
		// message.
		if (complaints == null) {
			complaints = new String[1];
			complaints[0] = GlobalData.NO_COMPLAINTS_FOR_VEHICLE_STR;
		}

		ArrayList<String> planetList = new ArrayList<String>();
		planetList.addAll(Arrays.asList(complaints));

		// Create ArrayAdapter using the planet list.
		ArrayAdapter<String> complaintsListAdapter = new ArrayAdapter<String>(
				showComplaintsListVehicleActivity,
				R.layout.complaints_list_row, planetList);

		// Set the ArrayAdapter as the ListView's adapter.
		complaintsListView.setAdapter(complaintsListAdapter);

	}
}
