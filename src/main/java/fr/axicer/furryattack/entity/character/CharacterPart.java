package fr.axicer.furryattack.entity.character;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.render.shaders.CharacterPartShader;

/**
 * A character part
 * @author Axicer
 *
 */
public class CharacterPart implements Renderable,Updateable,Destroyable{
	
	//size in both axis for collision detection, rot is rotation of the model part
	private float sizeX, sizeY, rot;
	//local matrix is the transformation from parent to this (fixed in constructor) whereas root bind is the transformation from root point (calculated)
	private Matrix4f localBindTransform, rootBindTransform;
	//shader to draw model part
	private CharacterPartShader shader;
	//list of childrens attached to this part
	private List<CharacterPart> childrens;
	
	/**
	 * {@link CharacterPart} constructor
	 * @param part {@link ModelPart} represented here
	 * @param localBindTransform {@link Matrix4f} transformation from parent to this part
	 * @param rot {@link Float} rotation to apply
	 */
	public CharacterPart(ModelPart part, Matrix4f localBindTransform, float rot) {
		this.childrens = new ArrayList<>();
		this.shader = new CharacterPartShader();
		this.localBindTransform = localBindTransform;
		this.rot = rot;
		
		shader.bind();
		//TODO store shader data
		shader.unbind();
	}
	
	/**
	 * @return {@link Float} model part's size on X axis
	 */
	public float getSizeX() {
		return sizeX;
	}
	/**
	 * @return {@link Float} model part's size on Y axis
	 */
	public float getSizeY() {
		return sizeY;
	}
	/**
	 * @return {@link Float} model part's rotation
	 */
	public float getRotation() {
		return rot;
	}
	/**
	 * Set the rotation of the model part to a given angle in radians
	 * @param rot {@link Float} desired rotation angle
	 */
	public void setRotation(float rot) {
		this.rot = rot;
	}
	
	/**
	 * Calculate the rootBindTransform matrix for this model part from parent rootBindTransform
	 * @param parentRootTransform {@link Matrix4f} parent rootbindTransform
	 */
	protected void calculateRootBindTransform(Matrix4f parentRootTransform) {
		this.rootBindTransform = parentRootTransform.mul(localBindTransform, new Matrix4f());
		for(CharacterPart part : childrens) {
			part.calculateRootBindTransform(rootBindTransform);
		}
	}
	
	/**
	 * Reset this model part's texture
	 */
	protected void resetTexture() {
		//TODO inject new texture inside the shader
	}
	
	@Override
	public void destroy() {
		// TODO delete all stored stuff
		
	}
	@Override
	public void update() {
		// TODO update shader data, and collisionBox
		
	}
	@Override
	public void render() {
		// TODO render all things
		
	}
}
