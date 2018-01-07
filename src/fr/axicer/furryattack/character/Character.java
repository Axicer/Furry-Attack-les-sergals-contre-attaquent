package fr.axicer.furryattack.character;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.character.animation.CharacterAnimation;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.render.shader.CharacterShader;
import fr.axicer.furryattack.util.Color;

public class Character implements Renderable,Updateable{

	public static final float CHARACTER_HEIGHT = 1.7f;
	public static final float CHARACTER_WIDTH = 1f;
	
	private Vector2f pos;
	
	private Species race;
	private Color primaryColor;
	private Color secondaryColor;
	private String expression;
	
	private Matrix4f modelMatrix;
	private CharacterAnimation walk;
	private CharacterShader shader;
	private int VERTEX_VBO_ID;
	private int TEXCOORD_VBO_ID;
	
	public Character(Species race, Color primary, Color secondary, String expression, CharacterAnimation walkAnim) {
		this.race = race;
		this.primaryColor = primary;
		this.secondaryColor = secondary;
		this.expression = expression;
		this.pos = new Vector2f();
		this.walk = walkAnim;
		this.modelMatrix = new Matrix4f();
		
		shader = new CharacterShader();
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(4*3);
		vertexBuffer.put(new float[] {
				0, 					CHARACTER_HEIGHT, 	-0.8f,
				CHARACTER_WIDTH, 	CHARACTER_HEIGHT, 	-0.8f,
				CHARACTER_WIDTH, 	0, 					-0.8f,
				0,					0,					-0.8f
		});
		vertexBuffer.flip();
		FloatBuffer texcoordBuffer = BufferUtils.createFloatBuffer(4*2);
		texcoordBuffer.put(new float[] {
				0,0,
				1,0,
				1,1,
				0,1
		});
		texcoordBuffer.flip();
		
		VERTEX_VBO_ID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VERTEX_VBO_ID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		TEXCOORD_VBO_ID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, TEXCOORD_VBO_ID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texcoordBuffer, GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformvec3f("primaryColor", new Vector3f(primaryColor.x, primaryColor.y, primaryColor.z));
		shader.setUniformvec3f("secondaryColor", new Vector3f(secondaryColor.x, secondaryColor.y, secondaryColor.z));
		shader.setUniformf("spriteWidth", ((float)walk.getTexture().width/(float)walk.getKeyframes().length)/(float)walk.getTexture().width);
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
		walk.updateState();
		modelMatrix = modelMatrix.identity().translate(pos.x, pos.y, 0).scale(100);
		shader.bind();
		shader.setUniformf("offsetX", walk.getBeforeKeyFrame().diffX);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.unbind();
	}

	@Override
	public void render() {
		//GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		walk.getTexture().bind(0);
		shader.bind();
		shader.setUniformi("tex", 0);
		
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "vertices");
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VERTEX_VBO_ID);
		GL20.glVertexAttribPointer(vertexAttribLocation, 3, GL11.GL_FLOAT, false, 0, 0);
		
		int texcoordAttribLocation = GL20.glGetAttribLocation(shader.program, "baseTexCoord");
		GL20.glEnableVertexAttribArray(texcoordAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, TEXCOORD_VBO_ID);
		GL20.glVertexAttribPointer(texcoordAttribLocation, 2, GL11.GL_FLOAT, false, 0, 0);
		
		GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
		
		GL20.glDisableVertexAttribArray(texcoordAttribLocation);
		GL20.glDisableVertexAttribArray(vertexAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		shader.unbind();
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void destroy() {
		GL15.glDeleteBuffers(VERTEX_VBO_ID);
		GL15.glDeleteBuffers(TEXCOORD_VBO_ID);
	}
}
