package de.fhg.fokus.participationApp.vehicle;

// imports


/**
 * This class is responsible for obtaining various information related to the current vehicle. 
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class VehicleGateway {

	/** The instance for the singleton pattern. */
	private static VehicleGateway instance = null;

	/** Constructor for the singleton pattern. */
	private VehicleGateway() {
	}

	/**
	 * The getInstance method for the singleton pattern.
	 * 
	 * @return the singleton instance.
	 */
	public static VehicleGateway getInstance() {
		if (instance == null) {
			instance = new VehicleGateway();
		}
		return instance;
	}

	/**
	 * Returns the latitude.
	 * 
	 * @return the latitude.
	 */
	public String getLatitude() {
		// TODO Auto-generated method stub
		return "XXX";
	}

	/**
	 * Returns the latitude.
	 * 
	 * @return the latitude.
	 */
	public String getLongitude() {
		// TODO Auto-generated method stub
		return "XXX";
	}

	/**
	 * Returns the country code.
	 * 
	 * @return the country code.
	 */
	public String getCountryCode() {
		// TODO Auto-generated method stub
		return "XXX";
	}

	/**
	 * Returns the city.
	 * 
	 * @return the city.
	 */
	public String getCity() {
		// TODO Auto-generated method stub
		return "XXX";
	}

	/**
	 * Returns the latitude.
	 * 
	 * @return the latitude.
	 */
	public String getStreet() {
		// TODO Auto-generated method stub
		return "XXX";
	}

	/**
	 * Returns the Postal Code.
	 * 
	 * @return the Postal Code.
	 */
	public String getPostalCode() {
		// TODO Auto-generated method stub
		return "XXX";
	}

	/**
	 * Returns the HouseNo.
	 * 
	 * @return the HouseNo.
	 */
	public String getHouseNo() {
		// TODO Auto-generated method stub
		return "XXX";
	}

}
