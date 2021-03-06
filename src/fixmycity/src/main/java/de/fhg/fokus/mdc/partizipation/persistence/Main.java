package de.fhg.fokus.mdc.partizipation.persistence;

 
import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.xpath.XPathExpressionException;


 
/**
 * @author gerard
 *
 */
public class Main {
 
    private static JAXBContext context;
    private static EntityManagerFactory entityManagerFactory;
 
    /**
     * @param args
     * @throws JAXBException 
     * @throws IOException 
     * @throws XPathExpressionException 
     */
    public static void main(String[] args) throws JAXBException, IOException, XPathExpressionException {
 
//	context = JAXBContext.newInstance("org.jvnet.hyperjaxb3.ejb.tests.po");
//	ObjectFactory objectFactory = new ObjectFactory();
//	// Unmarshaller
//	final Unmarshaller unmarshaller = context.createUnmarshaller();
//	// Unmarshal
//	final Object object = unmarshaller.unmarshal(new File("src/test/samples/po.xml"));
//	// Cast
//	@SuppressWarnings("unchecked")
//	final PurchaseOrderType purchaseOrder = ((JAXBElement<PurchaseOrderType>) object).getValue();
// 
//	System.out.println("Purchase Order Comment "+purchaseOrder.getComment());
//     //marshall
//	//create an object from the jaxb annotated model class
//	final PurchaseOrderType purchaseOrdertoMarshal = objectFactory.createPurchaseOrderType();
//	purchaseOrdertoMarshal.setShipTo(objectFactory.createUSAddress());
//	purchaseOrdertoMarshal.getShipTo().setCity("New Orleans");
//	//store it in a jaxbelement
//	final JAXBElement<PurchaseOrderType> purchaseOrderElement = objectFactory
//		.createPurchaseOrder(purchaseOrder);
//	final Marshaller marshaller = context.createMarshaller();
//	final DOMResult result = new DOMResult();
//	//here the result of marshalling can be stored in stream or written to a file, we set dom tree.
//	marshaller.marshal(purchaseOrderElement, result);
//	//check your result by comparing result from dom and xpath query from xml file
//	final XPathFactory xPathFactory = XPathFactory.newInstance();
//	System.out.println(xPathFactory.newXPath().evaluate("/purchaseOrder/shipTo/city",result.getNode()));
//	
//	
//	final Properties persistenceProperties = new Properties();
//	InputStream is = null;
//	
//	try {
//		//URL url = Thread.currentThread().getContextClassLoader().getResource("/persistence.properties");
//		
//		is= Main.class.getResourceAsStream("/persistence.properties");
//		//persistenceProperties.load(url.openStream());
//		persistenceProperties.load(is);
//		System.out.println("after properties load");
//	} finally {
//		if (is != null) {
//			try {
//				
//				is.close();
//			} catch (IOException ignored) {
//				
//			}
//		}
//	}
//	System.out.println("Url "+persistenceProperties.getProperty("hibernate.connection.url"));
// 
//	//JPA
////	entityManagerFactory = Persistence.createEntityManagerFactory(
////			"org.jvnet.hyperjaxb3.ejb.tests.po", persistenceProperties);
// 
////	final EntityManager saveManager = entityManagerFactory
////		.createEntityManager();
////	saveManager.getTransaction().begin();
////	saveManager.persist(purchaseOrder);
////	saveManager.getTransaction().commit();
////	saveManager.close();
//// 
////	final Long id = purchaseOrder.getHjid();
////	System.out.println("Purchase Order Id "+id);
// 
  }
 
}

