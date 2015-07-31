package de.fhg.fokus.mdc.odrClientProxy.model;

import java.util.ArrayList;
import java.util.List;

import de.fhg.fokus.odp.registry.ckan.json.ScopeBean;

public class GemoMetadata {

	private String author;

	private String licenceId;

	private String name;

	private String description;

	private List<ScopeBean> scopes;

	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<ScopeBean> getScopes() {
		if (scopes == null) {
			scopes = new ArrayList<ScopeBean>();
		}
		return scopes;
	}

	public void setScopes(List<ScopeBean> scopes) {
		this.scopes = scopes;
	}

	private List<GemoApplicationResource> resources;

	public List<GemoApplicationResource> getResources() {
		if (resources == null) {
			resources = new ArrayList<GemoApplicationResource>();
		}
		return resources;
	}

	public void setResources(List<GemoApplicationResource> resources) {
		this.resources = resources;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLicenceId() {
		return licenceId;
	}

	public void setLicenceId(String licenceId) {
		this.licenceId = licenceId;
	}

}
