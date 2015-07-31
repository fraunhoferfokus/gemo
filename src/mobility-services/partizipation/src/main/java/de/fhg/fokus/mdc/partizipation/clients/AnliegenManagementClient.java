package de.fhg.fokus.mdc.partizipation.clients;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_FIXMYCITY;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_FIXMYCITY_CATEGORY;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_FIXMYCITY_COMPLAINT;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_EFZID;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_LAT;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_LIMIT;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_LON;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_OFFSET;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_RADIUS;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import de.fhg.fokus.mdc.clients.GemoClient;
import de.fhg.fokus.mdc.partizipation.model.Category;
import de.fhg.fokus.mdc.partizipation.model.Complaint;
import de.fhg.fokus.mdc.partizipation.model.ComplaintRequest;
import de.fhg.fokus.mdc.partizipation.services.NoHostCheckSimpleClientHttpRequestFactory;
import de.fhg.fokus.mdc.partizipation.services.NullHostnameVerifier;
import de.fhg.fokus.mdc.partizipation.services.ParticipationResponseHandler;

/**
 * @author Ilke Zilci, ilke.zilci@fokus.fraunhofer.de
 * 
 *         Client for fixmycity service
 */
public class AnliegenManagementClient extends GemoClient {

	/** The instance for the singleton pattern. */
	private static AnliegenManagementClient instance = null;

	private static Logger fixmycityClientLogger = Logger
			.getLogger(AnliegenManagementClient.class.getName());

	private final String qParam;
	private final String latParam;
	private final String lonParam;
	private final String radParam;
	private final String offsetParam;
	private final String limitParam;
	private final String statusParam;

	/** The path to reach the fixmxcity ComplaintResource. */
	private final String complaintPath;

	/** The path to reach the fixmxcity CategoryResource. */
	private final String categoryPath;

	// private final String username;
	// private final String password;

	/**
	 * Function to deliver the singleton instance.
	 * 
	 * @param url
	 *            the url of the REST service.
	 * 
	 * @return the pre-configured data store client.
	 */
	public static AnliegenManagementClient getInstance() {
		if (instance == null) {
			instance = new AnliegenManagementClient();
		}

		return instance;
	}

	/**
	 * Constructor for the singleton pattern.
	 * 
	 * @param url
	 *            the url of the REST service.
	 */
	private AnliegenManagementClient() {
		url = props.getProperty(SERVICE_URI_FIXMYCITY);
		// to set up RestTemplate to use HttpComponentsClientHttpRequestFactory
		// restTemplate = new RestTemplate(
		// new HttpComponentsClientHttpRequestFactory());
		// if HttpComponentsClientHttpRequestFactory() set, RestTemplate uses
		// Apache HttpComponents, this requires changes in the trustAllhosts and
		// NoHostCheck...
		HostnameVerifier verifier = new NullHostnameVerifier();
		NoHostCheckSimpleClientHttpRequestFactory factory = new NoHostCheckSimpleClientHttpRequestFactory(
				verifier);
		restTemplate = new RestTemplate(factory);
		qParam = props
				.getProperty(SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_EFZID);
		latParam = props
				.getProperty(SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_LAT);
		lonParam = props
				.getProperty(SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_LON);
		radParam = props
				.getProperty(SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_RADIUS);
		offsetParam = props
				.getProperty(SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_OFFSET);
		limitParam = props
				.getProperty(SERVICE_URI_PARTICIPATION_COMPLAINT_QUERY_LIMIT);
		statusParam = "statusId=";

		complaintPath = props.getProperty(SERVICE_URI_FIXMYCITY_COMPLAINT);

		categoryPath = props.getProperty(SERVICE_URI_FIXMYCITY_CATEGORY);
		// username = props.getProperty(SERVICE_ACCESS_TO_FIXMYCITY_USERNAME);
		// password = props.getProperty(SERVICE_ACCESS_TO_FIXMYCITY_PASSWORD);

	}

	// used in tests to set a mocked restTemplate
	public void setRestTemplate(RestTemplate rt) {
		HostnameVerifier verifier = new NullHostnameVerifier();
		NoHostCheckSimpleClientHttpRequestFactory factory = new NoHostCheckSimpleClientHttpRequestFactory(
				verifier);
		restTemplate.setRequestFactory(factory);
		this.restTemplate = rt;
	}

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
			// RestTemplate uses org.apache.http.conn.ssl.SSLSocketFactory not
			// the one from javax if it is set to use HttpComponentsClientHttp
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HttpHeaders getBasicAuthHeaders(String userId, String username,
			String password) {
		HttpHeaders headers = new HttpHeaders();
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset
				.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		headers.set("Authorization", authHeader);
		headers.set("userId", userId);
		return headers;
	}

	@Deprecated
	// TODO Return appropriate error messages
	public ResponseEntity<String> getComplaintJSON(String path,
			String username, String password) throws URISyntaxException {

		// the variable to return
		ResponseEntity<String> toreturn = null;
		// prepare authentication and media type headers
		// first parameter in getAuthHeaders is userid, currently not actively
		// used
		HttpHeaders headers = getBasicAuthHeaders("", username, password);
		headers.setContentType(new MediaType("application", "json"));
		// set headers for the GET request
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

		ResponseErrorHandler errorHandler = new ParticipationResponseHandler();
		restTemplate.setErrorHandler(errorHandler);
		trustAllHosts();
		fixmycityClientLogger.info("participation sending request to : " + url
				+ path);
		URI uri = new URI(url + path);
		toreturn = restTemplate.exchange(uri, HttpMethod.GET, requestEntity,
				String.class);

		return toreturn;

	}

	/*
	 * Sends a POST request to the path with the map in request body as form
	 * data
	 */
	@Deprecated
	public ResponseEntity<String> postFormData(String path,
			MultiValueMap<String, Object> map, String username, String password) {

		// init message converter
		FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
		restTemplate.getMessageConverters().add(formHttpMessageConverter);

		// prepare authentication and media type headers
		HttpHeaders headers = getBasicAuthHeaders("", username, password);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		// set headers for the POST request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
				map, headers);
		// issue the post request
		fixmycityClientLogger.info("participation sending request to : " + url
				+ path);
		ResponseEntity<String> complaint = restTemplate.exchange(url + path,
				HttpMethod.POST, requestEntity, String.class);
		// complaint.getBody().toString()
		return complaint;
	}

	// TODO Return appropriate error messages
	public ResponseEntity<String> getEntityAsJSON(String path)
			throws URISyntaxException {
		// the variable to return
		ResponseEntity<String> toreturn = null;
		// prepare authentication and media type headers
		// first parameter in getAuthHeaders is userid, currently not actively
		// used
		authHeaders.setContentType(new MediaType("application", "json"));
		// set headers for the GET request
		HttpEntity<String> requestEntity = new HttpEntity<String>(authHeaders);

		ResponseErrorHandler errorHandler = new ParticipationResponseHandler();
		restTemplate.setErrorHandler(errorHandler);
		trustAllHosts();
		fixmycityClientLogger.info("participation sending request to : " + url
				+ path);
		URI uri = new URI(url + path);
		toreturn = restTemplate.exchange(uri, HttpMethod.GET, requestEntity,
				String.class);

		return toreturn;

	}

	public ResponseEntity<String> getComplaintByIdAsJSON(String complaintId)
			throws URISyntaxException {
		// the variable to return
		ResponseEntity<String> toreturn = null;
		toreturn = getEntityAsJSON(complaintPath + "/" + complaintId);
		return toreturn;
	}

	public ResponseEntity<String> queryComplaintsAsJSON(String query)
			throws URISyntaxException {
		// the variable to return
		ResponseEntity<String> toreturn = null;
		toreturn = getEntityAsJSON(complaintPath + query);
		return toreturn;
	}

	/*
	 * Sends a POST request to the path with the map in request body as form
	 * data
	 */
	public ResponseEntity<String> postFormData(
			MultiValueMap<String, Object> map, String path) {

		// init message converter
		FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
		restTemplate.getMessageConverters().add(formHttpMessageConverter);

		authHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// set headers for the POST request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
				map, authHeaders);
		// issue the post request
		fixmycityClientLogger.info("participation sending request to : " + url
				+ path);
		ResponseEntity<String> response = restTemplate.exchange(url + path,
				HttpMethod.POST, requestEntity, String.class);
		// complaint.getBody().toString()
		return response;
	}

	// TODO clean up here
	public Response queryConcernsByFmcId(String query)
			throws URISyntaxException {
		ResponseEntity<String> re = queryComplaintsAsJSON(query);
		return Response.status(re.getStatusCode().value()).entity(re.getBody())
				.build();
		// if (re == null) {
		// return Response
		// .status(404)
		// .entity("{\"message\":\"Background service not available\"}")
		// .type(MediaType.APPLICATION_JSON).build();
		// } else if (re.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
		// return Response
		// .status(500)
		// .entity("{\"message\":\"An error occured on the server side\"}")
		// .type(MediaType.APPLICATION_JSON).build();
		// }
		// return
		// Response.status(re.getStatusCode().value()).entity(re.getBody())
		// .type(MediaType.APPLICATION_JSON).build();
	}

	public String buildConcernQuery(String offset, String limit, String lat,
			String lon, String radius, String damageCodeP,
			String fmcIdOfGemoEntity) {
		String query = "";
		query += "?" + qParam + fmcIdOfGemoEntity + "&" + offsetParam + offset
				+ "&" + limitParam + limit + "&" + latParam + lat + "&"
				+ lonParam + lon + "&" + radParam + radius;
		if (damageCodeP != null && !damageCodeP.isEmpty()) {
			query += "&" + statusParam + damageCodeP;
		}
		return query;
	}

	/**
	 * creates a fixmycity category by sending a POST request to cm/categories
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * */
	public Category createFMCCategory(String eVehicleID)
			throws JsonParseException, JsonMappingException, IOException {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("title", eVehicleID);
		map.add("description", "Participation inputs for " + eVehicleID);
		ResponseEntity<String> categoryResponse = postFormData(map,
				categoryPath);

		fixmycityClientLogger.info("response to add category request : "
				+ categoryResponse.getBody());
		// save the received categoryid for evehicleid mapping
		ObjectMapper mapper = new ObjectMapper();
		Category category = mapper.readValue(categoryResponse.getBody(),
				Category.class);
		return category;
	}

	public Complaint createFMCComplaint(String title, String description,
			String tags, String lat, String lon, String fmcIdOfGemoEntity)
			throws JsonParseException, JsonMappingException, IOException {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("title", title);
		map.add("description", description);
		map.add("categoryId", fmcIdOfGemoEntity);
		// TODO map.add statusId statusids are generated and stored by
		// fixmycity's DB
		map.add("tags", tags);
		map.add("latitude", lat);
		map.add("longitude", lon);
		ResponseEntity<String> complaintResponse = postFormData(map,
				complaintPath);
		// save the received categoryid for evehicleid mapping
		ObjectMapper mapper = new ObjectMapper();
		Complaint complaint = mapper.readValue(complaintResponse.getBody(),
				Complaint.class);
		return complaint;

	}

	public String createFMCComplaintStr(String title, String description,
			String tags, String lat, String lon, String fmcIdOfGemoEntity)
			throws JsonParseException, JsonMappingException, IOException {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("title", title);
		map.add("description", description);
		map.add("categoryId", fmcIdOfGemoEntity);
		// TODO map.add statusId statusids are generated and stored by
		// fixmycity's DB
		map.add("tags", tags);
		map.add("latitude", lat);
		map.add("longitude", lon);
		ResponseEntity<String> complaintResponse = postFormData(map,
				complaintPath);
		// save the received categoryid for evehicleid mapping

		return complaintResponse.getBody();
	}

	/*
	 * Sends a POST request to the path with ComplaintRquest in request body as
	 * JSON
	 */
	// TODO The method which responds to this request in fixmycity has not been
	// tested, when that's ready test this one.
	@Deprecated
	public String addComplaint(String path, ComplaintRequest complaintRequest,
			String username, String password) {

		// initialize message converter
		MappingJacksonHttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJacksonHttpMessageConverter();
		restTemplate.getMessageConverters().add(
				mappingJacksonHttpMessageConverter);

		// prepare authentication and media type headers
		HttpHeaders headers = getBasicAuthHeaders("", username, password);
		headers.setContentType(MediaType.APPLICATION_JSON);

		// set headers for the POST request
		HttpEntity<ComplaintRequest> requestEntity = new HttpEntity<ComplaintRequest>(
				complaintRequest, headers);
		// issue the post request
		ResponseEntity<String> complaint = restTemplate.exchange(url + path,
				HttpMethod.POST, requestEntity, String.class);
		return complaint.getBody().toString();
	}

}
