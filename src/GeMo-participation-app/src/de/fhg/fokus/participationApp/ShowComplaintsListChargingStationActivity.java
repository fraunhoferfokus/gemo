package de.fhg.fokus.participationApp;

// imports
import java.util.ArrayList;
import java.util.Arrays;

import de.fhg.fokus.participationApp.mdc.MDCGateway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

/**
 * The activity is used to show the list of complaints.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class ShowComplaintsListChargingStationActivity extends Activity {

	/** The list view for the complaints. */
	private ListView complaintsListView;

	/** The list adapter for the complaints. */
	private ArrayAdapter<String> complaintsListAdapter;

	/** The method is called upon creating the activity. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// invoke the basic class constructor
		super.onCreate(savedInstanceState);

		// set the content view
		setContentView(R.layout.show_complaints_list_vehicle_activity_fullscreen);

		// Find the ListView resource.
		complaintsListView = (ListView) findViewById(R.id.complaint_list_view);

		// Create and populate a List of planet names.
		String[] complaints = MDCGateway.getInstance().getChargingStationComplaints();

		ArrayList<String> planetList = new ArrayList<String>();
		planetList.addAll(Arrays.asList(complaints));

		// Create ArrayAdapter using the planet list.
		complaintsListAdapter = new ArrayAdapter<String>(this,
				R.layout.complaints_list_row, planetList);

		// Set the ArrayAdapter as the ListView's adapter.
		complaintsListView.setAdapter(complaintsListAdapter);
		
		// set the listener for on item clicks
		complaintsListView.setOnItemClickListener(new OnItemClickListener() {
            
			/**
			 * The function which is invoked on a click.
			 */
			@Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	String item = ((TextView)view).getText().toString();
            	
    			Intent pIntent = new Intent(view.getContext(), ParticipationChargingStationActivity.class);
                pIntent.putExtra("position", ""+position);
                pIntent.putExtra("id", ""+id);
                pIntent.putExtra("item", ""+item);
    			
    			startActivityForResult(pIntent , 0);   	
            }
        });

	}
}