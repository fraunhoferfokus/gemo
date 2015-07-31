package de.fhg.fokus.mdc.utils.security.clients;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServiceScopeEntry {
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
	@JsonProperty("service_name")
	private String serviceName = "";

	/**
	 * description of the scope
	 */
	@JsonProperty("scope_pkey")
	private String scopeId = "";

	public ServiceScopeEntry() {
	}

	public ServiceScopeEntry(String serviceName, String scopeId) {
		this.serviceName = serviceName;
		this.scopeId = scopeId;
	}

}
