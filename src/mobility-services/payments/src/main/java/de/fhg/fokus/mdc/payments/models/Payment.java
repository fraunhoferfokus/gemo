package de.fhg.fokus.mdc.payments.models;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Bill/Payment type for receiving all information (prices) about the current
 * reservation (session).
 * 
 * This POJO will be persisted using the PaymentDataStoreClient
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */

public class Payment {

	/**
	 * system id (pk)
	 */
	@JsonProperty("_id")
	private Integer id = -1;

	/**
	 * the user profile id (fk)
	 */
	@JsonProperty("userprofileid")
	private Integer userprofileID = -1;

	/**
	 * the userprofile to work with
	 */
	@JsonIgnore
	private Userprofile userprofile;

	/**
	 * the group id (sequence id) of the eVehicle reservations
	 */
	@JsonProperty("reservationsequenceid")
	private Integer reservationSequenceID = -1;

	/**
	 * the list of all reservations to work with (proce calculation) it is also
	 * used to generate the items for the storage (text of all reservation
	 * items)
	 */
	@JsonIgnore
	private List<Reservation> reservations;

	/**
	 * by default false. Before the user can pay a bill, it must be confirmed. A
	 * confirmed bill is paid automatically (or triggers the electronic payment)
	 */
	@JsonProperty("confirmed")
	private Boolean confirmed = false;

	/**
	 * Derived attribute (from confirmed), by default false. Should be set by
	 * the payment process/service.
	 */
	@JsonProperty("paid")
	private Boolean paid = false;

	/**
	 * serialized and readable (plain text) version of List<Reservation> to
	 * get/save a very first variant of billed items (later pdf) later
	 */
	@JsonProperty("items")
	private String items = "";

	/**
	 * the total price of all items
	 */
	@JsonProperty("totalprice")
	private float totalPrice = 0;

	/**
	 * The date when the bill was created the very first time (important for the
	 * dunning). It will be set automatically.
	 */
	@JsonProperty("createdat")
	private Date createdAt;

	/**
	 * The date when the user had paid. It will be set automatically.
	 */
	@JsonProperty("paidat")
	private Date paidAt;

	/**
	 * by default false. A deleted payment is marked as deleted only
	 */
	@JsonProperty("deleted")
	private Boolean deleted = false;

	// empty constructor
	public Payment() {

	}

	/**
	 * constructor for a new bill
	 * 
	 * @info the fields "id", "confirmed" and "createdAt" are automated stuff,
	 *       because the user must confirm a new bill at first, id comes from
	 *       the DB and createdAt is the current system creation time.
	 * 
	 * @param userprofile
	 * @param reservationSequenceID
	 * @param reservations
	 */
	public Payment(Userprofile userprofile, Integer reservationSequenceID,
			List<Reservation> reservations) {

		this.setUserprofile(userprofile);
		this.setReservationSequenceID(reservationSequenceID);
		this.setReservations(reservations);

		// autom. generated content:
		this.setConfirmed(false);// user must confirm first
		this.setCreatedAt(new Date()); // of course now
	}

	// -------------[ getter, setter ]------------------

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserprofileID() {
		return userprofileID;
	}

	/**
	 * derived from userprofile
	 * 
	 * @param userprofileID
	 */
	private void setUserprofileID(Integer userprofileID) {
		this.userprofileID = userprofileID;
	}

	public Userprofile getUserprofile() {
		return userprofile;
	}

	public void setUserprofile(Userprofile userprofile) {
		this.userprofile = userprofile;
		this.setUserprofileID(this.userprofile._id);
	}

	public Integer getReservationSequenceID() {
		return reservationSequenceID;
	}

	public void setReservationSequenceID(Integer sequenceID) {
		this.reservationSequenceID = sequenceID;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	/**
	 * a confirmed bill in this prototype is automated marked as paid
	 * 
	 * @see paid (setPaid), paidAt
	 * @param confirmed
	 */
	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
		if (confirmed)
			this.setPaid(true);
	}

	public Boolean getPaid() {
		return paid;
	}

	/**
	 * derived from confirmed, no public setter here
	 * 
	 * @param paid
	 */
	private void setPaid(Boolean paid) {
		this.paid = paid;
		if (paid)
			this.setPaidAt(new Date()); // now
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getPaidAt() {
		return paidAt;
	}

	/**
	 * derived attribute (from paid), no public setter here
	 * 
	 * @return
	 */
	private void setPaidAt(Date paidAt) {
		this.paidAt = paidAt;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	// -------------[ simple business logic ]------------------

	// pay this payment
	public void pay() {
		this.setConfirmed(true);
	}
}