package de.fhg.fokus.mdc.odrClientProxy.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import de.fhg.fokus.mdc.odrClientProxy.util.AppDeployer;
import de.fhg.fokus.odp.registry.model.exception.OpenDataRegistryException;

@Path("/deployapps")
public class ApplicationDeploymentService {

	private AppDeployer appDeployer = new AppDeployer();

	/*
	 * Expects fileName and file in the multipart form data. Intended only for
	 * manage-dataset portlet
	 */

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response createApplication(MultipartFormDataInput input,
			@DefaultValue("true") @QueryParam("noTimestamp") String noTimestamp)
			throws IOException, OpenDataRegistryException {

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		// Get file name
		String fileName = uploadForm.get("fileName").get(0).getBodyAsString();

		// Get file data to save
		List<InputPart> inputParts = uploadForm.get("file");

		for (InputPart inputPart : inputParts) {
			try {
				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class,
						null);
				appDeployer.handleFile(inputStream, fileName, noTimestamp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// TODO return GemoDataReponse
		return Response.status(200).entity("Uploaded file name : " + fileName)
				.build();
	}

	@GET
	public Response getApplication() {
		// TODO extend GemoDataResponse
		return Response.ok()
				.entity("deploy app by posting the war package to this url")
				.build();
	}
}
