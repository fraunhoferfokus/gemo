package de.fhg.fokus.mdc.serviceVerfuegbarkeitLadestationen.services.responses;

import org.codehaus.jackson.annotate.JsonProperty;

import de.fhg.fokus.mdc.jsonObjects.GemoDataResponse;

/**
 * Return Container if the station is free
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class QueryStationAvailabilityDataResponse extends GemoDataResponse {

	public QueryStationAvailabilityDataResponse() {

	}

	@JsonProperty("StationAvailability")
	private Boolean stationAvailability = true;

}
