package fr.axicer.furryattack.entity.modelised.animation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.axicer.furryattack.entity.modelised.ModelisedEntity;

/**
 * A pose for a single animation class
 * @author Axicer
 *
 */
public class AnimationPose {

	private int counter = 0;
    private int duration;
    private List<Integer> visbleID;
    private List<AnimationPosePartData> datas;

    /**
     * Constructor of a pose
     * @param json {@link JSONObject} data to parse
     */
    @SuppressWarnings("unchecked")
	public AnimationPose(JSONObject json) {
    	try {
    		//set duration
    		this.duration = (int)json.get("duration");
    		//set visibles
    		JSONArray visibles = (JSONArray)json.get("visibles");
    		this.visbleID = new ArrayList<>();
    		visibles.forEach(obj ->{
    			this.visbleID.add((int)obj);
    		});
    		//set datas
    		this.datas = new ArrayList<>();
    		JSONArray parts = (JSONArray)json.get("data");
    		parts.forEach(obj ->{
    			AnimationPosePartData partData = new AnimationPosePartData((JSONObject)obj);
    			datas.add(partData);
    		});
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    public int getDuration() {
        return this.duration;
    }

    public List<Integer> getVisibleParts() {
        return this.visbleID;
    }

    /**
     * Appy the current pose to the given {@link ModelisedEntity}
     * @param entity {@link ModelisedEntity} to apply pose
     */
    public void applyPose(ModelisedEntity entity) {
    	//for each data pose, filter only valid poses the apply pose
        datas.stream().filter(data -> data.isValid()).collect(Collectors.toList()).forEach(data -> {
        	data.apply(entity);
        });
    }

    public boolean isEnded() {
    	return counter >= duration;
    }
    
    public void update(ModelisedEntity entity) {
    	if(counter == 0) {
    		applyPose(entity);
    	}
    	counter++;
    }
}