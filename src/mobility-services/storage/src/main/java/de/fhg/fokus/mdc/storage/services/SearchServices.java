package de.fhg.fokus.mdc.storage.services;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import de.fhg.fokus.mdc.storage.config.StorageConfig;
import de.fhg.fokus.mdc.storage.config.UserConfig;
import de.fhg.fokus.mdc.storage.utils.MappingUtils;

@Path("/search")
public class SearchServices {

	private SessionFactory sessionFactory;
	private ServiceRegistry serviceRegistry;

	@SuppressWarnings("unchecked")
	private String search(Configuration conf, String queryString) {

		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				conf.getProperties()).buildServiceRegistry();
		sessionFactory = conf.buildSessionFactory(serviceRegistry);

		Session session = sessionFactory.openSession();

		Transaction tx = session.beginTransaction();

		Query query = session.createSQLQuery(queryString);
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		List<Map<String, Object>> resultListMap = query.list();

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

		return MappingUtils.transformedResultListMapToJSONString(resultListMap);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public String query(@QueryParam("query") String queryString) {

		Configuration conf = StorageConfig.getInstance().getCfg();
		return search(conf, queryString);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("private")
	public String privateQuery(@QueryParam("query") String queryString) {

		Configuration conf = UserConfig.getInstance().getCfg();
		return search(conf, queryString);
	}

}
