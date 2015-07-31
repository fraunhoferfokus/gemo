package de.fhg.fokus.odp.portal.managedatasets.utils;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.faces.bridge.model.UploadedFile;
import com.liferay.faces.portal.context.LiferayFacesContext;
import com.liferay.portal.theme.ThemeDisplay;

public class NameUtils {
	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(NameUtils.class);

	/**
	 * returns the usable name (for storage or files to persist)
	 * 
	 * @param file
	 * @return
	 */
	public static String UsableNamefromFile(UploadedFile file) {
		return FilenameUtils.removeExtension(file.getName());
	}

	/**
	 * returns the usable name for a file that is owned by a liferay user
	 * 
	 * @param file
	 * @return
	 */
	public static String UsersUsableUniqueNamefromFile(UploadedFile file) {
		LiferayFacesContext lfc = LiferayFacesContext.getInstance();
		ThemeDisplay td = lfc.getThemeDisplay();
		LOG.debug("usable name from file: " + UsableNamefromFile(file));
		return "userid_" + td.getUserId() + "_" + UsableNamefromFile(file);
	}
}
