package de.fraunhofer.fokus.mdc.util.deploy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.odrClientProxy.model.CKANCategories;
import de.fhg.fokus.mdc.odrClientProxy.model.GemoApplicationResource;
import de.fhg.fokus.mdc.odrClientProxy.model.GemoMetadata;
import de.fhg.fokus.odp.registry.ckan.json.ScopeBean;

/**
 * @author Ilke Zilci, ilke.zilci@fokus.fraunhofer.de Generates mobility
 *         services metadata to be stored in ckan db via odrClientProxy
 * **/

public class GemoServicesMetadataGenerator {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Properties prop = new Properties();

	public GemoServicesMetadataGenerator() throws IOException {
		prop.load(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("deployment.properties"));
	}

	private List<GemoMetadata> getGemoServicesList() {
		Set<String> propertyNames = prop.stringPropertyNames();
		List<GemoMetadata> gemoServicesMetadata = new ArrayList<GemoMetadata>();
		for (String propertyName : propertyNames) {
			GemoMetadata gemoMetadata = new GemoMetadata();
			String gemoservice = prop.getProperty(propertyName);
			gemoMetadata.setName(gemoservice);
			gemoMetadata.setAuthor("GeMo");
			gemoMetadata.setDescription("Mobility Service");
			gemoMetadata.setLicenceId("gpl-3.0");

			List<ScopeBean> scopes = new ArrayList<ScopeBean>();
			scopes.add(new ScopeBean("read",
					"read erlaubt dem Client lesend auf die Services zuzugreifen"));
			scopes.add(new ScopeBean("write",
					"write erlaubt dem Client schreibend auf die Services zuzugreifen."));
			gemoMetadata.setScopes(scopes);

			List<GemoApplicationResource> gemoResources = new ArrayList<GemoApplicationResource>();
			GemoApplicationResource gemoResource = new GemoApplicationResource();
			gemoResource
					.setUrl("https://gitlab.fokus.fraunhofer.de/benjamin.dittwald/gemo/tree/master/src/mobility-services/"
							+ gemoservice);
			gemoResource.setFormat("Code");
			gemoResource.setDescription("Repository");
			gemoResources.add(gemoResource);
			gemoMetadata.setResources(gemoResources);
			gemoMetadata.setCategory(CKANCategories.CATEGORY_MOBILITY);
			if (gemoservice.contains("Efz"))
				gemoMetadata.setCategory(CKANCategories.CATEGORY_E_CARS);
			if (gemoservice.contains("Ladestation"))
				gemoMetadata
						.setCategory(CKANCategories.CATEGORY_CHARGING_STATION);

			gemoServicesMetadata.add(gemoMetadata);

		}
		return gemoServicesMetadata;
	}

	public void serializeGemoMetadata() throws JsonGenerationException,
			JsonMappingException, IOException {
		List<GemoMetadata> gemoServicesMetadata = getGemoServicesList();
		// TODO write the list to a file
		ObjectMapper mapper = new ObjectMapper();
		for (GemoMetadata gemometadata : gemoServicesMetadata) {
			mapper.writeValue(new File("../../etc/gemoservices_init/"
					+ gemometadata.getName() + ".json"), gemometadata);
		}
	}

	public void generateBashScript() {
		List<GemoMetadata> gemoServicesMetadata = getGemoServicesList();
		String script = "";
		for (GemoMetadata gemometadata : gemoServicesMetadata) {
			script += "curl -H \"accessToken:token\" -H \"scope:scope\" -H \"username:gemodeveloper\" -i -F file=@"
					+ gemometadata.getName()
					+ ".war -F fileMetadata="
					+ gemometadata.getName()
					+ " -F \"gemoMetadata=@"
					+ gemometadata.getName()
					+ ".json;type=application/json\" http://193.175.133.248/service/odrClientProxy/applications\n";

		}

		FileWriter output = null;
		BufferedWriter writer = null;
		try {
			output = new FileWriter("../../etc/gemoservices_init/"
					+ "gemoservices_init.sh");
			writer = new BufferedWriter(output);
			writer.write(script);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (output != null) {
				try {
					output.close();

				} catch (IOException e) {
					// Ignore issues during closing
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		GemoServicesMetadataGenerator gsmg = new GemoServicesMetadataGenerator();

		// List<GemoMetadata> gemoServicesMetadata = gsmg.getGemoServicesList();
		gsmg.serializeGemoMetadata();
		// gsmg.generateBashScript();
	}
}
