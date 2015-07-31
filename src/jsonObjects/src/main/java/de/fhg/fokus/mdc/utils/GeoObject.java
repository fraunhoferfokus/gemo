/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhg.fokus.mdc.utils;

import java.io.Serializable;

/**
 * 
 * @author benjamin
 */
public class GeoObject implements Serializable {

	private double longitude;
	private double latitude;

	public GeoObject() {
		// default
	}

	/**
	 * Create object initialized with longitude and latitude.
	 * 
	 * @param longitude
	 *            Longitude value.
	 * @param latitude
	 *            Latitude value.
	 */
	public GeoObject(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	/**
	 * 
	 * @return
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * 
	 * @param latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * 
	 * @return
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * 
	 * @param longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
