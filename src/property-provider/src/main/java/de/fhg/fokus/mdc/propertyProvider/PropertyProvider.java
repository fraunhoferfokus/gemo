/**
 * 
 */
package de.fhg.fokus.mdc.propertyProvider;

import static de.fhg.fokus.mdc.propertyProvider.Constants.DEFAULT_PROPERTY_PATH;
import static de.fhg.fokus.mdc.propertyProvider.Constants.PROVIDER_PROPERTY_FILENAME;
import static de.fhg.fokus.mdc.propertyProvider.Constants.TEST_PROPERTY_PATH;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * The Class PropertyProvider.
 * 
 * @author tsc
 */
public class PropertyProvider {
    /** The instance for the singleton pattern. */
    private static PropertyProvider instance = null;

    public static PropertyProvider getInstance() {
        if (instance == null) {
            instance = new PropertyProvider();
        }

        return instance;
    }

    /**
     * Constructor for the singleton pattern.
     * 
     */
    private PropertyProvider() {

    }

    /**
     * @return
     * @throws IOException
     */
    public Properties loadProperties() throws IOException {

        // consider if test test_provider.properties file exits than load this,
        // else load provider.properties
        // Properties props2 = new Properties();
        // props2.load(Thread.currentThread().getContextClassLoader()
        // .getResourceAsStream("provider.properties"));
        // System.out.println("Test: test.properties "
        // + props2.getProperty("test.property.path")
        // + "  und hier original properties: "
        // + props2.getProperty("gemo.storage.uri"));

        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(PROVIDER_PROPERTY_FILENAME));

        String propertyPath = null;

        if (props.getProperty(TEST_PROPERTY_PATH) != null
                && !props.getProperty(TEST_PROPERTY_PATH).equals("")) {
            propertyPath = (String) props.getProperty(TEST_PROPERTY_PATH);
        } else {
            propertyPath = (String) props.getProperty(DEFAULT_PROPERTY_PATH);
        }

        System.out.println("Property Path: " + propertyPath);

        return loadProperties(propertyPath);

    }

    /**
     * @param filePath
     * @return
     * @throws IOException
     */
    public Properties loadProperties(String filePath) throws IOException {

        /** Read property file */
        FileInputStream fis = new FileInputStream(filePath);
        InputStreamReader in = new InputStreamReader(fis, "UTF-8");

        /** create Properties object of file */
        Properties serviceProps = new Properties();
        serviceProps.load(in);
        return serviceProps;
    }

}
