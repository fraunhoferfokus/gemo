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
public class MainActivity extends Activity {

	/**
	 * The method for creating the app upon start.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// get any saved instance and invoke the constructor of the super class
		super.onCreate(savedInstanceState);

		// set the view
		setContentView(R.layout.main_activity_fullscreen);

		// setup the required listeners
		findViewById(R.id.vehicles_button).setOnClickListener(vehiclesOnClickListener);
		findViewById(R.id.charging_stations_button).setOnClickListener(chargingStationsOnClickListener );
	}

	/**
	 * 
	 * The following lines specify the listener that is activated after clicking
	 * on the "new complaint" button.
	 */
	View.OnClickListener vehiclesOnClickListener = new View.OnClickListener() {

		/**
		 * The method activated after clicking on the button.
		 * 
		 * @param v
		 *            the belonging view.
		 */
		@Override
		public void onClick(View v) {
			Intent pIntent = new Intent(v.getContext(), GeneralVehicleActivity.class);
            startActivityForResult(pIntent , 0);
		}
	};
	
	
	/**
	 * 
	 * The following lines specify the listener that is activated after clicking
	 * on the "show list" button.
	 */
	View.OnClickListener chargingStationsOnClickListener = new View.OnClickListener() {

		/**
		 * The method activated after clicking on the button.
		 * 
		 * @param v
		 *            the belonging view.
		 */
		@Override
		public void onClick(View v) {
			Intent pIntent = new Intent(v.getContext(), GeneralChargingStationActivity.class);
            startActivityForResult(pIntent , 0);
		}
	};
}
		
