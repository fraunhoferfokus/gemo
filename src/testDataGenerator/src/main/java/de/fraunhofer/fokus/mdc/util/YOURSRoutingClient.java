/**
 * 
 */
package de.fraunhofer.fokus.mdc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author Thomas Scheel, thomas.scheel@fokus.fraunhofer.de
 * 
 * @see http://wiki.openstreetmap.org/wiki/YOURS#Routing_API
 * 
 */
public class YOURSRoutingClient {
    /**
     * The instance for the singleton pattern.
     */
    private static YOURSRoutingClient instance = null;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String YOURS_URL = "http://www.yournavigation.org/api/1.0/gosmore.php";

    /**
     * The url string.
     */
    private String url = null;
    /**
     * The local REST template.
     */
    private RestTemplate restTemplate = null;

    /**
     * Function to deliver the singleton instance.
     * 
     * @param url
     *            the url of the REST service.
     * 
     * @return the pre-configured data store client.
     */
    public static YOURSRoutingClient getInstance(String url) {
        if (instance == null) {
            instance = new YOURSRoutingClient(url);
        }

        return instance;
    }

    /**
     * Constructor for the singleton pattern.
     * 
     * @param url
     *            the url of the REST service. if url is null default YOURS url
     *            is used
     */
    private YOURSRoutingClient(String url) {
        if (url == null || url.equals("")) {
            this.url = YOURS_URL;
        }
        this.url = url;
        // von hier
        // DefaultHttpClient httpClient = new DefaultHttpClient();
        // BasicCredentialsProvider credentialsProvider = new
        // BasicCredentialsProvider();
        // credentialsProvider.setCredentials(AuthScope.ANY, new
        // UsernamePasswordCredentials(user, password);
        // httpClient.setCredentialsProvider(credentialsProvider);
        // ClientHttpRequestFactory rf = new
        // HttpComponentsClientHttpRequestFactory(httpClient);
        // template = new RestTemplate(rf);
        // }

        // nach hier

        restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJacksonHttpMessageConverter());
        restTemplate.setMessageConverters(converters);
    }

    public void setRestTemplate(RestTemplate rt) {
        this.restTemplate = rt;
    }

    /**
     * Route. uses YOURS to calculate a route
     * 
     * see {@link http://wiki.openstreetmap.org/wiki/YOURS#Routing_API} for more
     * information
     * 
     * @param params
     *            the params
     * @return the details in the form of a JSON String.
     */
    public JsonNode getYOURSRoute(HashMap<String, Object> params) {

        // the variable to return
        JsonNode toReturn = null;

        // String urlParams =
        // "?format={format}&flat={flat}&flon={flon}&tlat={tlat}&tlon={tlon}&v={v}&fast={fast}&layer={layer}&instructions={instructions}&lang={lang}";
        String urlParams = "?format={format}&flat={flat}&flon={flon}&tlat={tlat}&tlon={tlon}&v={v}";

        LOGGER.debug("query route: " + params.toString() + " with uri " + url
                + urlParams);
        toReturn = (ObjectNode) restTemplate.getForObject(url + urlParams,
                JsonNode.class, params);

        return toReturn;
    }

    // /**
    // * The method issues the update of the availability status of an electric
    // * vehicle.
    // *
    // * @param path
    // * the path to use.
    // *
    // * @param efzid
    // * the id of the electric vehicle.
    // *
    // * @param status
    // * the new status to set.
    // *
    // * @return the response from the data store, in general "OK" or "FAIL".
    // */
    // public String updateElectricVehicleAvailability(String path, String
    // efzid,
    // String status) {
    //
    // // the to string to return
    // String toreturn = "OK";
    //
    // // prepare the post parameter
    // MultiValueMap<String, String> map = new LinkedMultiValueMap<String,
    // String>();
    // map.add("where", "efzid=\'" + efzid + "\'");
    // map.add("status", status);
    // map.add("tableName", "efzavailability");
    //
    // // issue the post request
    // toreturn = restTemplate.postForObject(url + path, map, String.class);
    // if (toreturn == null) {
    // toreturn = "FAIL";
    // }
    //
    // return toreturn;
    // }
}
