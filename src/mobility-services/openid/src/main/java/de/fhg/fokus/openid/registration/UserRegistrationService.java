package de.fhg.fokus.openid.registration;

import java.io.IOException;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.view.Viewable;

import de.fhg.fokus.data.User;
import de.fhg.fokus.data.UserDAO;
import de.fhg.fokus.utils.BCrypt;

@Path("/reg")
@ManagedBean
public class UserRegistrationService {
	@Inject
	UserDAO users;
	@Context
	ServletContext context;

	@GET
	@Path("registration_form")
	@Produces(MediaType.TEXT_HTML)
	public Response redirectToRegistrationForm() {
		Viewable view = new Viewable("/registration.jsp", null);
		return Response.ok().entity(view).build();
	}

	@POST
	@Path("register")
	@Produces(MediaType.TEXT_HTML)
	public Response registerUser(MultivaluedMap<String, String> params) {
		String username = params.getFirst("username");
		// if (username == null || database.doesUserExist(username)
		// || username.isEmpty() || username.contains("/")) {
		if (username == null || users.doesUserExist(new User(username))
				|| username.isEmpty() || username.contains("/")) {
			return Response.status(Status.BAD_REQUEST)
					.entity(new Viewable("/error.jsp")).build();
		}

		User toCreate = new User(params.getFirst("firstname"),
				params.getFirst("lastname"), params.getFirst("username"),
				BCrypt.hashpw(params.getFirst("password"), BCrypt.gensalt()),
				params.getFirst("email"), params.getFirst("birthdate"),
				params.getFirst("street"), params.getFirst("housenr"),
				params.getFirst("postalcode"), params.getFirst("location"),
				params.getFirst("phonenumber"),
				params.getFirst("bankaccountowner"),
				params.getFirst("bankaccountnumber"),
				params.getFirst("bankcode"), params.getFirst("preferences"),
				params.getFirst("vehicletype"),
				params.getFirst("publictransportaffinity"),
				params.getFirst("drivinglicenseclass"),
				params.getFirst("drivinglicensedate"),
				params.getFirst("drivinglicenselocation"),
				params.getFirst("drivinglicenseid"), params.getFirst("lang"),
				params.getFirst("creditcardnumber"),
				params.getFirst("termsandconditionsdate"));

		users.saveUser(toCreate);

		return Response.ok().entity(new Viewable("/success.jsp")).build();
	}

	@POST
	@Path("check_user")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUser(MultivaluedMap<String, String> params) {
		boolean result = users.doesUserExist(new User(params
				.getFirst("username")));
		try {
			return Response.ok()
					.entity(new ObjectMapper().writeValueAsString(result))
					.build();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}