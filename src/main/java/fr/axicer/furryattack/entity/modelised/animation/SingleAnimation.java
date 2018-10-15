package fr.axicer.furryattack.entity.modelised.animation;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.axicer.furryattack.entity.modelised.ModelisedEntity;

/**
 * A single animation class
 * @author Axicer
 *
 */
public class SingleAnimation {

    private int actualPoseID = 0;
    private int poseCount;
    private List<AnimationPose> poses;
    private AnimationType type;


    /**
     * {@link SingleAnimation} constructor
     * @param json {@link JSONObject} to load animation datas
     */
    @SuppressWarnings("unchecked")
	public SingleAnimation(JSONObject json) {
        try {
        	//set type
        	this.type = AnimationType.fromString((String)json.get("name"));
        	//set pose count
        	JSONArray posesJson = (JSONArray)json.get("poses");
        	this.poseCount = posesJson.size();
        	//set poses
        	this.poses = new ArrayList<>();
        	posesJson.forEach(obj ->{
        		AnimationPose pose = new AnimationPose((JSONObject)obj);
        		poses.add(pose);
        	});
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    public AnimationType getAnimationType() {
		return this.type;
    }

    public void update(ModelisedEntity entity) {
    	if(this.poses.get(actualPoseID).isEnded()) {
    		actualPoseID = (actualPoseID+1)%poseCount;
    	}
    	this.poses.get(actualPoseID).update(entity);
    }

}