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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import de.fhg.fokus.cm.ejb.Categories;
import de.fhg.fokus.cm.ejb.Category;
import de.fhg.fokus.cm.service.CategoryService;

/**
 * @author Fabian Manzke
 * 
 */
@Path("/categories")
public class CategoryResource {

	static Logger logger = Logger.getLogger(CategoryResource.class.getName());
	private final ResourceHelper rh = new ResourceHelper(logger);

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getCategories(@QueryParam(ResourceHelper.LIMIT) Integer limit, @QueryParam(ResourceHelper.OFFSET) Integer offset) {
		CategoryService cms = rh.getCategoryServiceImpl();
		Categories c = null;
		try {
			c = cms.getCategories(limit, offset);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		return Response.status(Status.OK).entity(c).build();
	}

	// @POST
	// @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	// public Response addCategoryUrlencoded(
	// @FormParam(ResourceHelper.CATEGORY_TITLE) String title,
	// @FormParam(ResourceHelper.CATEGORY_DESCRIPTION) String description) {
	// CategoryService cms = rh.getCategoryServiceImpl();
	// Category category = new Category();
	// Category newCategory = null;
	// category.setTitle(title);
	// category.setDescription(description);
	// try {
	// newCategory = cms.addOrUpdateCategory(category);
	// } catch (Exception e) {
	// rh.throwWebapplicationException(e, 400);
	// }
	// if (newCategory == null)
	// throw new WebApplicationException(Response.status(
	// Status.INTERNAL_SERVER_ERROR).build());
	// return Response.status(Status.CREATED).entity(newCategory).build();
	//
	// }

	// @PUT TODO
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addCategoryUrlencoded(@FormParam(ResourceHelper.CATEGORY_TITLE) String title,
	        @FormParam(ResourceHelper.CATEGORY_DESCRIPTION) String description) {
		CategoryService cms = rh.getCategoryServiceImpl();
		Category category = new Category();
		Category newCategory = null;
		category.setTitle(title);
		category.setDescription(description);
		try {
			newCategory = cms.addCategory(category);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		if (newCategory == null)
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).build());
		ResponseBuilder status = Response.status(Status.CREATED);
		ResponseBuilder entity = status.entity(newCategory);
		Response response = entity.build();
		return response;

	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addCategory(Category category) {
		CategoryService cms = rh.getCategoryServiceImpl();
		Category newCategory = null;
		try {
			newCategory = cms.addCategory(category);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		if (newCategory == null)
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).build());
		return Response.status(Status.CREATED).entity(newCategory).build();
	}

	@Path("{" + ResourceHelper.CATEGORY_ID + "}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getCategory(@PathParam(ResourceHelper.CATEGORY_ID) Long categoryId) {
		CategoryService cms = rh.getCategoryServiceImpl();
		Category c = null;
		try {
			c = cms.getCategory(categoryId);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		if (c == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).build());
		return Response.status(Status.OK).entity(c).build();

	}

	@Path("{" + ResourceHelper.CATEGORY_ID + "}")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Category updateCategoryUrlencoded(@PathParam(ResourceHelper.CATEGORY_ID) Long statusId,
	        @FormParam(ResourceHelper.CATEGORY_TITLE) String title, @FormParam(ResourceHelper.CATEGORY_DESCRIPTION) String description) {
		CategoryService cms = rh.getCategoryServiceImpl();
		Category status = new Category();
		status.setId(statusId);
		status.setTitle(title);
		status.setDescription(description);
		try {
			return cms.addOrUpdateCategory(status);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		return null;
	}

	@Path("{" + ResourceHelper.CATEGORY_ID + "}")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Category updateCategory(@PathParam(ResourceHelper.CATEGORY_ID) Long statusId, Category status) {
		CategoryService cms = rh.getCategoryServiceImpl();
		status.setId(statusId);
		try {
			return cms.addOrUpdateCategory(status);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		return null;
	}

	@Path("{" + ResourceHelper.CATEGORY_ID + "}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Category deleteCategory(@PathParam(ResourceHelper.CATEGORY_ID) Long statusId) {
		CategoryService cms = rh.getCategoryServiceImpl();
		try {
			return cms.deleteCategory(statusId);
		} catch (Exception e) {
			rh.throwWebapplicationException(e, 400);
		}
		return null;
	}
}
