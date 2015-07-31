package de.fhg.fokus.participationApp.chargingStation;

/**
 * The class consitutes the methods to access charging station.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 */
public class ChargingStationGateway {

	
	/** The singleton instance. */
	private static ChargingStationGateway instance = null;
	
	
	/** Constructor for the singleton instance. */
	private ChargingStationGateway () {};
	
	
	/**
	 * Method for the singleton.
	 * 
	 * @return the singleton instance.
	 */
	public static ChargingStationGateway getInstance() {
		if (instance == null) {
			instance = new ChargingStationGateway();
		}
		return instance;
	}


	/**
	 * The method returns the latitude.
	 * 
	 * @return the latitude.
	 */
	public String getLatitude() {
		// TODO Auto-generated method stub
		return "XXX";
	}


	/**
	 * The method returns the country code.
	 * 
	 * @return the country code.
	 */
	public String getCountryCode() {
		// TODO Auto-generated method stub
		return "XXX";
	}

	/**
	 * The method returns the city.
	 * 
	 * @return the country city.
	 */
	public String getCity() {
		// TODO Auto-generated method stub
		return "XXX";
	}

	/**
	 * The method returns the street.
	 * 
	 * @return the country street.
	 */
	public String getStreet() {
		// TODO Auto-generated method stub
		return "XXX";
	}


	/**
	 * The method returns the house No.
	 * 
	 * @return the country house No.
	 */
	public String getHouseNo() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * The method returns the postal code.
	 * 
	 * @return the postal code.
	 */
	public String getPostalCode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * The method returns the longtitude.
	 * 
	 * @return the postal longtitude.
	 */
	public String getLongtitude() {
		// TODO Auto-generated method stub
		return null;
	}

}
