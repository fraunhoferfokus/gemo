/**
 * 
 */
package de.fraunhofer.fokus.mdc.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Scheel, thomas.scheel@fokus.fraunhofer.de
 * 
 */
public class RandomPositionGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final double RADIUS_EARTH = 6372.796924; // in km

    /**
     * Generate positions.
     * 
     * @param count
     *            the count
     * @param radius
     *            the radius in km
     * @return the array list
     */
    public static ArrayList<HashMap<String, Double>> generatePositions(
            int count, Double radius, Double pLat, Double pLong) {
        ArrayList<HashMap<String, Double>> positions = new ArrayList<>();
        for (int i = 0; i < count; i++) {

            // generatePoint
            HashMap<String, Double> point = generatePointCircular(pLong, pLat,
                    radius);

            System.out.println(i + " generated position: lat, lon: "
                    + point.get("lat") + ", " + point.get("lon"));
            positions.add(point);
        }

        return positions;
    }

    /**
     * Generate point circular. like it's described here {@link http
     * ://www.geomidpoint.com/random/calculation.html}
     * 
     * @param startlon
     *            the startlon
     * @param startlat
     *            the startlat
     * @param lon
     *            the lon
     * @param lat
     *            the lat
     * @param distance
     *            the distance in km
     */
    public static HashMap<String, Double> generatePointCircular(
            Double startlon, Double startlat, Double distance) {
        Double lon = new Double(0.0);
        Double lat = new Double(0.0);

        // http://www.geomidpoint.com/random/calculation.html
        Double radLonStart = startlon * Math.PI / 180;
        Double radLatStart = startlat * Math.PI / 180;

        Double rand1 = (double) randomFloat(0, 1);
        Double rand2 = (double) randomFloat(0, 1);

        Double maxdist = distance / RADIUS_EARTH;

        // random distance
        Double dist = Math.acos(rand1 * (Math.cos(maxdist) - 1) + 1);

        // random bearing
        Double brg = 2 * Math.PI * rand2;

        // calculate random lat
        lat = Math.asin(Math.sin(radLatStart) * Math.cos(dist)
                + Math.cos(radLatStart) * Math.sin(dist) * Math.cos(brg));
        // calculate random lon
        lon = radLonStart
                + Math.atan2(
                        Math.sin(brg) * Math.sin(dist) * Math.cos(radLatStart),
                        Math.cos(dist) - Math.sin(radLatStart) * Math.sin(lat));

        // maybe this need to be moved up
        if (lon < -Math.PI) {
            lon = lon + 2 * Math.PI;
        }

        if (lon > Math.PI) {
            lon = lon - 2 * Math.PI;
        }
        // Convert back to degree from radial
        lon = lon * 180 / Math.PI;
        lat = lat * 180 / Math.PI;

        HashMap<String, Double> point = new HashMap<String, Double>();
        point.put("lon", lon);
        point.put("lat", lat);

        return point;
    }

    private static float randomFloat(float low, float high) {
        return (float) (Math.random() * (high - low) + low);
    }
}
