package fr.axicer.furryattack.entity.modelised.model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.axicer.furryattack.entity.modelised.ModelisedEntity;
import fr.axicer.furryattack.util.Util;

/**
 * static class for model rendering
 * @author Axicer
 *
 */
public class ModelPartReader {

	/**
	 * Create a new model from given data
	 * @param partholder {@link PartHolder} containing part already created
	 * @param modelPath path to json model parts {@link String}
	 * @return the root of all the parts which should be id 0
	 */
    @SuppressWarnings("unchecked")
	public static ModelPart create(ModelisedEntity entity, JSONObject modelJson) {
    	try {
    		//load the bound holder
    		String holderPath = (String) modelJson.getOrDefault("part", null);
    		ModelPartBoundHolder holder = new ModelPartBoundHolder(holderPath);
			//create a list of loaded parts
			List<ModelPart> loadedParts = new ArrayList<>();
			//read parts array
			JSONArray partsModelsArray = (JSONArray) modelJson.getOrDefault("model", null);
			//if null then return null
			if(partsModelsArray == null)return null;
			//iterate through each object readed
			for(Object obj : partsModelsArray) {
				//cats as a jsonObject
				JSONObject modelData = (JSONObject)obj;
				//read the id
				int id = (int)(long)modelData.getOrDefault("id", -1);
				//if -1 then do not load this part
				if(id == -1)continue;
				//else read data
				float rotation = (float)(double)modelData.getOrDefault("rotation", 0f);
				//load sizes times map's tile size
	    		float width = (float)(double)modelData.getOrDefault("width", 0f)*ModelPart.TMP_TILE_SIZE;
	    		float height = (float)(double)modelData.getOrDefault("height", 0f)*ModelPart.TMP_TILE_SIZE;
	    		
	    		//load translation
	    		JSONArray translationArray = (JSONArray) modelData.getOrDefault("translation", null);
	    		Vector2f translation = Util.fromJSONToVector2f(translationArray);
	    		if(translation == null)translation = new Vector2f();
	    		translation.mul(ModelPart.TMP_TILE_SIZE);
	    		
	    		//load child array
	    		JSONArray childs = (JSONArray)modelData.getOrDefault("child", null);
	    		int[] childArray = new int[0];
	    		if(childs != null) {
		    		childArray = new int[childs.size()];
		    		for(int i = 0 ; i < childArray.length; i++) {
		    			childArray[i] = (int)(long)childs.get(i);
		    		}
	    		}
	    		
	    		//create modelPart
	    		ModelPart part = new ModelPart(entity, id, width, height, rotation, translation, holder, childArray);
	    		//and add part to loaded ones
	    		loadedParts.add(part);
			}
			ModelPart p = null;
			//get the part width id 0 which is the root
			for(ModelPart part : loadedParts) {
				//inflate data to part
				part.inflate(loadedParts);
				//check for id ==0
				if(part.partID==0) {
					//and set it
					p = part;
				}
			}
			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    

}