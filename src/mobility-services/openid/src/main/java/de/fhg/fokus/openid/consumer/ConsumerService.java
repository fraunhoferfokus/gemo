package de.fhg.fokus.openid.consumer;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openid4java.OpenIDException;
import org.openid4java.association.AssociationSessionType;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.openid4java.util.HttpRequestOptions;

import de.fhg.fokus.mdc.propertyProvider.Constants;
import de.fhg.fokus.mdc.propertyProvider.PropertyProvider;
import de.fhg.fokus.utils.Util;

/**
 * ConsumerService - Relying Party Service NOTE: Some part of the code has been
 * adopted from `OpenID4Java` library wiki.
 * */
@Path("/consumer")
public class ConsumerService {

	private static final Log LOG = LogFactory.getLog(ConsumerService.class);
	private static final String OPENID_IDENTIFIER = "openid_identifier";
	private static ConsumerManager manager = null;
	private static final String RETURN_TO_URL;
	private static final String SERVICE_URL;
	private static final SecureRandom random = new SecureRandom();
	private static final Map<String, String> userTokenMap = new HashMap<String, String>();

	@Context
	ServletContext context;

	static {
		Properties props = null;
		try {
			props = PropertyProvider.getInstance().loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// configure return_to_url
		SERVICE_URL = props.getProperty(Constants.SERVICE_URL);
		RETURN_TO_URL = SERVICE_URL + "openid/consumer?is_return=true";
		manager = new ConsumerManager();
		manager.setAssociations(new InMemoryConsumerAssociationStore());
		manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
		manager.setMinAssocSessEnc(AssociationSessionType.DH_SHA256);

		HttpRequestOptions options = new HttpRequestOptions();
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put("Authorization", "Basic Zm9rdXM6ZjBrdTU=");
		options.setRequestHeaders(requestHeaders);
		manager.getDiscovery().getYadisResolver().getHttpFetcher()
				.setDefaultRequestOptions(options);
		manager.getHttpFetcher().setDefaultRequestOptions(options);

	}

	@GET
	@Consumes("application/x-www-form-urlencoded")
	public void get(@QueryParam("is_return") String is_return,
			@Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse) {
		try {

			String identifier = httpRequest.getParameter(OPENID_IDENTIFIER);
			if ("true".equals(is_return)) {
				ParameterList paramList = new ParameterList(
						httpRequest.getParameterMap());
				System.out.println("final parameter list>>>>>" + paramList);
				this.processReturn(httpRequest, httpResponse, paramList);
			} else {
				if (identifier != null) {
					MultivaluedHashMap<String, String> mvm = new MultivaluedHashMap<String, String>();

					Enumeration<String> enumer = httpRequest
							.getParameterNames();
					while (enumer.hasMoreElements()) {
						String nextElem = enumer.nextElement();
						mvm.addAll(nextElem,
								httpRequest.getParameterValues(nextElem));
					}

					this.authRequest(identifier, mvm, httpRequest, httpResponse);
				} else {
					context.getRequestDispatcher("/index.jsp").forward(
							httpRequest, httpResponse);
				}
			}
		} catch (ServletException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	public void post(@QueryParam("is_return") String is_return,
			@FormParam(OPENID_IDENTIFIER) String identifier,
			@Context HttpServletRequest httpRequest,
			@Context HttpServletResponse httpResponse,
			MultivaluedMap<String, String> params) {
		try {
			if ("true".equals(is_return)) {
				ParameterList paramList = new ParameterList(
						Util.getParamMap(params));
				System.out.println("final parameter list>>>>>" + paramList);
				this.processReturn(httpRequest, httpResponse, paramList);
			} else {
				if (identifier != null) {
					this.authRequest(identifier, params, httpRequest,
							httpResponse);
				} else {
					context.getRequestDispatcher("/index.jsp").forward(
							httpRequest, httpResponse);
				}
			}
		} catch (ServletException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	private void processReturn(HttpServletRequest req,
			HttpServletResponse resp, ParameterList params)
			throws ServletException, IOException {
		// verify response to ensure the communication has not been tampered
		// with
		Identifier identifier = this.verifyResponse(req, resp, params);
		if (identifier == null) {
			context.getRequestDispatcher("/index.jsp").forward(req, resp);
		} else {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("client_id", params.getParameterValue("client_id"));
			paramMap.put("redirect_uri",
					params.getParameterValue("redirect_uri"));
			paramMap.put("response_type",
					params.getParameterValue("response_type"));
			paramMap.put("state", params.getParameterValue("state"));
			paramMap.put("feedback", params.getParameterValue("feedback"));
			paramMap.put("scope",
					params.getParameterValue("openid.ext1.value.scope"));
			paramMap.put("username",
					params.getParameterValue("openid.ext1.value.username"));
			paramMap.put("token", generateToken(params
					.getParameterValue("openid.ext1.value.username")));
			req.setAttribute("paramMap", paramMap);
			// req.setAttribute("identifier", identifier.getIdentifier());
			context.getRequestDispatcher("/oauth-redirection.jsp").forward(req,
					resp);
		}
	}

	private String generateToken(String username) {
		String token = new BigInteger(130, random).toString(32);
		userTokenMap.put(username, token);
		return token;

	}

	@SuppressWarnings("unchecked")
	public String authRequest(String userSuppliedString,
			MultivaluedMap<String, String> params, HttpServletRequest httpReq,
			HttpServletResponse httpResp) throws IOException, ServletException {

		try {
			ParameterList paramList = new ParameterList(
					Util.getParamMap(params));

			System.out.println(paramList.toString());
			// perform discovery on the user-supplied identifier
			List<DiscoveryInformation> discoveries = manager
					.discover(userSuppliedString);

			// attempt to associate with the OpenID provider
			// and retrieve one service endpoint for authentication
			DiscoveryInformation discovered = manager.associate(discoveries);

			// store the discovery information in the user's session
			httpReq.getSession().setAttribute("openid-disc", discovered);

			// obtain a AuthRequest message to be sent to the OpenID provider
			AuthRequest authReq = manager.authenticate(discovered,
					RETURN_TO_URL);
			// Attribute Exchange example: fetching the 'email' attribute
			FetchRequest fetch = FetchRequest.createFetchRequest();
			fetch.addAttribute("email", // attribute alias
					"http://schema.openid.net/contact/email", // type URI
					true); // required
			fetch.addAttribute("username", // attribute alias
					"http://schema.openid.net/contact/username", // type URI
					true); // required

			fetch.addAttribute("scope", "http://schema.openid.net/oauth/scope",
					true);
			// attach the extension to the authentication request
			authReq.addExtension(fetch);

			// example using Simple Registration to fetching the 'email'
			// attribute
			SRegRequest sregReq = SRegRequest.createFetchRequest();
			sregReq.addAttribute("fullname", false);
			sregReq.addAttribute("dob", false);
			authReq.addExtension(sregReq);

			if (!discovered.isVersion2()) {
				httpResp.sendRedirect(authReq.getDestinationUrl(true));
				System.out.println("in version 2");
				return null;
			} else {
				System.out.println("in version 1");
				RequestDispatcher dispatcher = context
						.getRequestDispatcher("/provider-redirection.jsp");
				httpReq.setAttribute("message", authReq);
				httpReq.setAttribute("paramMap", Util.getParamMap(params));
				dispatcher.forward(httpReq, httpResp);
			}
		} catch (OpenIDException e) {
			throw new ServletException(e);
		}
		return null;
	}

	// processing the authentication response
	public Identifier verifyResponse(HttpServletRequest httpReq,
			HttpServletResponse resp, ParameterList params)
			throws ServletException {
		try {
			// extract the parameters from the authentication response
			// (which comes in as a HTTP request from the OpenID provider)
			ParameterList parameterList = new ParameterList(
					httpReq.getParameterMap());
			parameterList.addParams(params);
			// retrieve the previously stored discovery information
			DiscoveryInformation discovered = (DiscoveryInformation) httpReq
					.getSession().getAttribute("openid-disc");

			// verify the response; ConsumerManager needs to be the same
			// (static) instance used to place the authentication request
			VerificationResult verification = manager.verify(RETURN_TO_URL,
					parameterList, discovered);

			// examine the verification result and extract the verified
			// identifier
			Identifier verified = verification.getVerifiedId();
			if (verified != null) {
				AuthSuccess authSuccess = (AuthSuccess) verification
						.getAuthResponse();

				receiveSimpleRegistration(httpReq, authSuccess);

				receiveAttributeExchange(httpReq, authSuccess);
				return verified; // success
			}
		} catch (OpenIDException e) {
			// present error to the user
			throw new ServletException(e);
		}
		return null;
	}

	/**
	 * @param httpReq
	 * @param authSuccess
	 * @throws MessageException
	 */
	private void receiveSimpleRegistration(HttpServletRequest httpReq,
			AuthSuccess authSuccess) throws MessageException {
		if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
			MessageExtension ext = authSuccess
					.getExtension(SRegMessage.OPENID_NS_SREG);
			if (ext instanceof SRegResponse) {
				SRegResponse sregResp = (SRegResponse) ext;
				for (Iterator iter = sregResp.getAttributeNames().iterator(); iter
						.hasNext();) {
					String name = (String) iter.next();
					String value = sregResp.getParameterValue(name);
					httpReq.setAttribute(name, value);
				}
			}
		}
	}

	/**
	 * @param httpReq
	 * @param authSuccess
	 * @throws MessageException
	 */
	private void receiveAttributeExchange(HttpServletRequest httpReq,
			AuthSuccess authSuccess) throws MessageException {
		if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
			FetchResponse fetchResp = (FetchResponse) authSuccess
					.getExtension(AxMessage.OPENID_NS_AX);
			List<String> aliases = fetchResp.getAttributeAliases();
			Map<String, String> attributes = new LinkedHashMap<String, String>();
			for (Iterator<String> iter = aliases.iterator(); iter.hasNext();) {
				String alias = (String) iter.next();
				List<String> values = fetchResp.getAttributeValues(alias);
				if (values.size() > 0) {
					String[] arr = new String[values.size()];
					values.toArray(arr);
					attributes.put(alias, arr[0]);
				}
			}
			httpReq.setAttribute("attributes", attributes);
		}
	}

	@POST
	@Path("/validate")
	@Produces("application/json")
	public Response validateToken(@HeaderParam("username") String user,
			@HeaderParam("token") String token) {
		// String user = request.getHeader("username");
		// String token = request.getHeader("token");

		LOG.debug("username: " + user);
		LOG.debug("token: " + token);

		if (user == null || token == null) {
			LOG.debug("user or token = null");
			return Response.status(403).build();
		}
		if (userTokenMap.containsKey(user)) {
			if (userTokenMap.get(user).equals(token)) {
				LOG.debug("valid!!");
				userTokenMap.remove(user);
				return Response.ok().build();
			}
		}

		LOG.debug("failed!");
		return Response.status(403).build();
	}
}