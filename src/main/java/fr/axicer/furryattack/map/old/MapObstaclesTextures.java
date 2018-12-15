package fr.axicer.furryattack.map.old;

import org.lwjgl.opengl.GL11;

import fr.axicer.furryattack.render.textures.Texture;

/**
 * Texture enum relative to obstacles available texture
 * @author Axicer
 *
 */
public enum MapObstaclesTextures {
	/**
	 * Default texture enum
	 */
	BRICK("/img/map/brick.jpg");
	
	/**
	 * texture associated to the enum
	 */
	private Texture tex;
	
	/**
	 * Constructor of a {@link MapObstaclesTextures} enum
	 * @param path
	 */
	private MapObstaclesTextures(String path) {
		this.tex = Texture.loadTexture(path, GL11.GL_REPEAT, GL11.GL_LINEAR);
	}
	
	/**
	 * get texture associated to this enum
	 * @return
	 */
	public Texture getTexture() {
		return tex;
	}
	
	/**
	 * get a {@link MapObstaclesTextures} enum from a string or {@code MapObstaclesTextures.BRICK} (which is the default one) if not found
	 * @param input {@link String} input value
	 * @return {@link MapObstaclesTextures} enum
	 */
	public static MapObstaclesTextures fromString(String input) {
		for(MapObstaclesTextures tex : values()) {
			if(tex.name().equalsIgnoreCase(input))return tex;
		}
		return MapObstaclesTextures.BRICK;
	}
}
