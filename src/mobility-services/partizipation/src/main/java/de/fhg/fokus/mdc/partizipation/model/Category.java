package de.fhg.fokus.mdc.partizipation.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Category {
	@JsonProperty("title")
	protected String title;

	@JsonProperty("description")
	protected String description;

	@JsonProperty("@Id")
	protected Long id;

	/**
	 * Gets the value of the title property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */

	public String getTitle() {
		return title;
	}

	/**
	 * Sets the value of the title property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTitle(String value) {
		this.title = value;
	}

	public boolean isSetTitle() {
		return (this.title != null);
	}

	/**
	 * Gets the value of the description property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */

	public String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescription(String value) {
		this.description = value;
	}

	public boolean isSetDescription() {
		return (this.description != null);
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link Long }
	 * 
	 */

	public Long getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link Long }
	 * 
	 */
	public void setId(Long value) {
		this.id = value;
	}

}
