package de.fhg.fokus.mdc.odrClientProxy.tests;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URI_USERPROFILE;
import static de.fhg.fokus.odp.registry.ckan.Constants.PROPERTY_NAME_CKAN_URL;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.fokus.mdc.odrClientProxy.model.CKANCategories;
import de.fhg.fokus.mdc.odrClientProxy.model.GemoMetadata;
import de.fhg.fokus.mdc.odrClientProxy.registry.OdrcProxy;
import de.fhg.fokus.mdc.odrClientProxy.services.ApplicationRegistrationAndDeploymentService;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.odp.registry.ckan.json.ScopeBean;
import de.fhg.fokus.odp.registry.model.Licence;
import de.fhg.fokus.odp.registry.model.Metadata;
import de.fhg.fokus.odp.registry.model.MetadataEnumType;
import de.fhg.fokus.odp.registry.model.User;
import de.fhg.fokus.odp.registry.model.exception.OpenDataRegistryException;
import de.fhg.fokus.odp.registry.queries.Query;

public class OdrcProxyTest {

	private OdrcProxy odrcp;
	private static Properties props = null;

	// TODO organize test cases, register,query,update,query etc.
	// TODO add clean up method
	@Before
	public void setup() {
		odrcp = new OdrcProxy();
		try {
			odrcp.init();
		} catch (OpenDataRegistryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	// public Metadata createMetadata() {
	//
	// Metadata metadata = odr.createMetadata(MetadataEnumType.DATASET);
	// metadata.setTitle("Test Create Metadata V");
	//
	// // metadata.setCreated(new Date());
	// metadata.setGeoCoverage("sumpfgebiete");
	// metadata.setGeoGranularity(GeoGranularityEnumType.CITY.toField());
	// metadata.setModified(new Date());
	// metadata.setNotes("Smetadatae Metadata for testing.");
	// metadata.setPublished(new Date());
	// metadata.setSector(SectorEnumType.OTHER);
	// metadata.setTemporalGranularity(TemporalGranularityEnumType.MONTH);
	// metadata.setTemporalGranularityFactor(3);
	// metadata.setUrl("http://www.fokus.fraunhofer.de/elan");
	// try {
	// metadata.setTemporalCoverageFrom(DateFormat.getInstance().parse(
	// "2012-06-01 00:00:00"));
	// metadata.setTemporalCoverageTo(DateFormat.getInstance().parse(
	// "2013-06-01 00:00:00"));
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// Contact publisher = metadata.newContact(RoleEnumType.PUBLISHER);
	// publisher
	// .setAddress("Kaiserin-Augusta-Allee 31, 10589 Berlin, Deutschland");
	// publisher.setEmail("publisher@fokus.fraunhofer.de");
	// publisher.setName("I'm the Publisher");
	//
	// metadata.getSpatialData().addCoordinate(10.0000, 8.0000);
	// metadata.getSpatialData().addCoordinate(10.3000, 8.5000);
	// metadata.getSpatialData().addCoordinate(10.0000, 8.5000);
	// metadata.getSpatialData().addCoordinate(10.3000, 8.0000);
	//
	// metadata.setExtra("extra1", "value for extra 1");
	// metadata.setExtra("extra2", "value for extra 2");
	//
	// metadata.newTag("test-create-4");
	// return metadata;
	// }

	// public void odrcProxyShouldCreateMetadata() {
	//
	// Metadata appMetadata = odrcp.createMetadata();
	// User user = odrcp.getOdr().findUser("sim");
	// try {
	// odrcp.getOdr().persistMetadata(user, appMetadata);
	// } catch (OpenDataRegistryException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// to do adjust
	// // @Test
	// public void testCreateMetadataWrapper() {
	// Map<String, Object> appMetadata = new HashMap<String, Object>();
	// appMetadata.put("name", "newApp");
	// Metadata metadata = odrcp.createMetadata(appMetadata);
	// assertEquals("newApp", metadata.getTitle());
	// }

	@Ignore("not ready")
	@Test
	public void testRegisterMetadataWrapperWithForm()
			throws OpenDataRegistryException {

		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setName("newApplicationn");
		odrcp.registerMetadata(gemoMetadata);
		// assertEquals("newApp", metadata.getTitle());
	}

	// no ckan api call
	@Ignore("not ready")
	@Test
	public void testCreateMetadata() {
		int comparisonExpected = 0;
		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor("izi");
		gemoMetadata.setLicenceId("gpl-3.0");
		gemoMetadata.setName("ODP Plus");
		Metadata metadata = odrcp.createMetadata(gemoMetadata);

		// name test only after persisting
		assertEquals("ODP Plus", metadata.getTitle());
		assertEquals("gpl-3.0", metadata.getLicence().getName());
		assertEquals(comparisonExpected,
				metadata.getType().compareTo(MetadataEnumType.APPLICATION));
		// metadataimpl does not set the author from contacts list, the
		// overwriting in the constructor is intended for a govdata specific
		// setting, check only if contacts list of metadataimpl is set correctly
		assertEquals("izi", metadata.getContacts().get(0).getName());
	}

	@Ignore("not ready")
	@Test
	public void testSerializeScope() throws JsonGenerationException,
			JsonMappingException, IOException {

		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor("izi");
		gemoMetadata.setLicenceId("gpl-3.0");
		gemoMetadata.setName("ODP Plus");
		List<ScopeBean> scopes = new ArrayList<ScopeBean>();
		scopes.add(new ScopeBean("read",
				"read erlaubt dem Client lesend auf die Services zuzugreifen"));
		scopes.add(new ScopeBean("write",
				"write erlaubt dem Client schreibend auf die Services zuzugreifen."));
		gemoMetadata.setScopes(scopes);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File("c:\\Users\\izi\\Desktop\\temp.json"),
				gemoMetadata);
	}

	@Ignore
	@Test
	public void testStoreScope() throws JsonGenerationException,
			JsonMappingException, IOException, OpenDataRegistryException {

		ApplicationRegistrationAndDeploymentService deployService = new ApplicationRegistrationAndDeploymentService();
		List<ScopeBean> scopes = new ArrayList<ScopeBean>();
		scopes.add(new ScopeBean("read", "does only read"));
		scopes.add(new ScopeBean("write", "does only write"));
		String appid = "verfuegbarkeitLadestationen";
		deployService.saveScope(appid, scopes);
	}

	@Ignore
	@Test
	public void testCheckScope() throws JsonGenerationException,
			JsonMappingException, IOException, OpenDataRegistryException {
		// ServiceScopeDataStoreClient dsclient = new
		// ServiceScopeDataStoreClient();
		// String scope = "read";
		// String serviceName = "verfuegbarkeitLadestationen";
		// boolean isScopeValid = dsclient
		// .checkScopeforService(scope, serviceName);
		// Assert.assertTrue(isScopeValid);

	}

	@Ignore
	@Test
	public void testdeSerializeScope() throws JsonGenerationException,
			JsonMappingException, IOException {

		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor("izi");
		gemoMetadata.setLicenceId("gpl-3.0");
		gemoMetadata.setName("ODP Plus");
		List<ScopeBean> scopes = new ArrayList<ScopeBean>();
		scopes.add(new ScopeBean("read", "does only read"));
		scopes.add(new ScopeBean("write", "does only write"));
		gemoMetadata.setScopes(scopes);
		ObjectMapper mapper = new ObjectMapper();
		GemoMetadata dese = mapper.readValue(new File(
				"c:\\Users\\izi\\Desktop\\user.json"), GemoMetadata.class);
		assertEquals("read", dese.getScopes().get(0).getName());
	}

	// with ckan api call

	@Test
	public void testNameTitleRegisterMetadata()
			throws OpenDataRegistryException {
		int comparisonExpected = 0;
		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor("izi");
		gemoMetadata.setLicenceId("gpl-3.0");
		gemoMetadata.setName("OD portal");
		gemoMetadata.setCategory(CKANCategories.CATEGORY_MOBILITY);
		String appId = odrcp.registerMetadata(gemoMetadata);
		// the following is only true after persisting which was done in
		// registerMetadata
		assertEquals("od-portal", appId);

	}

	// with ckan api call
	@Ignore
	@Test
	public void testScopeRegisterMetadata() throws OpenDataRegistryException {
		int comparisonExpected = 0;
		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor("izi");
		gemoMetadata.setLicenceId("gpl-3.0");
		gemoMetadata.setName("open data b");
		List<ScopeBean> scopes = new ArrayList<ScopeBean>();
		scopes.add(new ScopeBean("read", "does only read"));
		scopes.add(new ScopeBean("write", "does only write"));
		gemoMetadata.setScopes(scopes);
		String appId = odrcp.registerMetadata(gemoMetadata);
		// the following is only true after persisting which was done in
		// registerMetadata
		assertEquals("open-data-b", appId);

	}

	@Test
	@Ignore
	public void testQueryMetadataScopes() {
		Query query = new Query();
		query.setSearchterm("open-data-b");
		String result = null;
		List<GemoMetadata> gemoMetadataList = odrcp.queryMetadata(query);
		for (GemoMetadata gemoMetadata : gemoMetadataList) {
			System.out.println(gemoMetadata.getAuthor());
			result = gemoMetadata.getAuthor();
		}
		assertEquals("izi", result);
	}

	@Ignore("not ready")
	@Test
	public void testQueryMetadata() {
		Query query = new Query();
		query.setSearchterm("od-portal");
		String result = null;
		List<GemoMetadata> gemoMetadataList = odrcp.queryMetadata(query);
		for (GemoMetadata gemoMetadata : gemoMetadataList) {
			System.out.println(gemoMetadata.getAuthor());
			result = gemoMetadata.getAuthor();
		}
		assertEquals("izi", result);
	}

	@Ignore("not ready")
	@Test
	public void testQueryandUpdateMetadataSuccess()
			throws OpenDataRegistryException {
		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor("else");
		gemoMetadata.setName("OD portal");
		gemoMetadata.setLicenceId("gpl-3.0");
		List<GemoMetadata> updatedMetadata = odrcp
				.queryandUpdateMetadata(gemoMetadata);
		assert updatedMetadata.get(0).getAuthor().equals("else");
		assert updatedMetadata.size() == 1;
	}

	@Ignore("not ready")
	@Test
	public void testQueryandUpdateMetadataFail()
			throws OpenDataRegistryException {
		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor("else");
		gemoMetadata.setName("OD portall");
		gemoMetadata.setLicenceId("gpl-3.0");
		List<GemoMetadata> updatedMetadata = odrcp
				.queryandUpdateMetadata(gemoMetadata);
		assert updatedMetadata.size() == 0;
	}

	@Ignore("not ready")
	@Test
	public void testQueryandUpdateMetadataFailMultipleRes()
			throws OpenDataRegistryException {
		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor("else");
		gemoMetadata.setName("wasser");
		gemoMetadata.setLicenceId("gpl-3.0");
		List<GemoMetadata> updatedMetadata = odrcp
				.queryandUpdateMetadata(gemoMetadata);
		assert updatedMetadata.size() == 0;
	}

	// with ckan api call
	@Ignore("not ready")
	@Test
	public void testWriteIntoODRMetadatawithPersist()
			throws OpenDataRegistryException {
		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor("izi");
		gemoMetadata.setLicenceId("gpl-3.0");
		gemoMetadata.setName("OD portal plus");
		String appId = odrcp.registerMetadata(gemoMetadata);
		// the name is set during persisting in registerMetadata
		assertEquals("od-portal-plus", appId);

	}

	@Ignore("not ready")
	@Test
	public void getLicencesfromCKAN() {
		List<Licence> licences = odrcp.getOdrc().listLicenses();
		Licence defaultLicence = null;
		for (Licence licence : licences) {
			System.out.println(licence.getName());
			if (licence.getName().equals("app_opensource")) {
				defaultLicence = licence;
			}
		}
		System.out.println("Default is set to: " + defaultLicence.getName()
				+ defaultLicence.getUrl() + defaultLicence.getTitle());
	}

	@Ignore("just to try")
	@Test
	public void getMetadataReturnsAll() throws OpenDataRegistryException {
		odrcp.getOdrc().getMetadata(null, "od-portal");

	}

	@Ignore("just to try")
	@Test
	public void getApps() {
		Query query = new Query();
		List<GemoMetadata> gemoMetadataList = odrcp.queryMetadata(query);

	}

	@Ignore("just to try")
	@Test
	public void queryDatasets() throws OpenDataRegistryException {
		Query query = new Query();
		query.setSearchterm("bremen");
		odrcp.getOdrc().queryDatasets(query);
	}

	@Ignore("just to try")
	@Test
	public void verifyCKANURIparsed() throws URISyntaxException,
			MalformedURLException {
		// get the string ckan uri from properties
		String base = props.getProperty(PROPERTY_NAME_CKAN_URL);
		String anotherbase = props.getProperty(SERVICE_URI_USERPROFILE);
		URI userprofileuri = new URI(anotherbase);
		base = base + "       \n";
		URL url = new URL(base);

		URI ckanuri = url.toURI();

		// URI ckanuri = new URI(base);
		// System.out.println(ckanuri);
		// URI manualuri = new URI("http://localhost/ckan%20");
		// String host = ckanuri.getHost();

		userprofileuri.getHost();
	}

	@Ignore("not ready")
	@Test
	public void stringShouldBeParsedToDate() throws ParseException {
		Date d = DateFormat.getInstance().parse("6/20/08");
		System.out.println(d.toString());
	}

	@Ignore("not ready")
	@Test
	public void stringShouldBeParsedToDateandBack() throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss")
				.parse("2012-05-20 09:00:00");
		// String formattedDate = new
		// SimpleDateFormat("dd/MM/yyyy, Ka").format(date));
		System.out.println(date.toString());
	}

	@Ignore
	@Test
	public void createUser() {
		User user1 = odrcp.createUser("bremen", "email@email.com", "pass22");
		assertEquals("bremen", user1.getName());
	}

}
