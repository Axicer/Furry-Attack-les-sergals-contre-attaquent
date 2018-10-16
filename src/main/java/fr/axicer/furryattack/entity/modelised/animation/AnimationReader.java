package fr.axicer.furryattack.entity.modelised.animation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.axicer.furryattack.entity.modelised.ModelisedEntity;
import fr.axicer.furryattack.entity.modelised.model.PartReader;
import fr.axicer.furryattack.util.Util;

/**
 * static class for animation reading
 * @author Axicer
 *
 */
public class AnimationReader {

	/**
	 * Create a new {@link fr.axicer.furryattack.entity.animation.Animation}
	 * @param entity {@link ModelisedEntity} to move
	 * @param animPath path to anim json {@link String}
	 * @return
	 */
    public static Animation create(ModelisedEntity entity, String animPath) {
    	String jsonString = Util.getStringFromInputStream(PartReader.class.getResourceAsStream(animPath));
		try {
			JSONObject jobj = (JSONObject) new JSONParser().parse(jsonString);
			return new Animation(entity, jobj);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return null;
    }

}