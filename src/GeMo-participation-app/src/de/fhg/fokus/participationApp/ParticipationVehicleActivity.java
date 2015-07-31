package de.fhg.fokus.participationApp;

// imports
import java.util.HashMap;

import de.fhg.fokus.participationApp.mdc.MDCGateway;
import de.fhg.fokus.participationApp.mdc.MDCThread;
import de.fhg.fokus.participationApp.utils.GlobalData;
import de.fhg.fokus.participationApp.vehicle.VehicleGateway;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The activity for editing and adding complaints.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class ParticipationVehicleActivity extends Activity {

	/**
	 * The method for creating the app upon start.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// get any saved instance and invoke the constructor of the super class
		super.onCreate(savedInstanceState);

		// set the view
		setContentView(R.layout.participation_vehicle_activity_fullscreen);

		// setup the required listener
		findViewById(R.id.submit_button).setOnClickListener(
				submitOnClickListener);

		// get the parameters if any and set the corresponding fields
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			try {
				int position = Integer.parseInt(extras.getString("position"));
				HashMap<String, String> details = MDCGateway.getInstance()
						.getDetailsForVehicleComplaint(position);

				EditText titleEditText = (EditText) findViewById(R.id.titleEditText);
				titleEditText.setText(details.get("title"),
						TextView.BufferType.EDITABLE);

				EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
				descriptionEditText.setText(details.get("description"),
						TextView.BufferType.EDITABLE);

				EditText tagsEditText = (EditText) findViewById(R.id.tagsEditText);
				tagsEditText.setText(details.get("tags"),
						TextView.BufferType.EDITABLE);

				EditText categoriesEditText = (EditText) findViewById(R.id.categoriesEditText);
				categoriesEditText.setText(details.get("category"),
						TextView.BufferType.EDITABLE);

			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	/**
	 * The method which is activated when the back button was pressed.
	 */
	@Override
	public void onBackPressed() {

		MDCGateway.getInstance().getVehicleComplaints(
				GlobalData.complaintsListView,
				GlobalData.showComplaintsListVehicleActivity);
		super.onBackPressed();

		return;
	}

	/**
	 * 
	 * The following lines specify the listener that is activated after clicking
	 * on the "Submit" button.
	 */
	View.OnClickListener submitOnClickListener = new View.OnClickListener() {

		/**
		 * The method gets activated after clicking on the submit button.
		 * 
		 * @param v
		 *            the belonging view.
		 */
		@Override
		public void onClick(View v) {

			// get the information from the fields
			EditText titleEditText = (EditText) findViewById(R.id.titleEditText);
			String title = titleEditText.getText().toString();

			EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
			String description = descriptionEditText.getText().toString();

			EditText tagsEditText = (EditText) findViewById(R.id.tagsEditText);
			String tags = tagsEditText.getText().toString();

			EditText categoriesEditText = (EditText) findViewById(R.id.categoriesEditText);
			String categories = categoriesEditText.getText().toString();

			// EditText latitudeEditText = (EditText)
			// findViewById(R.id.latitudeEditText);
			// String latitude = latitudeEditText.getText().toString();
			String latitude = VehicleGateway.getInstance().getLatitude();

			// EditText longitudeEditText = (EditText)
			// findViewById(R.id.longitudeEditText);
			// String longitude = longitudeEditText.getText().toString();
			String longitude = VehicleGateway.getInstance().getLongitude();

			//
			// EditText countryCodeEditText = (EditText)
			// findViewById(R.id.countryCodeEditText);
			// String countryCode = countryCodeEditText.getText().toString();
			String countryCode = VehicleGateway.getInstance().getCountryCode();
			//
			// EditText cityEditText = (EditText)
			// findViewById(R.id.cityEditText);
			// String city = cityEditText.getText().toString();
			String city = VehicleGateway.getInstance().getCity();
			//
			// EditText streetEditText = (EditText)
			// findViewById(R.id.streetEditText);
			// String street = streetEditText.getText().toString();
			String street = VehicleGateway.getInstance().getStreet();

			// EditText houseNoEditText = (EditText)
			// findViewById(R.id.houseNoEditText);
			// String houseNo = houseNoEditText.getText().toString();
			String houseNo = VehicleGateway.getInstance().getHouseNo();

			// EditText postalCodeEditText = (EditText)
			// findViewById(R.id.postalCodeEditText);
			// String postalCode = postalCodeEditText.getText().toString();
			String postalCode = VehicleGateway.getInstance().getPostalCode();

			// invoke the rest call for submitting the complaint
			MDCGateway.getInstance().submitComplaint(getApplicationContext(),
					title, description, tags, categories, latitude, longitude,
					countryCode, city, street, houseNo, postalCode);

		}
	};

}
