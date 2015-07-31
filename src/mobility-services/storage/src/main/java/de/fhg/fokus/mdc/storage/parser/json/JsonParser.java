package de.fhg.fokus.mdc.storage.parser.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.fhg.fokus.mdc.storage.parser.Parser;

public class JsonParser implements Parser {

	public String createTableSqlQueryString(InputStream file, String tableName)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> createInsertSqlQueryString(InputStream file,
			String tableName) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * The method is meant to create update SQL queries from the contents of a
	 * file.
	 * 
	 * @param byteArrayInputStream
	 *            the input stream containing the data.
	 * @param tableName
	 *            the name of the to update.
	 * 
	 * @return list of SQL update string.
	 */
	public List<String> updateTableSqlQueryString(
			ByteArrayInputStream byteArrayInputStream, String tableName)
			throws IOException {
		// TODO: implement
		return null;
	}

}
