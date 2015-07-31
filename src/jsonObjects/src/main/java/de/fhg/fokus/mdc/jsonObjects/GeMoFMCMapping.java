package de.fhg.fokus.mdc.jsonObjects;

import org.codehaus.jackson.annotate.JsonProperty;

public class GeMoFMCMapping {
	/**
	 * system id (pk)
	 */
	@JsonProperty("_id")
	private Integer id = -1;

	/**
	 * vehicle id
	 */
	@JsonProperty("gemoentityid")
	private String gemoEntityId = "";

	/**
	 * user id
	 */
	@JsonProperty("categoryid")
	private String categoryId = "";

	public String getGemoEntityId() {
		return gemoEntityId;
	}

	public void setGemoEntityId(String gemoEntityId) {
		this.gemoEntityId = gemoEntityId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

}
