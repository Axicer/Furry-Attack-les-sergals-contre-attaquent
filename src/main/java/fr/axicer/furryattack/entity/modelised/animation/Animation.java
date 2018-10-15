package fr.axicer.furryattack.entity.modelised.animation;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.axicer.furryattack.entity.modelised.ModelisedEntity;

/**
 * A {@link ModelisedEntity} animation
 * @author Axicer
 *
 */
public class Animation {

	private AnimationType defaultAnimationType;
    private AnimationType actualAnimationType;
    private List<SingleAnimation> animations;
    private ModelisedEntity entity;

    /**
     * Constructor of an animation
     * @param entity {@link ModelisedEntity} which will be updated
     * @param json {@link JSONObject} data to parse
     */
    @SuppressWarnings("unchecked")
	public Animation(ModelisedEntity entity, JSONObject json) {
    	this.entity = entity;
    	try {
    		//load types
    		this.defaultAnimationType = AnimationType.fromString((String)json.get("default"));
    		this.actualAnimationType = defaultAnimationType;
    		//load animations
    		this.animations = new ArrayList<>();
    		JSONArray anims = (JSONArray)json.get("animations");
    		anims.forEach(obj ->{
    			SingleAnimation anim = new SingleAnimation((JSONObject)json);
    			animations.add(anim);
    		});
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    public void setActualAnimationType(AnimationType type) {
    	this.actualAnimationType = type;
    }

    private SingleAnimation getSingleAnimationByType(AnimationType type){
    	for(SingleAnimation anim : animations) {
    		if(anim.getAnimationType().equals(type)) {
    			return anim;
    		}
    	}
    	return null;
    }
    
    public void update() {
    	SingleAnimation anim = getSingleAnimationByType(actualAnimationType);
    	if(anim == null) {
    		anim = getSingleAnimationByType(defaultAnimationType);
    	}
    	anim.update(entity);
    }

}