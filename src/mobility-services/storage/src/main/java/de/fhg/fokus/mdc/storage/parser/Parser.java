package de.fhg.fokus.mdc.storage.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface Parser {
	public String createTableSqlQueryString(InputStream file, String tableName)
			throws IOException;

	public List<String> createInsertSqlQueryString(InputStream file,
			String tableName) throws IOException;

	public List<String> updateTableSqlQueryString(
			ByteArrayInputStream byteArrayInputStream, String tableName) throws IOException;
}
