package de.fhg.fokus.data;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import de.fhg.fokus.mdc.propertyProvider.Constants;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.utils.BCrypt;
import de.fhg.fokus.utils.Util;

@ApplicationScoped
@Alternative
public class StorageDatabase implements DataAccess {
	private static RestTemplate restTemplate = new RestTemplate();

	private static final String AUTH_QUERY = "select * from authcode where auth_code='";
	private static final String ACCESS_QUERY = "select * from accesstoken where access_token='";
	private static final String REFRESH_QUERY = "select * from refreshtoken where refresh_token='";
	private static final String CLIENT_QUERY = "select * from clients where client_id='";
	private static final String SCOPE_QUERY = "select * from scopes where name='";

	private static final long HALF_YEAR_OFFSET = 15811200;
	private static final long TEN_MINUTE_OFFSET = 600;
	private static final long ONE_HOUR_OFFSET = 3600;

	private static String STORAGE_SEARCH_PATH;
	private static String STORAGE_INSERT_PATH;
	private static String STORAGE_UPDATE_PATH;
	private static String QUERY_PATH;

	static {
		Properties props = null;
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
		STORAGE_SEARCH_PATH = props.getProperty(Constants.STORAGE_URI)
				+ props.getProperty(Constants.SERVICE_URI_STORAGE_PRIVATE_SEARCH);
		STORAGE_INSERT_PATH = props.getProperty(Constants.STORAGE_URI)
				+ props.getProperty(Constants.SERVICE_URI_STORAGE_PRIVATE_INSERT);
		STORAGE_UPDATE_PATH = props.getProperty(Constants.STORAGE_URI)
				+ props.getProperty(Constants.SERVICE_URI_STORAGE_PRIVATE_UPDATE);
		QUERY_PATH = props
				.getProperty(Constants.SERVICE_URI_STORAGE_PRIVATE_SEARCH_PARAMETER);
	}

	/**
	 * 
	 * @param client_id
	 *            client to whom the token belongs
	 * @param refreshToken
	 *            Token to be checked
	 * @param scope
	 *            given scope equals scope stored in DB
	 * @return true, if refresh token is still valid and belongs to given
	 *         client_id
	 */
	public boolean isValidRefreshToken(String client_id, String refreshToken,
			String scope) {

		refreshToken = Util.sanitizeSQLInput(refreshToken);

		String refreshTokenDetails = restTemplate.getForObject(
				STORAGE_SEARCH_PATH + QUERY_PATH + REFRESH_QUERY + refreshToken
						+ "'", String.class);
		if (refreshTokenDetails == null) {
			return false;
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode rootNode = mapper.readTree(refreshTokenDetails);
				if (rootNode.isArray()) {
					if (rootNode.size() != 1) {
						return false;
					} else {
						JsonNode details = rootNode.get(0);
						// make sure that details is NOT empty
						if (details.iterator().hasNext()) {
							return Util.isStillValid(details.get("valid_until")
									.asLong())
									&& details.get("client_id").asText()
											.equals(client_id)
									&& isValidScope(scope, details.get("scope")
											.asText());
						} else {
							return false;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void setSTORAGE_SEARCH_PATH(String sTORAGE_SEARCH_PATH) {
		STORAGE_SEARCH_PATH = sTORAGE_SEARCH_PATH;
	}

	public void setSTORAGE_INSERT_PATH(String sTORAGE_INSERT_PATH) {
		STORAGE_INSERT_PATH = sTORAGE_INSERT_PATH;
	}

	public void setSTORAGE_UPDATE_PATH(String sTORAGE_UPDATE_PATH) {
		STORAGE_UPDATE_PATH = sTORAGE_UPDATE_PATH;
	}

	public String getSTORAGE_SEARCH_PATH() {
		return STORAGE_SEARCH_PATH;
	}

	public String getSTORAGE_INSERT_PATH() {
		return STORAGE_INSERT_PATH;
	}

	public String getSTORAGE_UPDATE_PATH() {
		return STORAGE_UPDATE_PATH;
	}

	public boolean isAuthenticatedClient(String client_id, String client_secret) {

		client_id = Util.sanitizeSQLInput(client_id);

		String clientDetails = restTemplate.getForObject(STORAGE_SEARCH_PATH
				+ QUERY_PATH + CLIENT_QUERY + client_id + "'", String.class);
		if (clientDetails == null) {
			return false;
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode rootNode = mapper.readTree(clientDetails);
				if (rootNode.isArray()) {
					if (rootNode.size() != 1) {
						return false;
					} else {
						JsonNode details = rootNode.get(0);
						return BCrypt.checkpw(client_secret,
								details.get("hashed_client_secret").asText());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * This method saves a refresh token in token database. Refresh Token are
	 * valid for a half year.
	 * 
	 * @param refreshToken
	 *            The generated refresh token.
	 * @param scope
	 *            The given scope.
	 * @param username
	 */
	public void saveRefreshToken(String refreshToken, String client_id,
			String scope, String username) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("tableName", "refreshtoken");
		map.add("client_id", client_id);
		map.add("refresh_token", refreshToken);
		map.add("username", username);
		map.add("valid_until",
				String.valueOf(Util.computeExpiryDate(HALF_YEAR_OFFSET)));
		map.add("scope", scope);
		restTemplate.postForObject(STORAGE_INSERT_PATH, map, String.class);
	}

	/**
	 * This method saves an access token in token database. Access Token are
	 * valid for ten minutes.
	 * 
	 * @param accessToken
	 *            The generated access token.
	 * @param scope
	 *            The given scope.
	 */
	public void saveAccessToken(String accessToken, String scope,
			String username) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("tableName", "accesstoken");
		map.add("access_token", accessToken);
		map.add("username", username);
		map.add("valid_until",
				String.valueOf(Util.computeExpiryDate(ONE_HOUR_OFFSET)));
		map.add("scope", scope);
		restTemplate.postForObject(STORAGE_INSERT_PATH, map, String.class);
	}

	public void saveAuthCode(String authCode, String client_id,
			String redirect_uri, String scope, String username) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("tableName", "authcode");
		map.add("auth_code", authCode);
		map.add("client_id", client_id);
		map.add("valid_until",
				String.valueOf(Util.computeExpiryDate(TEN_MINUTE_OFFSET)));
		map.add("scope", scope);
		map.add("username", username);
		map.add("redirect_uri", redirect_uri);
		restTemplate.postForObject(STORAGE_INSERT_PATH, map, String.class);
	}

	public boolean isValidClientId(String clientId) {

		clientId = Util.sanitizeSQLInput(clientId);

		String clientDetails = restTemplate.getForObject(STORAGE_SEARCH_PATH
				+ QUERY_PATH + CLIENT_QUERY + clientId + "'", String.class);
		if (clientDetails == null) {
			return false;
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode rootNode = mapper.readTree(clientDetails);
				if (rootNode.isArray()) {
					return (rootNode.size() == 1);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void saveClient(String client_id, String client_secret,
			String client_name, String client_url, String client_description,
			String redirect_uri) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("tableName", "clients");
		map.add("client_id", client_id);
		map.add("hashed_client_secret",
				BCrypt.hashpw(client_secret, BCrypt.gensalt()));
		map.add("client_name", client_name);
		map.add("client_url", client_url);
		map.add("client_description", client_description);
		map.add("redirect_uri", redirect_uri);
		restTemplate.postForObject(STORAGE_INSERT_PATH, map, String.class);
	}

	public boolean isValidAccessToken(String accessToken, String scope,
			String username) {
		if (username == null || scope == null) {
			return false;
		}

		accessToken = Util.sanitizeSQLInput(accessToken);

		String accessTokenDetails = restTemplate.getForObject(
				STORAGE_SEARCH_PATH + QUERY_PATH + ACCESS_QUERY + accessToken
						+ "'", String.class);
		if (accessTokenDetails == null) {
			return false;
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode rootNode = mapper.readTree(accessTokenDetails);
				if (rootNode.isArray()) {
					if (rootNode.size() != 1) {
						return false;
					} else {
						JsonNode details = rootNode.get(0);
						return Util.isStillValid(details.get("valid_until")
								.asLong())
								&& isValidScope(scope, details.get("scope")
										.asText())
								&& username.equals(details.get("username")
										.asText());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * @param authCode
	 * @param scope
	 * @param client_id
	 * @param redirect_uri
	 * @return
	 */
	public boolean isValidAuthCode(String authCode, String client_id,
			String redirect_uri, String scope) {

		authCode = Util.sanitizeSQLInput(authCode);

		String authCodeDetails = restTemplate.getForObject(STORAGE_SEARCH_PATH
				+ QUERY_PATH + getAuthQuery() + authCode + "'", String.class);
		if (authCodeDetails == null) {
			return false;
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode rootNode = mapper.readTree(authCodeDetails);
				if (rootNode.isArray()) {
					if (rootNode.size() != 1) {
						return false;
					} else {
						JsonNode details = rootNode.get(0);
						if (Util.isStillValid(details.get("valid_until")
								.asLong())
								&& details.get("client_id").asText()
										.equals(client_id)
								&& details.get("redirect_uri").asText()
										.equals(redirect_uri)
								&& isValidScope(scope, details.get("scope")
										.asText())) {
							invalidateAuthCode(authCode);
							return true;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void invalidateAuthCode(String authCode) {

		authCode = Util.sanitizeSQLInput(authCode);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("tableName", "authcode");
		map.add("valid_until", "0");
		map.add("where", "auth_code='" + authCode + "'");
		restTemplate.postForObject(STORAGE_UPDATE_PATH, map, String.class);

	}

	/**
	 * @param givenScope
	 *            scope given by user
	 * @param dbScope
	 *            scope stored in DB
	 * @return true, if scopes are equal
	 */
	public boolean isValidScope(String givenScope, String dbScope) {
		if (givenScope == null || givenScope.isEmpty()) {
			return false;
		}
		// create set of space separated string
		Set<String> givenScopesSet = OAuthUtils.decodeScopes(givenScope);
		Set<String> dbScopesSet = OAuthUtils.decodeScopes(dbScope);

		// check if dbScopesSet is a superset from givenScopesSet
		return dbScopesSet.containsAll(givenScopesSet);
	}

	public static String getAuthQuery() {
		return AUTH_QUERY;
	}

	public static String getRefreshQuery() {
		return REFRESH_QUERY;
	}

	@Override
	public String getUserNameForAuthCode(String authcode) {

		authcode = Util.sanitizeSQLInput(authcode);

		String tokenDetails = restTemplate.getForObject(STORAGE_SEARCH_PATH
				+ QUERY_PATH + StorageDatabase.getAuthQuery() + authcode + "'",
				String.class);
		if (tokenDetails == null) {
			return null;
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode rootNode = mapper.readTree(tokenDetails);
				if (rootNode.isArray()) {
					if (rootNode.size() != 1) {
						return null;
					} else {
						JsonNode details = rootNode.get(0);
						return details.get("username").asText();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String getUserNameForRefreshToken(String refreshtoken) {

		// TODO sanitize input
		refreshtoken = Util.sanitizeSQLInput(refreshtoken);

		String tokenDetails = restTemplate.getForObject(STORAGE_SEARCH_PATH
				+ QUERY_PATH + StorageDatabase.getRefreshQuery() + refreshtoken
				+ "'", String.class);
		if (tokenDetails == null) {
			return null;
		} else {
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode rootNode = mapper.readTree(tokenDetails);
				if (rootNode.isArray()) {
					if (rootNode.size() != 1) {
						return null;
					} else {
						JsonNode details = rootNode.get(0);
						return details.get("username").asText();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean isValidScope(String givenScope) {
		Set<String> givenScopesSet = OAuthUtils.decodeScopes(givenScope);

		for (String scope : givenScopesSet) {
			scope = Util.sanitizeSQLInput(scope);

			String scopeDetails = restTemplate.getForObject(
					STORAGE_SEARCH_PATH + QUERY_PATH + SCOPE_QUERY + scope
							+ "'", String.class);
			if (scopeDetails == null) {
				return false;
			} else {
				ObjectMapper mapper = new ObjectMapper();
				try {
					JsonNode rootNode = mapper.readTree(scopeDetails);
					if (rootNode.isArray()) {
						if (rootNode.size() != 1) {
							return false;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	@Override
	public Map<String, String> getDescriptionForScope(String scope) {
		// TODO Auto-generated method stub
		return null;
	}

}