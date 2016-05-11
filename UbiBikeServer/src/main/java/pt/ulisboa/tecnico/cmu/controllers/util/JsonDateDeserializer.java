package pt.ulisboa.tecnico.cmu.controllers.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@Component
public class JsonDateDeserializer extends JsonDeserializer<Date>{

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext arg1) 
			throws IOException, JsonProcessingException {
		 try
	        {
	            return dateFormat.parse(jsonParser.getText());
	        }catch (java.text.ParseException e) {
	        	throw new JsonParseException("Could not parse date", jsonParser.getCurrentLocation(), e);
			}
	}

}
