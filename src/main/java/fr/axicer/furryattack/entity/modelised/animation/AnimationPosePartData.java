package fr.axicer.furryattack.entity.modelised.animation;
import org.joml.Matrix4f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.axicer.furryattack.entity.modelised.ModelisedEntity;
import fr.axicer.furryattack.entity.modelised.model.ModelPart;

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
    private Matrix4f localBindTransform;

    /**
     * constructor of this data, parse the {@link JSONObject} given in parameters as a pose data
     * @param json {@link JSONObject} json data to parse
     */
    public AnimationPosePartData(JSONObject json) {
    	try {
    		this.ID = (int)json.get("id");
    		this.rotation = (float)json.get("rotation");
    		this.width = (float)json.get("width");
    		this.height = (float)json.get("height");
    		JSONArray translation = (JSONArray) json.get("translation");
    		this.localBindTransform = new Matrix4f().identity().translate((float)translation.get(0), (float)translation.get(1), (float)translation.get(2));
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
	    	target.setRotation(rotation);
	    	target.setWidth(width);
	    	target.setHeight(height);
	    	
    		//this will also update the root transform
    		target.setLocalBindTransform(localBindTransform);
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