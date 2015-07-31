package de.fhg.fokus.mdc.odrClientProxy.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.jsonObjects.GemoDataResponse;
import de.fhg.fokus.mdc.jsonObjects.OdrcGemoDataResponse;
import de.fhg.fokus.mdc.odrClientProxy.model.ApplicationRegistrationMultipartForm;
import de.fhg.fokus.mdc.odrClientProxy.model.GemoApplicationResource;
import de.fhg.fokus.mdc.odrClientProxy.model.GemoMetadata;
import de.fhg.fokus.mdc.odrClientProxy.registry.OdrcProxy;
import de.fhg.fokus.mdc.odrClientProxy.util.AppDeployer;
import de.fhg.fokus.mdc.utils.security.clients.GemoScopeBean;
import de.fhg.fokus.mdc.utils.security.clients.ServiceScopeDataStoreClient;
import de.fhg.fokus.odp.registry.ckan.json.ScopeBean;
import de.fhg.fokus.odp.registry.model.User;
import de.fhg.fokus.odp.registry.model.exception.OpenDataRegistryException;
import de.fhg.fokus.odp.registry.queries.Query;
//to generate wadl run
//mvn compile com.sun.jersey.contribs:maven-wadl-plugin:generate
//TODO return wadl as usual at /application.wadl
//TODO move SecurityInterceptor to jsonObjects and register in web.xml

@Path("/applications")
public class ApplicationRegistrationAndDeploymentService {

	private AppDeployer appDeployer = new AppDeployer();
	private OdrcProxy odrcp = null;
	private final Logger log = LoggerFactory.getLogger(getClass());

	public ApplicationRegistrationAndDeploymentService()
			throws OpenDataRegistryException {

		odrcp = new OdrcProxy();
		odrcp.init();
	}

	public ApplicationRegistrationAndDeploymentService(OdrcProxy odrcpx)
			throws OpenDataRegistryException {

		odrcp = odrcpx;
		odrcp.init();
	}

	/* Get a list of metadata of all applications */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllApplications() {
		Query query = new Query();
		query.setMax(100);
		GemoDataResponse r = new GemoDataResponse();
		List<GemoMetadata> allApplications = odrcp.queryMetadata(query);
		if (allApplications.size() < 1) {
			// TODO return help message
			return Response.noContent().build();
		}
		List<String> allApplicationNames = new ArrayList<String>();
		for (GemoMetadata gemoMetadata : allApplications) {
			allApplicationNames.add(gemoMetadata.getName());
		}
		return Response.ok(allApplicationNames).build();
	}

	/* Get an application file with id, returns a file stream */
	@GET
	@Path("{applicationName}/resources")
	public Response getApplicationResources(
			@PathParam("applicationName") String identifier) {
		// depends on resources implementation, first have to do that
		// TODO get application file with id, returns a file stream
		return Response.ok("coming soon").build();
	}

	/*
	 * Get the metadata of an application
	 */
	@GET
	@Path("{applicationName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMetadata(@PathParam("applicationName") String identifier) {
		GemoMetadata gemoMetadata;
		try {
			gemoMetadata = odrcp.getMetadata(identifier);
		} catch (OpenDataRegistryException e) {
			return Response.status(404).build();
		}
		return Response.ok(gemoMetadata).build();
	}

	/* Post a Query to /applications which might return 0..* results */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMetadataforQuery(Query query) {
		// impl.write returns JsonNode (MetadataBean), but it is the CKAN based
		// implementation of Metadata which is too complex for GeMo external
		// communication
		List<GemoMetadata> gemoMetadataList = odrcp.queryMetadata(query);
		if (gemoMetadataList.size() < 1) {
			// TODO return help message
			return Response.noContent().build();
		}
		return Response.ok(gemoMetadataList).build();
	}

	@POST
	@Path("{applicationName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMetadata(GemoMetadata gemoMetadata,
			@Context HttpHeaders headers) {
		OdrcGemoDataResponse res = new OdrcGemoDataResponse();
		String updatedMetadata;
		try {
			User ckanUser = getCKANUserforGemoUser(headers);
			updatedMetadata = odrcp
					.getandUpdateMetadata(ckanUser, gemoMetadata);
			res.setMessage("Updated the application with name: "
					+ updatedMetadata);
			return Response.ok().entity(res).build();
		} catch (OpenDataRegistryException e) {
			return Response.status(404).build();
		}

	}

	// for testing:
	// curl -i -F file=@metadata.json -F appName=newApplication -F
	// "gemoMetadata=@gemometadata.json;type=application/json"
	// http://localhost:8080/odrClientProxy/applications
	// expecting resteasyheaders because liferay classloader on the server
	// cannot find the
	// implementation class for jaxrs httpheaders
	@POST
	@Consumes("multipart/form-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFileAndMetadata(
			@MultipartForm ApplicationRegistrationMultipartForm form,
			@DefaultValue("true") @QueryParam("noTimestamp") String noTimestamp,
			@Context HttpHeaders headers) throws IOException,
			OpenDataRegistryException {
		log.debug("headers :" + headers.getHeaderString("username"));
		if (form.getFile_input() != null && form.getName() != null
				&& !form.getName().matches("^\\s*$")
				&& form.getMetadata() != null) {
			InputStream stream = form.getFile_input();
			String name = form.getName();
			List<GemoApplicationResource> gemoAppResources = appDeployer
					.handleFile(stream, name, noTimestamp);
			GemoMetadata gm = form.getMetadata();
			for (GemoApplicationResource gar : gemoAppResources) {
				gm.getResources().add(gar);

			}
			User ckanUser = getCKANUserforGemoUser(headers);
			String appIdentifier = handleMetadata(gm, ckanUser);

			saveScope(appIdentifier, gm.getScopes());
			OdrcGemoDataResponse res = new OdrcGemoDataResponse();
			// TODO add odrclientproxy path to mdc.properties
			res.setMessage("See your application metadata at: "
					+ appDeployer.getGemoUri() + "odrClientProxy/applications/"
					+ appIdentifier);
			return Response.ok().entity(res).build();

		}
		OdrcGemoDataResponse errorRes = new OdrcGemoDataResponse();

		errorRes.setMessage("Missing input");
		return Response.status(400).entity(errorRes).build();
	}

	public void saveScope(String appIdentifier, List<ScopeBean> scopes)
			throws JsonParseException, JsonMappingException, IOException {
		ServiceScopeDataStoreClient authClient = new ServiceScopeDataStoreClient();
		List<GemoScopeBean> gemoscopes = new ArrayList<GemoScopeBean>();
		// TODO this is only temporarily here, make it consistent gemometadata
		// has gemoscopebean in scopes, for that you have to edit
		// writeintoodrmetadata too

		if (!scopes.isEmpty()) {
			for (ScopeBean s : scopes) {
				GemoScopeBean gs = new GemoScopeBean();
				gs.setName(s.getName());
				gs.setDescription(s.getDescription());
				gemoscopes.add(gs);
			}
			authClient.writeScopesForService(appIdentifier, gemoscopes);
		}
	}

	private User getCKANUserforGemoUser(HttpHeaders headers) {

		String name = headers.getHeaderString("username");
		String accessToken = headers.getHeaderString("accessToken");
		// set username as ckan username and accessToken as ckan user password
		// TODO add email?
		User user = odrcp.getUserByName(name, accessToken);
		return user;
	}

	private String handleMetadata(GemoMetadata gemometadata, User user)
			throws OpenDataRegistryException {
		// TODO exception handling :D

		odrcp.registerMetadata(user, gemometadata);
		return odrcp.titleToName(gemometadata.getName());
	}

	// for parsing the form manually, add MultipartFormDataInput parsing method
	// to OdrcFormDataParser
	// @POST
	// @Path("/upload")
	// @Consumes("multipart/form-data")
	// public Response uploadFile(MultipartFormDataInput form)
	// throws OpenDataRegistryException, IOException {
	//
	// OdrcFormDataParser formDataParser = new OdrcFormDataParser();
	// Map<String, Object> appMetadata = formDataParser.formDataToMap(form);
	// return Response.noContent().build();
	//
	// }

}
