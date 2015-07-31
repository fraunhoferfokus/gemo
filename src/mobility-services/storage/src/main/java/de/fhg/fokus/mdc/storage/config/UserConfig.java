package de.fhg.fokus.mdc.storage.config;

import org.hibernate.cfg.Configuration;

public class UserConfig {

	private static UserConfig instance = new UserConfig();

	private final Configuration cfg;

	private UserConfig() {
		this.cfg = new Configuration().configure("hibernate-user.cfg.xml");
	}

	public Configuration getCfg() {
		return cfg;
	}

	public static UserConfig getInstance() {
		return instance;
	}
}
