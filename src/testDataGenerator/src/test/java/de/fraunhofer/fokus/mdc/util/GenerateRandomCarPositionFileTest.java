package de.fraunhofer.fokus.mdc.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * The Class GenerateRandomCarPositionFileTest.
 * 
 * @author tsc
 */
public class GenerateRandomCarPositionFileTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final Double LON = new Double(13.313195);
    private static final Double LAT = new Double(52.525713);

    private GenerateRandomCarPositionFile gen = null;

    @BeforeTest
    public void beforeTest() throws IOException {
        gen = new GenerateRandomCarPositionFile();
    }

    @Test
    public void generatePositionsTest() {

        ArrayList<HashMap<String, Double>> StreetPositions = null;

        ArrayList<HashMap<String, Double>> randomPositions = RandomPositionGenerator
                .generatePositions(9, 1.5, LAT, LON);

        StreetPositions = gen.getStreetPositions(randomPositions);

        System.out.println("##############\n");
        for (HashMap<String, Double> point : StreetPositions) {
            System.out.println(point.get("lat") + ", " + point.get("lon"));
        }

    }

    /**
     * Generate position XML file with config.properties.
     * 
     * @throws IOException
     */
    @Test
    public void generatePositionFileTest() throws IOException {

        // test the init way via config file, central Berlin
        gen.generatePositionFile();
        // test the diy way with point in central Cologne
        // gen.generatePositionFile(50.936440, 6.958427, 1001, 4.0);
    }
}
