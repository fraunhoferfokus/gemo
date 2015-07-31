package de.fhg.fokus.mdc.storage.parser.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.Ostermiller.util.BadDelimiterException;

import de.fhg.fokus.mdc.storage.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlParser implements Parser {
    
        private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	/**
	 * The method is meant to create update SQL queries from the contents of a
	 * file.
	 * 
	 * @param byteArrayInputStream
	 *            the input stream containing the data.
	 * @param tableName
	 *            the name of the to update.
	 * 
	 * @return list of update queries.
	 */
	public List<String> updateTableSqlQueryString(
			ByteArrayInputStream byteArrayInputStream, String tableName)
			throws IOException {
		// TODO: implement
		return null;
	}

	public String createTableSqlQueryString(InputStream file, String tableName)
			throws IOException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);

		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");

		XmlHandler xmlHandler = new XmlHandler(true, tableName);
		XMLReader reader;

		try {
			factory.setSchema(schemaFactory
					.newSchema(new Source[] { new StreamSource(getClass()
							.getResourceAsStream("/db.xsd")) }));
			reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(xmlHandler);
			reader.parse(new InputSource(file));
		} catch (SAXException e) {
			if (e.getClass().isInstance(new BreakSaxException())) {
				// breaking sax parsing...
                            LOGGER.debug(e.getMessage());
			} else {
                            LOGGER.error(e.getMessage());
			}
		}

		return xmlHandler.getCreateTableQuery();
	}

	public List<String> createInsertSqlQueryString(InputStream file,
			String tableName) throws BadDelimiterException, IOException {

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);

		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");

		XmlHandler xmlHandler = new XmlHandler(false, tableName);
		XMLReader reader;

		try {
			factory.setSchema(schemaFactory
					.newSchema(new Source[] { new StreamSource(getClass()
							.getResourceAsStream("/db.xsd")) }));
			reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(xmlHandler);
			reader.parse(new InputSource(file));
		} catch (SAXException e) {
			LOGGER.error(e.getMessage());
		}

		return xmlHandler.getInsertQueries();
	}

}
