package de.fhg.fokus.cm.webapp.filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import de.fhg.fokus.cm.webapp.Messages;

public class NotificationFilter implements Filter {
	static Logger logger = Logger.getLogger(NotificationFilter.class.getName());

	String notificationUrl = Messages
			.getString("NotificationFilter.notificationUrl"); //$NON-NLS-1$

	String notificationPath = Messages
			.getString("NotificationFilter.notificationPath"); //$NON-NLS-1$

	private WebResource webResource = null;

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// String method = req.getMethod();
		// String reqType = req.getHeader("Content-Type");
		// String url = req.getRequestURL().toString();
		// String[] urlParts = url.split("/");
		// boolean readId = false;
		// String complaintId = "";
		// for (String string : urlParts) {
		// if (readId) {
		// complaintId = string;
		// break;
		// }
		// if (string.equals("complaints"))
		// readId = true;
		//
		// }
		// logger.info("Request url " + url + " method " + method +
		// " complaintId"
		// + complaintId + " content type " + reqType);

		// proceed along the chain
		chain.doFilter(request, response);

		StatusExposingServletResponse statusResponse = new StatusExposingServletResponse(
				res);
		int status = statusResponse.getStatus();

		ResponseWrapper responseWrapper = new ResponseWrapper(res);
		String resType = responseWrapper.getContentType();

		// TODO unmashalling complaint from json or xml object

		// if (status < 400)

		// getWebResource(notificationUrl).path(notificationPath)
		// .queryParam("action", method)
		// .queryParam("complaintId", complaintId).post();
	}

	public void destroy() {
	}

	private WebResource getWebResource(String notificationUrl) {
		if (this.webResource == null) {
			ClientConfig clientConfig = new DefaultClientConfig();
			Client client = Client.create(clientConfig);
			this.webResource = client.resource(UriBuilder.fromUri(
					notificationUrl).build());
		}
		return this.webResource;
	}

	public class StatusExposingServletResponse extends
			HttpServletResponseWrapper {

		private int httpStatus = 200;

		public StatusExposingServletResponse(HttpServletResponse response) {
			super(response);
		}

		@Override
		public void sendError(int sc) throws IOException {
			httpStatus = sc;
			super.sendError(sc);
		}

		@Override
		public void sendError(int sc, String msg) throws IOException {
			httpStatus = sc;
			super.sendError(sc, msg);
		}

		@Override
		public void setStatus(int sc) {
			httpStatus = sc;
			super.setStatus(sc);
		}

		public int getStatus() {
			return httpStatus;
		}

	}

	public class ResponseWrapper extends HttpServletResponseWrapper {

		public ResponseWrapper(HttpServletResponse response) {
			super(response);
			// TODO Auto-generated constructor stub
		}

	}

}
