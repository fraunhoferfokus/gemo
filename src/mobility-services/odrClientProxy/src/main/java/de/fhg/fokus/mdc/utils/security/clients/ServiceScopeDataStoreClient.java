package de.fhg.fokus.mdc.utils.security.clients;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import de.fhg.fokus.mdc.propertyProvider.Constants;

public class ServiceScopeDataStoreClient extends AuthDataStoreClient {

	/**
	 * implements DataStoreClient to communicate with token database
	 * 
	 * @author izi (Ilke Zilci, ilke.zilci@fokus.fraunhofer.de)
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());

	ScopeDataStoreClient scopeClient = new ScopeDataStoreClient();

	// public ServiceScopeDataStoreClient() {
	// super();
	// }

	@Override
	public void defineTableName() {
		table = props.getProperty(Constants.SERVICES_SCOPES_TABLE);
	}

	public boolean checkScopeforService(String scope, String serviceName)
			throws JsonParseException, JsonMappingException, IOException {
		String scopeId = scopeClient.getScopeIdForScopeName(scope);

		String serviceScopeEntry = select("select * from " + table
				+ " where service_name LIKE " + "\'" + serviceName + "%" + "\'"
				+ "and scope_pkey= " + "\'" + scopeId + "\'");
		log.debug("query to storage" + "select * from " + table
				+ " where service_name LIKE " + "\'" + serviceName + "%" + "\'"
				+ "and scope_pkey= " + "\'" + scopeId + "\'");
		log.debug("response from storage" + serviceScopeEntry);
		ObjectMapper mapper = new ObjectMapper();
		ServiceScopeEntry[] resultSet = mapper.readValue(serviceScopeEntry,
				ServiceScopeEntry[].class);
		if (resultSet.length > 0) {
			return true;
		}
		return false;
	}

	public void writeScopesForService(String appIdentifier,
			List<GemoScopeBean> scopes) throws JsonParseException,
			JsonMappingException, IOException {

		List<String> scopeIds = scopeClient.getScopeIdsForScopes(scopes);
		for (String scopeId : scopeIds) {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("tableName", table);
			map.add("service_name", appIdentifier);
			map.add("scope_pkey", scopeId);
			insert(map);
		}

	}

}
