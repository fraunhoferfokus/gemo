package de.fhg.fokus.cm.webapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;

import de.fhg.fokus.cm.ejb.Address;
import de.fhg.fokus.cm.ejb.Category;
import de.fhg.fokus.cm.ejb.Comment;
import de.fhg.fokus.cm.ejb.CommentRequest;
import de.fhg.fokus.cm.ejb.Comments;
import de.fhg.fokus.cm.ejb.Complaint;
import de.fhg.fokus.cm.ejb.ComplaintRequest;
import de.fhg.fokus.cm.ejb.Complaints;
import de.fhg.fokus.cm.ejb.Geolocation;
import de.fhg.fokus.cm.ejb.Photo;
import de.fhg.fokus.cm.ejb.Photos;
import de.fhg.fokus.cm.ejb.Rating;
import de.fhg.fokus.cm.ejb.RatingRequest;
import de.fhg.fokus.cm.ejb.Ratings;
import de.fhg.fokus.cm.ejb.Status;
import de.fhg.fokus.cm.service.CategoryService;
import de.fhg.fokus.cm.service.ComplaintService;
import de.fhg.fokus.cm.service.StatusService;

/**
 * @author fma
 */

@Path("/complaints")
public class ComplaintResource {
	private static final String NOTIFICATION_PATH_KEY = "NotificationFilter.notificationPath";
	private static final String NOTIFICATION_URL_KEY = "NotificationFilter.notificationUrl";
	private static Integer limit = 1000;

	private Properties getProps(ServletContext servletContext) {

		Properties props = new Properties();
		try {
			if (servletContext != null) {
				InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/classes/" + "messages.properties");
				props.load(inputStream);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return props;
	}

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;
	@Context
	ServletContext servletContextByContext;
	@Resource
	ServletContext servletContextByResource;

	@Context
	HttpHeaders headers;

	private static final String TAGS_DELIMITER = ","; //$NON-NLS-1$

	static Logger logger = Logger.getLogger(ComplaintResource.class.getName());

	static final String COMLPAINTID = "complaintId"; //$NON-NLS-1$

	static final String TITLE = "title"; //$NON-NLS-1$
	static final String DESCRIPTION = "description"; //$NON-NLS-1$
	static final String TAGS = "tags"; //$NON-NLS-1$
	static final String CATEGORYID = "categoryId"; //$NON-NLS-1$
	static final String STATUSID = "statusId"; //$NON-NLS-1$
	static final String LATITUDE = "latitude"; //$NON-NLS-1$
	static final String LONGITUDE = "longitude"; //$NON-NLS-1$
	static final String COUNTRYCODE = "countryCode"; //$NON-NLS-1$
	static final String CITY = "city"; //$NON-NLS-1$
	static final String STREET = "street"; //$NON-NLS-1$
	static final String HOUSENO = "houseNo"; //$NON-NLS-1$
	static final String POSTALCODE = "postalCode"; //$NON-NLS-1$
	static final String RADIUS = "radius"; //$NON-NLS-1$
	static final String HASPHOTO = "hasPhoto"; //$NON-NLS-1$
	static final String MINRATING = "minRating"; //$NON-NLS-1$
	static final String MAXRATING = "maxRating"; //$NON-NLS-1$
	static final String LIMIT = "limit"; //$NON-NLS-1$
	static final String OFFSET = "offset"; //$NON-NLS-1$
	static final String USERID = "userId";
	static final String USER = "user";
	static final String ORDERBY = "orderBy";
	static final String ORDERDESC = "orderDesc";

	static final String COMMENTID = "commentId"; //$NON-NLS-1$
	static final String RATINGID = "ratingId"; //$NON-NLS-1$
	static final String PHOTOID = "photoId"; //$NON-NLS-1$

	static final String MESSAGE = "message"; //$NON-NLS-1$
	static final String NEWSTATUSID = "newStatusId"; //$NON-NLS-1$

	private static final String VALUE = "value"; //$NON-NLS-1$
	private static final String DEFAULT_LIMIT = "10";
	private static final String DEFAULT_OFFSET = "0";

	private final ResourceHelper rh = new ResourceHelper(logger);

	ComplaintService cms = rh.getComplaintServiceImpl(limit);
	CategoryService cat = rh.getCategoryServiceImpl();
	StatusService stat = rh.getStatusServiceImpl();

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response searchComplaints(@QueryParam(TAGS) String tagsP, @QueryParam(LATITUDE) String latitudeP,
	        @QueryParam(LONGITUDE) String longitudeP, @QueryParam(RADIUS) String radiusP, @QueryParam(CATEGORYID) String categoryIdP,
	        @QueryParam(STATUSID) String statusIdP, @QueryParam(HASPHOTO) String hasPhotoP, @QueryParam(MINRATING) Double minRating,
	        @QueryParam(MAXRATING) Double maxRating, @QueryParam(LIMIT) Integer limit, @QueryParam(OFFSET) Integer offset,
	        @QueryParam(USERID) String userIdP, @QueryParam(USER) String userP, @QueryParam(ORDERBY) String orderByP,
	        @QueryParam(ORDERDESC) String orderDescP, @Context ServletContext servletContext) {
		Complaints complaints;
		try {
			List<String> tags = null;
			if (tagsP != null) {
				String[] tagsString = tagsP.split(TAGS_DELIMITER);
				if (tagsString != null) {
					tags = new ArrayList<String>();
					for (String string : tagsString) {
						tags.add(string);
					}
				}
			}

			Double latitude = null;
			Double longitude = null;
			Double radius = null;
			Long categoryId = null;
			Long statusId = null;

			// Double minRating = null;
			// Double maxRating = null;
			// Integer limit = null;
			// Integer offset = null;
			Boolean orderDesc = null;
			Boolean hasPhoto = null;

			latitude = ResourceHelper.parseParameterD(latitudeP);
			longitude = ResourceHelper.parseParameterD(longitudeP);
			radius = ResourceHelper.parseParameterD(radiusP);
			categoryId = ResourceHelper.parseParameterL(categoryIdP);
			statusId = ResourceHelper.parseParameterL(statusIdP);
			hasPhoto = ResourceHelper.parseParameterB(hasPhotoP);
			// minRating = parseParameterD(minRatingP);
			// maxRating = parseParameterD(maxRatingP);
			// limit = parseParameterI(limitP);
			// offset = parseParameterI(offsetP);
			orderDesc = ResourceHelper.parseParameterB(orderDescP);

			complaints = cms.searchComplaints(tags, latitude, longitude, radius, categoryId, statusId, hasPhoto, minRating, maxRating,
			        limit, offset, userIdP, userP, orderByP, orderDesc);
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(complaints).build();
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addComplaint(@FormParam(TITLE) String title, @FormParam(DESCRIPTION) String description, @FormParam(TAGS) String tags,
	        @FormParam(CATEGORYID) Long categoryId, @FormParam(LATITUDE) Double latitude, @FormParam(LONGITUDE) Double longitude,
	        @FormParam(COUNTRYCODE) String countryCode, @FormParam(CITY) String city, @FormParam(STREET) String street,
	        @FormParam(HOUSENO) String houseNo, @FormParam(POSTALCODE) String postalCode, @HeaderParam(USERID) String userId,
	        @Context ServletContext servletContext) {

		Complaint c = new Complaint();
		Address a = new Address();
		a.setCity(city);
		a.setCountryCode(countryCode);
		a.setHouseNo(houseNo);
		a.setPostalCode(postalCode);
		a.setStreet(street);
		Geolocation g = new Geolocation();
		g.setLatitude(latitude);
		g.setLongitude(longitude);
		c.setAddress(a);
		c.setGeolocation(g);
		c.setTags(tags);
		c.setUserId(userId);
		c.setDescription(description);
		c.setTitle(title);

		Category category = null;
		try {
			category = cat.getCategory(categoryId);

		} catch (Exception e) {
			logger.warn("cannot parse category value [" + categoryId + "] " + e.getMessage() + " trying to set default category");
			try {
				category = cat.getCategory(Long.parseLong(getProps(servletContext).getProperty(
				        "ComplaintResource.defaultComplaintCategoryId")));
			} catch (Exception e1) {
				logger.error("cannot parse category value [" + categoryId + "] " + e1.getMessage());
				category = null;
			}
		}

		try {
			if (category != null) {
				c = cms.addComplaint(c);
				c.setCategory(category);
				Status s = stat.getStatus(Long.parseLong(getProps(servletContext)
				        .getProperty("ComplaintResource.inititalComplaintStatusId")));
				c.setStatus(s); //$NON-NLS-1$
				// Comment comment = new Comment();
				// comment.setUserId(userId);
				// comment.setNewStatus(s);
				// comment.setMessage("Status update: Complaint has been successfully created");
				// if (c.getComments() == null) {
				// c.setComments(new Complaint.Comments());
				// }
				// c.getComments().getComments().add(comment);
				c = cms.updateComplaint(c);
				notify(c.getId(), servletContext);
			}
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.CREATED).entity(c).build();
	}

	// @POST
	// @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// public Response addComplaint(ComplaintRequest cr,
	// @HeaderParam(USERID) String userId,
	// @Context ServletContext servletContext) {
	//
	// Complaint c = new Complaint();
	// Status s = null;
	// try {
	// c.setAddress(cr.getAddress());
	// c.setCategory(cat.getCategory(Long.parseLong(cr.getCategoryId())));
	// c.setDescription(cr.getDescription());
	// c.setGeolocation(cr.getGeolocation());
	// s = stat.getStatus(Long.parseLong(cr.getStatusId()));
	// c.setStatus(s);
	// c.setTags(cr.getTags());
	// c.setTitle(cr.getTitle());
	// c.setUserId(userId);
	//
	// Comment comment = new Comment();
	// comment.setUserId(userId);
	// comment.setNewStatus(s);
	// comment.setMessage("Status update: Complaint has been successfully created");
	// if (c.getComments() == null) {
	// c.setComments(new Complaint.Comments());
	// }
	// c.getComments().getComments().add(comment);
	// c = cms.updateComplaint(c);
	// notify(c.getId(), servletContext);
	// } catch (Exception e) {
	// return Response.status(Response.Status.BAD_REQUEST)
	// .entity(ResourceHelper.buildError(e)).build();
	// }
	// return Response.status(Response.Status.CREATED).entity(c).build();
	// }

	@Path("{" + COMLPAINTID + "}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getComplaint(@PathParam(COMLPAINTID) Long complaintId) {
		Complaint c = null;
		try {
			c = cms.getComplaintBase(complaintId);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(c).build();
	}

	@Path("{" + COMLPAINTID + "}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteComplaint(@PathParam(COMLPAINTID) Long complaintId, @Context ServletContext servletContext) {
		Complaint c = null;
		try {
			c = cms.deleteComplaint(complaintId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		notify(complaintId, servletContext);
		return Response.status(Response.Status.OK).entity(c).build();

	}

	@Path("{" + COMLPAINTID + "}")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateComplaint(@PathParam(COMLPAINTID) Long complaintId, @FormParam(TITLE) String title,
	        @FormParam(DESCRIPTION) String description, @FormParam(TAGS) String tags, @FormParam(CATEGORYID) Long categoryId,
	        @FormParam(STATUSID) Long statusId, @FormParam(LATITUDE) Double latitude, @FormParam(LONGITUDE) Double longitude,
	        @FormParam(COUNTRYCODE) String countryCode, @FormParam(CITY) String city, @FormParam(STREET) String street,
	        @FormParam(HOUSENO) String houseNo, @FormParam(POSTALCODE) String postalCode, @FormParam(USERID) String userId,
	        @HeaderParam(USERID) String headerUserId, @Context ServletContext servletContext) {
		Complaint c = null;
		try {
			logger.debug("updateComplaint(" + "','" + complaintId + "','" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			        + title + "','" + description + "','" + tags + "','" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			        + categoryId + "','" + statusId + "','" + latitude + "','" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			        + longitude + "','" + countryCode + "','" + city + "','" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			        + street + "','" + houseNo + "','" + postalCode + "')"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			c = cms.getComplaint(complaintId);
			Address a = c.getAddress();
			Status oldStatus = c.getStatus();
			Status newStatus = stat.getStatus(statusId);
			a.setCity(getContent(city, a.getCity()));
			a.setCountryCode(getContent(countryCode, a.getCountryCode()));
			a.setHouseNo(getContent(houseNo, a.getHouseNo()));
			a.setPostalCode(getContent(postalCode, a.getPostalCode()));
			a.setStreet(getContent(street, a.getStreet()));
			Geolocation g = c.getGeolocation();
			g.setLatitude(latitude);
			g.setLongitude(longitude);
			c.setTags(tags);
			c.setDescription(description);
			c.setTitle(title);
			c.setUserId(userId);
			c.setCategory(cat.getCategory(categoryId));
			c.setStatus(stat.getStatus(statusId));
			c.setAddress(a);
			c.setGeolocation(g);

			Comment comment = new Comment();
			comment.setUserId(headerUserId);
			comment.setNewStatus(newStatus);
			comment.setMessage("Status update: The status of the complaint has been changed from '" + oldStatus.getTitle() + "' into '"
			        + newStatus.getTitle() + "'");

			if (c.getComments() == null) {
				c.setComments(new Complaint.Comments());
			}
			c.getComments().getComments().add(comment);
			c = cms.updateComplaint(c);
			notify(c.getId(), servletContext);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(c).build();
	}

	@Path("{" + COMLPAINTID + "}")
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateComplaint(@PathParam(COMLPAINTID) Long complaintId, ComplaintRequest cr, @Context ServletContext servletContext) {
		Complaint complaint = null;
		try {
			complaint = cms.getComplaint(complaintId);
			Status oldStatus = complaint.getStatus();
			Status newStatus = stat.getStatus(Long.parseLong(cr.getStatusId()));
			complaint.setAddress(cr.getAddress());
			complaint.setCategory(cat.getCategory(Long.parseLong(cr.getCategoryId())));
			complaint.setDescription(cr.getDescription());
			complaint.setGeolocation(cr.getGeolocation());
			complaint.setStatus(newStatus);
			complaint.setTags(cr.getTags());
			complaint.setTitle(cr.getTitle());

			Comment comment = new Comment();
			comment.setUserId(complaint.getUserId());
			comment.setNewStatus(newStatus);
			comment.setMessage("Status update: The status of the complaint has been changed from '" + oldStatus.getTitle() + "' into '"
			        + newStatus.getTitle() + "'");

			if (complaint.getComments() == null) {
				complaint.setComments(new Complaint.Comments());
			}
			complaint.getComments().getComments().add(comment);
			complaint = cms.updateComplaint(complaint);
			notify(complaint.getId(), servletContext);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}

		return Response.status(Response.Status.OK).entity(complaint).build();
	}

	@Path("{" + COMLPAINTID + "}/comments")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getComplaintComments(@PathParam(COMLPAINTID) Long complaintId,
	        @DefaultValue(DEFAULT_LIMIT) @QueryParam(LIMIT) Integer limit,
	        @DefaultValue(DEFAULT_OFFSET) @QueryParam(OFFSET) Integer offset, @QueryParam(USERID) String userId,
	        @QueryParam(ORDERBY) String orderByP, @QueryParam(ORDERDESC) String orderDescP) {
		Comments c;

		try {
			c = cms.getComments(complaintId, limit, offset, userId);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(c).build();
	}

	@Path("{" + COMLPAINTID + "}/comments")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addComplaintComment(@HeaderParam(USERID) String userId, @PathParam(COMLPAINTID) Long complaintId,
	        @FormParam(MESSAGE) String message, @FormParam(NEWSTATUSID) Long newStatusId) {
		Comment comment = new Comment();
		try {
			comment = new Comment();
			comment.setMessage(message);
			if (newStatusId != null) {
				comment.setNewStatus(stat.getStatus(newStatusId));
			}
			comment.setUserId(userId);
			comment = cms.addComment(complaintId, comment);
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.CREATED).entity(comment).build();
	}

	@Path("{" + COMLPAINTID + "}/comments")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addComplaintComment(@HeaderParam(USERID) String userId, @PathParam(COMLPAINTID) Long complaintId, CommentRequest cr) {
		Comment comment = null;
		try {
			comment = new Comment();
			comment.setMessage(cr.getMessage());
			if (cr.getNewStatusId() != null) {
				comment.setNewStatus(stat.getStatus(Long.parseLong(cr.getNewStatusId())));
			}
			comment.setUserId(userId);
			comment = cms.addComment(complaintId, comment);
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.CREATED).entity(comment).build();
	}

	@Path("{" + COMLPAINTID + "}/comments/{" + COMMENTID + "}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getComplaintComment(@PathParam(COMLPAINTID) Long complaintId, @PathParam(COMMENTID) Long commentId) {
		Comment c;
		try {
			c = cms.getComplaintComment(complaintId, commentId);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(c).build();
	}

	@Path("{" + COMLPAINTID + "}/comments/{" + COMMENTID + "}")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateComplaintComment(@PathParam(COMLPAINTID) Long complaintId, @PathParam(COMMENTID) Long commentId, CommentRequest cr) {
		Comment comment = null;
		try {
			comment = cms.getComplaintComment(complaintId, commentId);
			comment.setMessage(cr.getMessage());
			if (cr.getNewStatusId() != null) {
				comment.setNewStatus(stat.getStatus(Long.parseLong(cr.getNewStatusId())));
			}
			comment = cms.updateComment(complaintId, comment);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(comment).build();
	}

	@Path("{" + COMLPAINTID + "}/comments/{" + COMMENTID + "}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteComplaintComment(@PathParam(COMLPAINTID) Long complaintId, @PathParam(COMMENTID) Long commentId) {
		Comment o = null;
		try {
			o = cms.deleteComplaintComment(complaintId, commentId);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(o).build();
	}

	@Path("{" + COMLPAINTID + "}/ratings")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getComplaintRatings(@PathParam(COMLPAINTID) Long complaintId,
	        @DefaultValue(DEFAULT_LIMIT) @QueryParam(LIMIT) Integer limit,
	        @DefaultValue(DEFAULT_OFFSET) @QueryParam(OFFSET) Integer offset, @QueryParam(USERID) String userId) {
		Ratings r;
		try {
			r = cms.getRatings(complaintId, limit, offset, userId);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(r).build();
	}

	@Path("{" + COMLPAINTID + "}/ratings")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addComplaintRating(@HeaderParam(USERID) String userId, @PathParam(COMLPAINTID) Long complaintId,
	        @FormParam(VALUE) String value) {
		Rating r = null;
		try {
			r = new Rating();
			r.setValue(Integer.parseInt(value));
			r.setUserId(userId);
			r = cms.addRating(complaintId, r);
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.CREATED).entity(r).build();
	}

	@Path("{" + COMLPAINTID + "}/ratings")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addComplaintRating(@HeaderParam(USERID) String userId, @PathParam(COMLPAINTID) Long complaintId, RatingRequest rr) {
		Rating r = null;
		try {
			r = new Rating();
			r.setValue(rr.getValue());
			r.setUserId(userId);
			r = cms.addRating(complaintId, r);
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.CREATED).entity(r).build();
	}

	@Path("{" + COMLPAINTID + "}/ratings/{" + RATINGID + "}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getComplaintRating(@PathParam(COMLPAINTID) Long complaintId, @PathParam(RATINGID) Long ratingId) {
		Rating r;
		try {
			r = cms.getComplaintRating(complaintId, ratingId);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(r).build();
	}

	@Path("{" + COMLPAINTID + "}/ratings/{" + RATINGID + "}")
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateComplaintRating(@PathParam(COMLPAINTID) Long complaintId, @PathParam(RATINGID) Long ratingId, RatingRequest rr) {
		Rating r = null;
		try {
			r = cms.getComplaintRating(complaintId, ratingId);
			r.setValue(rr.getValue());
			r = cms.updateRating(complaintId, r);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(r).build();
	}

	@Path("{" + COMLPAINTID + "}/ratings/{" + RATINGID + "}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteComplaintRating(@PathParam(COMLPAINTID) Long complaintId, @PathParam(RATINGID) Long ratingId) {
		Rating r = null;
		try {
			r = cms.deleteComplaintRating(complaintId, ratingId);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(r).build();
	}

	@Path("{" + COMLPAINTID + "}/photos")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getComplaintPhotos(

	@PathParam(COMLPAINTID) Long complaintId, @DefaultValue(DEFAULT_LIMIT) @QueryParam(LIMIT) Integer limit,
	        @DefaultValue(DEFAULT_OFFSET) @QueryParam(OFFSET) Integer offset, @QueryParam(USERID) String userId) {
		Photos p = null;
		try {
			p = cms.getPhotos(complaintId, limit, offset, userId);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(p).build();

	}

	@Path("{" + COMLPAINTID + "}/photos/{" + PHOTOID + "}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPhoto(@PathParam(COMLPAINTID) Long complaintId, @PathParam(PHOTOID) Long photoId) {
		Photo p = null;
		try {
			p = cms.getComplaintPhoto(complaintId, photoId);
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(p).build();
	}

	@Path("{" + COMLPAINTID + "}/photos")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addComplaintPhoto(@HeaderParam(USERID) String userId, @PathParam(COMLPAINTID) Long complaintId,
	        @FormDataParam(value = "file") FormDataMultiPart formData, @Context ServletContext servletContext) {
		Photo ph = null;
		try {
			String fileName = persistPhoto(formData);
			ph = new Photo();
			ph.setUrl(fileName);
			ph.setCreationTimeItem(new Date());
			ph.setUserId(userId); //$NON-NLS-1$

			ph = cms.addPhoto(complaintId, ph);
			this.notify(complaintId, servletContext);
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.CREATED).entity(ph).build();
	}

	@Path("{" + COMLPAINTID + "}/photos/{" + PHOTOID + "}")
	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateComplaintPhoto(@PathParam(COMLPAINTID) Long complaintId, @PathParam(PHOTOID) Long photoId,
	        @FormDataParam(value = "file") FormDataMultiPart formData, @Context ServletContext servletContext) {
		Photo ph = null;
		try {
			String fileName = persistPhoto(formData);
			ph = cms.getComplaintPhoto(complaintId, photoId);
			removePhoto(ph.getUrl());

			ph.setUrl(fileName);
			ph.setCreationTimeItem(new Date());
			ph.setId(photoId);
			ph = cms.updatePhoto(complaintId, ph);

		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		this.notify(complaintId, servletContext);
		return Response.status(Response.Status.OK).entity(ph).build();
	}

	@Path("{" + COMLPAINTID + "}/photos/{" + PHOTOID + "}")
	@DELETE
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteComplaintPhoto(@PathParam(COMLPAINTID) Long complaintId, @PathParam(PHOTOID) Long photoId) {
		Photo o = null;
		try {
			o = cms.deleteComplaintPhoto(complaintId, photoId);
			logger.debug(o.getClass().getSimpleName() + " [" + o.getId() //$NON-NLS-1$
			        + "] deleted"); //$NON-NLS-1$
			removePhoto(o.getUrl());
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(ResourceHelper.buildError(e)).build();
		}
		return Response.status(Response.Status.OK).entity(o).build();
	}

	private void removePhoto(String url) {
		WebResource service = RestHelper.getWebResource("http://localhost:8080/photostorage/rest");
		service.path(url).delete(ClientResponse.class);
	}

	private String persistPhoto(FormDataMultiPart formData) throws IOException {
		WebResource service = RestHelper.getWebResource("http://localhost:8080/photostorage/rest");
		ClientResponse clientResponse = service.type(MediaType.MULTIPART_FORM_DATA).post(ClientResponse.class, formData);
		return clientResponse.getEntity(String.class);
	}

	private String getContent(String newString, String oldString) {
		if (newString != null && !newString.equals("")) //$NON-NLS-1$
			return newString;
		else
			return oldString;
	}

	private void notify(Long complaintId, ServletContext servletContext) {
		try {
			String notificationUrl = (String) getProps(servletContext).get(NOTIFICATION_URL_KEY);
			String notificationPath = (String) getProps(servletContext).get(NOTIFICATION_PATH_KEY);

			if (notificationUrl != null && notificationPath != null) {

				WebResource service = RestHelper.getWebResource(notificationUrl);

				ClientResponse clientResponse = service.path(notificationPath).queryParam("complaintId", complaintId.toString())
				        .get(ClientResponse.class);

				logger.info(clientResponse);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
}