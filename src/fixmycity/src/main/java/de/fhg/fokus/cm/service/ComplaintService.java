/**
 * 
 */
package de.fhg.fokus.cm.service;

import java.util.List;

import de.fhg.fokus.cm.ejb.Category;
import de.fhg.fokus.cm.ejb.Comment;
import de.fhg.fokus.cm.ejb.Comments;
import de.fhg.fokus.cm.ejb.Complaint;
import de.fhg.fokus.cm.ejb.Complaints;
import de.fhg.fokus.cm.ejb.Photo;
import de.fhg.fokus.cm.ejb.Photos;
import de.fhg.fokus.cm.ejb.Rating;
import de.fhg.fokus.cm.ejb.Ratings;
import de.fhg.fokus.cm.ejb.Status;

/**
 * @author Louay Bassbouss, Fabian Manzke
 * 
 */
public interface ComplaintService {

	/**
	 * 
	 * @param complaintId
	 *            the ID of the {@link Complaint} to delete.
	 * @return the deleted {@link Complaint} or <code>null</code> if the
	 *         {@link Complaint} doesn't exist.
	 * @throws Exception
	 */
	public Complaint deleteComplaint(Long complaintId) throws Exception;

	/**
	 * 
	 * @param complaintId
	 *            the ID of the {@link Complaint} to get.
	 * @return the found {@link Complaint} or <code>null</code> if the
	 *         {@link Complaint} doesn't exist
	 * @throws Exception
	 */
	public Complaint getComplaint(Long complaintId) throws Exception;

	public Rating getComplaintRating(Long complaintId, Long ratingId)
			throws Exception;

	public Comment getComplaintComment(Long complaintId, Long commentId)
			throws Exception;

	public Photo getComplaintPhoto(Long complaintId, Long photoId)
			throws Exception;

	/**
	 * 
	 * @param tags
	 *            list of tags or <code>null</code>. in case of
	 *            <code>null</code> this parameter will be ignored
	 * @param latitude
	 *            the latitude of the search center or <code>null</code>. in
	 *            case of <code>null</code> this parameter will be ignored
	 * @param longitude
	 *            the longitude of the search center or <code>null</code>. in
	 *            case of <code>null</code> this parameter will be ignored
	 * @param radius
	 *            the search radius in KM or <code>null</code>. in case of
	 *            <code>null</code> this parameter will be ignored.
	 * @param categoryId
	 *            the ID of the {@link Category} or <code>null</code>. in case
	 *            of <code>null</code> this parameter will be ignored.
	 * @param statusId
	 *            the ID of the {@link Status} or <code>null</code>. in case of
	 *            <code>null</code> this parameter will be ignored.
	 * @param hasPhoto
	 *            specifies if the complaints has photos or not. in case of
	 *            <code>null</code> this parameter will be ignored.
	 * @param minRating
	 *            the MIN rating or <code>null</code>. in case of
	 *            <code>null</code> this parameter will be ignored.
	 * @param maxRating
	 *            the MAX rating or <code>null</code>. in case of
	 *            <code>null</code> this parameter will be ignored.
	 * @param limit
	 *            the limit of the result set
	 * @param offset
	 *            the offset of the result set
	 * @return list of found complaints
	 */
	public Complaints searchComplaints(List<String> tags, Double latitude,
			Double longitude, Double radius, Long categoryId,
			Long statusId, Boolean hasPhoto, Double minRating,
			Double maxRating, Integer limit, Integer offset, String userId, String user, String orderByColumn, Boolean orderAsc) throws Exception;
	
	public Comments getComments(Long complaintId, Integer limit,
			Integer offset, String userId) throws Exception;

	public Comment deleteComplaintComment(Long complaintId, Long commentId)
			throws Exception;

	public Photo addPhoto(Long complaintId, Photo photo) throws Exception;

	public Ratings getRatings(Long complaintId, Integer limit, Integer offset, String userId)
			throws Exception;

	public Rating deleteComplaintRating(Long complaintId, Long ratingId)
			throws Exception;

	public Photos getPhotos(Long complaintId, Integer limit, Integer offset, String userId)
			throws Exception;

	public Photo deleteComplaintPhoto(Long complaintId, Long photoId)
			throws Exception;

	public Complaint getComplaintBase(Long complaintId) throws Exception;

	public Photo updatePhoto(Long complaintId, Photo photo) throws Exception;

	public Rating updateRating(Long complaintId, Rating rating)
			throws Exception;

	public Comment updateComment(Long complaintId, Comment comment)
			throws Exception;

	public Rating addRating(Long complaintId, Rating rating)
			throws Exception;

	public Comment addComment(Long complaintId, Comment comment)
			throws Exception;

	public Complaint addComplaint(Complaint complaint) throws Exception;

	Complaint updateComplaint(Complaint complaint) throws Exception;
}
