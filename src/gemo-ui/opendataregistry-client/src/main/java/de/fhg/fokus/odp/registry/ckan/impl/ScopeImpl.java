package de.fhg.fokus.odp.registry.ckan.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import de.fhg.fokus.odp.registry.ckan.ODRClientImpl;
import de.fhg.fokus.odp.registry.ckan.json.ScopeBean;
import de.fhg.fokus.odp.registry.model.Scope;

public class ScopeImpl implements Scope, Serializable {

	private final ScopeBean scope;

	public ScopeImpl(ScopeBean scope) {
		this.scope = scope;
	}

	@Override
	public String getName() {
		return scope.getName();
	}

	@Override
	public void setName(String name) {
		scope.setName(name);
	}

	@Override
	public String getDescription() {
		return scope.getDescription();
	}

	@Override
	public void setDescription(String description) {
		scope.setDescription(description);
	}

	public static List<Scope> read(JsonNode scopes) {
		List<Scope> scopeList = new ArrayList<Scope>();

		if (scopes != null && scopes.isArray()) {
			for (JsonNode scopeNode : scopes) {
				ScopeBean scopeBean = ODRClientImpl.convert(scopeNode,
						ScopeBean.class);
				scopeList.add(new ScopeImpl(scopeBean));
			}
		}
		return scopeList;
	}

	public JsonNode write() {
		return ODRClientImpl.convert(scope);
	}
}
