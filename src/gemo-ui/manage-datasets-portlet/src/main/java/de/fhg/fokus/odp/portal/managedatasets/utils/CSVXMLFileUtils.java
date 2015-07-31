package de.fhg.fokus.odp.portal.managedatasets.utils;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jboss.resteasy.util.Base64;
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
public class CSVXMLFileUtils extends FileUtils {

	/** The logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(CSVXMLFileUtils.class);

	/**
	 * Upload CSV or XML file.
	 * 
	 * @param csvXmlFile
	 *            the file
	 * @return the url
	 * @throws Exception
	 */
	public static String uploadToDocmentLibrary(UploadedFile file)
			throws Exception {
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
	 * Save CSV or XML file with the storage component
	 * 
	 * @expects "GEMO portal wide params" in portal-ext.properties:
	 *          gemo.storage.upload.url=http://?????????/service/storage/upload
	 *          gemo.storage.search.query.url=http://?????????/service/storage/
	 *          search?query=
	 * 
	 * @param csvXmlFile
	 *            the file
	 * @return the url
	 * @throws SystemException
	 *             the system exception
	 * @throws URISyntaxException
	 *             the uRI syntax exception
	 * @throws IOException
	 * @throws PortalException
	 */
	public static String uploadToStorage(UploadedFile csvXmlFile)
			throws SystemException, URISyntaxException, IOException,
			PortalException, NullPointerException {
		// grab props from constants
		final String storage_uri = PrefsPropsUtil.getString(
				PortalProperties.GEMO_STORAGE_URL, "http://localhost/storage");
		final String upload_uri = PrefsPropsUtil.getString(
				PortalProperties.GEMO_STORAGE_UPLOAD_URL,
				"http://localhost/storage/upload");
		final String upload_base64_uri = PrefsPropsUtil.getString(
				PortalProperties.GEMO_STORAGE_UPLOADBASE64_URL,
				"http://localhost:8080/storage/uploadbase64");
		final String query_uri = PrefsPropsUtil.getString(
				PortalProperties.GEMO_STORAGE_SEARCH_QUERY_URL,
				"http://localhost/storage/search?query=");

		final String tableName = NameUtils
				.UsersUsableUniqueNamefromFile(csvXmlFile);

		try {
			byte[] bytes = csvXmlFile.getBytes();
			String theBase64Bytes = Base64.encodeBytes(bytes);

			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget target = client.target(upload_base64_uri);

			MultipartFormDataOutput mdo = new MultipartFormDataOutput();
			mdo.addFormData("tableName", tableName, MediaType.TEXT_PLAIN_TYPE);
			mdo.addFormData("fileName", csvXmlFile.getName(),
					MediaType.TEXT_PLAIN_TYPE);
			mdo.addFormData("fileBase64", theBase64Bytes,
					MediaType.TEXT_PLAIN_TYPE.withCharset("utf-8"));

			GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(
					mdo) {
			};
			Response r = target.request().post(
					Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
			String obj = r.readEntity(String.class);
			LOG.debug("Storage returned: " + obj);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// httpclient.getConnectionManager().shutdown();
		}
		return query_uri + "select * from " + tableName + " LIMIT 10";
	}
}
