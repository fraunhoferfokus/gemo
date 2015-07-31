package de.fhg.fokus.cm.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import de.fhg.fokus.cm.ejb.Status;
import de.fhg.fokus.cm.ejb.Statuses;
import de.fhg.fokus.cm.service.StatusService;

public class StatusServiceImpl implements StatusService {

	private final String SELECT = "SELECT ";
	private final String FROM = "FROM ";
	private final String AS = "AS ";

	private static ServiceHelper serviceHelper;

	@Override
	public Statuses getStatuses(Integer limit, Integer offset) throws Exception {
		offset = ServiceHelper.checkOffset(offset);
		limit = ServiceHelper.checkLimit(limit);
		final EntityManager em = createEntityManager();
		List<Status> resultList = em
				.createQuery(
						SELECT + "o " + FROM + Status.class.getCanonicalName()
								+ " " + AS + "o ").setFirstResult(offset)
				.setMaxResults(limit).getResultList();

		Long total = (Long) em.createQuery(
				SELECT + " count(*) " + FROM + Status.class.getCanonicalName()
						+ " " + AS + "o ").getSingleResult();

		Statuses statuses = new Statuses();
		statuses.setStatuses(resultList);

		Integer newLimit = ServiceHelper.calcLimit(offset, limit,
				total.intValue());

		statuses.setTotal(total.intValue());
		statuses.setOffset(offset);
		statuses.setResult(newLimit);
		return statuses;
	}

	@Override
	public Status getStatus(Long statusId) throws Exception {
		final EntityManager em = createEntityManager();
		Status s;
		try {
			s = em.find(Status.class, statusId);
		} finally {
			em.close();
		}
		return s;

	}

	@Override
	public Status addOrUpdateStatus(Status status) throws Exception {
		final EntityManager em = createEntityManager();
		Status updateStatus;
		try {
			em.getTransaction().begin();
			updateStatus = em.find(Status.class, status.getId());
			if (updateStatus != null) {
				updateStatus.setTitle(status.getTitle());
				updateStatus.setDescription(status.getDescription());
			} else {
				updateStatus = status;
				em.persist(updateStatus);
			}
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return updateStatus;
	}

	@Override
	public Status updateStatus(Status status) throws Exception {
		final EntityManager em = createEntityManager();
		Status updateStatus;
		try {
			em.getTransaction().begin();
			updateStatus = em.find(Status.class, status.getId());
			if (updateStatus != null) {
				updateStatus.setTitle(status.getTitle());
				updateStatus.setDescription(status.getDescription());
				em.persist(updateStatus);
			} else {
				// return resource not found
			}
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return updateStatus;
	}

	/*
	 * creates or overwrites a status in response to PUT requests to
	 * StatusesResource
	 */
	@Override
	public Status addStatus(Status status) throws Exception {
		final EntityManager em = createEntityManager();

		em.getTransaction().begin();
		em.persist(status);
		em.getTransaction().commit();

		em.close();

		return status;
	}

	@Override
	public Status deleteStatus(Long statusId) throws Exception {
		final EntityManager em = createEntityManager();
		Status status;
		try {
			em.getTransaction().begin();
			status = em.find(Status.class, statusId);
			em.remove(status);
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return status;
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

	@Override
	public Statuses getStatuses() throws Exception {
		return getStatuses(null, null);
	}

}
