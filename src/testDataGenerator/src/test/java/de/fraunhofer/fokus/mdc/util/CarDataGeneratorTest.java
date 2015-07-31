/**
 * 
 */
package de.fraunhofer.fokus.mdc.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author tsc
 * 
 */
public class CarDataGeneratorTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Properties props = new Properties();

    // private TransformerFactory transFactory;
    // private Transformer trans;

    // private static final String SOURCE = "govuk_ckan";
    // private static final String SOURCE_URL = "http://data.gov.uk/api/";

    private CarDataGenerator gen = null;

    @BeforeTest
    public void beforeTest() {
        gen = new CarDataGenerator();
    }

    @Test
    public void generatePositionFileTest() {

        try {
            gen.generatePositionFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
