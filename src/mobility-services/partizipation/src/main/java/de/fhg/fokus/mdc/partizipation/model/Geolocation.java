//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.28 at 05:03:36 PM MEZ 
//

package de.fhg.fokus.mdc.partizipation.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Geolocation complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Geolocation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="latitude" type="{http://www.fokus.fraunhofer.de/cm/schema/v1}Latitude"/>
 *         &lt;element name="longitude" type="{http://www.fokus.fraunhofer.de/cm/schema/v1}Longitude"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Geolocation", propOrder = { "latitude", "longitude" })
public class Geolocation {

	protected double latitude;
	protected double longitude;

	/**
	 * Gets the value of the latitude property.
	 * 
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the value of the latitude property.
	 * 
	 */
	public void setLatitude(double value) {
		this.latitude = value;
	}

	/**
	 * Gets the value of the longitude property.
	 * 
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the value of the longitude property.
	 * 
	 */
	public void setLongitude(double value) {
		this.longitude = value;
	}

}
