package fr.axicer.furryattack.entity.modelised.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.axicer.furryattack.util.Util;

public class PartReader {
	
	/**
	 * Create a new partHolder filled with data given in parameters
	 * @param partPath path to json file to load data 
	 * @return filled {@link PartHolder} with all part from config of null if an error was detected
	 */
    public static PartHolder getNewPartHolder(String partPath) {
    	try {
    		String jsonString = Util.getStringFromInputStream(PartReader.class.getResourceAsStream(partPath));
			JSONObject jobj = (JSONObject) new JSONParser().parse(jsonString);
			return new PartHolder(jobj);
    	} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }

}