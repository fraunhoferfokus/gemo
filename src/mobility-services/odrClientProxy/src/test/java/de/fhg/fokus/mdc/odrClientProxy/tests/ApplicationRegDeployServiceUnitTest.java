package de.fhg.fokus.mdc.odrClientProxy.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Matchers;

import de.fhg.fokus.mdc.odrClientProxy.model.GemoMetadata;
import de.fhg.fokus.mdc.odrClientProxy.registry.OdrcProxy;
import de.fhg.fokus.mdc.odrClientProxy.services.ApplicationRegistrationAndDeploymentService;
import de.fhg.fokus.odp.registry.model.exception.OpenDataRegistryException;
import de.fhg.fokus.odp.registry.queries.Query;

public class ApplicationRegDeployServiceUnitTest {
	@Test
	public void testWithMockDataStoreClient() throws OpenDataRegistryException {
		List<GemoMetadata> applications = populateResponse();
		// arrange what ApplicationRegistrationService receives from
		// OdrProxy
		OdrcProxy mockOdrcProxy = mock(OdrcProxy.class);

		// when(obj.method()).thenReturn(mockanswer);
		when(mockOdrcProxy.queryMetadata(Matchers.isA(Query.class)))
				.thenReturn(applications);

		List<String> jsonResponse = null;
		ApplicationRegistrationAndDeploymentService appregs = new ApplicationRegistrationAndDeploymentService(
				mockOdrcProxy);
		jsonResponse = (List<String>) appregs.getAllApplications().getEntity();

		assertEquals("[ODP Plus, ODP Pluss, ODP Plusss]",
				jsonResponse.toString());

	}

	public List<GemoMetadata> populateResponse() {
		List<GemoMetadata> allApps = new ArrayList<GemoMetadata>();
		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor("izi");
		gemoMetadata.setLicenceId("gpl-3.0");
		gemoMetadata.setName("ODP Plus");
		allApps.add(gemoMetadata);
		GemoMetadata gemoMetadata2 = new GemoMetadata();
		gemoMetadata2.setAuthor("iziz");
		gemoMetadata2.setLicenceId("gpl-3.0");
		gemoMetadata2.setName("ODP Pluss");
		allApps.add(gemoMetadata2);
		GemoMetadata gemoMetadata3 = new GemoMetadata();
		gemoMetadata3.setAuthor("izizi");
		gemoMetadata3.setLicenceId("gpl-3.0");
		gemoMetadata3.setName("ODP Plusss");
		allApps.add(gemoMetadata3);
		return allApps;
	}
}
