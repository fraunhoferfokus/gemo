package de.fhg.fokus.mdc.serviceReservierungEfz.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.spi.container.servlet.WebComponent;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.external.ExternalTestContainerFactory;

import de.fhg.fokus.mdc.exceptions.ExpectedBusinessException;
import de.fhg.fokus.mdc.jsonObjects.Reservation;
import de.fhg.fokus.mdc.serviceReservierungEfz.services.VehicleReservationService;
import de.fhg.fokus.mdc.utils.SimpleValidator;

/**
 * Jersey Test class
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 * 
 */
public class UnitTest extends JerseyTest {
	/** testing url */
	private static final String TEST_SERVICEURL = "http://localhost:8080/reservierungEfz/reservations/";
	private static final String TEST_CLEANSERVICEURL = "http://localhost:8080/reservierungEfz/reservations";
	private static final String RESERVATION_TABLE_NAME = "e_vehicle_reservation";

	// ----------------[ basic methods from JerseyTest]---------------------
	@Override
	public WebAppDescriptor configure() {

		return new WebAppDescriptor.Builder()
				.initParam(WebComponent.RESOURCE_CONFIG_CLASS,
						ClassNamesResourceConfig.class.getName())
				.initParam(ClassNamesResourceConfig.PROPERTY_CLASSNAMES,
						VehicleReservationService.class.getName()).build();
	}

	@Override
	protected TestContainerFactory getTestContainerFactory() {
		return new ExternalTestContainerFactory();
	}

	/***********************************************************************
	 ************************ [ Unit Tests ] *******************************
	 ***********************************************************************/

	@Test
	public void testPOSTReservation() throws JsonParseException,
			JsonMappingException, IOException {
		Reservation testReservation = null;
		try {
			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getFeatures().put(
					JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
			Client client = Client.create(clientConfig);

			// create time examples
			String resBegin = new SimpleDateFormat(
					SimpleValidator.DATE_TIME_PATTERN).format(new Date());
			String resEnd = new SimpleDateFormat(
					SimpleValidator.DATE_TIME_PATTERN).format(new Date()
					.getTime() + (15 * 60000));

			MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
			// insert params in map
			formData.add("eVehicleID", "ecar_00010");
			formData.add("userID", "1");
			formData.add("timeFrom", resBegin);
			formData.add("timeTo", resEnd);
			formData.add("longStart", "13.54313");
			formData.add("latStart", "52.42746");
			formData.add("type", "parking");

			testReservation = client.resource(TEST_SERVICEURL).post(
					Reservation.class, formData);

			assertTrue("reservation exists (_id>=0)",
					testReservation.getId() >= 0);

		} catch (UniformInterfaceException e) {
			fireExpectedBusinessExceptions(e);
		}
	}

	/**
	 * GET specific reservation id
	 * 
	 * @throws IOException
	 * @throws UniformInterfaceException
	 * @throws ClientHandlerException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Test
	public void test_GET() throws JsonParseException, JsonMappingException,
			ClientHandlerException, UniformInterfaceException, IOException {

		try {
			// -----------------[ Test 2: get single item ]-----------------
			// GET single reservation using a jersey WebResource
			WebResource webResource = client().resource(TEST_SERVICEURL);
			String jsonResponse = webResource.path("1").get(String.class);
			ObjectMapper mapper = new ObjectMapper();
			Reservation expectedReservation = mapper.readValue(jsonResponse,
					Reservation.class);
			// Test 1, compare reservation with id 1
			assertEquals(expectedReservation.getId(), (Integer) 1);

		} catch (UniformInterfaceException e) {
			fireExpectedBusinessExceptions(e);
		}
	}

	private void fireExpectedBusinessExceptions(UniformInterfaceException e) {
		try {
			// try to find serialized BusinessException in the response body or
			// do nothing
			ExpectedBusinessException.BusinessException businessException = e
					.getResponse().getEntity(
							ExpectedBusinessException.BusinessException.class);
			// throw expected Business Exception
			throw (ExpectedBusinessException) Class
					.forName(businessException.getType())
					.getConstructor(String.class)
					.newInstance(businessException.getMessage());
		} catch (ExpectedBusinessException createdException) {
			// throw away, because this was the goal here ;-)
			// We need this catch-block to be able to react on true exceptions
			// from the creation process
			throw createdException;
		} catch (Exception e2) {
			// there is no expected BusinessException, but the normal
			// UniformInterfaceException or normal exceptions during the
			// creation process
			throw e;
		}
	}
}
