package de.fhg.fokus.cm.webapp;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import de.fhg.fokus.cm.ejb.Status;
import de.fhg.fokus.cm.ejb.Statuses;
import de.fhg.fokus.cm.service.StatusService;

@Path("/states")
public class StatusesResource {
	static Logger logger = Logger.getLogger(ComplaintResource.class.getName());
	private ResourceHelper rh = new ResourceHelper(logger);

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStatuses(
			@QueryParam(ResourceHelper.LIMIT) Integer limit,
			@QueryParam(ResourceHelper.OFFSET) Integer offset) {
		StatusService cms = rh.getStatusServiceImpl();
		Statuses s = null;
		try {
			s = cms.getStatuses(limit, offset);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		return Response.status(javax.ws.rs.core.Response.Status.OK).entity(s)
				.build();
	}

	// @PUT TODO
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Status addStatusUrlencoded(
			@FormParam(ResourceHelper.STATUS_TITLE) String title,
			@FormParam(ResourceHelper.STATUS_DESCRIPTION) String description) {

		StatusService cms = rh.getStatusServiceImpl();
		Status status = new Status();
		status.setTitle(title);
		status.setDescription(description);
		try {
			cms.addStatus(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Status updateStatus(
			@PathParam(ResourceHelper.STATUS_ID) String statusId, Status status) {
		StatusService cms = rh.getStatusServiceImpl();
		try {
			status.setId(Long.valueOf(statusId));
			return cms.updateStatus(status);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		return null;
	}

}
