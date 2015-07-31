package de.fhg.fokus.cm.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.fhg.fokus.cm.ejb.Complaint;

public class ServiceHelper {

	private static final String PERSISTENCE_PROPERTIES = "persistence.properties";
	private static final String PERSISTENCE_UNIT = "de.fhg.fokus.cm.ejb";

	public static Integer LIMITMIN = 1;
	public static Integer LIMITMAX = 100;
	public static Integer LIMITDEFAULT = 10;

	public static Integer OFFSETDEFAULT = 0;
	public static Integer OFFSETMIN = 0;

	// http://www.coderanch.com/t/485741/ORM/java/EntityManagerFactory-EntityManager
	private static EntityManagerFactory entityManagerFactory = null;

	/**
	 * @return
	 * @throws Exception
	 */
	public EntityManager createEntityManager() throws Exception {

		final Properties persistenceProperties = new Properties();
		InputStream is = null;
		try {
			is = getClass().getClassLoader().getResourceAsStream(
					PERSISTENCE_PROPERTIES);
			persistenceProperties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage() + "Database error!");
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignored) {
				}
			}
		}

		try {
			if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
				entityManagerFactory = Persistence.createEntityManagerFactory(
						PERSISTENCE_UNIT, persistenceProperties);
			}

			final EntityManager entityManager = entityManagerFactory
					.createEntityManager();

			return entityManager;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage() + "Database error!");
		}
	}

	public static String printComplaint(Complaint c) {
		StringBuffer sb = new StringBuffer();
		sb.append("[" + c.getId() + "] " + c.getTitle());
		sb.append("\n\t" + c.getTags());
		sb.append("\n\t" + c.getGeolocation().getLatitude() + " | "
				+ c.getGeolocation().getLongitude());
		sb.append("\n\tAverageRating: " + c.getAverageRating());
		try {
			sb.append("\n\tRatings: " + c.getRatings().getRatings().size());
		} catch (Exception e) {
		}
		try {
			sb.append("\n\tPhotos: " + c.getPhotos().getPhotos().size());
		} catch (Exception e) {
		}
		try {
			sb.append("\n\tComments: " + c.getComments().getComments().size());
		} catch (Exception e) {
		}
		return sb.toString();
	}

	/**
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	public static Integer checkOffset(Integer offset) throws Exception {
		if (offset == null) {
			offset = ServiceHelper.OFFSETDEFAULT;
		}
		if (offset < ServiceHelper.OFFSETMIN)
			throw new Exception("offset < " + ServiceHelper.OFFSETMIN); //$NON-NLS-1$
		return offset;
	}

	public static Integer calcLimit(Integer offset, Integer limit, Integer total)
			throws Exception {

		if (limit == null) {
			limit = ServiceHelper.LIMITDEFAULT;
		}

		if (offset >= total && total != 0)
			throw new Exception("offset >= " + total + " (total)");

		if (limit + offset > total)
			return total - offset;
		return limit;
	}

	public static Integer checkLimit(Integer limit) throws Exception {
		if (limit == null) {
			limit = ServiceHelper.LIMITDEFAULT;
		}
		if (limit > ServiceHelper.LIMITMAX || limit < ServiceHelper.LIMITMIN)
			throw new Exception(
					"limit out of valid range [" + ServiceHelper.LIMITMIN + "," //$NON-NLS-1$ //$NON-NLS-2$
							+ ServiceHelper.LIMITMAX + "]"); //$NON-NLS-1$
		return limit;
	}

}
