package de.fhg.fokus.participationApp.utils;

// imports
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.fhg.fokus.participationApp.mdc.VehicleComplaintsData;

/**
 * The class contains utility functions for the participation app.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * 
 */
public class ParticipationAppUtils {

	/**
	 * The method extracts all the complaints from a JSON string.
	 * 
	 * @param jsonStr
	 *            the json string to work on.
	 * 
	 * @return a string of arrays containing the titles of the complaints, or
	 *         null if anything has gone wrong.
	 */
	public static String[] getComplaintsFromJSONString(String jsonStr) {

		// check the input parameter
		if (jsonStr == null) {
			return null;
		}

		// the variable to return
		ArrayList<String> toreturn = new ArrayList<String>();

		try {

			// get the json object
			JSONObject jsonObj = new JSONObject(jsonStr);

			// get the array with the complaints
			JSONArray arr = (JSONArray) jsonObj.get("complaint");
			if (arr == null) {
				return null;
			}

			// iterate over the JSON array
			for (int i = 0; i < arr.length(); i++) {

				// get the next JSON object and the belonging title
				JSONObject nextObj = (JSONObject) arr.get(i);

				String title = (String) nextObj.get("title");
				if (title != null) {
					toreturn.add(title);
				}
			}
		} catch (JSONException e) {
			// print the stack trace
			e.printStackTrace();
			return null;
		}

		return (String[]) toreturn.toArray(new String[0]);
	}

	/**
	 * The function is considered with extracting the properties of the
	 * complaints encoded in a JSON string.
	 * 
	 * @param jsonStr
	 *            the JSON string to work on.
	 * 
	 * @param vComplaintsData
	 *            the vehicle complaints data container to fill in the data.
	 */
	public static void extractComplaintsProperties(String jsonStr,
			VehicleComplaintsData vComplaintsData) {

		// check the input parameters
		if (jsonStr == null || vComplaintsData == null) {
			return;
		}

		try {

			// get the json object
			JSONObject jsonObj = new JSONObject(jsonStr);

			// get the array with the complaints
			JSONArray arr = (JSONArray) jsonObj.get("complaint");
			if (arr == null) {
				return;
			}

			// iterate over the JSON array
			for (int i = 0; i < arr.length(); i++) {

				// get the next JSON object and the belonging title
				JSONObject nextObj = (JSONObject) arr.get(i);

				// get the properties
				String title = (String) nextObj.get("title");
				String description = (String) nextObj.get("description");
				String tags = (String) nextObj.get("tags");
				
				// extract the categories
				String categories = "";
				// pick the list of categories
				JSONObject categoriesObj = (JSONObject) nextObj.get("category");
				if (categoriesObj != null) {
					categories += (String) categoriesObj.get("title");
				}

				// next we need to set all the values in the container
				vComplaintsData.categoryForVehicle.add(categories);
				vComplaintsData.descriptionsForVehicle.add(description);
				vComplaintsData.tagsForVehicle.add(tags);
				vComplaintsData.titlesForVehicle.add(title);
			}
		} catch (JSONException e) {
			// print the stack trace
			e.printStackTrace();
			return;
		}
	}

}
