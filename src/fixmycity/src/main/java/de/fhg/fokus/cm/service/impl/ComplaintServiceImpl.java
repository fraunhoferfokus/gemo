/**
 * 
 */
package de.fhg.fokus.cm.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import de.fhg.fokus.cm.ejb.Comment;
import de.fhg.fokus.cm.ejb.Comments;
import de.fhg.fokus.cm.ejb.Complaint;
import de.fhg.fokus.cm.ejb.Complaints;
import de.fhg.fokus.cm.ejb.Photo;
import de.fhg.fokus.cm.ejb.Photos;
import de.fhg.fokus.cm.ejb.Rating;
import de.fhg.fokus.cm.ejb.Ratings;
import de.fhg.fokus.cm.service.ComplaintService;

/**
 * @author Louay Bassbouss, Fabian Manzke
 * 
 */
public class ComplaintServiceImpl implements ComplaintService {

	private Double LATITUDEMIN = -90.0;
	private Double LATITUDEMAX = 90.0;

	private Double LONGITUDEMIN = -180.0;
	private Double LONGITUDEMAX = 180.0;

	private Double RADIUSMIN_EX = 0.0;
	private Double RADIUSMAX = 40.0;
	private Double RADIUSDEFAULT = 20.0;

	private Double RATINGMIN = 1.0;
	private Double RATINGMAX = 5.0;

	static Logger logger = Logger.getLogger(ComplaintServiceImpl.class
			.getName());

	private final String SELECT = "SELECT "; //$NON-NLS-1$
	private final String FROM = "FROM "; //$NON-NLS-1$
	private Integer LIMITMAX = ServiceHelper.LIMITMAX;
	private static ServiceHelper serviceHelper;

	public ComplaintServiceImpl() {

	}

	public ComplaintServiceImpl(Integer limitMax) {
		LIMITMAX = limitMax;
	}

	/**
	 * @throws Exception
	 * @see de.fhg.fokus.cm.service.ComplaintService#deleteComplaint(java.lang.String)
	 * @return null
	 */
	@Override
	public Complaint deleteComplaint(Long complaintId) throws Exception {
		EntityManager em = createEntityManager();
		Complaint complaint;
		try {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			// TODO try to cascade the delete to comments, photos and ratings
			complaint = em.find(Complaint.class, complaintId);
			complaint.getComments().getComments().clear();

			//
			// try {
			// List<Comment> list = complaint.getComments().getComments();
			// for (Comment object : list)
			// this.deleteComplaintComment(complaintId, object.getId());
			// } catch (Exception e) {
			// // no Comments, nothing to do
			// }
			// try {
			// List<Photo> list = complaint.getPhotos().getPhotos();
			// for (Photo object : list)
			// this.deleteComplaintPhoto(complaintId, object.getId());
			// } catch (Exception e) {
			// // no Photos, nothing to do
			// }
			// try {
			// List<Rating> list = complaint.getRatings().getRatings();
			// for (Rating object : list)
			// this.deleteComplaintRating(complaintId, object.getId());
			// } catch (Exception e) {
			// // no Ratings, nothing to do
			// }

			String deleteComplaint = "DELETE " + Complaint.class.getName()
					+ " c WHERE c.id = :complaintId";
			em.createQuery(deleteComplaint)
					.setParameter("complaintId", complaintId).executeUpdate();
			logger.info("complaint [" + complaintId + "] deleted"); //$NON-NLS-1$ //$NON-NLS-2$
			tx.commit();
		} finally {
			// em.close();
		}
		return null;
	}

	/**
	 * @throws Exception
	 * @see de.fhg.fokus.cm.service.ComplaintService#getComplaint(java.lang.String)
	 */
	@Override
	public Complaint getComplaint(Long complaintId) throws Exception {
		return getComplaint(complaintId, false);
	}

	/**
	 * @throws Exception
	 * @see de.fhg.fokus.cm.service.ComplaintService#getComplaint(java.lang.String)
	 */
	@Override
	public Complaint getComplaintBase(Long complaintId) throws Exception {
		return getComplaint(complaintId, true);
	}

	/**
	 * @throws Exception
	 * @see de.fhg.fokus.cm.service.ComplaintService#getComplaint(java.lang.String)
	 */
	private Complaint getComplaint(Long complaintId, boolean withoutCRP)
			throws Exception {
		final EntityManager entityManager = createEntityManager();
		final Complaint complaint = entityManager.find(Complaint.class,
				complaintId);
		logger.info("complaint [" + complaint.getId() + "] selected"); //$NON-NLS-1$ //$NON-NLS-2$
		if (withoutCRP)
			return refactorComplaint(complaint);
		return complaint;
	}

	/**
	 * @param complaintAbbr
	 * @return
	 * @throws Exception
	 */
	private String getOrderByClause(final String complaintAbbr, String orderBy,
			Boolean orderDesc) throws Exception {
		String ASC = "DESC";
		if (orderDesc == null || orderDesc == false)
			ASC = "ASC";

		if (orderBy == null || orderBy == "")
			orderBy = "id";
		Complaint c = new Complaint();
		c.getAverageRating();
		if (orderBy.equals("id") || orderBy.equals("averageRating")
				|| orderBy.equals("title") || orderBy.equals("description")
				|| orderBy.equals("userId") || orderBy.equals("tags"))

			return " ORDER BY " + complaintAbbr + "." + orderBy + " " + ASC; //$NON-NLS-1$ //$NON-NLS-2$
		else
			throw new Exception("orderBy=" + orderBy + " is not allowed");
	}

	@Override
	public Comments getComments(Long complaintId, Integer limit,
			Integer offset, String userId) throws Exception {
		offset = ServiceHelper.checkOffset(offset);
		Comments comments = new Comments();
		Complaint complaint = getComplaint(complaintId);
		List<Comment> allComments = complaint.getComments().getComments();
		List<Comment> rl = allComments;
		if (userId != null) {
			rl = new ArrayList<Comment>();
			for (Comment comment : allComments) {
				if (userId.equals(comment.getUserId())) {
					rl.add(comment);
				}
			}
		}
		Integer newLimit = ServiceHelper.calcLimit(offset, limit, rl.size());
		comments.setComments(rl.subList(offset, offset + newLimit));
		comments.setOffset(offset);
		comments.setResult(newLimit);
		comments.setTotal(rl.size());
		return comments;
	}

	@Override
	public Photo addPhoto(Long complaintId, Photo photo) throws Exception {
		final EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin();
			Complaint c = em.find(Complaint.class, complaintId);
			photo.setId(0);
			if (c.getPhotos() == null)
				c.setPhotos(new Complaint.Photos());
			c.getPhotos().getPhotos().add(photo);
			em.getTransaction().commit();

		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		} finally {
			// em.close();
		}
		return photo;
	}

	@Override
	public Comment addComment(Long complaintId, Comment comment)
			throws Exception {
		final EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin();
			Complaint c = em.find(Complaint.class, complaintId);
			comment.setId(0);
			if (c.getComments() == null)
				c.setComments(new Complaint.Comments());

			c.getComments().getComments().add(comment);
			em.getTransaction().commit();

		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		} finally {
			// em.close();
		}
		return comment;
	}

	@Override
	public Rating addRating(Long complaintId, Rating rating) throws Exception {
		final EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin();
			Complaint c = em.find(Complaint.class, complaintId);
			rating.setId(0);
			if (c.getRatings() == null)
				c.setRatings(new Complaint.Ratings());

			c.getRatings().getRatings().add(rating);
			setAvgRating(c);
			em.getTransaction().commit();
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		} finally {
			// em.close();
		}
		return rating;
	}

	@Override
	public Photo updatePhoto(Long complaintId, Photo photo) throws Exception {
		final EntityManager em = createEntityManager();
		em.getTransaction().begin();
		Complaint c = em.find(Complaint.class, complaintId);
		Photo updatePhoto = findPhoto(c, photo.getId());
		if (updatePhoto != null)
			try {
				updatePhoto = em.find(Photo.class, photo.getId());
				updatePhoto.setCreationTimeItem(photo.getCreationTimeItem());
				updatePhoto.setUrl(photo.getUrl());
				updatePhoto.setUserId(photo.getUserId());
				em.getTransaction().commit();
			} finally {
				// em.close();
			}
		return updatePhoto;
	}

	@Override
	public Comment updateComment(Long complaintId, Comment comment)
			throws Exception {
		final EntityManager em = createEntityManager();
		em.getTransaction().begin();
		Complaint c = em.find(Complaint.class, complaintId);
		Comment updateComment = findComment(c, comment.getId());
		if (updateComment != null)
			try {
				updateComment = em.find(Comment.class, comment.getId());
				updateComment
						.setCreationTimeItem(comment.getCreationTimeItem());
				updateComment.setMessage(comment.getMessage());
				updateComment.setUserId(comment.getUserId());
				updateComment.setNewStatus(comment.getNewStatus());
				em.getTransaction().commit();
			} finally {
				// em.close();
			}
		return updateComment;
	}

	@Override
	public Rating updateRating(Long complaintId, Rating rating)
			throws Exception {
		final EntityManager em = createEntityManager();
		em.getTransaction().begin();
		Complaint c = em.find(Complaint.class, complaintId);
		Rating updateRating = findRating(c, rating.getId());
		if (updateRating != null)
			try {
				updateRating = em.find(Rating.class, rating.getId());
				updateRating.setCreationTimeItem(rating.getCreationTimeItem());
				updateRating.setValue(rating.getValue());
				updateRating.setUserId(rating.getUserId());
				setAvgRating(c);
				em.getTransaction().commit();
			} finally {
				// em.close();
			}

		return updateRating;
	}

	private void setAvgRating(Complaint c) {
		Float avgRating = 0.0f;
		for (Rating r : c.getRatings().getRatings()) {
			avgRating += r.getValue();
			c.setAverageRating(avgRating / c.getRatings().getRatings().size());
		}
	}

	@Override
	public Photos getPhotos(Long complaintId, Integer limit, Integer offset,
			String userId) throws Exception {
		offset = ServiceHelper.checkOffset(offset);
		Photos photos = new Photos();
		Complaint complaint = getComplaint(complaintId);
		List<Photo> allPhotos = complaint.getPhotos().getPhotos();
		List<Photo> rl = allPhotos;
		if (userId != null) {
			rl = new ArrayList<Photo>();
			for (Photo photo : allPhotos) {
				if (userId.equals(photo.getUserId())) {
					rl.add(photo);
				}
			}
		}
		Integer newLimit = ServiceHelper.calcLimit(offset, limit, rl.size());
		photos.setPhotos(rl.subList(offset, offset + newLimit));
		photos.setOffset(offset);
		photos.setResult(newLimit);
		photos.setTotal(rl.size());
		return photos;
	}

	@Override
	public Ratings getRatings(Long complaintId, Integer limit, Integer offset,
			String userId) throws Exception {
		offset = ServiceHelper.checkOffset(offset);
		Ratings ratings = new Ratings();
		Complaint complaint = getComplaint(complaintId);
		List<Rating> allRatings = complaint.getRatings().getRatings();
		List<Rating> rl = allRatings;
		if (userId != null) {
			rl = new ArrayList<Rating>();
			for (Rating rating : allRatings) {
				if (userId.equals(rating.getUserId())) {
					rl.add(rating);
				}
			}
		}
		Integer newLimit = ServiceHelper.calcLimit(offset, limit, rl.size());
		ratings.setRatings(rl.subList(offset, offset + newLimit));
		ratings.setOffset(offset);
		ratings.setResult(newLimit);
		ratings.setTotal(rl.size());
		return ratings;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private EntityManager createEntityManager() throws Exception {
		if (serviceHelper == null)
			serviceHelper = new ServiceHelper();
		return serviceHelper.createEntityManager();

	}

	private String getTagListWhereClause(List<String> tags, String tableName,
			boolean useAND) {
		if (tags == null || tags.isEmpty())
			return ""; //$NON-NLS-1$
		StringBuffer whereClause = new StringBuffer();
		for (int i = 0; i < tags.size(); i++) {
			if (i > 0)
				if (useAND)
					whereClause.append(" AND "); //$NON-NLS-1$
				else
					whereClause.append(" OR "); //$NON-NLS-1$
			whereClause.append(tableName + ".tags LIKE " + ":tag" + i + " "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return " AND ( " + whereClause.toString() + " ) "; //$NON-NLS-1$ //$NON-NLS-2$
	}

	private String getStatusWhereClause(Long statusId, String tableName) {
		if (statusId == null)
			return ""; //$NON-NLS-1$
		String whereClause = " AND " + tableName + ".status.id = :statusId ";
		return whereClause;
	}

	private String getUserIdWhereClause(String userId, String tableName) {
		if (userId == null)
			return ""; //$NON-NLS-1$
		String whereClause = " AND " + tableName + ".userId LIKE :userId"; //$NON-NLS-1$ //$NON-NLS-2$
		return whereClause;
	}

	private String getUserWhereClause(String user, String tableName) {
		if (user == null)
			return ""; //$NON-NLS-1$
		String whereClause = " AND " + tableName + ".userId LIKE :user"; //$NON-NLS-1$ //$NON-NLS-2$
		return whereClause;
	}

	private String getCategoryWhereClause(Long categoryId, String tableName) {
		if (categoryId == null)
			return ""; //$NON-NLS-1$
		String whereClause = " AND " + tableName + ".category.id = :categoryId"; //$NON-NLS-1$ //$NON-NLS-2$
		return whereClause;
	}

	private String getSimpleLocationWhereClause(Double latitude,
			Double longitude, Double radius, String objVar) throws Exception {

		if (latitude == null || longitude == null)
			return ""; //$NON-NLS-1$

		if (radius == null) {
			radius = RADIUSDEFAULT;
		}

		if (latitude > LATITUDEMAX || latitude < LATITUDEMIN)
			throw new Exception("latitude out of valid range [" + LATITUDEMIN //$NON-NLS-1$
					+ "," + LATITUDEMAX + "]"); //$NON-NLS-1$ //$NON-NLS-2$

		if (longitude > LONGITUDEMAX || longitude < LONGITUDEMIN)
			throw new Exception("longitude out of valid range [" + LONGITUDEMIN //$NON-NLS-1$
					+ "," + LONGITUDEMAX + "]"); //$NON-NLS-1$ //$NON-NLS-2$

		if (radius > RADIUSMAX || radius <= RADIUSMIN_EX)
			throw new Exception("radius out of valid range ]" + RADIUSMIN_EX //$NON-NLS-1$
					+ "," + RADIUSMAX + "]"); //$NON-NLS-1$ //$NON-NLS-2$

		String whereClause = null;

		String dx = "71.5" + " * (" + longitude + " - " + objVar //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ".longitude)"; //$NON-NLS-1$
		String dy = "111.3" + " * (" + latitude + " - " + objVar + ".latitude)"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		String dx2 = dx + " * " + dx; //$NON-NLS-1$
		String dy2 = dy + " * " + dy; //$NON-NLS-1$

		Double r2 = radius * radius;

		whereClause = " AND " + r2 + " >= " + "(" + dx2 + " + " + dy2 + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		return whereClause;
	}

	private String getPhotosWhereClause(Boolean hasPhoto, String objVar) {
		if (hasPhoto) {
			String whereClause;
			whereClause = " AND " + objVar + ".photos IS NOT EMPTY"; //$NON-NLS-1$ //$NON-NLS-2$
			return whereClause;
		} else
			return ""; //$NON-NLS-1$
	}

	private String getRatingsWhereClause(Double minRating, Double maxRating,
			String complObj) throws Exception {
		if (minRating == null || maxRating == null)
			return ""; //$NON-NLS-1$

		if (minRating < RATINGMIN || maxRating > RATINGMAX
				|| maxRating < RATINGMIN || minRating > RATINGMAX)
			throw new Exception("minRating or maxRating out of valid range [" //$NON-NLS-1$
					+ RATINGMIN + "," + RATINGMAX + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		String whereClause;
		whereClause = " AND (" + complObj + ".averageRating BETWEEN " //$NON-NLS-1$ //$NON-NLS-2$
				+ minRating + " AND " + maxRating + " ) "; //$NON-NLS-1$ //$NON-NLS-2$
		return whereClause;
	}

	/**
	 * exact calculation
	 * 
	 * @param c
	 *            the Complaint
	 * @param radius
	 *            radius of area
	 * @param latitude
	 *            latitude of center of area
	 * @param longitude
	 *            longitude of center of area
	 * @return true if complaint is in area
	 */
	@SuppressWarnings("unused")
	private Boolean isComplaintInArea(Complaint c, Double radius,
			Double latitude, Double longitude) {

		// http://www.kompf.de/gps/distcalc.html
		final double EARTH_DIAMETER = 6378.388;
		double lat1 = latitude.doubleValue();
		double lon1 = longitude.doubleValue();
		double lat2 = c.getGeolocation().getLatitude();
		double lon2 = c.getGeolocation().getLongitude();

		if (radius > EARTH_DIAMETER
				* Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
						* Math.cos(lat2) * Math.cos(lon2 - lon1)))
			return true;
		return false;
	}

	@Override
	public Comment getComplaintComment(Long complaintId, Long commentId)
			throws Exception {
		return findComment(getComplaint(complaintId), commentId);
	}

	@Override
	public Photo getComplaintPhoto(Long complaintId, Long photoId)
			throws Exception {
		return findPhoto(getComplaint(complaintId), photoId);
	}

	@Override
	public Rating getComplaintRating(Long complaintId, Long ratingId)
			throws Exception {
		return findRating(getComplaint(complaintId), ratingId);
	}

	/**
	 * sets properties comments, ratings and photos to null
	 * 
	 * @param c
	 *            the complete complaint
	 * @return the complaint without comments, ratings and photos
	 */
	private Complaint refactorComplaint(Complaint c) {
		c.setComments(null);
		c.setRatings(null);
		c.setPhotos(null);
		return c;
	}

	@Override
	public Comment deleteComplaintComment(Long complaintId, Long objectId)
			throws Exception {
		final EntityManager em = createEntityManager();
		Complaint c;
		Comment o = null;
		try {
			c = em.find(Complaint.class, complaintId);
			o = em.find(Comment.class, objectId);
			c.getComments().getComments().remove(o);
			em.remove(o);
			return o;
		} finally {
			// //em.close();
		}
	}

	@Override
	public Photo deleteComplaintPhoto(Long complaintId, Long objectId)
			throws Exception {
		final EntityManager em = createEntityManager();
		em.getTransaction().begin();
		Complaint c;
		Photo o = null;
		try {
			c = em.find(Complaint.class, complaintId);
			o = em.find(Photo.class, objectId);
			c.getPhotos().getPhotos().remove(o);
			em.remove(o);
			em.getTransaction().commit();
			return o;
		} finally {
			// em.close();
		}
	}

	@Override
	public Rating deleteComplaintRating(Long complaintId, Long objectId)
			throws Exception {
		final EntityManager em = createEntityManager();
		em.getTransaction().begin();
		Complaint c;
		Rating o = null;
		try {
			c = em.find(Complaint.class, complaintId);
			o = em.find(Rating.class, objectId);
			c.getRatings().getRatings().remove(o);
			em.remove(o);
			setAvgRating(c);
			em.getTransaction().commit();

			return o;
		} finally {
			// em.close();
		}
	}

	private Rating findRating(Complaint c, Long objectId) throws Exception {
		String objectName = "object"; //$NON-NLS-1$
		for (Rating o : c.getRatings().getRatings()) {
			objectName = o.getClass().getSimpleName();
			if (o.getId() == objectId) {
				logger.debug(objectName + " [" + objectId //$NON-NLS-1$
						+ "] is attached to complaint [" + c.getId() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				return o;
			}

		}
		throw new Exception(objectName + " [" + objectId //$NON-NLS-1$
				+ "] is NOT attached to complaint [" + c.getId() + "]"); //$NON-NLS-1$ //$NON-NLS-2$

	}

	private Comment findComment(Complaint c, Long objectId) throws Exception {
		String objectName = "object"; //$NON-NLS-1$
		for (Comment o : c.getComments().getComments()) {
			objectName = o.getClass().getSimpleName();
			if (o.getId() == objectId) {
				logger.debug(objectName + " [" + objectId //$NON-NLS-1$
						+ "] is attached to complaint [" + c.getId() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				return o;
			}
		}
		throw new Exception(objectName + " [" + objectId //$NON-NLS-1$
				+ "] is NOT attached to complaint [" + c.getId() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private Photo findPhoto(Complaint c, Long objectId) throws Exception {
		String objectName = "object"; //$NON-NLS-1$
		for (Photo o : c.getPhotos().getPhotos()) {
			objectName = o.getClass().getSimpleName();
			if (o.getId() == objectId) {
				logger.debug(objectName + " [" + objectId //$NON-NLS-1$
						+ "] is attached to complaint [" + c.getId() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				return o;
			}

		}
		throw new Exception(objectName + " [" + objectId //$NON-NLS-1$
				+ "] is NOT attached to complaint [" + c.getId() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public Complaint addComplaint(Complaint complaint) throws Exception {
		final EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(complaint);
			em.getTransaction().commit();
			logger.debug("complaint [" + complaint.getId() + "] inserted"); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			em.close();
		}
		return refactorComplaint(complaint);
	}

	@Override
	public Complaint updateComplaint(Complaint complaint) throws Exception {
		final EntityManager em = createEntityManager();
		Complaint updateComplaint;
		try {
			em.getTransaction().begin();
			updateComplaint = em.find(Complaint.class, complaint.getId());
			if (updateComplaint != null) {
				// updating
				updateComplaint.setAddress(complaint.getAddress());
				updateComplaint.setAverageRating(complaint.getAverageRating());
				updateComplaint.setCategory(complaint.getCategory());
				updateComplaint.setComments(complaint.getComments());
				updateComplaint.setCreationTime(complaint.getCreationTime());
				updateComplaint.setDescription(complaint.getDescription());
				updateComplaint.setGeolocation(complaint.getGeolocation());
				updateComplaint.setPhotos(complaint.getPhotos());
				updateComplaint.setRatings(complaint.getRatings());
				updateComplaint.setStatus(complaint.getStatus());
				updateComplaint.setTags(complaint.getTags());
				updateComplaint.setTitle(complaint.getTitle());
				updateComplaint.setUserId(complaint.getUserId());
				em.merge(updateComplaint);
				logger.debug("complaint [" + complaint.getId() + "] updated"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				throw new Exception("complaint [" + complaint.getId() //$NON-NLS-1$
						+ "] not found"); //$NON-NLS-1$
			}
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return refactorComplaint(updateComplaint);
	}

	public Complaints searchComplaints(List<String> tags, Double latitude,
			Double longitude, Double radius, Long categoryId, Long statusId,
			Boolean hasPhoto, Double minRating, Double maxRating,
			Integer limit, Integer offset, String userId, String user,
			String orderBy, Boolean orderDesc) throws Exception {
		logger.debug("searchComplaints(" + tags + "," + latitude + "," //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ longitude + "," + radius + "," + categoryId + "," + statusId //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ "," + hasPhoto + "," + minRating + "," + maxRating + "," //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ limit + "," + offset + "," + userId + "," + orderBy
				+ "," + orderDesc + ")"); //$NON-NLS-1$ //$NON-NLS-2$

		EntityManager em = null;
		Integer total = null;

		final String complaintTable = Complaint.class.getName();
		final String complaintAbbr = " c"; //$NON-NLS-1$
		final String geolocationTable = "geolocation"; //$NON-NLS-1$
		final String geolocationAbbr = " g"; //$NON-NLS-1$

		List<Complaint> resultList = null;

		String select = SELECT + " DISTINCT " + complaintAbbr + " "; //$NON-NLS-1$ //$NON-NLS-2$

		String from = FROM + complaintTable + " " + complaintAbbr + " JOIN " //$NON-NLS-1$ //$NON-NLS-2$
				+ complaintAbbr + "." + geolocationTable + geolocationAbbr; //$NON-NLS-1$

		// starting where clause, to add other conditions dynamically
		String where = " WHERE TRUE = TRUE "; //$NON-NLS-1$
		where += getTagListWhereClause(tags, complaintAbbr, false);
		where += getSimpleLocationWhereClause(latitude, longitude, radius,
				geolocationAbbr);
		where += getCategoryWhereClause(categoryId, complaintAbbr);
		where += getStatusWhereClause(statusId, complaintAbbr);
		where += getPhotosWhereClause(hasPhoto, complaintAbbr);

		where += getRatingsWhereClause(minRating, maxRating, complaintAbbr);
		where += getUserIdWhereClause(userId, complaintAbbr);
		where += getUserWhereClause(user, complaintAbbr);

		String orderby = getOrderByClause(complaintAbbr, orderBy, orderDesc);

		em = createEntityManager();
		String getComplaintsQuery = select + from + where + orderby;
		Query query = em.createQuery(getComplaintsQuery);

		// TODO using criteria
		if (statusId != null)
			query.setParameter("statusId", statusId);

		if (categoryId != null)
			query.setParameter("categoryId", categoryId);

		if (user != null)
			query.setParameter("user", "%" + user + "%");

		if (userId != null)
			query.setParameter("userId", userId);

		if (tags != null) {
			int i = 0;
			for (String tag : tags) {
				query.setParameter("tag" + i, "%" + tag + "%");
				i++;
			}
		}

		offset = ServiceHelper.checkOffset(offset);
		limit = ServiceHelper.checkLimit(limit);

		// full query to obtain total number of results
		List<Complaint> fullResultList = query.getResultList();
		total = fullResultList.size();
		limit = ServiceHelper.calcLimit(offset, limit, total);
		// returning sublist for pagination
		resultList = fullResultList.subList(offset, offset + limit);

		// remove comments, photos and ratings from complaint
		for (Complaint complaint : resultList) {
			complaint = refactorComplaint(complaint);
		}

		Complaints complaints = new Complaints();
		complaints.setComplaints(resultList);
		complaints.setOffset(offset);
		complaints.setResult(limit);
		complaints.setTotal(total);

		return complaints;
	}
}