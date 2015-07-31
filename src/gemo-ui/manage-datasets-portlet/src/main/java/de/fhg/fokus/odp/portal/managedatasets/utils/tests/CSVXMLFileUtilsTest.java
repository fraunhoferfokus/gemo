package de.fhg.fokus.odp.portal.managedatasets.utils.tests;

/*
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
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
import org.junit.Test;
*/

public class CSVXMLFileUtilsTest {

	/*
	//Test
	public void testUploadToStorage() throws ClientProtocolException, IOException {
		
		try
        {
			URL url = getClass().getResource("/test.xml");
			File csvXmlFile = new File("C:\\Users\\dsc\\Documents\\18.xml");
			//File csvXmlFile = new File(url.getPath());
			InputStream csvXmlFileStream = getClass().getResourceAsStream("test.xml");
			
			//UploadedFile csvXmlFile;
			final String storage_uri = "http://localhost:8080/storage";
			//final String tableName = NameUtils.UsersUsableUniqueNamefromFile(csvXmlFile);
			final String tableName = "blababla";//csvXmlFile.getName();
			//final String query_uri = PrefsPropsUtil.getString(PortalProperties.GEMO_STORAGE_SEARCH_QUERY_URL, "http://localhost/service/storage/search?query=");
			
			
			//following http://stackoverflow.com/questions/10808246/resteasy-client-framework-file-upload
        	//FileInputStream theFile = (FileInputStream)csvXmlFile.get
			FileInputStream theFile = (FileInputStream) csvXmlFileStream;

        	ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target(storage_uri+"/upload");

            MultipartFormDataOutput mdo = new MultipartFormDataOutput();
            mdo.addFormData("tableName", tableName, MediaType.TEXT_PLAIN_TYPE);
            //mdo.addFormData("fileName", "blababa", MediaType.TEXT_PLAIN_TYPE);
            //mdo.addFormData("file", csvXmlFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
            mdo.addFormData("file", csvXmlFile, MediaType.TEXT_XML_TYPE.withCharset("utf-8"));
            
            GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(
                    mdo) {};

            Response r = target.request().post( Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
            
            String obj = r.getEntity().toString();
            assertNotNull(obj);
            
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		//URI uri = new URI(query_uri+"select * from "+tableName+" LIMIT 10"); 
	}
	
	@Test
	public void testUploadToStorage2() throws ClientProtocolException, IOException {

		try
        {
			URL url = getClass().getResource("/test.xml");
			File csvXmlFile = new File("C:\\Users\\dsc\\Documents\\18.xml");
			byte[] bytes = Files.readAllBytes(csvXmlFile.toPath());
			String theBase64Bytes = Base64.encodeBytes(bytes);
			
			//UploadedFile csvXmlFile;
			final String storage_uri = "http://localhost:8080/storage";
			final String tableName = "blababla";//csvXmlFile.getName();

			ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target(storage_uri+"/uploadbase64");

            MultipartFormDataOutput mdo = new MultipartFormDataOutput();
            mdo.addFormData("tableName", tableName, MediaType.TEXT_PLAIN_TYPE);
            mdo.addFormData("fileName", "blababa", MediaType.TEXT_PLAIN_TYPE);
            mdo.addFormData("fileBase64", theBase64Bytes, MediaType.TEXT_PLAIN_TYPE.withCharset("utf-8"));
            
            GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(mdo) {};
            Response r = target.request().post( Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
            String obj = r.readEntity(String.class);
			
            assertNotNull(obj);
            
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	*/
}
