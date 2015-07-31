package de.fhg.fokus.mdc.jsonObjects;

import org.codehaus.jackson.annotate.JsonProperty;

public class TotpSeed {
	/**
	 * system id (pk)
	 */
	@JsonProperty("_id")
	private Integer id = -1;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public String getSeedtype() {
		return seedtype;
	}

	public void setSeedtype(String seedtype) {
		this.seedtype = seedtype;
	}

	/**
	 * seed itself
	 */
	@JsonProperty("seed")
	private String seed = "";

	/**
	 * for which hash the seed can be used; takes values byte20, byte32, byte64
	 * respectively for HmacSHA1, HmacSHA256, HmacSHA512
	 */
	@JsonProperty("seedtype")
	private String seedtype = "";
}
