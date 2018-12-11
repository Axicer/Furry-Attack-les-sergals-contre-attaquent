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

	//current animation in use
	private SingleAnimation currentAnimation;
	//default animation used if actual is null
	private AnimationType defaultAnimationType;
	//actual animation used
    private AnimationType actualAnimationType;
    //list of all animations from a character
    private List<SingleAnimation> animations;
    //entity to move
    private ModelisedEntity entity;

    /**
     * Constructor of an animation
     * @param entity {@link ModelisedEntity} which will be updated
     * @param json {@link JSONObject} data to parse
     */
	public Animation(ModelisedEntity entity, JSONObject json) {
    	//set entity and init list
    	this.entity = entity;
    	this.animations = new ArrayList<>();
    	try {
    		//load types
    		this.defaultAnimationType = AnimationType.fromString((String)json.get("default"));
    		this.actualAnimationType = defaultAnimationType;
    		//load animations
    		this.animations = new ArrayList<>();
    		//for each animations in data
    		JSONArray anims = (JSONArray)json.get("animations");
    		for(Object obj : anims) {
    			//create an animation and add it to the list
    			SingleAnimation anim = new SingleAnimation((JSONObject)obj);
    			animations.add(anim);    			
    		}
    		//set current animation to default
    		this.currentAnimation = getSingleAnimationByType(defaultAnimationType);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    public void setActualAnimationType(AnimationType type) {
    	this.actualAnimationType = type;
    	//try to get the actual animation or default if not found
    	this.currentAnimation = getSingleAnimationByType(actualAnimationType);
    	if(this.currentAnimation == null) {
    		this.currentAnimation = getSingleAnimationByType(defaultAnimationType);
    	}
    }

    private SingleAnimation getSingleAnimationByType(AnimationType type){
    	for(SingleAnimation anim : animations) {
    		if(anim.getAnimationType().equals(type)) {
    			return anim;
    		}
    	}
    	return null;
    }
    
    /**
     * Update the animation
     */
    public void update() {
    	//update the single entity
    	this.currentAnimation.update(entity);
    }

}