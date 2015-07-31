package de.fhg.fokus.mdc.jsonObjects;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.fhg.fokus.mdc.utils.JsonDateDeserializer;
import de.fhg.fokus.mdc.utils.JsonDateSerializer;

/**
 * model for one reservation segment (detail)
 * 
 * @see table e_vehicle_reservation_details
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class ReservationSegment {

	/**
	 * system id (pk)
	 */
	@JsonProperty("_id")
	private Integer id = -1;

	/**
	 * system id (pk)
	 */
	@JsonProperty("reservationid")
	private Integer reservationid = -1;

	/**
	 * reservation begin of this segment
	 */
	@JsonProperty("segbegin")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date segbegin;

	/**
	 * reservation end of this segment
	 */
	@JsonProperty("segend")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date segend;

	/**
	 * the reservation type as enum string
	 * 
	 * @see gemo.reservation.type (at the moment
	 *      maintenance,tour_business,tour_private,parking)
	 */
	@JsonProperty("reservationtype")
	public String reservationtype;

	// empty constructor
	public ReservationSegment() {

	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the reservationid
	 */
	public Integer getReservationid() {
		return reservationid;
	}

	/**
	 * @return the segbegin
	 */
	public Date getSegbegin() {
		return segbegin;
	}

	/**
	 * @return the segend
	 */
	public Date getSegend() {
		return segend;
	}

	/**
	 * @return the reservationtype
	 */
	public String getReservationtype() {
		return reservationtype;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param reservationid
	 *            the reservationid to set
	 */
	public void setReservationid(Integer reservationid) {
		this.reservationid = reservationid;
	}

	/**
	 * @param segbegin
	 *            the segbegin to set
	 */
	public void setSegbegin(Date segbegin) {
		this.segbegin = segbegin;
	}

	/**
	 * @param segend
	 *            the segend to set
	 */
	public void setSegend(Date segend) {
		this.segend = segend;
	}

	/**
	 * @param reservationtype
	 *            the reservationtype to set
	 */
	public void setReservationtype(String reservationtype) {
		this.reservationtype = reservationtype;
	}

}
