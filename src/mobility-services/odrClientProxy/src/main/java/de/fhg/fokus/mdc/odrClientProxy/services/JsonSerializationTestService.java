package de.fhg.fokus.mdc.odrClientProxy.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.fhg.fokus.mdc.odrClientProxy.model.GemoMetadata;

@Path("/test")
public class JsonSerializationTestService {
	/* Get an application file with id, returns a file stream */
	@GET
	@Path("{applicationName}/testJson")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSerialzedToJsonString(
			@PathParam("applicationName") String identifier) {
		// depends on resources implementation, first have to do that
		// TODO get application file with id, returns a file stream
		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor("izi");
		gemoMetadata.setLicenceId("gpl-3.0");
		gemoMetadata.setName(identifier);
		return Response.ok().entity(gemoMetadata).build();

	}
}
