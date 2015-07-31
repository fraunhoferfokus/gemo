package de.fhg.fokus.openid.registration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import de.fhg.fokus.IntegrationTest;

@Category(IntegrationTest.class)
public class UserRegistrationServiceTest {
	static final String adminUserName = "asdfasdf2";
	static final String invalidUserName = "";
	static final String testUserName = "testuser01";

	@Test
	public void testCheckUser() {
		given().contentType("application/x-www-form-urlencoded")
				.param("username", adminUserName).expect().statusCode(200)
				.contentType("application/json").body(containsString("true"))
				.post("http://localhost:8080/openid/reg/check_user");
	}

	@Test
	public void testCheckUserWithEmpty() {
		given().contentType("application/x-www-form-urlencoded")
				.param("username", "").expect().statusCode(200)
				.contentType("application/json").body(containsString("false"))
				.post("http://localhost:8080/openid/reg/check_user");
	}

	@Test
	public void testRegisterUser() {
		Response resp = given()
				.contentType("application/x-www-form-urlencoded")
				.param("username", adminUserName)
				.post("http://localhost:8080/openid/reg/check_user");

		boolean exists = Boolean.valueOf(resp.getBody().asString());

		assumeTrue(!exists);

		given().contentType("application/x-www-form-urlencoded")
				.param("username", adminUserName).param("password", "pass")
				.param("email", "asdf@de.de").expect().statusCode(200)
				.contentType(ContentType.HTML).body(is(notNullValue()))
				.post("http://localhost:8080/openid/reg/register");
	}
}