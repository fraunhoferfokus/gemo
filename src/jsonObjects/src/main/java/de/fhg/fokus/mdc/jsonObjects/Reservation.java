package de.fhg.fokus.mdc.jsonObjects;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.fhg.fokus.mdc.carAccess.EVehicleToken;
import de.fhg.fokus.mdc.utils.JsonDateDeserializer;
import de.fhg.fokus.mdc.utils.JsonDateSerializer;

/**
 * model for eVehicle reservation (reservierungEfz service)
 * 
 * @see table e_vehicle_reservation
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class Reservation {

	/**
	 * system id (pk)
	 */
	@JsonProperty("_id")
	private Integer id = -1;

	/**
	 * vehicle id
	 */
	@JsonProperty("evehicleid")
	private String eVehicleID = "";

	/**
	 * user id
	 */
	@JsonProperty("userid")
	private String userID = "";

	/**
	 * reservation begin
	 */
	@JsonProperty("resbegin")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date resBegin;

	/**
	 * reservation end
	 */
	@JsonProperty("resend")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date resEnd;

	/**
	 * longitude start
	 */
	@JsonProperty("longstart")
	private Double longStart = 0d;

	/**
	 * longitude end
	 */
	@JsonProperty("longend")
	private Double longEnd = 0d;

	/**
	 * latitude start
	 */
	@JsonProperty("latstart")
	private Double latStart = 0d;

	/**
	 * latitude end
	 */
	@JsonProperty("latend")
	private Double latEnd = 0d;

	/**
	 * Wifi code
	 */
	@JsonProperty("wifi_code")
	private String wifiCode = "";

	/**
	 * Reservation type
	 * 
	 * @see gemo.reservation.type =
	 *      maintenance,idle,tour_business,tour_private,parking
	 */
	@JsonProperty("restype")
	private String resType = "parking";

	/**
	 * Token
	 */
	@JsonProperty("eVehicleToken")
	private EVehicleToken eVehicleToken;

	@JsonIgnore
	public EVehicleToken getEVehicleToken() {
		return eVehicleToken;
	}

	@JsonProperty("eVehicleToken")
	public void setEVehicleToken(EVehicleToken eVehicleToken) {
		this.eVehicleToken = eVehicleToken;
	}

	/**
	 * Bluetooth MAC address of the smartphone in the format 12:34:56:78:9A:BC
	 */
	@JsonProperty("macaddress")
	private String macAddress = "";

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	/********************* [ constructor ] *********************/

	public Reservation() {

	}

	/******************** [ getter setter] *********************/

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
	 * @return the eVehicleID
	 */
	public String geteVehicleID() {
		return eVehicleID;
	}

	/**
	 * @param eVehicleID
	 *            the eVehicleID to set
	 */
	public void seteVehicleID(String eVehicleID) {
		this.eVehicleID = eVehicleID;
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * @return the resBegin
	 */
	public Date getResBegin() {
		return resBegin;
	}

	/**
	 * @param resBegin
	 *            the resBegin to set
	 */
	public void setResBegin(Date resBegin) {
		this.resBegin = resBegin;
	}

	/**
	 * @return the resEnd
	 */
	public Date getResEnd() {
		return resEnd;
	}

	/**
	 * @param resEnd
	 *            the resEnd to set
	 */
	public void setResEnd(Date resEnd) {
		this.resEnd = resEnd;
	}

	/**
	 * @return the longStart
	 */
	public Double getLongStart() {
		return longStart;
	}

	/**
	 * @param longStart
	 *            the longStart to set
	 */
	public void setLongStart(Double longStart) {
		this.longStart = longStart;
	}

	/**
	 * @return the longEnd
	 */
	public Double getLongEnd() {
		return longEnd;
	}

	/**
	 * @param longEnd
	 *            the longEnd to set
	 */
	public void setLongEnd(Double longEnd) {
		this.longEnd = longEnd;
	}

	/**
	 * @return the latStart
	 */
	public Double getLatStart() {
		return latStart;
	}

	/**
	 * @param latStart
	 *            the latStart to set
	 */
	public void setLatStart(Double latStart) {
		this.latStart = latStart;
	}

	/**
	 * @return the latEnd
	 */
	public Double getLatEnd() {
		return latEnd;
	}

	/**
	 * @param latEnd
	 *            the latEnd to set
	 */
	public void setLatEnd(Double latEnd) {
		this.latEnd = latEnd;
	}

	/**
	 * @return the wifiCode
	 */
	public String getWifiCode() {
		return wifiCode;
	}

	/**
	 * @param wifiCode
	 *            the wifiCode to set
	 */
	public void setWifiCode(String wifiCode) {
		this.wifiCode = wifiCode;
	}

	/**
	 * @return the resType
	 */
	public String getResType() {
		return resType;
	}

	/**
	 * @param resType
	 *            the resType to set
	 */
	public void setResType(String resType) {
		this.resType = resType;
	}

	@JsonIgnore
	public void generateToken() {
		this.eVehicleToken = new EVehicleToken(this);
	}

}
