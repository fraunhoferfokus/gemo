package de.fhg.fokus.mdc.storage.config;

import org.hibernate.cfg.Configuration;

public class StorageConfig {

	private static StorageConfig instance = new StorageConfig();

	private final Configuration cfg;

	private StorageConfig() {
		this.cfg = new Configuration().configure("hibernate-storage.cfg.xml");
	}

	public Configuration getCfg() {
		return cfg;
	}

	public static StorageConfig getInstance() {
		return instance;
	}
}
