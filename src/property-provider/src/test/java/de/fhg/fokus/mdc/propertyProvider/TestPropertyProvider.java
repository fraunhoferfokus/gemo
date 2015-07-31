/**
 * 
 */
package de.fhg.fokus.mdc.propertyProvider;

import static de.fhg.fokus.mdc.propertyProvider.Constants.STORAGE_URI;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

/**
 * @author tsc
 * 
 * 
 */
public class TestPropertyProvider {

    @Test
    public void loadProperties() {

        Properties props = null;
        try {

            /** für lokale Test individuelle Instazierung mit Testadressen */
            // props = PropertyProvider.loadProperties(path);
            PropertyProvider propertyProvider = PropertyProvider.getInstance();
            /** Standardmäßiger aufruf der Properties aus Default Path lädt */
            props = propertyProvider.loadProperties();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(props.getProperty(STORAGE_URI));

    }
}
