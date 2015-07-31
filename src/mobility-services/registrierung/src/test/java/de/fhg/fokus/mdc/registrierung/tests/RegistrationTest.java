package de.fhg.fokus.mdc.registrierung.tests;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_GENERAL_EFZ_DATA;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_GENERAL_EFZ_DATA_QUERY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Properties;

import org.junit.Test;

import junit.framework.TestCase;

import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;

public class RegistrationTest {
	private static Properties props = null;
	private static final String pathToGeneral;
	private static final String pathToQueryGeneral;

	static {
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pathToGeneral = props.getProperty(SERVICE_URI_GENERAL_EFZ_DATA);
		pathToQueryGeneral = props
				.getProperty(SERVICE_URI_GENERAL_EFZ_DATA_QUERY);
	}

	@Test
	public void testRegistration() throws IOException {

		String path = "http://localhost:8080/Registrierung/register?" +
				"Vorname="+URLEncoder.encode("Benjamin", "UTF-8") + "&" +
				"Nachname="+URLEncoder.encode("Dittwald", "UTF-8") +"&" +
				"Passwort=pwd&" +
				"Email="+URLEncoder.encode("benjamin.dittwald@fokus.fraunhofer.de", "UTF-8") + "&" +
				"Geburtsdatum="+URLEncoder.encode("01.01.1901", "UTF-8") + "&" +
				"Strasse=test-strasse&" +
				"Hausnummer=38&" +
				"PLZ=13187&" +
				"Ort=Berlin&" +
				"Telefonnummer=123456&" +
				"Kontoinhaber="+URLEncoder.encode("Benjamin Dittwald", "UTF-8") + "&" +
				"Kontonummer=1111111&" +
				"Bankleitzahl=111111&" +
				"Praeferenzen=12&" +
				"Fahrzeugtyp=van&" +
				"OEPNV_Affinitaet=4&" +
				"Fuehrerschein_Klasse=A&" +
				"Fuehrerschein_Datum="+ URLEncoder.encode("01.01.01", "UTF-8") + "&" +
				"Fuehrerschein_Ort=Berlin&" +
				"Fuehrerschein_Nr=11111&" +
				"Sprache=DE&" +
				"Kreditkarten_Nr=12345&" +
				"AGB_akzeptiert_Datum=" + URLEncoder.encode("01.01.01", "UTF-8") + "&";

		for (int i = 0; i < 1000; i++) {
			String pathNew = path + "Nutzername=huhuhuhu" + i;
			System.out.println ("" + i + ":" + pathNew);
			
			URL u = new URL(pathNew);
			URLConnection yc = u.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
			}
			in.close();
		}
	}

}
