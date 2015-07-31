package de.fhg.fokus.mdc.testing.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonNode;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

/**
 * Live ReST service WADL
 * https://193.175.133.248/service/storage/application.wadl
 * 
 */
public interface Storage {

	@Path("/search")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	JsonNode getSearchResult(@QueryParam("query") String query);

	@Path("/upload")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	// cause service uses FormDataContentDisposition fileDetails we need to use
	// MultipartFormDataOutput instead of actual Interface :/
	Response postUpload(MultipartFormDataOutput multipartFormDataOutput);

	// TODO proxy other ReST actions from storage service WADL
	// https://193.175.133.248/service/storage/application.wadl
	// under paths "/update" and "/insert".

	@Path("/update")
	// de.fhg.fokus.mdc.storage.services.UpdateServices.updateDataPostParams
	// why do we update via POST, shouldn't it be PUT? Cf.
	// http://en.wikipedia.org/wiki/Representational_state_transfer#RESTful_web_APIs
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	String getUpdateResults(MultivaluedMap<String, String> params,
			@FormParam("tableName") String tableName,
			@FormParam("where") String where);

	@Path("/update")
	// de.fhg.fokus.mdc.storage.services.UpdateServices.updateDataFilestream
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	// cause service uses FormDataContentDisposition fileDetails we need to use
	// MultipartFormDataOutput instead of actual Interface :/
	String updateDataFilestream(MultipartFormDataOutput multipartFormDataOutput);

}
