package de.fhg.fokus.mdc.utils.auth.clients;

import static de.fhg.fokus.mdc.propertyProvider.Constants.SCOPES_TABLE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.mdc.utils.auth.GemoScopeBean;

public class ScopeDataStoreClient extends AuthDataStoreClient {
	private final Logger log = LoggerFactory.getLogger(getClass());

	// public ScopeDataStoreClient() {
	// super();
	// }

	@Override
	public void defineTableName() {
		table = props.getProperty(SCOPES_TABLE);

	}

	public String getScopeIdForScopeName(String scopeName)
			throws JsonParseException, JsonMappingException, IOException {
		GemoScopeBean[] scopeEntries = queryScopeId(scopeName);
		String scopeId = null;
		if (scopeEntries.length != 0) {
			scopeId = scopeEntries[0].getId();
		}
		return scopeId;
	}

	public List<String> getScopeIdsForScopes(List<GemoScopeBean> scopes)
			throws JsonParseException, JsonMappingException, IOException {
		List<String> scopeIds = new ArrayList<String>();
		for (GemoScopeBean s : scopes) {
			String scopeName = s.getName();
			log.debug("query from scope datastoreclient" + "select * from "
					+ table + " where name=" + "\'" + scopeName + "\'");

			GemoScopeBean[] scopeEntries = queryScopeId(scopeName);

			if (scopeEntries.length != 0) {
				scopeIds.add(scopeEntries[0].getId());
			}
		}
		return scopeIds;
	}

	private GemoScopeBean[] queryScopeId(String scopeName)
			throws JsonParseException, JsonMappingException, IOException {
		String scopeEntry = select("select * from " + table + " where name="
				+ "\'" + scopeName + "\'");
		ObjectMapper mapper = new ObjectMapper();
		GemoScopeBean[] resultSet = mapper.readValue(scopeEntry,
				GemoScopeBean[].class);
		return resultSet;
	}

}
