package de.fhg.fokus.mdc.userprofile.tests;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class UserProfileIntegrationTest {
	@Test
	public void addUserRequest() {
		String path = "http://localhost:8080/userprofile/add_user";
		Client client = new Client();
		WebResource webResource = client.resource(path);

		MultivaluedMapImpl map = new MultivaluedMapImpl();
		map.add("Vorname", "dummy");
		map.add("Nachname", "dummy");
		map.add("Nutzername", "dummy");
		map.add("Passwort", "dummy");
		map.add("Email", "dummy");
		map.add("Geburtsdatum", "dummy");
		map.add("Strasse", "dummy");
		map.add("Hausnummer", "dummy");
		map.add("PLZ", "dummy");
		map.add("Ort", "dummy");
		map.add("Telefonnummer", "dummy");
		map.add("Kontoinhaber", "dummy");
		map.add("Kontonummer", "dummy");
		map.add("Bankleitzahl", "dummy");
		map.add("Praeferenzen", "dummy");
		map.add("Fahrzeugtyp", "dummy");
		map.add("OEPNV_Affinitaet", "dummy");
		map.add("Fuehrerschein_Klasse", "dummy");
		map.add("Fuehrerschein_Datum", "dummy");
		map.add("Fuehrerschein_Ort", "dummy");
		map.add("Fuehrerschein_Nr", "dummy");
		map.add("Sprache", "dummy");
		map.add("Kreditkarten_Nr", "dummy");
		map.add("AGB_akzeptiert_Datum", "dummy");

		ClientResponse response = webResource
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("accessToken", "39393939kkk000")
				.header("scope", "write").header("username", "developer")
				.post(ClientResponse.class, map);
		System.out.println("response : " + response.getEntity(String.class));
		assertEquals(200, response.getClientResponseStatus().getStatusCode());
	}

	@Test
	public void updateUserRequest() {
		String path = "http://localhost:8080/userprofile/update_user_details";
		Client client = new Client();
		WebResource webResource = client.resource(path);

		MultivaluedMapImpl map = new MultivaluedMapImpl();
		map.add("Vorname", "dummyUpdated");
		map.add("Nachname", "dummyUpdated");
		// nutzername is used to find which record will be updated
		map.add("Nutzername", "dummy");
		map.add("Passwort", "dummyUpdated");
		map.add("Email", "dummyUpdated");
		map.add("Geburtsdatum", "dummyUpdated");
		map.add("Strasse", "dummyUpdated");
		map.add("Hausnummer", "dummyUpdated");
		map.add("PLZ", "dummyUpdated");
		map.add("Ort", "dummyUpdated");
		map.add("Telefonnummer", "dummyUpdated");
		map.add("Kontoinhaber", "dummyUpdated");
		map.add("Kontonummer", "dummyUpdated");
		map.add("Bankleitzahl", "dummyUpdated");
		map.add("Praeferenzen", "dummyUpdated");
		map.add("Fahrzeugtyp", "dummyUpdated");
		map.add("OEPNV_Affinitaet", "dummyUpdated");
		map.add("Fuehrerschein_Klasse", "dummyUpdated");
		map.add("Fuehrerschein_Datum", "dummyUpdated");
		map.add("Fuehrerschein_Ort", "dummyUpdated");
		map.add("Fuehrerschein_Nr", "dummyUpdated");
		map.add("Sprache", "dummyUpdated");
		map.add("Kreditkarten_Nr", "dummyUpdated");
		map.add("AGB_akzeptiert_Datum", "dummyUpdated");

		ClientResponse response = webResource
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("accessToken", "39393939kkk000")
				.header("scope", "write").header("username", "developer")
				.post(ClientResponse.class, map);
		System.out.println("response : " + response.getEntity(String.class));
		assertEquals(200, response.getClientResponseStatus().getStatusCode());
	}

	@Test
	public void getUserRequest() {
		String path = "http://localhost:8080/userprofile/get_user_details";
		Client client = new Client();
		WebResource webResource = client.resource(path);
		MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
		queryParams.add("username", "Admin");
		ClientResponse response = webResource.queryParams(queryParams)
				.header("accessToken", "39393939kkk111")
				.header("scope", "read").header("username", "developer")
				.get(ClientResponse.class);
		assertEquals(200, response.getClientResponseStatus().getStatusCode());
	}

	@Test
	public void addUserRequestConflict() {
		String path = "http://localhost:8080/userprofile/add_user";
		Client client = new Client();
		WebResource webResource = client.resource(path);

		MultivaluedMapImpl map = new MultivaluedMapImpl();
		map.add("Vorname", "dummy");
		map.add("Nachname", "dummy");
		map.add("Nutzername", "dummy");
		map.add("Passwort", "dummy");
		map.add("Email", "dummy");
		map.add("Geburtsdatum", "dummy");
		map.add("Strasse", "dummy");
		map.add("Hausnummer", "dummy");
		map.add("PLZ", "dummy");
		map.add("Ort", "dummy");
		map.add("Telefonnummer", "dummy");
		map.add("Kontoinhaber", "dummy");
		map.add("Kontonummer", "dummy");
		map.add("Bankleitzahl", "dummy");
		map.add("Praeferenzen", "dummy");
		map.add("Fahrzeugtyp", "dummy");
		map.add("OEPNV_Affinitaet", "dummy");
		map.add("Fuehrerschein_Klasse", "dummy");
		map.add("Fuehrerschein_Datum", "dummy");
		map.add("Fuehrerschein_Ort", "dummy");
		map.add("Fuehrerschein_Nr", "dummy");
		map.add("Sprache", "dummy");
		map.add("Kreditkarten_Nr", "dummy");
		map.add("AGB_akzeptiert_Datum", "dummy");

		ClientResponse response = webResource
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.header("accessToken", "39393939kkk000")
				.header("scope", "write").header("username", "developer")
				.post(ClientResponse.class, map);
		System.out.println("response : " + response.getEntity(String.class));
		assertEquals(409, response.getClientResponseStatus().getStatusCode());
	}

}
