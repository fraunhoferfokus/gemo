/**
 * 
 */
package de.fhg.fokus.cm.service;

import de.fhg.fokus.cm.ejb.Status;
import de.fhg.fokus.cm.ejb.Statuses;

/**
 * @author Fabian Manzke
 * 
 */
public interface StatusService {

	public Statuses getStatuses() throws Exception;

	public Statuses getStatuses(Integer limit, Integer offset) throws Exception;

	public Status getStatus(Long statusId) throws Exception;

	public Status deleteStatus(Long statusId) throws Exception;

	public Status addOrUpdateStatus(Status status) throws Exception;

	public Status addStatus(Status status) throws Exception;

	public Status updateStatus(Status status) throws Exception;

}
