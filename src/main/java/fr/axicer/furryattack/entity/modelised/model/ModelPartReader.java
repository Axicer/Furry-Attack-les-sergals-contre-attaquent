package fr.axicer.furryattack.entity.modelised.model;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.axicer.furryattack.render.textures.Texture;
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
	public static ModelPart create(PartHolder partholder, String modelPath) {
    	try {
			JSONObject modelJson = (JSONObject)new JSONParser().parse(Util.getStringFromInputStream(ModelPartReader.class.getResourceAsStream(modelPath)));
			Texture tex = Texture.loadTexture((String) modelJson.get("texture"), GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
			List<ModelPart> tmppart = new ArrayList<>();
			JSONArray models = (JSONArray) modelJson.get("model");
			models.forEach(obj ->{
				JSONObject modelData = (JSONObject)obj;
				int id = (int)modelData.get("id");
				if(partholder.getPart(id) != null) {
					//get data
					float rotation = (float)modelData.get("rotation");
		    		float width = (float)modelData.get("width");
		    		float height = (float)modelData.get("height");
		    		JSONArray translation = (JSONArray) modelData.get("translation");
		    		Matrix4f localBindTransform = new Matrix4f().identity().translate((float)translation.get(0), (float)translation.get(1), (float)translation.get(2));
		    		JSONArray childs = (JSONArray)modelData.get("child");
		    		int[] childArray = new int[childs.size()];
		    		for(int i = 0 ; i < childArray.length; i++) {
		    			childArray[i] = (int)childs.get(i);
		    		}
		    		
		    		//create modelPart
		    		ModelPart part = new ModelPart(id, partholder, width, height, rotation, tex, localBindTransform, childArray);
		    		tmppart.add(part);
				}
			});
			//get the part width id 0 which is the root
			for(ModelPart part : tmppart) {
				//check for id ==0
				if(part.getID()==0) {
					//and return it
					return part;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }

}