//package de.fhg.fokus.mdc.odrClientProxy.tests;
//
//import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
//import org.junit.Test;
//
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.WebResource;
//import com.sun.jersey.api.client.config.ClientConfig;
//import com.sun.jersey.api.client.config.DefaultClientConfig;
//import com.sun.jersey.api.core.ClassNamesResourceConfig;
//import com.sun.jersey.spi.container.servlet.WebComponent;
//import com.sun.jersey.test.framework.JerseyTest;
//import com.sun.jersey.test.framework.WebAppDescriptor;
//import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
//import com.sun.jersey.test.framework.spi.container.external.ExternalTestContainerFactory;
//
//import de.fhg.fokus.mdc.odrClientProxy.model.GemoMetadata;
//import de.fhg.fokus.mdc.odrClientProxy.services.ApplicationRegistrationAndDeploymentService;
//import de.fhg.fokus.odp.registry.model.Metadata;
//import de.fhg.fokus.odp.registry.queries.Query;
//import de.fhg.fokus.odp.registry.queries.QueryResult;
//
//public class ODRClientProxyIntegrationTest extends JerseyTest {
//	private static final String TEST_SERVICEURL = "http://localhost:8080/odrClientProxy/applications";
//
//	// // client for the ApplicationRegisterService
//	@Override
//	public WebAppDescriptor configure() {
//
//		return new WebAppDescriptor.Builder()
//				.initParam(WebComponent.RESOURCE_CONFIG_CLASS,
//						ClassNamesResourceConfig.class.getName())
//				.initParam(ClassNamesResourceConfig.PROPERTY_CLASSNAMES,
//						ApplicationRegistrationAndDeploymentService.class.getName()).build();
//	}
//
//	@Override
//	protected TestContainerFactory getTestContainerFactory() {
//		return new ExternalTestContainerFactory();
//	}
//
//	@Test
//	public void testPOSTQuery() {
//		ClientConfig cc = new DefaultClientConfig();
//		cc.getClasses().add(JacksonJaxbJsonProvider.class);
//		Client client = Client.create(cc);
//		WebResource webResource = client.resource(TEST_SERVICEURL);
//		Query q = new Query();
//		q.setSearchterm("new");
//		QueryResult<Metadata> response = webResource.type("application/xml")
//				.post(QueryResult.class, q);
//		System.out.println(response.getResult().get(0).getName());
//
//	}
//
//	@Test
//	public void testPOSTAppRegForm() {
//		ClientConfig cc = new DefaultClientConfig();
//		cc.getClasses().add(JacksonJaxbJsonProvider.class);
//		Client client = Client.create(cc);
//		WebResource webResource = client.resource(TEST_SERVICEURL);
//		GemoMetadata gemoMetadata = new GemoMetadata();
//		gemoMetadata.setAuthor("izi");
//		gemoMetadata.setLicenceId("gpl-3.0");
//		gemoMetadata.setName("ODP Plus");
//		String response = webResource.type("application/xml").post(
//				String.class, gemoMetadata);
//		System.out.println(response);
//
//	}
// }
