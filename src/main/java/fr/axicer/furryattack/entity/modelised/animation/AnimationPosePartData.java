package fr.axicer.furryattack.entity.modelised.animation;
import org.joml.Vector2f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.axicer.furryattack.entity.modelised.ModelisedEntity;
import fr.axicer.furryattack.entity.modelised.model.ModelPart;
import fr.axicer.furryattack.map.TileContainer;
import fr.axicer.furryattack.util.Util;

/**
 * A single part data for a specified animation class
 * @author Axicer
 *
 */
public class AnimationPosePartData {

    private int ID;
    private float rotation;
    private float width;
    private float height;
    private Vector2f localTransform;

    /**
     * constructor of this data, parse the {@link JSONObject} given in parameters as a pose data
     * @param json {@link JSONObject} json data to parse
     */
    public AnimationPosePartData(JSONObject json) {
    	try {
    		this.ID = (int)(long)json.get("id");
    		this.rotation = json.containsKey("rotation") ? (float)(double)json.get("rotation") : -1;
    		this.width = json.containsKey("width") ? (float)(double)json.get("width")*TileContainer.TILE_SIZE : -1;
    		this.height = json.containsKey("height") ? (float)(double)json.get("height")*TileContainer.TILE_SIZE : -1;
    		if(json.containsKey("translation")) {
    			JSONArray translation = (JSONArray) json.get("translation");
    			this.localTransform = Util.fromJSONToVector2f(translation);
    		}else {
    			this.localTransform = null;
    		}
    	}catch(Exception e) {
    		//if any exception is detected when parsing then set ID to -1 (which means invalid pose)
    		e.printStackTrace();
    		this.ID = -1;
    	}
    }

    /**
     * Apply the current pose to the current {@link ModelPart}
     * @param entity entity to get {@link ModelPart} from
     */
    public void apply(ModelisedEntity entity) {
    	ModelPart target = entity.getSpecificModelPart(ID);
    	if(target != null) {
	    	if(rotation != -1)target.setRotation(rotation);
	    	if(width != -1)target.setWidth(width);
	    	if(height != -1)target.setHeight(height);
	    	if(localTransform != null)target.setTranslation(localTransform);
    	}else {
    		System.out.println("UNABLE TO LOAD POSE TO A ENTITY WITHOUT THE GIVEN ID FOR MODELPART");
    	}
    }
    
    /**
     * Get whether this data is valid (represented as ID = -1 here)
     * @return
     */
    public boolean isValid() {
    	return this.ID != -1;
    }

}