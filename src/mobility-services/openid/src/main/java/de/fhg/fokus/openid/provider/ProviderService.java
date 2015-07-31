package de.fhg.fokus.openid.provider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openid4java.association.AssociationException;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.DirectError;
import org.openid4java.message.Message;
import org.openid4java.message.MessageException;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.Parameter;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.openid4java.server.ServerManager;

import de.fhg.fokus.data.DataAccess;
import de.fhg.fokus.data.User;
import de.fhg.fokus.data.UserDAO;
import de.fhg.fokus.mdc.propertyProvider.Constants;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.utils.Util;

/**
 * ProviderService - OpenID provider NOTE: Some part of the code has been
 * adopted from `OpenID4Java` library wiki.
 * */
@Path("/provider")
@ManagedBean
public class ProviderService {
	@Inject
	UserDAO users;
	@Inject
	DataAccess database;

	/** The logger of the class. */
	private static Logger log = LogManager.getLogger(ProviderService.class
			.getName());

	private static final String SERVICE_URL;

	static {
		Properties props = null;
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}

		SERVICE_URL = props.getProperty(Constants.SERVICE_URL);
	}

	private static final String SERVER_ENDPOINT = SERVICE_URL
			+ "openid/provider/server/o2";
	private static ServerManager manager = null;

	@Context
	ServletContext context;

	static {
		manager = new ServerManager();
		manager.setOPEndpointUrl(SERVER_ENDPOINT);
		manager.getRealmVerifier().setEnforceRpId(false);
	}

	@GET
	@Path("/server/logout")
	@Produces("text/plain")
	public Response logoutUser(@Context HttpServletRequest httpRequest) {
		HttpSession httpSession = httpRequest.getSession();
		httpSession.invalidate();
		return Response.ok().entity("User logged out").build();
	}

	@POST
	@Path("server/o2")
	public Response handlePostAction(MultivaluedMap<String, String> parameter,
			@Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse) throws ServerException,
			AssociationException, MessageException, URISyntaxException,
			ServletException, IOException {

		ParameterList parameterList = new ParameterList(
				Util.getParamMap(parameter));

		return handleAction(httpRequest, httpResponse, parameterList);
	}

	@GET
	@Path("server/o2")
	public Response handleGetAction(@Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse)
			throws URISyntaxException, ServerException, AssociationException,
			MessageException, ServletException, IOException {

		ParameterList parameterList = new ParameterList(
				httpRequest.getParameterMap());

		return handleAction(httpRequest, httpResponse, parameterList);
	}

	private Response handleAction(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, ParameterList parameterList)
			throws ServletException, IOException, MessageException,
			AssociationException {

		log.debug("Provided Parameter list>>>>>>>>>>" + parameterList);
		// Login Action
		boolean isLoginAction = parameterList.hasParameter("_loginAction");
		// boolean isRegisterAction = parameterList
		// .hasParameter("_registerAction");
		if (isLoginAction) {
			String loginIdentifier = parameterList
					.getParameterValue("identifier");
			if (this.authenticate(parameterList.getParameterValue("password"),
					loginIdentifier, httpRequest)) {
				System.out.println("authentication successful!");
			} else {
				if (isOAuthFlow(parameterList)) {
					httpRequest.setAttribute("client_id",
							parameterList.getParameterValue("client_id"));
					httpRequest.setAttribute("redirect_uri",
							parameterList.getParameterValue("redirect_uri"));
					httpRequest.setAttribute("state",
							parameterList.getParameterValue("state"));
					httpRequest.setAttribute("scope",
							parameterList.getParameterValue("scope"));
					httpRequest.setAttribute("response_type",
							parameterList.getParameterValue("response_type"));
					this.redirectToOAuthPage(loginIdentifier, httpRequest,
							httpResponse);
					return null;
				} else {
					this.redirectToLogin(loginIdentifier, httpRequest,
							httpResponse);
					return null;
				}
			}
		}
		// End - Login Action
		String mode = parameterList.getParameterValue("openid.mode");
		Message messageResponse;
		if ("associate".equals(mode)) {
			// process an association request
			messageResponse = manager.associationResponse(parameterList);
		} else if ("checkid_setup".equals(mode)
				|| "checkid_immediate".equals(mode)) {

			// interact with the user and obtain data needed to continue
			String userSelectedId = parameterList
					.getParameterValue("openid.identity");
			HttpSession session = httpRequest.getSession();
			boolean sameUser = userSelectedId
					.equals(session.getAttribute("id"));
			Boolean isAuthenticated = Boolean.parseBoolean((String) session
					.getAttribute("isAuthenticated"));
			if (!sameUser && isAuthenticated) {
				session.invalidate();
				isAuthenticated = false;
				session = httpRequest.getSession();
			}
			if (!isAuthenticated) {
				session.setAttribute("paramsList", parameterList);
				List<Parameter> parameters = parameterList.getParameters();
				HashMap<String, String> paramMap = new HashMap<String, String>();
				Iterator<Parameter> iter = parameters.iterator();
				while (iter.hasNext()) {
					Parameter item = iter.next();
					paramMap.put(item.getKey(), item.getValue());
				}

				httpRequest.setAttribute("paramMap", paramMap);
				if (isOAuthFlow(parameterList)) {
					httpRequest.setAttribute("client_id",
							parameterList.getParameterValue("client_id"));
					httpRequest.setAttribute("redirect_uri",
							parameterList.getParameterValue("redirect_uri"));
					httpRequest.setAttribute("state",
							parameterList.getParameterValue("state"));
					httpRequest.setAttribute("scope",
							parameterList.getParameterValue("scope"));
					httpRequest.setAttribute("response_type",
							parameterList.getParameterValue("response_type"));
					this.redirectToOAuthPage(userSelectedId, httpRequest,
							httpResponse);
					return null;
				}
				this.redirectToLogin(userSelectedId, httpRequest, httpResponse);
				return null;
			}
			String userSelectedClaimedId = parameterList
					.getParameterValue("openid.claimed_id");

			// process an authentication request
			AuthRequest authReq = AuthRequest.createAuthRequest(parameterList,
					manager.getRealmVerifier());

			messageResponse = manager.authResponse(parameterList,
					userSelectedId, userSelectedClaimedId,
					isAuthenticated.booleanValue(), false); // Sign after we
															// added extensions.

			if (messageResponse instanceof DirectError) {
				return Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(httpResponse).build();
			} else {
				// UserModel registrationModel = (UserModel) session
				// .getAttribute("userModel");
				User user = (User) session.getAttribute("user");

				if (authReq.hasExtension(AxMessage.OPENID_NS_AX)) {
					Map<String, String> axData = new HashMap<String, String>();
					MessageExtension extensionRequestObject = authReq
							.getExtension(AxMessage.OPENID_NS_AX);
					if (extensionRequestObject instanceof FetchRequest) {
						FetchRequest fetchReq = (FetchRequest) extensionRequestObject;
						Map required = fetchReq.getAttributes(true);
						if (required != null && required.size() > 0) {
							axData.put("email", user.getEmailAddress());
							axData.put("scope",
									parameterList.getParameterValue("scope"));
							axData.put("username", user.getUsername());
							FetchResponse fetchResp = FetchResponse
									.createFetchResponse(fetchReq, axData);
							messageResponse.addExtension(fetchResp);
						}
					}
				}
				if (authReq.hasExtension(SRegMessage.OPENID_NS_SREG)) {
					MessageExtension extensionRequestObject = authReq
							.getExtension(SRegMessage.OPENID_NS_SREG);
					if (extensionRequestObject instanceof SRegRequest) {
						Map<String, String> registrationData = new HashMap<String, String>();
						registrationData.put("fullname", user.getFullName());
						registrationData.put("dob", user.getDateOfBirth());

						SRegRequest sregReq = (SRegRequest) extensionRequestObject;
						SRegResponse sregResp = SRegResponse
								.createSRegResponse(sregReq, registrationData);

						messageResponse.addExtension(sregResp);
					}
				}

				// Sign the auth success message.
				try {
					manager.sign((AuthSuccess) messageResponse);
				} catch (org.openid4java.server.ServerException e) {
					e.printStackTrace();
				}

				String feedback = parameterList.getParameterValue("feedback");
				if ("reject".equals(feedback)) {
					// user rejected request, send denial back to client
					// invalidate authentication
					HttpSession httpSession = httpRequest.getSession();
					httpSession.removeAttribute("id");
					httpSession.removeAttribute("user");
					httpSession.removeAttribute("isAuthenticated");
				}
				RequestDispatcher dispatcher = context
						.getRequestDispatcher("/consumer-redirection.jsp");
				httpRequest.setAttribute("parameterMap",
						messageResponse.getParameterMap());
				httpRequest.setAttribute("destinationUrl",
						messageResponse.getDestinationUrl(false));
				httpRequest.setAttribute("client_id",
						parameterList.getParameterValue("client_id"));
				httpRequest.setAttribute("redirect_uri",
						parameterList.getParameterValue("redirect_uri"));
				httpRequest.setAttribute("response_type",
						parameterList.getParameterValue("response_type"));
				httpRequest.setAttribute("state",
						parameterList.getParameterValue("state"));
				httpRequest.setAttribute("feedback",
						parameterList.getParameterValue("feedback"));
				HttpSession httpSession = httpRequest.getSession();
				httpSession.removeAttribute("id");
				httpSession.removeAttribute("user");
				httpSession.removeAttribute("isAuthenticated");
				dispatcher.forward(httpRequest, httpResponse);
				return null;
			}
		} else if ("check_authentication".equals(mode)) {
			messageResponse = manager.verify(parameterList);
		} else {
			return Response.status(Status.BAD_REQUEST).entity(httpResponse)
					.build();
		}
		// return the result to the user
		return Response.ok().entity(messageResponse.keyValueFormEncoding())
				.build();
	}

	private boolean isOAuthFlow(ParameterList parameterList) {
		return parameterList.hasParameter("client_id")
				&& parameterList.hasParameter("redirect_uri")
				&& parameterList.hasParameter("state")
				&& parameterList.hasParameter("scope")
				&& parameterList.hasParameter("response_type");
	}

	public boolean authenticate(String password, String identifier,
			@Context HttpServletRequest httpRequest) throws ServletException,
			IOException {
		if (identifier != null && users.isValidUser(identifier, password)) {
			HttpSession httpSession = httpRequest.getSession();
			httpSession.setAttribute("id", identifier);
			httpSession.setAttribute("user",
					users.getUser(Util.extractUserName(identifier)));
			httpSession.setAttribute("isAuthenticated", "true");
			return true;
		}
		return false;
	}

	private void redirectToOAuthPage(String identifier,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException, IOException {
		String scope = (String) httpRequest.getAttribute("scope");
		Map<String, String> scopeDescriptions = database
				.getDescriptionForScope(scope);
		RequestDispatcher dispatcher = context
				.getRequestDispatcher("/oauth-confirmation.jsp");
		httpRequest.setAttribute("identifier", identifier);
		httpRequest.setAttribute("scopeDescriptions", scopeDescriptions);
		httpRequest.setAttribute("destinationUrl", SERVER_ENDPOINT);
		dispatcher.forward(httpRequest, httpResponse);

	}

	public void redirectToLogin(String identifier,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServletException, IOException {

		RequestDispatcher dispatcher = context
				.getRequestDispatcher("/login.jsp");
		httpRequest.setAttribute("identifier", identifier);
		httpRequest.setAttribute("destinationUrl", SERVER_ENDPOINT);
		dispatcher.forward(httpRequest, httpResponse);
	}
}