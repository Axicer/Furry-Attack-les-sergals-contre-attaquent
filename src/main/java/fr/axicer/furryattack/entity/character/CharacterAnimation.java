package fr.axicer.furryattack.entity.character;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.Util;

/**
 * A character animation
 * @author Axicer
 *
 */
public class CharacterAnimation implements Updateable{
	
	//list of all poses
	private List<CharacterAnimationPose> poses;
	//actual pose index
	int actualPose;
	private Character character;
	
	public CharacterAnimation(String path, Character character) {
		this.actualPose = 0;
		this.poses = new ArrayList<>();
		this.character = character;
		try {
			parse((JSONObject) new JSONParser().parse(Util.getStringFromInputStream(CharacterAnimation.class.getResourceAsStream(path))));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void parse(JSONObject json) {
		//TODO parse the actual animation
	}

	@Override
	public void update() {
		if(this.poses.get(actualPose).isEnded()) {
			actualPose = (actualPose+1)%this.poses.size();
		}else {
			this.poses.get(actualPose).update();
		}
		this.poses.get(actualPose).applyPose(character);
	}
}
