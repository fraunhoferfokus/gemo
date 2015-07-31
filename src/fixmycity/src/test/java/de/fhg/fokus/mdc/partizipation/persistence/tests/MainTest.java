package de.fhg.fokus.mdc.partizipation.persistence.tests;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;

public class MainTest {
	private static JAXBContext context;
	private static EntityManagerFactory entityManagerFactory;

	public void setUp() {

	}

	@Test
	public void test() throws IOException, JAXBException,
			XPathExpressionException {
		//
		// context =
		// JAXBContext.newInstance("org.jvnet.hyperjaxb3.ejb.tests.po");
		// ObjectFactory objectFactory = new ObjectFactory();
		// // Unmarshaller
		// final Unmarshaller unmarshaller = context.createUnmarshaller();
		// // Unmarshal
		// final Object object = unmarshaller.unmarshal(new File(
		// "src/test/samples/po.xml"));
		// // Cast
		// @SuppressWarnings("unchecked")
		// final PurchaseOrderType purchaseOrder =
		// ((JAXBElement<PurchaseOrderType>) object)
		// .getValue();
		//
		// System.out.println("Purchase Order Comment "
		// + purchaseOrder.getComment());
		// // marshall
		// // create an object from the jaxb annotated model class
		// final PurchaseOrderType purchaseOrdertoMarshal = objectFactory
		// .createPurchaseOrderType();
		// purchaseOrdertoMarshal.setShipTo(objectFactory.createUSAddress());
		// purchaseOrdertoMarshal.getShipTo().setCity("New Orleans");
		// // store it in a jaxbelement
		// final JAXBElement<PurchaseOrderType> purchaseOrderElement =
		// objectFactory
		// .createPurchaseOrder(purchaseOrder);
		// final Marshaller marshaller = context.createMarshaller();
		// final DOMResult result = new DOMResult();
		// // here the result of marshalling can be stored in stream or written
		// to
		// // a file, we set dom tree.
		// marshaller.marshal(purchaseOrderElement, result);
		// // check your result by comparing result from dom and xpath query
		// from
		// // xml file
		// final XPathFactory xPathFactory = XPathFactory.newInstance();
		// System.out.println(xPathFactory.newXPath().evaluate(
		// "/purchaseOrder/shipTo/city", result.getNode()));
		//
		// // JPA
		// final Properties persistenceProperties = new Properties();
		// InputStream is = null;
		//
		// try {
		// is = Thread.currentThread().getContextClassLoader()
		// .getResourceAsStream("persistence.properties");
		// persistenceProperties.load(is);
		// System.out.println("after properties load");
		// System.out.println("Url "
		// + persistenceProperties
		// .getProperty("hibernate.connection.url"));
		// } finally {
		// if (is != null) {
		// try {
		//
		// is.close();
		// } catch (IOException ignored) {
		//
		// }
		// }
		// }
		//
		// entityManagerFactory = Persistence.createEntityManagerFactory(
		// "org.jvnet.hyperjaxb3.ejb.tests.po", persistenceProperties);
		//
		// final PurchaseOrderType alpha =
		// objectFactory.createPurchaseOrderType();
		// alpha.setShipTo(objectFactory.createUSAddress());
		// alpha.getShipTo().setCity("izmir");
		// final EntityManager saveManager = entityManagerFactory
		// .createEntityManager();
		// saveManager.getTransaction().begin();
		// saveManager.persist(alpha);
		// saveManager.getTransaction().commit();
		// saveManager.close();
		//
		// final Long id = alpha.getHjid();
		// System.out.println("Purchase Order Id " + id);
		//
		// final EntityManager loadManager = entityManagerFactory
		// .createEntityManager();
		// final PurchaseOrderType beta = loadManager.find(
		// PurchaseOrderType.class, id);
		// loadManager.close();
		// // Check that we're still shipp
		// System.out.println(beta.getShipTo().getCity());
	}

}
