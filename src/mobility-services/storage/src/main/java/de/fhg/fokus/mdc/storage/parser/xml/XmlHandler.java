package de.fhg.fokus.mdc.storage.parser.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class XmlHandler implements ContentHandler {

	private final Logger logger = Logger.getLogger(XmlHandler.class.getName());
	private final String SCHEMA_DSC_TAG = "schema_dsc";
	private final String FIELD_ATTR = "field";
	private final String DATA_TAG = "data";
	private final String DATASET_TAG = "dataset";
	private StringBuilder sqlQuery;
	private final List<String> sqlQueries;
	private final String tableName;
	private final boolean schemaDsc;

	private List<String> columns;
	private List<String> values;
	private final Stack<String> currentElement;
	private final long idCount;

	/**
	 * @param schemaDsc
	 *            Indicates wether to break after schema tag or not.
	 * */
	public XmlHandler(boolean schemaDsc, String tableName) {
		super();
		this.schemaDsc = schemaDsc;
		this.tableName = tableName;
		this.sqlQuery = new StringBuilder();
		this.sqlQueries = new ArrayList<String>();
		this.currentElement = new Stack<String>();
		this.idCount = 0;
	}

	@Override
	public void startDocument() throws SAXException {
		logger.log(Level.INFO, "XML document parsing starts");
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		currentElement.push(qName);
		if (qName.equals(DATASET_TAG)) {
			return;
		}
		String parentElement = currentElement.get(currentElement.size() - 2);

		if (qName.equals(SCHEMA_DSC_TAG) && schemaDsc) {
			sqlQuery = new StringBuilder();
			sqlQuery.append("CREATE TABLE ");
			sqlQuery.append(tableName).append(" (");
			sqlQuery.append("_id").append(" ").append("SERIAL,");
			// sqlQuery.append("PRIMARY KEY (_id),");
		} else if (parentElement.equals(SCHEMA_DSC_TAG) && schemaDsc) {
			sqlQuery.append(attributes.getValue(FIELD_ATTR) + " " + qName + typeInfo(localName) + ",");
		} else if (parentElement.equals(DATA_TAG) && !qName.equals(DATA_TAG)) {
			columns.add(attributes.getValue(FIELD_ATTR));
		} else if (qName.equals(DATA_TAG)) {
			sqlQuery = new StringBuilder();
			columns = new ArrayList<String>();
			values = new ArrayList<String>();
		}
	}

	private String typeInfo(String type) {
		String result = "";

		if (type.equals("float")) {
			result = "(50)";
		}

		return result;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (qName.equals(DATA_TAG)) {
			// sqlQuery.append("INSERT INTO " + tableName + "(_id,");
			sqlQuery.append("INSERT INTO " + tableName + "(");
			for (String column : columns) {
				sqlQuery.append(column).append(",");
			}
			sqlQuery.deleteCharAt(sqlQuery.length() - 1).append(") VALUES (");
			// sqlQuery.append(idCount).append(",");
			// idCount++;
			for (String value : values) {
				sqlQuery.append("'" + value + "'").append(",");
			}
			sqlQuery.deleteCharAt(sqlQuery.length() - 1).append(")");

			sqlQueries.add(sqlQuery.toString());
		} else if (qName.equals(SCHEMA_DSC_TAG) & schemaDsc) {
			sqlQuery.append("PRIMARY KEY (_id))");
			throw new SAXException(new BreakSaxException());
		}
		currentElement.pop();
	}

	@Override
	public void endDocument() throws SAXException {
		logger.log(Level.INFO, "XML document parsing ends");
	}

	public String getCreateTableQuery() {
		return this.sqlQuery.toString();
	}

	public List<String> getInsertQueries() {
		return sqlQueries;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String cdata = new String(ch, start, length);

		if (currentElement.size() >= 2) {
			if (currentElement.get(currentElement.size() - 2).equals(DATA_TAG)) {
				values.add(cdata);
			}
		}
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// TODO Auto-generated method stub

	}

}
