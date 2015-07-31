/**
 * 
 */
package de.fhg.fokus.cm.service;

import de.fhg.fokus.cm.ejb.Categories;
import de.fhg.fokus.cm.ejb.Category;
import de.fhg.fokus.cm.ejb.Status;

/**
 * @author Fabian Manzke
 * 
 */
public interface CategoryService {

	public Categories getCategories() throws Exception;

	public Categories getCategories(Integer limit, Integer offset)
			throws Exception;

	public Category getCategory(Long categoryId) throws Exception;

	public Category deleteCategory(Long categoryId) throws Exception;

	public Category addOrUpdateCategory(Category category) throws Exception;

	public Category addCategory(Category category) throws Exception;

}
