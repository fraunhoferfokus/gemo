package de.fhg.fokus.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.sun.jersey.core.util.MultivaluedMapImpl;

public class UtilTest {
	static Logger logger = Logger.getLogger(UtilTest.class.getName());

	@Test
	public void testExtractUserName() {
		String openID = "BASE_URL/openid/user/philipp";
		String username = "philipp";
		assertEquals(username, Util.extractUserName(openID));
	}

	@Test
	public void testGetParamMap() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("username", "Admin");
		params.add("username", "Admin2");
		params.add("passwd", "pass");
		params.add("foo", "bar");
		params.add("openid.ns", "Namespace");

		Map<String, String> myMap = Util.getParamMap(params);

		// check if same size
		assertEquals(myMap.size(), params.size());

		for (String key : params.keySet()) {
			// check if same keys
			assertTrue(myMap.containsKey(key));
		}
		// both maps are equal - Success!

	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetParamMapException() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("openid.one", "Admin");
		params.add("openid.one", "pass");
		params.add("foo", "bar");
		Map<String, String> myMap = Util.getParamMap(params);

		// check if same size
		assertEquals(myMap.size(), params.size());

		for (String key : params.keySet()) {
			// check if same keys
			assertTrue(myMap.containsKey(key));
		}
	}

	@Test
	public void testComputeExpiryDate() {
		long offset = 1000l;
		long expirydate = Util.computeExpiryDate(offset);
		boolean equalOrHigher = (System.currentTimeMillis() + offset * 1000) >= expirydate;
		assertTrue(equalOrHigher);
	}

	@Test
	public void testIsExpired() {
		assertTrue(Util.isExpired(0));
	}

	@Test
	public void testIsStillValid() {
		assertTrue(Util.isStillValid(Long.MAX_VALUE));
		assertFalse(Util.isStillValid(Long.MIN_VALUE));
	}

	@Test
	public void testIsValidScopeValue1() {
		assertTrue(Util.isValidScopeValue("input"));
	}

	@Test
	public void testIsValidScopeValue2() {
		assertTrue(Util.isValidScopeValue("input input"));
	}

	@Test
	public void testIsValidScopeValue3() {
		assertFalse(Util.isValidScopeValue("input\\asd"));
	}

	@Test
	public void testIsValidScopeValue4() {
		assertFalse(Util.isValidScopeValue("input\""));
	}

	@Test
	public void testIsValidScopeValue5() {
		assertFalse(Util.isValidScopeValue("input as\" ass"));
	}

	@Test
	public void testIsValidScopeValue6() {
		assertFalse(Util.isValidScopeValue("inp\\ut as\" ass"));
	}

	@Test
	public void testIsValidScopeValue7() {
		assertFalse(Util.isValidScopeValue("in\\put as\" ass"));
	}

	@Test
	public void testIsValidClientIDValue1() {
		assertTrue(Util.isValidClientIDValue("input"));
	}

	@Test
	public void testIsValidClientIDValue2() {
		assertTrue(Util.isValidClientIDValue("input input"));
	}

	@Test
	public void testIsValidClientIDValue3() {
		assertFalse(Util.isValidClientIDValue(""));
	}

	@Test
	public void testIsValidClientIDValue4() {
		assertFalse(Util.isValidClientIDValue("\0"));
	}

	@Test
	public void testIsValidClientSecretValue1() {
		assertTrue(Util.isValidClientSecretValue("input"));
	}

	@Test
	public void testIsValidClientSecretValue2() {
		assertTrue(Util.isValidClientSecretValue("input input"));
	}

	@Test
	public void testIsValidClientSecretValue3() {
		assertFalse(Util.isValidClientSecretValue(""));
	}

	@Test
	public void testIsValidClientSecretValue4() {
		assertFalse(Util.isValidClientSecretValue("\0"));
	}

	@Test
	public void testIsValidClientSecretValue5() {
		assertTrue(Util.isValidClientSecretValue("';shutdown --"));
	}

	@Test
	public void testIsValidAccessTokenValue1() {
		assertTrue(Util.isValidAccessTokenValue("input"));
	}

	@Test
	public void testIsValidAccessTokenValue2() {
		assertTrue(Util.isValidAccessTokenValue("input input"));
	}

	@Test
	public void testIsValidAccessTokenValue3() {
		assertFalse(Util.isValidAccessTokenValue(""));
	}

	@Test
	public void testIsValidAccessTokenValue4() {
		assertFalse(Util.isValidAccessTokenValue("\0"));
	}

	@Test
	public void testIsValidRefreshTokenValue1() {
		assertTrue(Util.isValidRefreshTokenValue("input"));
	}

	@Test
	public void testIsValidRefreshTokenValue2() {
		assertTrue(Util.isValidRefreshTokenValue("input input"));
	}

	@Test
	public void testIsValidRefreshTokenValue3() {
		assertFalse(Util.isValidRefreshTokenValue(""));
	}

	@Test
	public void testIsValidRefreshTokenValue4() {
		assertFalse(Util.isValidRefreshTokenValue("\0"));
	}

	@Test
	public void testIsValidCodeValue1() {
		assertTrue(Util.isValidCodeValue("input"));
	}

	@Test
	public void testIsValidCodeValue2() {
		assertTrue(Util.isValidCodeValue("input input"));
	}

	@Test
	public void testIsValidCodeValue3() {
		assertFalse(Util.isValidCodeValue(""));
	}

	@Test
	public void testIsValidCodeValue4() {
		assertFalse(Util.isValidCodeValue("\0"));
	}

	@Test
	public void testIsValidGrantTypeValue1() {
		assertTrue(Util.isValidGrantTypeValue("."));
	}

	@Test
	public void testIsValidGrantTypeValue2() {
		assertTrue(Util.isValidGrantTypeValue(".."));
	}

	@Test
	public void testIsValidGrantTypeValue3() {
		assertTrue(Util.isValidGrantTypeValue("code"));
	}

	@Test
	public void testIsValidGrantTypeValue4() {
		assertTrue(Util.isValidGrantTypeValue("12312"));
	}

	@Test
	public void testIsValidGrantTypeValue5() {
		assertFalse(Util.isValidGrantTypeValue(" "));
	}

	@Test
	public void testIsValidGrantTypeValue6() {
		assertFalse(Util.isValidGrantTypeValue(""));
	}

	@Test
	public void testIsValidGrantTypeValue7() {
		assertFalse(Util.isValidGrantTypeValue("code_wrong or"));
	}

	@Test
	public void testIsValidGrantTypeValue8() {
		assertTrue(Util.isValidGrantTypeValue("code_true"));
	}
}
