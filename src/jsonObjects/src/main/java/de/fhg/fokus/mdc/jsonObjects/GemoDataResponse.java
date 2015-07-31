package de.fhg.fokus.mdc.jsonObjects;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Central container class as typed response. Please extend this class to offer
 * tidy responses, instead of dirty strings ^^
 * 
 * if no automation for object serialization runs on the live system, use the
 * the toString-Method or the common ObjectMapper to return your container
 * response objects like here:
 * 
 * <pre>
 * ObjectMapper mapper = new ObjectMapper();
 * String serializeJson = mapper.writeValueAsString(theObject);
 * //as Response
 * return Response.status(500).entity(serializeJson).build();
 * //as String
 * return serializeJson
 * </pre>
 * 
 * @example of output:
 * 
 *          //instead of "OK", output:
 * 
 *          <pre>
 * {
 * 		type: "DataResponseClassname", //automatism
 * 		availability: true //example field in your extension 
 * }
 * </pre>
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class GemoDataResponse {

	@JsonProperty("type")
	private String type = this.getClass().getName();

	public GemoDataResponse() {

	}

	@Override
	public String toString() {
		String res = "";
		try {
			res = new ObjectMapper().writeValueAsString(this);
		} catch (JsonGenerationException e) {
			res = e.toString();
		} catch (JsonMappingException e) {
			res = e.toString();
		} catch (IOException e) {
			res = e.toString();
		}
		return res;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
