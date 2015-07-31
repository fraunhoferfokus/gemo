package de.fhg.fokus.odp.portal.managedatasets.utils;

import com.liferay.portal.kernel.util.PropsKeys;

/**
 * Consts as compensatory for ext.properties, situated in liferay-HOME/portal-ext.properties
 * @author dsc
 *
 */
public class PortalProperties implements PropsKeys {
	public static final String GEMO_STORAGE_URL = "gemo.storage.url";
	public static final String GEMO_STORAGE_UPLOAD_URL = "gemo.storage.upload.url";
	public static final String GEMO_STORAGE_UPLOADBASE64_URL = "gemo.storage.uploadbase64.url";
	public static final String GEMO_STORAGE_SEARCH_QUERY_URL = "gemo.storage.search.query.url";
	public static final String GEMO_ODR_CLIENT_PROXY_URL = "gemo.odr.client.proxy.url";
}
