package fr.axicer.furryattack.entity;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.entity.render.animation.CharacterAnimation;
import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.render.shaders.CharacterShader;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;

public class Character extends Entity implements Updateable,Destroyable{

	private Species race;
	private Color primaryColor;
	private Color secondaryColor;
	private String expression;
	
	private Matrix4f modelMatrix;
	private CharacterAnimation walk;
	private CharacterShader shader;
	private int VERTEX_VBO_ID;
	
	public Character(Species race, Color primary, Color secondary, String expression, CharacterAnimation walkAnim) {
		this.race = race;
		this.primaryColor = primary;
		this.secondaryColor = secondary;
		this.expression = expression;
		this.walk = walkAnim;
		this.modelMatrix = new Matrix4f().identity().translate(pos.x, pos.y, 0f);
		
		shader = new CharacterShader();
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3);
		vertexBuffer.put(new float[] {0f,0f,0f});
		vertexBuffer.flip();
		
		VERTEX_VBO_ID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VERTEX_VBO_ID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformvec3f("primaryColor", new Vector3f(primaryColor.x, primaryColor.y, primaryColor.z));
		shader.setUniformvec3f("secondaryColor", new Vector3f(secondaryColor.x, secondaryColor.y, secondaryColor.z));
		shader.setUniformf("spriteWidth", walk.getNormalisedSpriteWidth());
		shader.setUniformf("spriteHeight", walk.getNormalisedSpriteHeight());
		shader.setUniformf("characterWidth", getWidth());
		shader.setUniformf("characterHeight", getHeight());
		shader.setUniformi("tex", 0);
		shader.unbind();
	}
	
	public Species getRace() {
		return race;
	}

	public void setRace(Species race) {
		this.race = race;
	}

	public Color getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(Color primaryColor) {
		this.primaryColor = primaryColor;
	}

	public Color getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(Color secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	@Override
	public void update() {
		//update collision box
		super.update();
		
		//update the animation step
		walk.updateState();
		//update the model matrix
		modelMatrix.identity().translate(pos.x, pos.y, 0f);
		//bind the shader
		shader.bind();
		//push matrices
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		//push animations offset values
		walk.setShaderVariables(shader);
		//unbind the shader
		shader.unbind();
	}

	@Override
	public void render() {
		//enable blending
		GL11.glEnable(GL11.GL_BLEND);
		//defines blend functions
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//bind the animation texture
		walk.getTexture().bind(0);
		//bind the shader
		shader.bind();
		//get the vertex attrib location ID from the shader
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "vertices");
		//bind the vertex attrib location for the shader
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		//bind the buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VERTEX_VBO_ID);
		//push buffer's data into the vertex attrib location in the shader
		GL20.glVertexAttribPointer(vertexAttribLocation, 3, GL11.GL_FLOAT, false, 0, 0);
		//draw shapes
		GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
		//unbind vertex attrib location for the shader
		GL20.glDisableVertexAttribArray(vertexAttribLocation);
		//unbind buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		//unbind the shader
		shader.unbind();
		//disable blending
		GL11.glDisable(GL11.GL_BLEND);
		
		//render collision box
		//super.render();
	}
	
	@Override
	public void destroy() {
		//delete the vertex buffer
		GL15.glDeleteBuffers(VERTEX_VBO_ID);
		//delete the animation texture
		walk.getTexture().delete();
	}

	@Override
	protected float getWidth() {
		//based on a screen of a resolution of 1280 by 720 the character should be 120 pixels wide
		//so it's 120/1280th of the width which is 0.09375th of the screen
		return 0.09375f*(float)Constants.WIDTH;
	}

	@Override
	protected float getHeight() {
		//based on a screen of a resolution of 1280 by 720 the character should be 170 pixels height
		//so it's 170/720th of the height which is 0.2361th of the screen
		return 0.2361f*(float)Constants.HEIGHT;
	}

}
