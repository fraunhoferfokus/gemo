package de.fhg.fokus.mdc.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.stereotype.Component;

/**
 * Used to serialize Java.util.Date, which is not a common JSON type, so we have
 * to create a custom serialize method,
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 */
@Component
public class JsonDateSerializer extends JsonSerializer<Date> {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd H:m:s");

	@Override
	public void serialize(Date date, JsonGenerator gen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

		String formattedDate = dateFormat.format(date);

		gen.writeString(formattedDate);
	}

}