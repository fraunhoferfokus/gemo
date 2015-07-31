package de.fhg.fokus.mdc.utils.auth;

import org.codehaus.jackson.annotate.JsonProperty;

public class GemoScopeBean {
	/**
	 * system id (pk)
	 */
	@JsonProperty("_id")
	private String id = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * scope name
	 */
	@JsonProperty("name")
	private String name = "";

	/**
	 * description of the scope
	 */
	@JsonProperty("description")
	private String description = "";

	public GemoScopeBean() {
	}

	public GemoScopeBean(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
