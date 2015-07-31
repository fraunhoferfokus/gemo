package de.fhg.fokus.mdc.odrClientProxy.registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.odrClientProxy.model.GemoApplicationResource;
import de.fhg.fokus.mdc.odrClientProxy.model.GemoMetadata;
import de.fhg.fokus.odp.registry.ODRClient;
import de.fhg.fokus.odp.registry.ckan.impl.LicenceImpl;
import de.fhg.fokus.odp.registry.ckan.impl.ScopeImpl;
import de.fhg.fokus.odp.registry.ckan.json.LicenceBean;
import de.fhg.fokus.odp.registry.ckan.json.ScopeBean;
import de.fhg.fokus.odp.registry.model.Category;
import de.fhg.fokus.odp.registry.model.Contact;
import de.fhg.fokus.odp.registry.model.Licence;
import de.fhg.fokus.odp.registry.model.Metadata;
import de.fhg.fokus.odp.registry.model.MetadataEnumType;
import de.fhg.fokus.odp.registry.model.Resource;
import de.fhg.fokus.odp.registry.model.RoleEnumType;
import de.fhg.fokus.odp.registry.model.Scope;

/*The MetadataWrapper adds a level of abstraction to the Metadata interface of odrc since in gemo not all the methods are needed to be exposed*/
public class MetadataWrapper {

	private Metadata odrMetadata;

	public Metadata getOdrMetadata() {
		return odrMetadata;
	}

	public void setOdrMetadata(Metadata odrMetadata) {
		this.odrMetadata = odrMetadata;
	}

	/** The logger. */
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	/** The licences. */
	private List<Licence> relevantLicences;

	/** The categories. */
	private List<Category> categories;

	/** The sectors. */
	// private List<SectorEnumType> sectors;

	/** The geo granularities. */
	// private List<GeoGranularityEnumType> geoGranularities;

	/** The temporal granularity enum types. */
	// private List<TemporalGranularityEnumType> temporalGranularityEnumTypes;

	/** The selected categories. */
	// private List<String> selectedCategories;

	/** The selected tags. */
	// private List<String> selectedTags;

	/** The author. */
	private Contact author;

	/** The maintainer. */
	// private Contact maintainer;

	/** The distributor. */
	// private Contact distributor;

	/** The date pattern. */
	public final static String DATE_PATTERN = "dd.MM.yyyy";

	// adjust the Map<String, Object> appMetadata method
	// public Metadata init(ODRClient odrc, Map<String, Object> appMetadata) {
	// odrMetadata = odrc.createMetadata(MetadataEnumType.APPLICATION);
	// odrMetadata.setTitle(appMetadata.get("name").toString());
	// return odrMetadata;
	// }

	public String getTitle() {
		return this.odrMetadata.getTitle();
	}

	public void setTitle(String title) {
		this.odrMetadata.setTitle(title);
	}

	public String getName() {
		return this.odrMetadata.getTitle();
	}

	public String getAuthor() {
		return this.odrMetadata.getAuthor();
	}

	public String getLicenceId() {
		return this.odrMetadata.getLicence().getName();
	}

	/* if odrMetadata is null, registering new */
	// TODO do not catch the exception here
	protected Metadata translateGemoMetadataToODR(ODRClient odrc,
			GemoMetadata gemoMetadata) {
		if (odrMetadata == null) {
			odrMetadata = odrc.createMetadata(MetadataEnumType.DOCUMENT);
			LOG.debug("created new Metadata object");
		}
		getLicencesforType(odrc);

		try {
			writeIntoODRMetadata(odrc, gemoMetadata);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return odrMetadata;
	}

	/* if odrMetadata is not null, if it is set with odrc.getM and odrc.queryM */
	private void writeIntoODRMetadata(ODRClient odrc, GemoMetadata gemoMetadata)
			throws JsonGenerationException, JsonMappingException, IOException {
		// TODO other metadata fields to be mapped
		// when creating metadata persistMetadata sets the odrMetadata.name
		// based on title,
		// when updating metadata, odrMetadata has already a unique name as
		// identifier, so cannot be changed
		odrMetadata.setTitle(gemoMetadata.getName());
		// set values for the author which is referenced by
		// metadataimpl.contacts list
		author = odrMetadata.newContact(RoleEnumType.AUTHOR);
		author.setName(gemoMetadata.getAuthor());
		setLicence(gemoMetadata.getLicenceId());
		// why list licences if set already?
		odrc.listLicenses();
		odrMetadata.setNotes(gemoMetadata.getDescription());
		categories = odrc.listCategories();
		// TODO change this with search for the name that gemometadata specified
		Category e = null;

		for (Category c : categories) {
			if (c.getName().equals(gemoMetadata.getCategory()))
				e = c;
		}

		odrMetadata.getCategories().add(e);

		// odrMetadata.set
		List<GemoApplicationResource> gemoResources = gemoMetadata
				.getResources();
		if (gemoResources.size() > 0) {
			for (GemoApplicationResource gemoR : gemoResources) {
				Resource r = odrc.createResource();
				r.setDescription(gemoR.getDescription());
				r.setFormat(gemoR.getFormat());
				r.setUrl(gemoR.getUrl());
				odrMetadata.getResources().add(r);
			}
		}

		List<ScopeBean> gemoScopes = new ArrayList<ScopeBean>();
		gemoScopes = gemoMetadata.getScopes();
		if (gemoScopes.size() > 0) {
			for (ScopeBean scopeBean : gemoScopes) {
				Scope odrScope = new ScopeImpl(scopeBean);
				odrMetadata.getScopes().add(odrScope);
			}
		}
	}

	protected GemoMetadata readIntoGemo(Metadata metadata) {
		GemoMetadata gemoMetadata = new GemoMetadata();
		gemoMetadata.setAuthor(metadata.getContacts().get(0).getName());
		gemoMetadata.setName(metadata.getName());
		gemoMetadata.setLicenceId(metadata.getLicence().getName());
		gemoMetadata.setDescription(metadata.getNotes());
		List<GemoApplicationResource> gemoResources = new ArrayList<GemoApplicationResource>();
		List<Resource> odrResources = metadata.getResources();
		// go through the resources list of metadata, add to gemoresources list
		for (Resource odrResource : odrResources) {
			GemoApplicationResource gemoResource = new GemoApplicationResource();
			gemoResource.setUrl(odrResource.getUrl());
			gemoResource.setFormat(odrResource.getFormat());
			gemoResource.setDescription(odrResource.getDescription());
			gemoResources.add(gemoResource);
		}

		gemoMetadata.setResources(gemoResources);
		List<ScopeBean> gemoScopes = new ArrayList<ScopeBean>();
		List<Scope> odrScopes = metadata.getScopes();
		for (Scope s : odrScopes) {
			ScopeBean gemoScope = new ScopeBean();
			gemoScope.setName(s.getName());
			gemoScope.setDescription(s.getDescription());
			gemoScopes.add(gemoScope);
		}
		gemoMetadata.setScopes(gemoScopes);
		// TODO other metadata fields to be mapped
		return gemoMetadata;
	}

	private void getLicencesforType(ODRClient odrClient) {
		relevantLicences = new ArrayList<Licence>();
		List<Licence> availableLicences = odrClient.listLicenses();
		/*
		 * Fill licences according to the metadata type: dataset, app, document
		 */
		if (availableLicences.size() > 0) {
			LOG.debug("Number of available licences: "
					+ availableLicences.size());
			try {
				if (odrMetadata.getType().equals(MetadataEnumType.DATASET)
						|| odrMetadata.getType().equals(
								MetadataEnumType.UNKNOWN)) {
					for (Licence licence : availableLicences) {
						if (licence.isDomainData()) {
							relevantLicences.add(licence);
						}
					}
				} else if (odrMetadata.getType().equals(
						MetadataEnumType.APPLICATION)) {
					for (Licence licence : availableLicences) {
						if (licence.isDomainSoftware()) {
							relevantLicences.add(licence);
						}
					}
				} else if (odrMetadata.getType().equals(
						MetadataEnumType.DOCUMENT)) {
					for (Licence licence : availableLicences) {
						if (licence.isDomainContent()) {
							relevantLicences.add(licence);
						}
					}
				}
			} catch (Exception e) {
				LOG.debug("probably json deserialization of of existing licences failed see ODRProvider error logs");
			}
		}
	}

	private void setLicence(String licenceId) {
		// it breaks if relevantLicences list is empty this might be because
		// there are no app licences in CKAN DB or because getLicencesforType
		// was not called prior to this method, reconsider the init function and
		// constructor of MetadataWrapper and decide what to do if no licences
		// in CKAN db
		boolean listed = false;
		if (relevantLicences.size() > 0) {
			for (Licence licence : relevantLicences) {
				if (licence.getName().equals(licenceId)) {
					odrMetadata.setLicence(licence);
					listed = true;
				}
				// licence was found in the govdata list of licences and set
				if (listed == true) {
					LOG.debug("set an existing license");
					break;
				}
			}
		}
		// licenceid was not in the govdata list of licences
		if (listed == false) {
			LOG.info("existing licences does not match, setting terms of use field");
			// TODO add factory method createLicence to odrCLient and
			// odrClientImpl
			LicenceBean lb = new LicenceBean();
			lb.setId(licenceId);
			Licence emptyLicence = new LicenceImpl(lb);
			emptyLicence.setTitle(licenceId);
			emptyLicence.setUrl("licence.com");
			odrMetadata.setLicence(emptyLicence);
		}

	}

	/*
	 * no need for this method since no real mandatory fields enforced by odrc,
	 * but it provides example usage
	 */
	// private void setMetadataDefaults(ODRClient odrClient) {
	//
	// // set a default licence for applications
	// Licence defaultLicence = null;
	//
	// for (Licence licence : relevantLicences) {
	// if (licence.getName().equals("app_opensource")) {
	// defaultLicence = licence;
	// }
	// }
	//
	// if (defaultLicence != null) {
	// odrMetadata.setLicence(defaultLicence);
	// } else {
	// LicenceBean lb = new LicenceBean();
	// Licence emptyLicence = new LicenceImpl(lb);
	// emptyLicence.setTitle("Open Source");
	// odrMetadata.setLicence(emptyLicence);
	// }
	//
	// /*
	// * Fill in contacts
	// */
	// author = odrMetadata.newContact(RoleEnumType.AUTHOR);
	// maintainer = odrMetadata.newContact(RoleEnumType.MAINTAINER);
	// distributor = odrMetadata.newContact(RoleEnumType.DISTRIBUTOR);
	//
	// odrMetadata.getContacts().add(author);
	// }

}
