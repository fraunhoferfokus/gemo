package de.fhg.fokus.mdc.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.springframework.stereotype.Component;

/**
 * Used to deserialize the incoming date format (from storage), which is not
 * common, so we have to create a custom serialize method,
 * 
 * @author dsc (Danilo Schmidt, danilo.schmidt@fokus.fraunhofer.de)
 */
@Component
public class JsonDateDeserializer extends JsonDeserializer<Date> {
	@Override
	public Date deserialize(JsonParser jsonparser,
			DeserializationContext deserializationcontext) throws IOException,
			JsonProcessingException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");// normally: "yyyy-MM-dd'T'HH:mm:ss"
		String date = jsonparser.getText();
		try {
			return format.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

	}

}
