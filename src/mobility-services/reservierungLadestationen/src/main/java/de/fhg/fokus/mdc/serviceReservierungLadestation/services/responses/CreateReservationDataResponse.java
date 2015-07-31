package de.fhg.fokus.mdc.serviceReservierungLadestation.services.responses;

import org.codehaus.jackson.annotate.JsonProperty;

import de.fhg.fokus.mdc.jsonObjects.ChargingStationReservation;
import de.fhg.fokus.mdc.jsonObjects.GemoDataResponse;

public class CreateReservationDataResponse extends GemoDataResponse {

	public CreateReservationDataResponse() {

	}

	public CreateReservationDataResponse(
			ChargingStationReservation chargingStationReservation) {
		setChargingStationReservation(chargingStationReservation);
	}

	@JsonProperty("ChargingStationReservation")
	private ChargingStationReservation chargingStationReservation;

	/**
	 * @return the chargingStationReservation
	 */
	public ChargingStationReservation getChargingStationReservation() {
		return chargingStationReservation;
	}

	/**
	 * @param chargingStationReservation
	 *            the chargingStationReservation to set
	 */
	public void setChargingStationReservation(
			ChargingStationReservation chargingStationReservation) {
		this.chargingStationReservation = chargingStationReservation;
	}

}
