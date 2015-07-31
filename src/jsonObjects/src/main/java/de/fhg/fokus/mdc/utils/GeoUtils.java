/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhg.fokus.mdc.utils;

// TODO: Auto-generated Javadoc
/**
 * The Class GeoUtils.
 * 
 * @author bdi
 */
public class GeoUtils {

	/** The Constant EARTH_RADIUS. */
	private static final double EARTH_RADIUS = 6371000; // m

	/**
	 * Calculates the distance in meters between two geo-coordinates.
	 * 
	 * @param clientLocation
	 *            Location of the client.
	 * @param cinemaLocation
	 *            Location of the note.
	 * @return The calculated distance in meters.
	 */
	public static double calculateDistance(GeoObject clientLocation, GeoObject cinemaLocation) {

		/*
		 * Calculate distance between two points
		 * 
		 * R = earth’s radius (mean radius = 6,371km) Δlat = lat2− lat1 Δlong =
		 * long2− long1 a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlong/2) c =
		 * 2.atan2(√a, √(1−a)) d = R.c
		 */

		double dLon = Math.toRadians(clientLocation.getLongitude() - cinemaLocation.getLongitude());
		double dLat = Math.toRadians(clientLocation.getLatitude() - cinemaLocation.getLatitude());

		double a = Math.pow(Math.sin(dLat / 2), 2)
				+ (Math.cos(Math.toRadians(cinemaLocation.getLatitude())) * (Math.cos(Math.toRadians(clientLocation.getLatitude()))) * (Math.pow(
						Math.sin(dLon / 2), 2)));

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c;
	}
}
