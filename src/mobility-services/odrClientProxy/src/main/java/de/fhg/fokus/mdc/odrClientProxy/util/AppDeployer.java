package de.fhg.fokus.mdc.odrClientProxy.util;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_APPLICATION_REGISTRATION_BINARY_PATH;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_APPLICATION_REGISTRATION_DEPLOYMENT_PATH;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_APPLICATION_REGISTRATION_HOTDEPLOY_PATH;
import static de.fhg.fokus.mdc.propertyProvider.Constants.SERVICE_URL;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.output.ByteArrayOutputStream;

import de.fhg.fokus.mdc.odrClientProxy.model.GemoApplicationResource;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;

public class AppDeployer {
	private String deploymentLocation;
	private String binaryLocation;
	private String hotDeployFolder;
	private String gemoUri;
	private String seperator = "/";
	private static Properties props = null;

	public AppDeployer() {
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {

			e.printStackTrace();
		}
		deploymentLocation = props
				.getProperty(SERVICE_APPLICATION_REGISTRATION_DEPLOYMENT_PATH);
		binaryLocation = props
				.getProperty(SERVICE_APPLICATION_REGISTRATION_BINARY_PATH);
		hotDeployFolder = props
				.getProperty(SERVICE_APPLICATION_REGISTRATION_HOTDEPLOY_PATH);
		gemoUri = props.getProperty(SERVICE_URL);

	}

	public String getGemoUri() {
		return gemoUri;
	}

	public String getDeployLocation() {
		return deploymentLocation;
	}

	public List<GemoApplicationResource> handleFile(InputStream stream,
			String name, String noTimestamp) throws IOException {
		// TODO extract constants
		List<GemoApplicationResource> gemoAppResources = new ArrayList<GemoApplicationResource>();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String timestamp = sdf.format(date);
		String fileName = name;
		if (noTimestamp == "false") {
			fileName = fileName + timestamp;
		}
		String fileDestination = binaryLocation + seperator + fileName + ".war";
		String deployDestination = hotDeployFolder + seperator + fileName
				+ ".war";

		// copy the contents of the input stream to read later again
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		org.apache.commons.io.IOUtils.copy(stream, baos);
		byte[] bytes = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		saveFile(bais, fileDestination);
		ByteArrayInputStream baisd = new ByteArrayInputStream(bytes);
		saveFile(baisd, deployDestination);
		// no need for this info in metadata, because from the outside no one
		// can access the file.
		// GemoApplicationResource binary = new GemoApplicationResource();
		// binary.setDescription("Binary - where the java web application package is stored");
		// binary.setFormat("WAR");
		// binary.setUrl(fileDestination);

		GemoApplicationResource deployment = new GemoApplicationResource();
		deployment
				.setDescription("Deployment - the url to make API calls to the application");
		deployment.setFormat("API");
		// TODO this url might not be correct since liferay deploy with the
		// build final name in pom
		deployment.setUrl(deploymentLocation + "/" + fileName
				+ "/application.wadl");

		// gemoAppResources.add(binary);
		gemoAppResources.add(deployment);
		// to deploy war package , call cf ruby/go script for the
		// fileDestination
		return gemoAppResources;
	}

	// save uploaded file to a defined location on the server
	private void saveFile(ByteArrayInputStream uploadedInputStream,
			String serverLocation) throws IOException {

		OutputStream outputStream = new FileOutputStream(new File(
				serverLocation));
		int read = 0;
		byte[] bytes = new byte[1024];

		outputStream = new FileOutputStream(new File(serverLocation));
		while ((read = uploadedInputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
		outputStream.flush();
		outputStream.close();
	}
}