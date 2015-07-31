package de.fhg.fokus.mdc.jsonObjects;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.fhg.fokus.mdc.utils.JsonDateDeserializer;
import de.fhg.fokus.mdc.utils.JsonDateSerializer;

/**
 * model for one charging station reservation
 * 
 * @see table charging_station_reservation
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class ChargingStationReservation {

	/**
	 * system id (pk)
	 */
	@JsonProperty("_id")
	private Integer id = -1;

	/**
	 * reference id (chargingstationID)
	 */
	@JsonProperty("chargingstationid")
	private Integer chargingstationid = -1;

	/**
	 * user id (userID)
	 */
	@JsonProperty("userid")
	private Integer userid = -1;

	/**
	 * reservation begin
	 */
	@JsonProperty("resbegin")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date resbegin;

	/**
	 * reservation end
	 */
	@JsonProperty("resend")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date resend;

	/**
	 * the reservation accesscode
	 */
	@JsonProperty("accesscode")
	private String accesscode;

	// empty constructor
	public ChargingStationReservation() {

	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the chargingstationid
	 */
	public Integer getChargingstationid() {
		return chargingstationid;
	}

	/**
	 * @param chargingstationid
	 *            the chargingstationid to set
	 */
	public void setChargingstationid(Integer chargingstationid) {
		this.chargingstationid = chargingstationid;
	}

	/**
	 * @return the userid
	 */
	public Integer getUserid() {
		return userid;
	}

	/**
	 * @param userid
	 *            the userid to set
	 */
	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	/**
	 * @return the resbegin
	 */
	public Date getResbegin() {
		return resbegin;
	}

	/**
	 * @param resbegin
	 *            the resbegin to set
	 */
	public void setResbegin(Date resbegin) {
		this.resbegin = resbegin;
	}

	/**
	 * @return the resend
	 */
	public Date getResend() {
		return resend;
	}

	/**
	 * @param resend
	 *            the resend to set
	 */
	public void setResend(Date resend) {
		this.resend = resend;
	}

	/**
	 * @return the accesscode
	 */
	public String getAccesscode() {
		return accesscode;
	}

	/**
	 * @param accesscode
	 *            the accesscode to set
	 */
	public void setAccesscode(String accesscode) {
		this.accesscode = accesscode;
	}

}
