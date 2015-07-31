package de.fhg.fokus.mdc.clients;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_INSERT;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_SEARCH;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_SEARCH_PARAMETER;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_STORAGE_UPDATE;
import static de.fhg.fokus.mdc.propertyProvider.Constants.STORAGE_URI;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * super class as client for the storage communication. It has property
 * provider- support and uses the springframework to communicate
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * @author izi
 */
public abstract class DataStoreClient extends GemoClient {

	/** table for implementation */
	protected String table = null;

	/**
	 * Constructor
	 * 
	 */
	public DataStoreClient() {
		restTemplate = new RestTemplate();
		url = props.getProperty(STORAGE_URI);
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

		String searchStr = props.getProperty(SERVICE_URI_STORAGE_SEARCH);
		String qParam = props.getProperty(SERVICE_URI_STORAGE_SEARCH_PARAMETER);
		HttpEntity<String> requestEntity = headerHelper
				.generateEntityForGet(authHeaders);
		URI uri;
		ResponseEntity<String> responseEntity = null;
		try {
			sqlString = URLEncoder.encode(sqlString, "UTF-8");
			log.debug("the url encoded sql string: " + sqlString);
			uri = new URI(url + searchStr + qParam + sqlString);
			// fire the request using the spring
			responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
					requestEntity, String.class);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return responseEntity.getBody();

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
		String insertStr = props.getProperty(SERVICE_URI_STORAGE_INSERT);
		HttpEntity<MultiValueMap<String, String>> requestEntity = headerHelper
				.generateEntityForPost(authHeaders, map);

		// fire the request using the spring
		ResponseEntity<String> responseEntity = restTemplate.exchange(url
				+ insertStr, HttpMethod.POST, requestEntity, String.class);

		return responseEntity.getBody();
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
		String updateStr = props.getProperty(SERVICE_URI_STORAGE_UPDATE);
		HttpEntity<MultiValueMap<String, String>> requestEntity = headerHelper
				.generateEntityForPost(authHeaders, map);

		// fire the request using the spring
		ResponseEntity<String> responseEntity = restTemplate.exchange(url
				+ updateStr, HttpMethod.POST, requestEntity, String.class);

		return responseEntity.getBody();
	}

}