package de.fhg.fokus.mdc.jsonObjects;

import org.codehaus.jackson.annotate.JsonProperty;

public class OdrcGemoDataResponse extends GemoDataResponse {
	@JsonProperty("message")
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
