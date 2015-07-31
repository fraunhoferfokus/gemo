package de.fhg.fokus.mdc.odrClientProxy.registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.odrClientProxy.model.GemoMetadata;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.odp.registry.ODRClient;
import de.fhg.fokus.odp.registry.model.Application;
import de.fhg.fokus.odp.registry.model.Metadata;
import de.fhg.fokus.odp.registry.model.User;
import de.fhg.fokus.odp.registry.model.exception.OpenDataRegistryException;
import de.fhg.fokus.odp.registry.queries.Query;
import de.fhg.fokus.odp.registry.queries.QueryResult;
import de.fhg.fokus.odp.spi.OpenDataRegistry;

/* OdrcProxy uses the interfaces from ODRClient ...registry.model instead of MetadataBean and
 MetadataImpl, so that SP (CKAN ODRClientImpl) stays changeable with help of SPI*/

public class OdrcProxy {

	// public OpenDataRegistryProvider odrProvider = new
	// OpenDataRegistryProvider();
	// public ODRClient odrc = odrProvider.createClient();do not use this
	private final Logger log = LoggerFactory.getLogger(getClass());

	private ODRClient odrc;

	private MetadataWrapper metadataWrapper;
	private static Properties props = null;

	public void init() throws OpenDataRegistryException {

		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {

			e.printStackTrace();
		}
		odrc = OpenDataRegistry.getClient();
		odrc.init(props);
		metadataWrapper = new MetadataWrapper();
		// you can use init(myPropertiesObject) as well, but
		// by default odr.init() loads ckan.properties
		// to change the filename, have to change ...registry.ckan.Constants

	}

	public ODRClient getOdrc() {
		return odrc;
	}

	public void setOdrc(ODRClient odrc) {
		this.odrc = odrc;
	}

	// adjust the Map<String, Object> appMetadata method
	// public Metadata createMetadata(Map<String, Object> appMetadata) {
	// MetadataWrapper metadataWrap = new MetadataWrapper();
	// Metadata metadata = metadataWrap.init(odrc, appMetadata);
	// return metadata;
	// }

	public User getUserByName(String name, String password) {
		// User user1 = odrc.createUser("developer", "email@email.com",
		// "pass22");
		User user = null;
		user = this.createUser(name, "email@email.com", password);
		if (user == null) {
			// user exists already, thats why odrc returns null, try to get it
			// with
			// find
			user = odrc.findUser(name);
			// TODO add check it might also be sth else that went wrong like
			// missing parameters
		}
		// if not null it created and returns the newly created user
		return user;
	}

	public User createUser(String name, String email, String password) {
		User user = odrc.createUser(name, email, password);
		return user;

	}

	// TODO change to private , now public because needed to test without ckan
	// api call
	public Metadata createMetadata(GemoMetadata gemoMetadata) {
		Metadata metadata = metadataWrapper.translateGemoMetadataToODR(odrc,
				gemoMetadata);
		return metadata;
	}

	public GemoMetadata getMetadata(String name)
			throws OpenDataRegistryException {
		Metadata metadata = odrc.getMetadata(null, name);
		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata = metadataWrapper.readIntoGemo(metadata);
		return gemoMetadata;
	}

	public String registerMetadata(GemoMetadata gemoMetadata)
			throws OpenDataRegistryException {
		String appId;
		Metadata metadata = createMetadata(gemoMetadata);
		log.debug("register metadata created metadata, initialized the objects and given fields for :"
				+ metadata.getTitle());
		persistMetadata(metadata);
		log.debug("register metadata persisted metadata, set the name as identifier: "
				+ metadata.getName());
		appId = metadata.getName();
		return appId;
	}

	public String registerMetadata(User user, GemoMetadata gemoMetadata)
			throws OpenDataRegistryException {
		String appId;
		Metadata metadata = createMetadata(gemoMetadata);
		log.debug("register metadata created metadata, initialized the objects and given fields for :"
				+ metadata.getTitle());
		persistMetadata(user, metadata);
		log.debug("register metadata persisted metadata, set the name as identifier: "
				+ metadata.getName());
		appId = metadata.getName();
		return appId;
	}

	// public void registerMetadata(Map<String, Object> appMetadata)
	// throws OpenDataRegistryException {
	// Metadata metadata = createMetadata(appMetadata);
	// persistMetadata(metadata);
	// }

	// use this method if no activity stream of CKAN will be used, odrc sets the
	// administrator key from properties file
	private void persistMetadata(Metadata metadata)
			throws OpenDataRegistryException {
		odrc.persistMetadata(null, metadata);
	}

	private void persistMetadata(User user, Metadata metadata)
			throws OpenDataRegistryException {
		// TODO add registry.model.User to parameters
		// User u = new User();
		odrc.persistMetadata(user, metadata);
	}

	public String titleToName(String title) {
		return odrc.mungeTitleToName(title);
	}

	public List<GemoMetadata> queryMetadata(Query query) {
		QueryResult<Application> result = odrc.queryApplications(query);
		List<GemoMetadata> gemoMetadataList = new ArrayList<GemoMetadata>();
		if (result.isSuccess()) {
			List<Application> results = result.getResult();
			log.debug("Number of results: " + result.getCount());
			for (Application metadata : results) {
				GemoMetadata gemoMetadata = new GemoMetadata();
				gemoMetadata = metadataWrapper.readIntoGemo(metadata);
				log.debug("odrcproxy read query result into gemometadata, author: "
						+ gemoMetadata.getAuthor());
				gemoMetadataList.add(gemoMetadata);
			}
		}
		return gemoMetadataList;
	}

	// persistMetadata checks MetadataImpl.getMetadataBean.id to decide
	// create or update
	// this code does not set the id of MetadataBean, so it is always
	// considered new and sends a request to package_create
	// here decided to query first which sets the metadatabean id to keep
	// depending on only
	// the Metadata interface
	public String getandUpdateMetadata(GemoMetadata gemoMetadata)
			throws OpenDataRegistryException {
		String applicationToUpdate = gemoMetadata.getName();
		Metadata metadataToUpdate = odrc.getMetadata(null, applicationToUpdate);
		// set the odrMetadata of MetadataWrapper
		metadataWrapper.setOdrMetadata(metadataToUpdate);
		// write gemometadata into odrMetadata
		metadataWrapper.translateGemoMetadataToODR(odrc, gemoMetadata);
		// now that the metadataToUpdate is in the current stand, persist it.
		persistMetadata(metadataToUpdate);
		// TODO exception handling
		return metadataToUpdate.getName();
	}

	public String getandUpdateMetadata(User user, GemoMetadata gemoMetadata)
			throws OpenDataRegistryException {
		String applicationToUpdate = gemoMetadata.getName();
		Metadata metadataToUpdate = odrc.getMetadata(null, applicationToUpdate);
		// set the odrMetadata of MetadataWrapper
		metadataWrapper.setOdrMetadata(metadataToUpdate);
		// write gemometadata into odrMetadata
		metadataWrapper.translateGemoMetadataToODR(odrc, gemoMetadata);
		// now that the metadataToUpdate is in the current stand, persist it.
		persistMetadata(user, metadataToUpdate);
		// TODO exception handling
		return metadataToUpdate.getName();
	}

	// TODO change so that bulk update is possible?
	// TODO make another version with user
	public List<GemoMetadata> queryandUpdateMetadata(GemoMetadata gemoMetadata)
			throws OpenDataRegistryException {

		Query q = new Query();
		q.setSearchterm(gemoMetadata.getName());
		QueryResult<Metadata> result = odrc.queryMetadata(q);
		List<GemoMetadata> gemoMetadataList = new ArrayList<GemoMetadata>();
		if (result.isSuccess() && result.getCount() == 1) {
			log.debug("number of results: " + result.getCount());
			List<Metadata> results = result.getResult();
			// TODO if not tell the query is ambigous
			Metadata metadata = results.get(0);
			metadataWrapper.setOdrMetadata(metadata);
			metadataWrapper.translateGemoMetadataToODR(odrc, gemoMetadata);
			persistMetadata(metadata);
			gemoMetadataList.add(gemoMetadata);
		}
		// return the list of updated metadata
		return gemoMetadataList;
	}
}
