package de.fhg.fokus.oauth.endpoints;

/**
 *       Copyright 2010 Newcastle University
 *
 *          http://research.ncl.ac.uk/smart/
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import de.fhg.fokus.IntegrationTest;

@Category(IntegrationTest.class)
public class ClientRegistrationTest {
	static Logger logger = Logger.getLogger(ClientRegistrationTest.class
			.getName());
	private static String jsonString = "{'type':'push','client_name':'asdf','redirect_url':'http://localhost:8080/openid/redirect','client_description':'Description is important','client_url':'http://www.google.com'}";

	@Test
	public void testPushMetadataRegistration() {
		logger.info("Calling /openid/register");
		given().contentType("application/json").body(jsonString).expect()
				.statusCode(200).contentType("application/json")
				.body("expires_in", equalTo(-1))
				.body("client_id", is(notNullValue()))
				.body("client_secret", is(notNullValue()))
				.body("issued_at", is(notNullValue())).when()
				.post("/openid/register");
	}

	public void testInvalidType() throws Exception {
		logger.info("Calling /openid/register");
		given().contentType("application/json").body(jsonString).expect()
				.statusCode(400).contentType("application/json")
				.post("/openid/register");
	}
}
