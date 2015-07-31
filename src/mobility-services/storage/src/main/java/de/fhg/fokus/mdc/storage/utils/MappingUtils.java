package de.fhg.fokus.mdc.storage.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public class MappingUtils {

	public static String transformedResultListMapToJSONString(
			List<Map<String, Object>> listMap) {

		// String date pattern that match to XML Timestamp pattern
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.S");

		JSONArray jsonArray = new JSONArray();

		for (Map<String, Object> entry : listMap) {
			for (String key : entry.keySet()) {

				// Convert Timestamp to String
				if (entry.get(key) instanceof Timestamp) {
					entry.put(key, sdf.format((Date) entry.get(key)));
				}
			}
			jsonArray.add(entry);
		}

		return jsonArray.toString();
	}
}
