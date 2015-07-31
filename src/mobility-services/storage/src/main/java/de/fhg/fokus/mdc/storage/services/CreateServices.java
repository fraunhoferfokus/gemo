package de.fhg.fokus.mdc.storage.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import de.fhg.fokus.mdc.storage.config.StorageConfig;
import de.fhg.fokus.mdc.storage.config.UserConfig;
import de.fhg.fokus.mdc.storage.parser.Parser;
import de.fhg.fokus.mdc.storage.parser.ParserFactory;

@Path("/upload")
public class CreateServices {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private SessionFactory sessionFactory;
	private ServiceRegistry serviceRegistry;

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadData(@FormDataParam("tableName") String tableName,
			@FormDataParam("file") InputStream file,
			@FormDataParam("file") FormDataContentDisposition fileDetails)
			throws IOException {
		Configuration conf = StorageConfig.getInstance().getCfg();
		return upload(conf, tableName, file, fileDetails);
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("private")
	public String privateUploadData(
			@FormDataParam("tableName") String tableName,
			@FormDataParam("file") InputStream file,
			@FormDataParam("file") FormDataContentDisposition fileDetails)
			throws IOException {
		Configuration conf = UserConfig.getInstance().getCfg();
		return upload(conf, tableName, file, fileDetails);
	}

	private String upload(Configuration conf,
			@FormDataParam("tableName") String tableName,
			@FormDataParam("file") InputStream file,
			@FormDataParam("file") FormDataContentDisposition fileDetails)
			throws IOException {

		byte[] fileBytes = IOUtils.toByteArray(file);

		LOGGER.info("Storage received new file: " + fileDetails.getFileName());

		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				conf.getProperties()).buildServiceRegistry();
		sessionFactory = conf.buildSessionFactory(serviceRegistry);

		Session session = sessionFactory.openSession();

		Transaction tx = session.beginTransaction();

		try {
			session.createSQLQuery("DROP TABLE \"" + tableName + "\"")
					.executeUpdate();
			LOGGER.info("Drop table [" + tableName + "] for updates.");
		} catch (HibernateException ex) {
			// nothing to do
		}

		tx.commit();
		session.close();

		session = sessionFactory.openSession();
		tx = session.beginTransaction();

		Parser parser = ParserFactory.createParser(FilenameUtils
				.getExtension(fileDetails.getFileName()));

		String createTableSqlQueryString = parser.createTableSqlQueryString(
				new ByteArrayInputStream(fileBytes), tableName);
		session.createSQLQuery(createTableSqlQueryString).executeUpdate();

		Iterator<String> queries = parser.createInsertSqlQueryString(
				new ByteArrayInputStream(fileBytes), tableName).iterator();

		while (queries.hasNext()) {
			String next = queries.next();
			LOGGER.info("Execute query: " + next);
			session.createSQLQuery(next).executeUpdate();
		}
		tx.commit();
		session.close();

		return "Table " + tableName + " created...";
	}
}
