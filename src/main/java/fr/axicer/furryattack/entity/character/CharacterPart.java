package fr.axicer.furryattack.entity.character;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import fr.axicer.furryattack.FurryAttack;
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
	
	// model part corresponding (given by constructor)
	private ModelPart part;
	//size in both axis for collision detection, rot is rotation
	private float sizeX, sizeY, rot;
	//local matrix is the transformation from parent to this (fixed in constructor) whereas root bind is the transformation from root point (calculated)
	private Matrix4f localBindTransform, rootBindTransform;
	//shader to draw
	private CharacterPartShader shader;
	//skin corresponding
	private CharacterSkin skin;
	//vertex buffer object
	private int VBO;
	//list of childrens attached
	private List<CharacterPart> childrens;
	//corresponding character
	private Character character;
	
	/**
	 * {@link CharacterPart} constructor
	 * @param part {@link ModelPart} represented here
	 * @param localBindTransform {@link Matrix4f} transformation from parent to this part
	 * @param rot {@link Float} rotation to apply
	 */
	public CharacterPart(Character character, ModelPart part, CharacterSkin skin, Matrix4f localBindTransform, float rot) {
		//set values
		this.childrens = new ArrayList<>();
		this.shader = new CharacterPartShader();
		this.localBindTransform = localBindTransform;
		this.rot = rot;
		this.part = part;
		this.skin = skin;
		this.character = character;
		
		//store shader data
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", rootBindTransform);
		shader.setUniformf("width", sizeX);
		shader.setUniformf("height", sizeY);
		shader.setUniformi("tex", 0);
		shader.unbind();
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3);
		vertexBuffer.put(new float[] {0f,0f,0f});
		vertexBuffer.flip();
		VBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
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
		//calc root bind transform
		this.rootBindTransform = parentRootTransform.mul(localBindTransform, new Matrix4f());
		//recursively call calculation
		for(CharacterPart part : childrens) {
			part.calculateRootBindTransform(rootBindTransform);
		}
	}
	
	/**
	 * Reset this model part's texture
	 */
	public void resetSkin(CharacterSkin skin, boolean recursive) {
		//change skin
		this.skin = skin;
		//if recursively, call childrens reset
		if(recursive) {
			for(CharacterPart part : childrens) {
				part.resetSkin(skin, true);
			}
		}
	}
	
	@Override
	public void destroy() {
		//destroy shader and buffers
		shader.destroy();
		GL15.glDeleteBuffers(VBO);
		//the recursively call delete for childrens
		for(CharacterPart part : childrens) {
			part.destroy();
		}
		
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
