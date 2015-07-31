package de.fhg.fokus.openid.users;

import static com.jayway.restassured.RestAssured.expect;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import de.fhg.fokus.IntegrationTest;

@Category(IntegrationTest.class)
public class UserServiceTest {
	static Logger logger = Logger.getLogger(UserServiceTest.class.getName());

	@Test
	public void testDiscovery() {
		logger.info("Calling /openid/user/phil and expecting a valid answer");
		expect().statusCode(200).when().get("/openid/user/phil");
	}
}