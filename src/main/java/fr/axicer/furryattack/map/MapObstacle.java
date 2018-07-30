package fr.axicer.furryattack.map;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.render.shaders.StandardShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.collision.CollisionBoxM;

/**
 * An obstacle in a {@link AbstractMap}
 * @author Axicer
 *
 */
public class MapObstacle extends CollisionBoxM implements Updateable{
	
	//default texture size on X axis
	private float textureMulX = 1;
	//default texture size on Y axis
	private float textureMulY = 1;
	//the texture vertex buffer object
	private int textureVBO;
	//the texture instance to apply
	private Texture usedTexture;
	private StandardShader shader;
	
	/**
	 * Create an obstacle from a given geometry
	 * @param corners array of {@link Vector2f} creating the geometry
	 */
	public MapObstacle(Vector2f... corners) {
		super(corners);
		shader = new StandardShader();
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
	    shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
	    shader.setUniformMat4f("modelMatrix", new Matrix4f());
		shader.unbind();
	}
	
	/**
	 * Define what texture to use for this obstacle
	 * @param tex {@link Texture} instance to use
	 */
	public void setTexture(Texture tex) {
		this.usedTexture = tex;
	}
	
	/**
	 * Get the actual texture used for this obstacle
	 * @return {@link Texture} instance actualy used
	 */
	public Texture getTexture() {
		return this.usedTexture;
	}
	
	/**
	 * set the texture size on X and Y axis
	 * @param x {@link Integer} size of the texture on X axis
	 * @param y {@link Integer} size of the texture on Y axis 
	 */
	public void setTextureMul(float x, float y) {
		textureMulX = x;
		textureMulY = y;
		updateRender();
	}
	
	/**
	 * Recreate the VBO and textureVBO
	 */
	public void updateRender() {
		//destroy both VBO and textureVBO
		destroy();
		
		//recreate VBO and textureVBO
		FloatBuffer buffer = BufferUtils.createFloatBuffer(points.length*3);
		FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(points.length*2);
		//fill the VBO and textureVBO
		for(int  i = 0 ; i < points.length ; i++) {
			buffer.put(new float[] {points[i].x, points[i].y, -1f});
			if(i%4 == 0) {
				textureBuffer.put(new float[] {0f,0f});
			}else if(i%4 == 1) {
				textureBuffer.put(new float[] {textureMulX,0f});
			}else if(i%4 == 2) {
				textureBuffer.put(new float[] {textureMulX,textureMulY});
			}else if(i%4 == 3) {
				textureBuffer.put(new float[] {0f,textureMulY});
			}
		}
		buffer.flip();
		textureBuffer.flip();
		
		//push the VBO and textureVBO to openGL
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		textureVBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public void render() {
		//to avoid problems set the line stroke to 2
		GL11.glLineWidth(2f);
		
		//binding the shader and defining the uniform variables
		shader.bind();
		shader.setUniformi("tex", 0);
		//bind the texture (to the texture unit 0 since we've set the uniform variable to 0) 
		usedTexture.bind(0);
		
		//push the VBO and textureVBO to the shader pipeline
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "position");
		int textureAttribLocation = GL20.glGetAttribLocation(shader.program, "texcoord");
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL20.glVertexAttribPointer(vertexAttribLocation, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL20.glEnableVertexAttribArray(textureAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureVBO);
		GL20.glVertexAttribPointer(textureAttribLocation, 2, GL11.GL_FLOAT, false, 0, 0);
		
		//draw the geometry
		GL11.glDrawArrays(GL11.GL_POLYGON, 0, points.length);
		
		//unbind the VBO and the textureVBO
		GL20.glDisableVertexAttribArray(vertexAttribLocation);
		GL20.glDisableVertexAttribArray(textureAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		//unbind the shader
		shader.unbind();
		
		//reset the line stroke to 1
		GL11.glLineWidth(1f);
		
		//show collision box
		//super.render();
	}
	
	/**
	 * Nothing is being updated for a default obstacle 
	 */
	@Override
	public void update() {}
	
	@Override
	public void destroy() {
		//destroy the vbo
		super.destroy();
		//destroy the texture VBO
		GL15.glDeleteBuffers(textureVBO);
	}
}
