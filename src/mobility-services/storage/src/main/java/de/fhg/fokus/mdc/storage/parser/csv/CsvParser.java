package de.fhg.fokus.mdc.storage.parser.csv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.LabeledCSVParser;

import de.fhg.fokus.mdc.storage.parser.Parser;

public class CsvParser implements Parser {

	private static final String TEXT_TYPE = "TEXT";

	public String createTableSqlQueryString(InputStream file, String tableName)
			throws IOException {

		String[] csvLabels = new LabeledCSVParser(new CSVParser(
				new InputStreamReader(file), ';')).getLabels();
		StringBuilder createTableQuery = new StringBuilder();

		createTableQuery.append("CREATE TABLE ");
		createTableQuery.append(tableName).append(" (");

		createTableQuery.append("_id").append(" ").append("SERIAL,");
		for (String label : csvLabels) {
			createTableQuery.append(label).append(" ");
			createTableQuery.append(TEXT_TYPE).append(",");
		}
		createTableQuery.append("PRIMARY KEY (_id))");

		return createTableQuery.toString();
	}

	public List<String> createInsertSqlQueryString(InputStream file,
			String tableName) throws IOException {

		List<String> queries = new ArrayList<String>();
		LabeledCSVParser csvParser = new LabeledCSVParser(new CSVParser(
				new InputStreamReader(file), ';'));

		while (csvParser.getLine() != null) {
			StringBuilder insertQuery = new StringBuilder();
			insertQuery.append("INSERT INTO ");
			insertQuery.append(tableName);
			insertQuery.append(" (");
			for (String label : csvParser.getLabels()) {
				insertQuery.append(label);
				insertQuery.append(",");
			}
			insertQuery.deleteCharAt(insertQuery.length() - 1);
			insertQuery.append(") ");
			
			insertQuery.append(" VALUES (");

			for (String label : csvParser.getLabels()) {
				insertQuery.append("'");
				insertQuery.append(csvParser.getValueByLabel(label).replaceAll(
						"'", "''"));
				insertQuery.append("'").append(",");
			}
			/**
			 * Remove trailing comma
			 */
			insertQuery.deleteCharAt(insertQuery.length() - 1);
			insertQuery.append(")");
			queries.add(insertQuery.toString());
		}

		return queries;
	}

	/**
	 * The method is meant to create update SQL queries from the contents of a
	 * file.
	 * 
	 * @param byteArrayInputStream
	 *            the input stream containing the data.
	 * @param tableName
	 *            the name of the to update. *
	 * @return list of SQL update strings.
	 */
	public List<String> updateTableSqlQueryString(
			ByteArrayInputStream byteArrayInputStream, String tableName)
			throws IOException {

		// the list of SQL queries to return
		List<String> queries = new ArrayList<String>();

		// the CVS parser to use
		LabeledCSVParser csvParser = new LabeledCSVParser(new CSVParser(
				new InputStreamReader(byteArrayInputStream), ';'));

		// iterate over the lines of the CSV file
		// update members set gender='male' where name='thomas';
		while (csvParser.getLine() != null) {
			// start building the query
			StringBuilder updateQuery = new StringBuilder();

			updateQuery.append("update " + tableName + " ");
			updateQuery.append("set ");

			// move over the labels and fields
			int count = 0;
			String whereCondition = null; // variable for the where condition
			for (String label : csvParser.getLabels()) {
				// check the label
				if (label.matches("^\\s*[W|w][H|h][E|e][R|r][E|e]\\s*$")) {
					// if this is the where condition
					whereCondition = csvParser.getValueByLabel(label);
					continue;
				}

				// in case of a none-where label
				if (count > 0) {
					updateQuery.append(" , ");
				}
				updateQuery.append(" " + label + "=");
				updateQuery.append("'"
						+ csvParser.getValueByLabel(label)
								.replaceAll("'", "''") + "' ");

				// count the current label
				count++;
			}

			// add the where condition
			if (whereCondition != null) {
				// append the where condition
				updateQuery.append(" where " + whereCondition);
				queries.add(updateQuery.toString());
			}
		}

		return queries;
	}
}
