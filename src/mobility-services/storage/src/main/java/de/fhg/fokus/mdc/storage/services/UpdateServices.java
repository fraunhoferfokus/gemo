package de.fhg.fokus.mdc.storage.services;

// imports
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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

/**
 * The class implements the update of values in the data store.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * 
 */
@Path("/update")
public class UpdateServices {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/** Variable for the session factory to use. */
	private SessionFactory sessionFactory;

	/** Variable for the service registry to use. */
	private ServiceRegistry serviceRegistry;

	/**
	 * This is the POST method for updating data based on the data stream.
	 * 
	 * @param tableName
	 *            the name of the table to update.
	 * @param file
	 *            the file with the data to update.
	 * @param fileDetails
	 *            details regarding the file.
	 * @return the String to return over HTTP - "OK" if everything was alright,
	 *         "FAIL" otherwise.
	 * @throws IOException
	 *             an IO exception if any.
	 */
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String updateDataFilestream(
			@FormDataParam("tableName") String tableName,
			@FormDataParam("file") InputStream file,
			@FormDataParam("file") FormDataContentDisposition fileDetails)
			throws IOException {

		// get the contents of the file
		byte[] fileBytes = IOUtils.toByteArray(file);

		// create the session factory
		Configuration conf = StorageConfig.getInstance().getCfg();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				conf.getProperties()).buildServiceRegistry();
		sessionFactory = conf.buildSessionFactory(serviceRegistry);

		// open a session and begin the transaction
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		// get the parser for the updates
		Parser parser = ParserFactory.createParser(FilenameUtils
				.getExtension(fileDetails.getFileName()));

		// the sql string is created
		List<String> updateTableSqlQueryStrings = parser
				.updateTableSqlQueryString(new ByteArrayInputStream(fileBytes),
						tableName);

		// update queries
		Iterator<String> queries = updateTableSqlQueryStrings.iterator();
		while (queries.hasNext()) {
			String next = queries.next();
			session.createSQLQuery(next).executeUpdate();
		}

		// commit the transaction and close the session
		tx.commit();
		session.close();

		return "OK";
	}

	/**
	 * The method builds an update string from the passed POST parameters. It is
	 * assumed that the parameters were error checked before.
	 * 
	 * @param tableName
	 *            the value of the tableFrom parameter.
	 * @param where
	 *            the value of the where parameter.
	 * @param params
	 *            the POST parameters, including the 'tableName' and the 'where'
	 *            parameters.
	 * @return the update SQL string.
	 */
	private String getUpdateStringFromPostParameters(String tableName,
			String where, MultivaluedMap<String, String> params) {

		// the variable to return
		String toreturn = null;

		// get the keys
		Set<String> keys = params.keySet();

		// check the keys and respond with OK if no parameters submitted
		if (keys == null || keys.isEmpty()) {
			return null;
		}

		// move over the keys and prepare the SQL string
		String sqlStr = "";
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {

			// get the next key
			String key = it.next();

			// variables for the values of the mandatory parameters
			if (key.toLowerCase()
					.matches("^\\s*[w|W][h|H][e|E][r|R][e|E]\\s*$")
					|| key.toLowerCase()
							.matches(
									"^\\s*[t|T][a|A][b|B][l|L][e|E][n|N][a|A][m|M][e|E]\\s*$")) {
				// in case we have to deal with on of the mandatory parameter
				continue;
			}

			// check on whether there is any content in the parameter
			if (params.get(key).get(0) == null) {
				continue;
			}

			// extend the SQL string
			if (!sqlStr.matches("^\\s*$")) {
				sqlStr += ",";
			}
			sqlStr += key + "=\'" + params.get(key).get(0) + "\'";
		}

		// finalize the SQL String
		toreturn = "update " + tableName + " set " + sqlStr + " where " + where;

		return toreturn;
	}

	/**
	 * This is the POST method for updating data based on variable parameters in
	 * the POST request.
	 * 
	 * REM: the method can be invoked with curl by using: curl -H
	 * "Content-Type:application/x-www-form-urlencoded" -d "tableName=
	 * <table name>
	 * &where=<condition>[&<param name>=<param_value>]
	 * "  <URI to data store application server>/storage/update"
	 * 
	 * Example: curl -H "Content-Type:application/x-www-form-urlencoded" -d
	 * "tableName=members&where="name=\'thomas\'"&email=th@yahoo.com"
	 * http://localhost:8090/storage/update
	 * 
	 * 
	 * 
	 * @param the
	 *            post parameters to proceed.
	 * @return the String to return over HTTP - "OK" if everything was aright,
	 *         "FAIL" otherwise.
	 * @throws IOException
	 *             an IO exception if any.
	 */
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateDataPostParams(MultivaluedMap<String, String> params,
			@FormParam("tableName") String tableName,
			@FormParam("where") String where) throws IOException {

		Configuration conf = StorageConfig.getInstance().getCfg();
		return update(conf, params, tableName, where);
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("private")
	public String privateUpdateDataPostParams(
			MultivaluedMap<String, String> params,
			@FormParam("tableName") String tableName,
			@FormParam("where") String where) throws IOException {

		Configuration conf = UserConfig.getInstance().getCfg();
		return update(conf, params, tableName, where);

	}

	private String update(Configuration conf,
			MultivaluedMap<String, String> params, String tableName,
			String where) {
		String result = "FAIL";

		try {
			if (entryWithKeyExist(conf, tableName, where)) {
				// get the SQL update query
				String sqlUpdateQuery = getUpdateStringFromPostParameters(
						tableName, where, params);

				// prepare the Hibernate stuff for execution

				// create the session factory
				serviceRegistry = new ServiceRegistryBuilder().applySettings(
						conf.getProperties()).buildServiceRegistry();
				sessionFactory = conf.buildSessionFactory(serviceRegistry);

				// open a session and begin the transaction
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();

				// execute the query
				session.createSQLQuery(sqlUpdateQuery).executeUpdate();

				// commit the transaction and close the session
				tx.commit();
				session.close();

				result = "OK";
			} else {
				result = "Entry in table " + tableName + " with key " + where
						+ " already exists";
			}

		} catch (Exception e) {
			LOGGER.error("Error while updating storage entry", e);
		}

		return result;
	}

	private boolean entryWithKeyExist(Configuration conf, String tableName,
			String key) {

		boolean result = false;
		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				conf.getProperties()).buildServiceRegistry();
		sessionFactory = conf.buildSessionFactory(serviceRegistry);

		// open a session and begin the transaction
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		Object queryResult = session.createSQLQuery(
				"SELECT * FROM " + tableName + " WHERE " + key).uniqueResult();

		if (queryResult != null) {
			result = true;
		}

		// commit the transaction and close the session
		tx.commit();
		session.close();

		return result;
	}
	// The following method allows for GET based updates --> it is currently not
	// being used

	// /**
	// * The method implementing the update functionality.
	// *
	// * @param updateString
	// * the SQL update string.
	// *
	// * @return "OK" or "FAILED" depending on the success.
	// */
	// @GET
	// @Produces(MediaType.APPLICATION_JSON)
	// @SuppressWarnings({ "unchecked" })
	// @Consumes(MediaType.TEXT_PLAIN)
	// public String query(@QueryParam("update") String updateString) {
	//
	// // check the update string
	// if (updateString == null || updateString.matches("^\\s*$")
	// || !updateString.matches("^\\s*[u|U][p|P][d|D][a|A][t|T][e|E]")) {
	// return "FAILED";
	// }
	//
	// // execute the update string on the database using Hibernate
	// Configuration conf = StorageConfig.getInstance().getCfg();
	// serviceRegistry = new ServiceRegistryBuilder().applySettings(
	// conf.getProperties()).buildServiceRegistry();
	// sessionFactory = conf.buildSessionFactory(serviceRegistry);
	//
	// Session session = sessionFactory.openSession();
	//
	// Transaction tx = session.beginTransaction();
	//
	// Query query = session.createSQLQuery(updateString);
	// query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	// // List<Map<String, Object>> resultListMap = query.list();
	//
	// tx.commit();
	// session.close();
	//
	// return "OK";
	// }

}
