package de.fhg.fokus.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.oltu.oauth2.common.OAuth;

/**
 * Very ugly workaround class for bug in Glassfish.
 * 
 * This wraps a MultivaluedMap<String, String> in a HttpServletRequest object.
 * It only implements all methods needed to work with Apache Oltu.
 * 
 * My bug report: https://issues.apache.org/jira/browse/OLTU-103
 * 
 * see
 * http://jersey.576304.n2.nabble.com/Tomcat-Deployment-Vs-Jetty-Deployment-no
 * -parameters-from-HttpServletRequest-td1516207.html
 * http://cxf.547215.n5.nabble.com/POST-data-missing-in-Context-td4391121.html
 * https://issues.apache.org/jira/browse/CXF-2993
 * 
 * @author Dominik Schürmann (dominik@dominikschuermann.de)
 */
public class MyHttpServletRequest implements HttpServletRequest {

	MultivaluedMap<String, String> params;

	public MyHttpServletRequest(MultivaluedMap<String, String> params) {
		this.params = params;
	}

	@Override
	public String getAuthType() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public Cookie[] getCookies() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public long getDateHeader(String name) {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getHeader(String name) {
		return params.getFirst(name);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public int getIntHeader(String name) {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getMethod() {
		return OAuth.HttpMethod.POST;
	}

	@Override
	public String getPathInfo() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getPathTranslated() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getContextPath() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getQueryString() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getRemoteUser() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public boolean isUserInRole(String role) {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public Principal getUserPrincipal() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getRequestedSessionId() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getRequestURI() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public StringBuffer getRequestURL() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getServletPath() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public HttpSession getSession(boolean create) {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public HttpSession getSession() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		throw new UnsupportedOperationException("Not supported!");
	}

	public boolean authenticate(HttpServletResponse response)
			throws IOException, ServletException {
		throw new UnsupportedOperationException("Not supported!");
	}

	public void login(String username, String password) throws ServletException {
		throw new UnsupportedOperationException("Not supported!");
	}

	public void logout() throws ServletException {
		throw new UnsupportedOperationException("Not supported!");
	}

	public Collection<Part> getParts() throws IOException, ServletException {
		throw new UnsupportedOperationException("Not supported!");
	}

	public Part getPart(String name) throws IOException, ServletException {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public Object getAttribute(String name) {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getCharacterEncoding() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public int getContentLength() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getContentType() {
		return OAuth.ContentType.URL_ENCODED;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getParameter(String name) {
		return params.getFirst(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String[] getParameterValues(String name) {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getProtocol() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getScheme() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getServerName() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public int getServerPort() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public BufferedReader getReader() throws IOException {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getRemoteAddr() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getRemoteHost() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public void setAttribute(String name, Object o) {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public void removeAttribute(String name) {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public Locale getLocale() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public Enumeration<Locale> getLocales() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public boolean isSecure() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getRealPath(String path) {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public int getRemotePort() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getLocalName() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public String getLocalAddr() {
		throw new UnsupportedOperationException("Not supported!");
	}

	@Override
	public int getLocalPort() {
		throw new UnsupportedOperationException("Not supported!");
	}

	public ServletContext getServletContext() {
		throw new UnsupportedOperationException("Not supported!");
	}

	public AsyncContext startAsync() throws IllegalStateException {
		throw new UnsupportedOperationException("Not supported!");
	}

	public AsyncContext startAsync(ServletRequest servletRequest,
			ServletResponse servletResponse) throws IllegalStateException {
		throw new UnsupportedOperationException("Not supported!");
	}

	public boolean isAsyncStarted() {
		throw new UnsupportedOperationException("Not supported!");
	}

	public boolean isAsyncSupported() {
		throw new UnsupportedOperationException("Not supported!");
	}

	public AsyncContext getAsyncContext() {
		throw new UnsupportedOperationException("Not supported!");
	}

	public DispatcherType getDispatcherType() {
		throw new UnsupportedOperationException("Not supported!");
	}
}