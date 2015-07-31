package de.fhg.fokus.mdc.storage.services;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import de.fhg.fokus.mdc.storage.config.StorageConfig;
import de.fhg.fokus.mdc.storage.config.UserConfig;

/**
 * The class implements the insert of values to tables the data store.
 * 
 * @author Nikolay Tcholtchev, nikolay.tcholtchev@fokus.fraunhofer.de
 * 
 */
@Path("/insert")
public class InsertServices {

	/** Logger. */
	private static Logger log = Logger
			.getLogger(InsertServices.class.getName());

	/**
	 * The method builds an insert string from the passed POST parameters. It is
	 * assumed that the parameters were error checked before.
	 * 
	 * @param tableName
	 *            the value of the tableName parameter.
	 * @param params
	 *            the POST parameters, including the 'tableName' parameter.
	 * @return the update SQL string.
	 */
	private String getInsertStringFromPostParameters(String tableName,
			MultivaluedMap<String, String> params) {

		// the variable to return
		String toreturn = null;

		// get the keys
		Set<String> keys = params.keySet();

		// check the keys and respond with OK if no parameters submitted
		if (keys == null || keys.isEmpty()) {
			return null;
		}

		// move over the keys and prepare the SQL string
		String values = "";
		String columns = "";

		for (Iterator<String> it = keys.iterator(); it.hasNext();) {

			// get the next key
			String key = it.next();

			// variables for the values of the mandatory parameters
			if (key.toLowerCase().matches(
					"^\\s*[t|T][a|A][b|B][l|L][e|E][n|N][a|A][m|M][e|E]\\s*$")) {
				// in case we have to deal with on of the mandatory parameter
				continue;
			}

			// check on whether there is any content in the parameter
			if (params.get(key).get(0) == null) {
				continue;
			}

			// extend the SQL string
			if (!columns.matches("^\\s*$")) {
				columns += ",";
			}
			columns += key;

			if (!values.matches("^\\s*$")) {
				values += ",";
			}

			values += "\'" + params.get(key).get(0) + "\'";
		}

		// finalize the SQL String
		toreturn = "INSERT INTO " + tableName + "(" + columns + ") VALUES ("
				+ values + ")";

		return toreturn;
	}

	/**
	 * This is the POST method for inserting data into a table based on variable
	 * parameters in the POST request.
	 * 
	 * REM: the method can be invoked with curl by using: curl -H
	 * "Content-Type:application/x-www-form-urlencoded" -d "tableName=
	 * <table name>
	 * &<param name>=<param_value>]
	 * "  <URI to data store application server>/storage/insert"
	 * 
	 * Example:C:\Users\ntc>"C:\Program Files (x86)\curl\curl" -H
	 * "Content-Type:application/x-www-form-urlencoded" -d
	 * "tableName=members&email=th@yahoo.com&name=k&gender=male"
	 * http://localhost:8080/storage/insert
	 * 
	 * 
	 * 
	 * @param the
	 *            post parameters to proceed.
	 * @return the String to return over HTTP - "OK" if everything was aright,
	 *         "FAIL" otherwise.
	 */
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String insertDataPostParams(MultivaluedMap<String, String> params,
			@FormParam("tableName") String tableName) {

		Configuration conf = StorageConfig.getInstance().getCfg();
		return insert(conf, params, tableName);
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("private")
	public String privateInsertDataPostParams(
			MultivaluedMap<String, String> params,
			@FormParam("tableName") String tableName) {

		Configuration conf = UserConfig.getInstance().getCfg();
		return insert(conf, params, tableName);
	}

	private String insert(Configuration conf,
			MultivaluedMap<String, String> params, String tableName) {
		// session and transaction
		Session session = null;
		Transaction tx = null;

		// Variable for the session factory to use.
		SessionFactory sessionFactory = null;

		// Variable for the service registry to use. */
		ServiceRegistry serviceRegistry = null;

		// to return variable
		String toreturn = "OK";

		// check whether 'where' and the 'tableName' were defined
		if (tableName == null || tableName.matches("^\\s*$")) {
			return "FAIL";
		}

		// get the keys
		Set<String> keys = params.keySet();

		// check the keys and respond with OK if no parameters submitted
		if (keys == null || keys.isEmpty()) {
			return "OK";
		}

		// get the SQL INSERT query
		String sqlInsertQuery = getInsertStringFromPostParameters(tableName,
				params);

		try {

			// prepare the Hibernate stuff for execution
			// create the session factory
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					conf.getProperties()).buildServiceRegistry();
			sessionFactory = conf.buildSessionFactory(serviceRegistry);

			// open a session and begin the transaction
			session = sessionFactory.openSession();
			tx = session.beginTransaction();

			// execute the query
			session.createSQLQuery(sqlInsertQuery).executeUpdate();

		} catch (Exception e) {
			log.log(Level.SEVERE, "Exception:" + e);
			toreturn = "FAIL:" + e.getMessage();

		} finally {
			// commit the transaction and close the session
			System.out.println("Closing ....");
			if (tx != null) {
				tx.commit();
			}
			if (session != null) {
				session.flush();
				session.close();
			}

			if (sessionFactory != null) {
				sessionFactory.close();
			}
		}

		return toreturn;
	}
}
