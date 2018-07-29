package fr.axicer.furryattack.generator.maps;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.axicer.furryattack.gui.elements.GUIAlignement;
import fr.axicer.furryattack.gui.elements.GUIImage;
import fr.axicer.furryattack.map.AbstractMap;
import fr.axicer.furryattack.map.MapObstacle;
import fr.axicer.furryattack.map.MapObstaclesTextures;
import fr.axicer.furryattack.map.MapType;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.config.Configuration;

/**
 * Generator of maps
 * @author Axicer
 *
 */
public class MapGenerator {
	
	/**
	 * Create a map from a config file
	 * @param config {@link Configuration} config to use
	 * @return {@link AbstractMap} created or null if there is any error
	 */
	@SuppressWarnings("unchecked")
	public static AbstractMap createMap(Configuration config) {
		//get map type
		MapType type = MapType.getMapTypeFromString(config.getString("type", "classic"));
		Class<? extends AbstractMap> clazz = type.getInvokedMapClass();
		try {
			//get constructor and create map
			Constructor<? extends AbstractMap> mapconstructor = clazz.getConstructor(String.class);
			AbstractMap map = mapconstructor.newInstance(config.getString("name", "default-name"));
			map.setGravity(config.getFloat("gravity", 1.0f));
			map.setBackground(new GUIImage("/img/map/"+config.getString("background", "bg")+".png", false, new Vector2f(1), 1f, 1f, new Vector3f(), 0f, 1f, GUIAlignement.CENTER, GUIAlignement.CENTER));
			//add obstacles
			for(Object object : config.getJSONArray("obstacles", new JSONArray()).toArray()) {
				if(object instanceof JSONObject) {
					//get json of every object
					JSONObject obj = (JSONObject) object;
					List<Vector2f> pos = new ArrayList<>();
					
					int pointsCount = 0;
					
					//iterate trough each json object of points
					JSONArray innerArray = (JSONArray)obj.getOrDefault("points", new JSONArray());
					for(Object innerObject : innerArray.toArray()) {
						if(innerObject instanceof JSONObject) {
							JSONObject innerobj = (JSONObject) innerObject;
							//add a new point
							pos.add(new Vector2f((float)((double)innerobj.getOrDefault("x", 0))*Constants.WIDTH, (float)((double)innerobj.getOrDefault("y", 0))*Constants.HEIGHT));
							pointsCount++;
						}
					}
					
					//obstacle's creation
					MapObstacle obstacle = new MapObstacle(pos.toArray(new Vector2f[pointsCount]));
					obstacle.setTexture(MapObstaclesTextures.fromString((String) obj.getOrDefault("texture", "brick")).getTexture());
					obstacle.setTextureMul((float)((double)obj.getOrDefault("textureMulX", 1)), (float)((double)obj.getOrDefault("textureMulY", 1)));
					map.getObstacles().add(obstacle);
				}
			}
			return map;
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
