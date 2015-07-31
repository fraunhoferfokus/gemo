package de.fhg.fokus.cm.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import de.fhg.fokus.cm.ejb.Categories;
import de.fhg.fokus.cm.ejb.Category;
import de.fhg.fokus.cm.ejb.Status;
import de.fhg.fokus.cm.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {

	private final String SELECT = "SELECT ";
	private final String FROM = "FROM ";
	private final String AS = "AS ";

	private static ServiceHelper serviceHelper;

	@Override
	public Category addOrUpdateCategory(Category category) throws Exception {
		final EntityManager em = createEntityManager();
		Category updateStatus;
		try {
			em.getTransaction().begin();
			updateStatus = em.find(Category.class, category.getId());
			if (updateStatus != null) {
				updateStatus.setTitle(category.getTitle());
				updateStatus.setDescription(category.getDescription());
			} else {
				updateStatus = category;
				em.persist(updateStatus);
			}
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return updateStatus;
	}

	@Override
	public Categories getCategories(Integer limit, Integer offset)
			throws Exception {
		offset = ServiceHelper.checkOffset(offset);
		limit = ServiceHelper.checkLimit(limit);
		final EntityManager em = createEntityManager();
		List<Category> resultList = em
				.createQuery(
						SELECT + "o " + FROM
								+ Category.class.getCanonicalName() + " " + AS
								+ "o ").setFirstResult(offset)
				.setMaxResults(limit).getResultList();

		Long total = (Long) em.createQuery(
				SELECT + " count(*) " + FROM
						+ Category.class.getCanonicalName() + " " + AS + "o ")
				.getSingleResult();

		Categories categories = new Categories();
		categories.setCategories(resultList);
		Integer newLimit = ServiceHelper.calcLimit(offset, limit,
				total.intValue());

		categories.setTotal(total.intValue());
		categories.setOffset(offset);
		categories.setResult(newLimit);
		return categories;
	}

	@Override
	public Category getCategory(Long categoryId) throws Exception {
		final EntityManager em = createEntityManager();
		Category c;
		try {
			c = em.find(Category.class, categoryId);
		} finally {
			em.close();
		}
		return c;
	}

	@Override
	public Category deleteCategory(Long categoryId) throws Exception {
		final EntityManager em = createEntityManager();
		Category category;
		try {
			em.getTransaction().begin();
			category = em.find(Category.class, categoryId);
			em.remove(category);
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return category;
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
	public Categories getCategories() throws Exception {
		return getCategories(null, null);
	}

	@Override
	public Category addCategory(Category category) throws Exception {
		final EntityManager em = createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(category);
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return category;
	}

}
