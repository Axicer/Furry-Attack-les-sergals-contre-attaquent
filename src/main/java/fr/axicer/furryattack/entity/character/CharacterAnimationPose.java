package fr.axicer.furryattack.entity.character;

import java.util.Map;

import org.joml.Matrix4f;
import org.json.simple.JSONObject;

import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.Tuple;

/**
 * A character animation's pose
 * @author Axicer
 *
 */
public class CharacterAnimationPose implements Updateable{

	private Map<Integer, Tuple<Matrix4f, Float>> partPos;
	private int duration, time;
	
	public CharacterAnimationPose(JSONObject pose) {
		//TODO fill poses list from json
	}
	
	/**
	 * Apply the current pose to the character
	 */
	public void applyPose(Character character) {
		for(int id : partPos.keySet()) {
			character.getRoot().setPose(id, partPos.get(id).getT(), partPos.get(id).getU());
		}
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
