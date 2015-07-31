package de.fhg.fokus.mdc.utils.security.clients;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_PRIVATE_INSERT;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_PRIVATE_SEARCH;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_PRIVATE_SEARCH_PARAMETER;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_PRIVATE_UPDATE;
import static de.fhg.fokus.mdc.propertyProvider.Constants.STORAGE_URI;

import java.io.IOException;
import java.util.Properties;

import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;

/**
 * super class as client for the storage communication. It has property
 * provider- support and uses the springframework to communicate
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * @author izi (Ilke Zilci, ilke.zilci@fokus.fraunhofer.de)
 * 
 */
// TODO allow loadproperties according to profile -> target storage or
// storage/private
public abstract class AuthDataStoreClient {

	/** table for implementation */
	protected String table = null;

	/** The url string. */
	protected static String url = null;

	/** local REST template. */
	private RestTemplate restTemplate = null;

	protected static Properties props = null;

	static {
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
		url = props.getProperty(STORAGE_URI);
	}

	/**
	 * Constructor
	 * 
	 */
	public AuthDataStoreClient() {
		restTemplate = new RestTemplate();
		defineTableName();
	}

	/**
	 * defines the table name to work with in the concrete implementation class
	 */
	public abstract void defineTableName();

	/**
	 * this method queries against the storage using a select statement
	 * 
	 * @param sqlString
	 *            the sql select statement
	 * @return data in form of a JSON String.
	 */
	protected String select(String sqlString) {

		String searchStr = props
				.getProperty(SERVICE_URI_STORAGE_PRIVATE_SEARCH);
		String qParam = props
				.getProperty(SERVICE_URI_STORAGE_PRIVATE_SEARCH_PARAMETER);

		// fire the request using the spring
		return restTemplate.getForObject(url + searchStr + qParam + sqlString,
				String.class);
	}

	/**
	 * this method sends form data to insert into the storage
	 * 
	 * @param map
	 *            the MultiValueMap<String, String> (Value map) to post to the
	 *            storage
	 * @return "OK" or "FAIL" from storage
	 */
	protected String insert(MultiValueMap<String, String> map) {
		String insertStr = props
				.getProperty(SERVICE_URI_STORAGE_PRIVATE_INSERT);

		// build the request path using the spring
		return restTemplate.postForObject(url + insertStr, map, String.class);
	}

	/**
	 * this method sends form data to update
	 * 
	 * @param map
	 *            the MultiValueMap<String, String> (Value map) to post to the
	 *            storage
	 * @return "OK" or "FAIL" from storage
	 */
	protected String update(MultiValueMap<String, String> map) {
		String updatetStr = props
				.getProperty(SERVICE_URI_STORAGE_PRIVATE_UPDATE);

		// build the request path using the spring
		return restTemplate.postForObject(url + updatetStr, map, String.class);
	}

}