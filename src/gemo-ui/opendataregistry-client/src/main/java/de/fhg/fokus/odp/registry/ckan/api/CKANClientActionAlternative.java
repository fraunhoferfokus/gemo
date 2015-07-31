package de.fhg.fokus.odp.registry.ckan.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonNode;

public interface CKANClientActionAlternative {
	@POST
	@Path("/api/3/action/package_search")
	@Produces("application/json")
	@Consumes("application/json")
	public Response metadataSearchAlternative(JsonNode body);
}
