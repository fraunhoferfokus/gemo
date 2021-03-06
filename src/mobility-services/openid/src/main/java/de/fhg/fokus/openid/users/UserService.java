package de.fhg.fokus.openid.users;

import java.io.File;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserService {

	@Context
	ServletContext context;

	@GET
	@Path("{userId}")
	@Produces("application/xrds+xml")
	public Response discovery(@PathParam("userId") String userId) {
		return Response.ok()
				.entity(new File(context.getRealPath("WEB-INF/classes/ud")))
				.build();
	}
}
