package de.fhg.fokus.participationApp;

// imports
import de.fhg.fokus.participationApp.mdc.MDCGateway;
import de.fhg.fokus.participationApp.utils.GlobalData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

/**
 * The activity is used to show the list of complaints.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class ShowComplaintsListVehicleActivity extends Activity {

	/** The list view for the complaints. */
	private ListView complaintsListView;

	/** The method is called upon creating the activity. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// invoke the basic class constructor
		super.onCreate(savedInstanceState);

		// set the content view
		setContentView(R.layout.show_complaints_list_charging_station_activity_fullscreen);

		// Find the ListView resource.
		complaintsListView = (ListView) findViewById(R.id.complaint_list_view);
		
		// invoke the method for obtaining the list of complaints and showing it
		// on the scroll pane
		MDCGateway.getInstance().getVehicleComplaints(complaintsListView, this);
		
		// set the global references
		GlobalData.complaintsListView = complaintsListView;
		GlobalData.showComplaintsListVehicleActivity = this;
		

		// set the listener for on item clicks
		complaintsListView.setOnItemClickListener(new OnItemClickListener() {

			/**
			 * The function which is invoked on a click.
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String item = ((TextView) view).getText().toString();

				Intent pIntent = new Intent(view.getContext(),
						ParticipationVehicleActivity.class);
				pIntent.putExtra("position", "" + position);
				pIntent.putExtra("id", "" + id);
				pIntent.putExtra("item", "" + item);

				startActivityForResult(pIntent, 0);
			}
		});

	}
}