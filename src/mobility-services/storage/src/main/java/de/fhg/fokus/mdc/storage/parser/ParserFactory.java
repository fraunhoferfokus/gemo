package de.fhg.fokus.mdc.storage.parser;

import java.io.IOException;

import org.hibernate.cfg.NotYetImplementedException;

import de.fhg.fokus.mdc.storage.parser.csv.CsvParser;
import de.fhg.fokus.mdc.storage.parser.xml.XmlParser;

public class ParserFactory {

	public static Parser createParser(String format) throws IOException {

		Parser parser = null;

		if (format.equalsIgnoreCase(FileFormats.CSV.toString())) {
			parser = new CsvParser();
		} else if (format.equalsIgnoreCase(FileFormats.JSON.toString())) {
			throw new NotYetImplementedException();
		} else if (format.equalsIgnoreCase(FileFormats.RDF.toString())) {
			throw new NotYetImplementedException();
		} else if (format.equalsIgnoreCase(FileFormats.XML.toString())) {
			parser = new XmlParser();
		}

		return parser;
	}
}
