package de.fhg.fokus.odp.portal.managedatasets.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.faces.bridge.model.UploadedFile;
import com.liferay.faces.portal.context.LiferayFacesContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;

/**
 * used to save and organize war files after upload
 * 
 * @author dsc
 * 
 */
public class WarFileUtils extends FileUtils {

	/** The logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(WarFileUtils.class);

	/**
	 * Upload War file to document library.
	 * 
	 * @param file
	 *            the file
	 * @return the uri
	 * @throws IOException
	 * @throws PortalException
	 * @throws SystemException
	 * @throws Exception
	 */
	public static String uploadToDocmentLibrary(UploadedFile file)
			throws SystemException, PortalException, IOException {
		LiferayFacesContext lfc = LiferayFacesContext.getInstance();
		ThemeDisplay themeDisplay = lfc.getThemeDisplay();

		// upload file to local liferay storage
		DLFileEntry fileEntry = FileUtils.uploadFileToFolder(file,
				PropsUtil.get("image.folder.id"), themeDisplay);

		return themeDisplay.getPortalURL()
				+ "/c/document_library/get_file?uuid=" + fileEntry.getUuid()
				+ "&groupId=" + themeDisplay.getScopeGroupId();
	}

	/**
	 * uploads the file to ilkes auo deploy service
	 * 
	 * @param warFile
	 * @throws SystemException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws PortalException
	 * @throws NullPointerException
	 */
	public static void uploadToAutoDeployService(UploadedFile warFile)
			throws SystemException, URISyntaxException, IOException,
			PortalException, NullPointerException {

		final String pathToODRClientProxy = PrefsPropsUtil.getString(
				PortalProperties.GEMO_ODR_CLIENT_PROXY_URL,
				"http://localhost:8080/odrClientProxy/deployapps");

		// get a valid and system wide file name for the user
		final String fileName = NameUtils
				.UsersUsableUniqueNamefromFile(warFile);

		ResteasyClient client = new ResteasyClientBuilder().build();
		client.register(new AuthHeadersRequestFilter("39393939kkk000", "write",
				"developer"));
		ResteasyWebTarget target = client.target(pathToODRClientProxy);

		MultipartFormDataOutput mdo = new MultipartFormDataOutput();
		mdo.addFormData("file",
				new FileInputStream(new File(warFile.getAbsolutePath())),
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		mdo.addFormData("fileName", fileName, MediaType.TEXT_PLAIN_TYPE);
		// define response format over a generic entity
		GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(
				mdo) {
		};
		Response r = target.request().post(
				Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
		LOG.debug("WAR-auto-deployment response: " + r.getStatus()
				+ r.toString());
	}
}
