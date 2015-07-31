package de.fhg.fokus.mdc.allgemeineEfzDaten.tests;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_GENERAL_EFZ_DATA;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_GENERAL_EFZ_DATA_QUERY;

import java.io.IOException;
import java.util.Properties;

import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;

public class VehicleGeneralTest extends JerseyTest {
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

	@Override
	protected AppDescriptor configure() {
		return new WebAppDescriptor.Builder().build();
	}

	// @Test
	public void testVehicleFetchesSuccess() {

		// WebResource webResource = client().resource(pathToGeneral);
		// ObjectMapper mapper = new ObjectMapper();
		// List<VehicleState> vehicleStates = null;
		//
		// String jsonResponse = webResource.path(pathToQueryGeneral)
		// .queryParam("EFzId", "6666").get(String.class);
		// try {
		// vehicleStates = mapper.readValue(jsonResponse,
		// new TypeReference<List<VehicleState>>() {
		// });
		// } catch (JsonGenerationException e) {
		//
		// e.printStackTrace();
		//
		// } catch (JsonMappingException e) {
		//
		// e.printStackTrace();
		//
		// } catch (IOException e) {
		//
		// e.printStackTrace();
		//
		// }
		// assertEquals("busy", vehicleStates.get(0).status);
	}

}
