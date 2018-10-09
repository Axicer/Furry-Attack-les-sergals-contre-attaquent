package fr.axicer.furryattack.entity.character;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.textures.Texture;

/**
 * Represent a character skin
 * @author Axicer
 *
 */
public class CharacterSkin implements Destroyable{
	
	//the whole texture of the skin
	private Texture tex;
	//corresponding character
	private Character character;
	
	/**
	 * {@link CharacterSkin} constructor
	 * @param path {@link String} path to the skin image to load
	 */
	public CharacterSkin(String path, Character character) {
		this.tex = Texture.loadTexture(path, GL11.GL_REPEAT, GL11.GL_NEAREST);
		this.character = character;
	}
	
	/**
	 * Reload the actual skin
	 * @param path {@link String} path to the skin image to reload
	 */
	public void reload(String path) {
		reload(Texture.loadTexture(path, GL11.GL_REPEAT, GL11.GL_NEAREST));
	}
	
	/**
	 * Reload the actual skin
	 * @param tex {@link Texture} to use as skin
	 */
	public void reload(Texture tex) {
		this.tex = tex;
		character.refreshSkin(this);
	}
	
	/**
	 * @return {@link Texture} character skin
	 */
	public Texture getTexture() {
		return tex;
	}
	
	/**
	 * Get a {@link Vector4f} bounds for a {@link ModelPart}
	 * @return {@link Vector4f} bounds corresponding to the given {@link ModelPart} like this
	 *			v.x = minX, v.y = minY, v.z = maxX, v.w = maxY
	 */
	public static Vector4f getModelPartBounds(ModelPart part) {
		Vector2f max = new Vector2f();
		Vector2f min = new Vector2f();
		switch(part) {
			case BODY:
				min.x = 0f;
				min.y = 0f;
				max.x = 64f/240f;
				max.y = 1f;
				break;
			case HEAD:
				min.x = 192f/240f;
				min.y = 0f;
				max.x = 1f;
				max.y = 48f/96f;
				break;
			case LEFT_ARM:
				min.x = 64f/240f;
				min.y = 0f;
				max.x = 96f/240f;
				max.y = 80f/96f;
				break;
			case LEFT_HAND:
				min.x = 192f/240f;
				min.y = 48f/96f;
				max.x = 216f/240f;
				max.y = 72f/96f;
				break;
			case LEFT_LEG:
				min.x = 128f/240f;
				min.y = 0f;
				max.x = 160f/240f;
				max.y = 80f/96f;
				break;
			case RIGHT_ARM:
				min.x = 96f/240f;
				min.y = 0f;
				max.x = 96f/240f;
				max.y = 80f/96f;
				break;
			case RIGHT_HAND:
				min.x = 216f/240f;
				min.y = 48f/96f;
				max.x = 1f;
				max.y = 72f/96f;
				break;
			case RIGHT_LEG:
				min.x = 160f/240f;
				min.y = 0f;
				max.x = 192f/240f;
				max.y = 80f/96f;
				break;
		}
		return new Vector4f(min.x, min.y, max.x, max.y);
	}

	@Override
	public void destroy() {
		tex.delete();
	}
}
