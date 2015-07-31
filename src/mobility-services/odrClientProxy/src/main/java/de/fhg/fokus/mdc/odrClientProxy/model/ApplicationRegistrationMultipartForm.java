package de.fhg.fokus.mdc.odrClientProxy.model;

import java.io.InputStream;

import javax.ws.rs.FormParam;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class ApplicationRegistrationMultipartForm {

	@FormParam("file")
	@PartType("application/octet-stream")
	private InputStream file_input;

	// TODO verify if it is required
	@FormParam("appName")
	@PartType("text/plain")
	private String name;

	@FormParam("gemoMetadata")
	@PartType("application/json")
	private GemoMetadata metadata;

	public GemoMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(GemoMetadata metadata) {
		this.metadata = metadata;
	}

	// make a metadata object here
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InputStream getFile_input() {
		return file_input;
	}

	public void setFile_input(InputStream file_input) {
		this.file_input = file_input;
	}

}
