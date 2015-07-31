package de.fhg.fokus.mdc.serviceVerfuegbarkeitEFz.tests;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_AVAILABILITY_EFZ;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_AVAILABILITY_EFZ_QUERY;
import static de.fhg.fokus.mdc.propertyProvider.Constants.STORAGE_URI;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.http.localserver.LocalTestServer;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;

import de.fhg.fokus.mdc.jsonObjects.VehicleState;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;

public class DataStoreClientStorageIntegrationTest extends JerseyTest {
	private static LocalTestServer server = null;
	private static String serverUrl = null;
	private static Properties props = null;
	private static final String pathToDataStore;
	private static final String pathToAvailability;
	private static final String pathToQueryAvailability;

	static {
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pathToAvailability = props.getProperty(SERVICE_URI_AVAILABILITY_EFZ);
		pathToQueryAvailability = props
				.getProperty(SERVICE_URI_AVAILABILITY_EFZ_QUERY);
		pathToDataStore = props.getProperty(STORAGE_URI);
	}

	// @Override
	// public TestContainerFactory getTestContainerFactory() {
	// return new GrizzlyWebTestContainerFactory();
	// }
	// TODO align with the implementation use the
	// VehicleAvailabitliyDataStoreClient instead of OldDataStore Client
	// @Override
	// public WebAppDescriptor configure() {
	// return new WebAppDescriptor.Builder()
	// .initParam(WebComponent.RESOURCE_CONFIG_CLASS,
	// ClassNamesResourceConfig.class.getName())
	// .initParam(
	// ClassNamesResourceConfig.PROPERTY_CLASSNAMES,
	// QueryVehicleAvailabilityService.class.getName() + ";"
	// + OldDataStoreClient.class.getName()).build();
	// }

	// @Test
	public void testQueryAvailabilityDataStoreIntegration() {
		// prepare request to vehicleAvailabilityService
		WebResource webResource = client().resource(pathToAvailability);
		String jsonResponse = webResource.path(pathToQueryAvailability)
				.queryParam("EFzId", "6666").get(String.class);
		ObjectMapper mapper = new ObjectMapper();
		List<VehicleState> vehicleStates = null;
		try {
			vehicleStates = mapper.readValue(jsonResponse,
					new TypeReference<List<VehicleState>>() {
					});
		} catch (JsonGenerationException e) {

			e.printStackTrace();

		} catch (JsonMappingException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		assertEquals("busy", vehicleStates.get(0).status);
	}
}
