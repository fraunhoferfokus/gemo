/**
 * 
 */
package de.fhg.fokus.cm.webapp;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import de.fhg.fokus.cm.ejb.Status;
import de.fhg.fokus.cm.ejb.Statuses;
import de.fhg.fokus.cm.service.StatusService;

/**
 * @author Fabian Manzke
 * 
 */
@Path("/statuses")
public class StatusResource {

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
			cms.addOrUpdateStatus(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Status addStatus(Status status) {

		StatusService cms = rh.getStatusServiceImpl();
		try {
			return cms.addOrUpdateStatus(status);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		return null;
	}

	@Path("{" + ResourceHelper.STATUS_ID + "}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStatus(@PathParam(ResourceHelper.STATUS_ID) Long statusId) {
		StatusService cms = rh.getStatusServiceImpl();
		Status s = null;
		try {
			s = cms.getStatus(statusId);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		if (s == null)
			throw new WebApplicationException(Response.status(
					javax.ws.rs.core.Response.Status.NOT_FOUND).build());
		return Response.status(javax.ws.rs.core.Response.Status.OK).entity(s)
				.build();
	}

	@Path("{" + ResourceHelper.STATUS_ID + "}")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Status updateStatusUrlencoded(
			@PathParam(ResourceHelper.STATUS_ID) String statusId,
			@FormParam(ResourceHelper.STATUS_TITLE) String title,
			@FormParam(ResourceHelper.STATUS_DESCRIPTION) String description) {
		StatusService cms = rh.getStatusServiceImpl();
		Status status = new Status();
		// status.setId(Integer.parseInt(statusId));
		status.setTitle(title);
		status.setDescription(description);
		try {
			return cms.addOrUpdateStatus(status);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		return null;
	}

	@Path("{" + ResourceHelper.STATUS_ID + "}")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Status updateStatus(
			@PathParam(ResourceHelper.STATUS_ID) String statusId, Status status) {
		StatusService cms = rh.getStatusServiceImpl();
		try {
			// status.setId(Integer.parseInt(statusId));
			return cms.addOrUpdateStatus(status);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		return null;
	}

	@Path("{" + ResourceHelper.STATUS_ID + "}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Status deleteStatus(
			@PathParam(ResourceHelper.STATUS_ID) Long statusId) {
		StatusService cms = rh.getStatusServiceImpl();
		try {
			return cms.deleteStatus(statusId);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		return null;
	}
}