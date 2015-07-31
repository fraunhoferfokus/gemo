package de.fhg.fokus.cm.webapp;

import org.apache.log4j.Logger;

import de.fhg.fokus.cm.ejb.Error;
import de.fhg.fokus.cm.service.CategoryService;
import de.fhg.fokus.cm.service.ComplaintService;
import de.fhg.fokus.cm.service.StatusService;
import de.fhg.fokus.cm.service.impl.CategoryServiceImpl;
import de.fhg.fokus.cm.service.impl.ComplaintServiceImpl;
import de.fhg.fokus.cm.service.impl.StatusServiceImpl;

public class ResourceHelper {

	Logger logger;
	private static ComplaintServiceImpl cms;
	private static CategoryServiceImpl cat;
	private static StatusServiceImpl stat;

	public static final String LIMIT = "limit";
	public static final String OFFSET = "offset";

	public static final String STATUS_TITLE = "title";
	public static final String STATUS_ID = "statusId";
	public static final String STATUS_DESCRIPTION = "description";

	public static final String CATEGORY_TITLE = "title";
	public static final String CATEGORY_ID = "categoryId";
	public static final String CATEGORY_DESCRIPTION = "description";

	/**
	 * @param logger
	 */
	public ResourceHelper(Logger logger) {
		this.logger = logger;
	}

	public void throwWebapplicationException(Exception e, int status) {
		logger.error(e.getMessage() + " WebapplicationException thrown, Code "
				+ status);
		Error error = new Error();
		error.setMessage(e.getMessage());
		throw new javax.ws.rs.WebApplicationException(javax.ws.rs.core.Response
				.status(status).entity(error).build());
	}

	public ComplaintService getComplaintServiceImpl(Integer limitMax) {
		if (cms == null) {
			logger.debug("new ComplaintServiceImpl(" + limitMax + ");");
			cms = new ComplaintServiceImpl(limitMax);
		}
		return cms;
	}

	public CategoryService getCategoryServiceImpl() {
		if (cat == null) {
			logger.debug("new CategoryServiceImpl();");
			cat = new CategoryServiceImpl();
		}
		return cat;
	}

	public StatusService getStatusServiceImpl() {
		if (stat == null) {
			logger.debug("new StatusServiceImpl();");
			stat = new StatusServiceImpl();
		}
		return stat;
	}

	public static Boolean parseParameterB(String p) throws Exception {
		try {
			return Boolean.parseBoolean(p);
		} catch (Exception e) {
			return null;
		}
	}

	public static Integer parseParameterI(String p) throws Exception {
		try {
			if (p.equals(null))
				return null;
			return Integer.parseInt(p);
		} catch (Exception e) {
			return null;
		}
	}

	public static Long parseParameterL(String p) {
		try {
			if (p.equals(null))
				return null;
			return Long.parseLong(p);
		} catch (Exception e) {
			return null;
		}
	}

	public static Double parseParameterD(String p) throws Exception {
		try {
			return Double.parseDouble(p);
		} catch (Exception e) {
			return null;
		}
	}

	public static Error buildError(Exception exception) {
		Error error = new Error();
		error.setMessage(exception.getMessage());
		// TODO setting errorcode
		// error.setCode(null);
		return error;
	}
}
