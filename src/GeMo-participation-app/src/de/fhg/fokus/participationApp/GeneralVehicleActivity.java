package de.fhg.fokus.participationApp;

// imports
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * The main activity class of the participation App.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class GeneralVehicleActivity extends Activity {

	/**
	 * The method for creating the app upon start.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// get any saved instance and invoke the constructor of the super class
		super.onCreate(savedInstanceState);

		// set the view
		setContentView(R.layout.general_vehicle_activity_fullscreen);

		// setup the required listeners
		findViewById(R.id.new_vehicle_complaint_button).setOnClickListener(newComplaintOnClickListener);
		findViewById(R.id.show_vehicle_list_button).setOnClickListener(showListOnClickListener );
		
	}

	/**
	 * 
	 * The following lines specify the listener that is activated after clicking
	 * on the "new complaint" button.
	 */
	View.OnClickListener newComplaintOnClickListener = new View.OnClickListener() {

		/**
		 * The method activated after clicking on the button.
		 * 
		 * @param v
		 *            the belonging view.
		 */
		@Override
		public void onClick(View v) {
			Intent pIntent = new Intent(v.getContext(), ParticipationVehicleActivity.class);
            startActivityForResult(pIntent , 0);
		}
	};
	
	
	/**
	 * 
	 * The following lines specify the listener that is activated after clicking
	 * on the "show list" button.
	 */
	View.OnClickListener showListOnClickListener = new View.OnClickListener() {

		/**
		 * The method activated after clicking on the button.
		 * 
		 * @param v
		 *            the belonging view.
		 */
		@Override
		public void onClick(View v) {
			Intent pIntent = new Intent(v.getContext(), ShowComplaintsListVehicleActivity.class);
            startActivityForResult(pIntent , 0);
		}
	};
}
		
