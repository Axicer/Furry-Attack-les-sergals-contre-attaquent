package fr.axicer.furryattack.entity.character;

import java.util.Map;

import org.joml.Matrix4f;
import org.json.simple.JSONObject;

import fr.axicer.furryattack.render.Updateable;

/**
 * A character animation's pose
 * @author Axicer
 *
 */
public class CharacterAnimationPose implements Updateable{

	private Map<Integer, Matrix4f> partPos;
	private int duration, time;
	private boolean ended = false;

	public CharacterAnimationPose(JSONObject pose) {
		//TODO fill poses list from json
	}
	
	/**
	 * Apply the current pose to the character
	 */
	public void applyPose(Character character) {
		//TODO
	}
	
	/**
	 * Get whether the pose should change = this pose is ended
	 * @return {@link Boolean} ended
	 */
	protected boolean isEnded() {
		return time >= duration;
	}

	@Override
	public void update() {
		time++;
	}
	
}
