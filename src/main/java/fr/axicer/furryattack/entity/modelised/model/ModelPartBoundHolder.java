package fr.axicer.furryattack.entity.modelised.model;

import org.joml.Vector4f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.axicer.furryattack.util.Util;

public class ModelPartBoundHolder{
	
	private JSONObject json;
	
	/**
	 * Constructor
	 * @param path path to .part json file
	 */
	public ModelPartBoundHolder(String path) {
		try {
			this.json = (JSONObject)new JSONParser().parse(Util.getStringFromInputStream(ModelPartReader.class.getResourceAsStream(path)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the bounds for a given ID
	 * @param ID int id to get bounds
	 * @return {@link Vector4f} bounds or null if not found or a parsing error occurs
	 */
	@SuppressWarnings("unchecked")
	public Vector4f getBounds(int ID) {
		//check for json not null
		if(this.json == null)return null;
		try {
			//get all parts
			JSONArray partsArray = (JSONArray) this.json.getOrDefault("parts", null);
			//if we get nothing return null
			if(partsArray == null)return null;
			//iterating through each part
			for(Object obj : partsArray) {	
				JSONObject partjson = (JSONObject)obj;
				int id = (int)(long)partjson.getOrDefault("id", -1);
				//if ids matches
				if(id == ID) {
					//return bounds or null if a arsing error occurs
					JSONArray array = (JSONArray) partjson.getOrDefault("bounds", null);
					return Util.fromJSONToVector4f(array);
				}
			};
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
