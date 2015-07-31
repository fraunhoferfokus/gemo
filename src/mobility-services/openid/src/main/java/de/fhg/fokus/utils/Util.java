package de.fhg.fokus.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

public class Util {

	public static Response buildErrorResponse(int statusCode,
			String errorRespone, String errorDescription)
			throws OAuthSystemException {
		OAuthResponse response = OAuthASResponse.errorResponse(statusCode)
				.setError(errorRespone).setErrorDescription(errorDescription)
				.buildJSONMessage();
		return Response.status(response.getResponseStatus())
				.entity(response.getBody()).build();
	}

	/**
	 * @param openID
	 * @return The extracted username from a provided OpenID.
	 */
	public static String extractUserName(String openID) {
		if (openID == null) {
			return openID;
		}
		return openID.substring(openID.lastIndexOf('/') + 1);
	}

	/**
	 * 
	 * @param multiValuedMap
	 *            MultivaluedMap of values
	 * @return Map of values
	 */
	public static Map<String, String> getParamMap(
			MultivaluedMap<String, String> multiValuedMap) {
		Map<String, String> map = new HashMap<String, String>();
		Iterator<String> keysIter = multiValuedMap.keySet().iterator();
		while (keysIter.hasNext()) {
			String name = keysIter.next();
			Object v = multiValuedMap.get(name);
			String value = null;
			if (v instanceof List) {
				@SuppressWarnings("unchecked")
				List<String> values = (List<String>) v;
				if (values.size() > 1 && name.startsWith("openid."))
					throw new IllegalArgumentException(
							"Multiple parameters with the same name: " + values);

				value = values.size() > 0 ? values.get(0).toString() : null;
			}
			map.put(name, value);
		}
		return map;
	}

	/**
	 * 
	 * @param offset
	 *            Time in seconds in which sth. shall expire.
	 * @return Expire Date in ms
	 */
	public static long computeExpiryDate(long offset) {
		return System.currentTimeMillis() + offset * 1000;
	}

	/**
	 * 
	 * @param dbValue
	 *            Date in ms retrieved from DB
	 * @return true, if dbValue is before current time
	 * 
	 */
	public static boolean isExpired(long dbValue) {
		return dbValue < System.currentTimeMillis();
	}

	/**
	 * 
	 * @param dbValue
	 *            Date in ms retrieved from DB
	 * @return true, if dbValue is after or equal current time. This means, that
	 *         dbValue is set to a value, which is in the future.
	 * 
	 */
	public static boolean isStillValid(long dbValue) {
		return !isExpired(dbValue);
	}

	public static String sanitizeSQLInput(String str) {
		// TODO sanitize input
		str = str.replaceAll("'", "\"");
		str = str.replaceAll(";", "");
		str = str.replaceAll("--", "");
		str = str.replaceAll("#", "");
		str = str.replaceAll("[^\\x0A\\x0D\\x20-\\x7E]", "");

		return str;

	}

	/**
	 * According to RFC 6749 the scope element is defined in the following way
	 * <ul>
	 * <li>scope = scope-token *( SP scope-token )</li>
	 * <li>scope-token = 1*NQCHAR</li>
	 * </ul>
	 * Where NQCHAR is defined as [%x21 / %x23-5B / %x5D-7E].
	 * 
	 * @param string
	 *            the input to be checked
	 * @return True, if string matches the regular expression for scope.
	 */
	public static boolean isValidScopeValue(String string) {
		return string
				.matches("[\\x21\\x23-\\x5B\\x5D-\\x7E]+( [\\x21\\x23-\\x5B\\x5D-\\x7E]+)*");
	}

	/**
	 * According to RFC 6749 the client_id element is defined in the following
	 * way
	 * <ul>
	 * <li>client-id = *VSCHAR</li>
	 * </ul>
	 * Where VSCHAR is defined as [%x20-7E].
	 * 
	 * @param client_id
	 *            the input to be checked
	 * @return True, if string matches the regular expression for client ID.
	 */
	public static boolean isValidClientIDValue(String client_id) {
		return client_id.matches("[\\x20-\\x7E]+");
	}

	/**
	 * According to RFC 6749 the client_secret element is defined in the
	 * following way
	 * <ul>
	 * <li>client-secret = *VSCHAR</li>
	 * </ul>
	 * Where VSCHAR is defined as [%x20-7E].
	 * 
	 * @param client_secret
	 *            the input to be checked
	 * @return True, if string matches the regular expression for client secret.
	 */
	public static boolean isValidClientSecretValue(String client_secret) {
		return client_secret.matches("[\\x20-\\x7E]+");
	}

	/**
	 * According to RFC 6749 the state element is defined in the following way
	 * <ul>
	 * <li>state = 1*VSCHAR</li>
	 * </ul>
	 * Where VSCHAR is defined as [%x20-7E].
	 * 
	 * @param state
	 *            the input to be checked
	 * @return True, if string matches the regular expression for client secret.
	 */
	public static boolean isValidStateValue(String state) {
		return state.matches("[\\x20-\\x7E]+");
	}

	/**
	 * According to RFC 6749 the code element is defined in the following way
	 * <ul>
	 * <li>code = 1*VSCHAR</li>
	 * </ul>
	 * Where VSCHAR is defined as [%x20-7E].
	 * 
	 * @param code
	 *            the input to be checked
	 * @return True, if string matches the regular expression for client secret.
	 */
	public static boolean isValidCodeValue(String code) {
		return code.matches("[\\x20-\\x7E]+");
	}

	/**
	 * According to RFC 6749 the access token element is defined in the
	 * following way
	 * <ul>
	 * <li>access token = 1*VSCHAR</li>
	 * </ul>
	 * Where VSCHAR is defined as [%x20-7E].
	 * 
	 * @param accesstoken
	 *            the input to be checked
	 * @return True, if string matches the regular expression for client secret.
	 */
	public static boolean isValidAccessTokenValue(String accesstoken) {
		return accesstoken.matches("[\\x20-\\x7E]+");
	}

	/**
	 * According to RFC 6749 the refresh token element is defined in the
	 * following way
	 * <ul>
	 * <li>refresh token = 1*VSCHAR</li>
	 * </ul>
	 * Where VSCHAR is defined as [%x20-7E].
	 * 
	 * @param refreshtoken
	 *            the input to be checked
	 * @return True, if string matches the regular expression for client secret.
	 */
	public static boolean isValidRefreshTokenValue(String refreshtoken) {
		return refreshtoken.matches("[\\x20-\\x7E]+");
	}

	/**
	 * According to RFC 6749 the grant type element is defined in the following
	 * way
	 * <ul>
	 * <li>grant-type = grant-name / URI-reference</li>
	 * <li>grant-name = 1*name-char </li
	 * <li>name-char = "-" / "." / "_" / DIGIT / ALPHA</li>
	 * </ul>
	 * 
	 * @param granttype
	 *            the input to be checked
	 * @return True, if string matches the regular expression for client secret.
	 */
	public static boolean isValidGrantTypeValue(String granttype) {
		return granttype.matches("[-._\\w]+");
	}
}
