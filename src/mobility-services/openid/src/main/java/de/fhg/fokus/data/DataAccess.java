package de.fhg.fokus.data;

import java.util.Map;

public interface DataAccess {

	/**
	 * 
	 * @param authcode
	 *            The authcode.
	 * @return
	 */
	String getUserNameForAuthCode(String authCode);

	/**
	 * 
	 * @param refeshtoken
	 * @return
	 */
	String getUserNameForRefreshToken(String refeshtoken);

	/**
	 * Invalidates the given auth_code.
	 * 
	 * @param auth_code
	 *            The authorization code to invalidate.
	 */
	void invalidateAuthCode(String auth_code);

	/**
	 * Checks if given credentials belong to each other.
	 * 
	 * @param client_id
	 * @param client_secret
	 * @return True, if client is authenticated with client_secret
	 */
	boolean isAuthenticatedClient(String client_id, String client_secret);

	/**
	 * valid access token?
	 * 
	 * @param accesstoken
	 * @param scope
	 * @param username
	 * @return True, if access token not null, scope subset of provided scope
	 *         und access token belongs to user name
	 */
	boolean isValidAccessToken(String accesstoken, String scope, String username);

	/**
	 * 
	 * @param authcode
	 * @param client_id
	 * @param redirect_uri
	 * @param scope
	 * @return
	 */
	boolean isValidAuthCode(String authcode, String client_id,
			String redirect_uri, String scope);

	/**
	 * 
	 * @param client_id
	 * @return
	 */
	boolean isValidClientId(String client_id);

	/**
	 * 
	 * @param client_id
	 * @param refresh_token
	 * @param scope
	 * @return
	 */
	boolean isValidRefreshToken(String client_id, String refresh_token,
			String scope);

	/**
	 * 
	 * @param givenScope
	 * @param dbScope
	 * @return
	 */
	boolean isValidScope(String givenScope, String dbScope);

	/**
	 * 
	 * @param givenScope
	 * @return
	 */
	boolean isValidScope(String givenScope);

	/**
	 * 
	 * @param access_token
	 * @param scope
	 * @param username
	 */
	void saveAccessToken(String access_token, String scope, String username);

	/**
	 * 
	 * @param authCode
	 * @param client_id
	 * @param redirect_uri
	 * @param scope
	 * @param username
	 */
	void saveAuthCode(String authCode, String client_id, String redirect_uri,
			String scope, String username);

	/**
	 * 
	 * @param client_id
	 * @param client_secret
	 * @param client_name
	 * @param client_url
	 * @param client_description
	 * @param redirect_uri
	 */
	void saveClient(String client_id, String client_secret, String client_name,
			String client_url, String client_description, String redirect_uri);

	/**
	 * 
	 * @param refreshToken
	 * @param client_id
	 * @param scope
	 * @param username
	 */
	void saveRefreshToken(String refreshToken, String client_id, String scope,
			String username);

	/**
	 * 
	 * @param scope
	 * @return
	 */
	Map<String, String> getDescriptionForScope(String scope);

}
