package de.fhg.fokus.cm.webapp.filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;

public class DurationLogFilter implements Filter {
	static Logger logger;

	public void init(FilterConfig config) throws ServletException {
		// check whether a category has been
		// configured in the deployment descriptor
		String category = config.getInitParameter("log_category");
		if (category != null) {
			logger = Logger.getLogger(category);
		} else {
			logger = Logger.getLogger(DurationLogFilter.class.getName());
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		String url = null;
		if (request instanceof HttpServletRequest) {
			url = ((HttpServletRequest) request).getRequestURL().toString();
		}
		long duration, starttime = System.currentTimeMillis();

		// proceed along the chain
		chain.doFilter(request, response);

		// after response returns, calculate duration and log it
		duration = System.currentTimeMillis() - starttime;
		if (logger.isDebugEnabled()) {
			logger.debug("duration(valve): " + duration + " - " + url);
			System.out.println("duration(valve): " + duration + " - " + url);
		}
	}

	public void destroy() {
	}

}
