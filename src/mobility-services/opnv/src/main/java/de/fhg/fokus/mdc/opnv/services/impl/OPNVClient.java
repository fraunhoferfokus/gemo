package de.fhg.fokus.mdc.opnv.services.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;

import de.fhg.fokus.mdc.opnv.schema.ReqC;
import de.fhg.fokus.mdc.opnv.schema.ResC;

//import org.springframework.http.HttpEntity;

/**
 * each complaint is an xml file GET cm/complaints/{complaintid} returns XML
 * file
 */
public class OPNVClient {
	/** The instance for the singleton pattern. */
	private static OPNVClient instance = null;

	/** The url string. */
	private String url = null;

	/** The local REST template. */
	private RestTemplate restTemplate = null;
	private XPathOperations xpathTemplate = null;

	/**
	 * Function to deliver the singleton instance.
	 * 
	 * @param url
	 *            the url of the REST service.
	 * 
	 * @return the pre-configured data store client.
	 */
	public static OPNVClient getInstance(String url) {
		if (instance == null) {
			instance = new OPNVClient(url);
		}

		return instance;
	}

	/**
	 * Constructor for the singleton pattern.
	 * 
	 * @param url
	 *            the url of the REST service.
	 */
	private OPNVClient(String url) {
		this.url = url;
		// set up RestTemplate to use HttpComponentsClientHttpRequestFactory

		restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory());
		xpathTemplate = new Jaxp13XPathTemplate();
	}

	public void setRestTemplate(RestTemplate rt) {
		this.restTemplate = rt;
	}

	public ResponseEntity<ResC> getRouteXML(ReqC reqc) {
		// no need to marshall
		ResponseEntity<ResC> toreturn = null;
		MarshallingHttpMessageConverter mmc = new MarshallingHttpMessageConverter();
		XStreamMarshaller xsmarshaller = new XStreamMarshaller();
		mmc.setMarshaller(xsmarshaller);
		mmc.setUnmarshaller(xsmarshaller);
		xsmarshaller.addAlias("Connection",
				de.fhg.fokus.mdc.opnv.schema.ResC.class);
		restTemplate.getMessageConverters().add(mmc);

		// try {
		//
		// JAXBContext jaxbContext = JAXBContext.newInstance(ReqC.class);
		// Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		//
		// // output pretty printed
		// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// jaxbMarshaller.marshal(reqc, xmlstream);

		// } catch (JAXBException e) {
		// e.printStackTrace();
		// }
		//
		// }
		toreturn = restTemplate.postForEntity(url, reqc, ResC.class);

		// HttpEntity<ReqC> requestEntity = new HttpEntity<ReqC>(reqc);
		// toreturn = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
		// Connection.class);
		// xpathTemplate.evaluate(arg0, arg1, arg2)
		// toreturn = restTemplate.getForObject(url + path, Source.class);
		if (toreturn == null) {
			// return HttpStatus.BAD_REQUEST;
		}
		return toreturn;

	}
}
