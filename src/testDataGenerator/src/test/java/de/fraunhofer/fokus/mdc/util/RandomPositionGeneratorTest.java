/**
 * 
 */
package de.fraunhofer.fokus.mdc.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Thomas Scheel, thomas.scheel@fokus.fraunhofer.de
 * 
 */
public class RandomPositionGeneratorTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final Double LON = new Double(13.313195);
    private static final Double LAT = new Double(52.525713);

    private ArrayList<HashMap<String, Double>> positions = null;

    // private RandomPositionGenerator gen = null;

    @BeforeTest
    public void beforeTest() {
        // gen = new RandomPositionGenerator();
    }

    @Test
    public void generatePositionsTest() {

        positions = RandomPositionGenerator.generatePositions(1000, 1.5, LAT,
                LON);

    }
}
