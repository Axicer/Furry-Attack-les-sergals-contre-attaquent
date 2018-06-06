package fr.axicer.furryattack.map;

import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.collision.CollisionBoxM;

/**
 * An obstacle in a {@link AbstractMap}
 * @author Axicer
 *
 */
public class MapObstacle extends CollisionBoxM implements Updateable{
	
	private float textureMulX = 1;
	private float textureMulY = 1;
	private int textureVBO;
	private Texture usedTexture;
	
	/**
	 * Create an obstacle from a given geometry
	 * @param corners array of {@link Vector2f} creating the geometry
	 */
	public MapObstacle(Vector2f... corners) {
		super(corners);
	}
	
	public void setTexture(Texture tex) {
		this.usedTexture = tex;
	}
	
	public Texture getTexture() {
		return this.usedTexture;
	}
	
	public void setTextureMul(float x, float y) {
		textureMulX = x;
		textureMulY = y;
		updateRender();
	}
	
	public void updateRender() {
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(textureVBO);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(points.length*3);
		FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(points.length*2);
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
		GL11.glLineWidth(2f);
		shader.bind();
		shader.setUniformi("tex", 0);
		usedTexture.bind(0);
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "position");
		int textureAttribLocation = GL20.glGetAttribLocation(shader.program, "texcoord");
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL20.glVertexAttribPointer(vertexAttribLocation, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL20.glEnableVertexAttribArray(textureAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureVBO);
		GL20.glVertexAttribPointer(textureAttribLocation, 2, GL11.GL_FLOAT, false, 0, 0);
		
		GL11.glDrawArrays(GL11.GL_POLYGON, 0, points.length);
		
		GL20.glDisableVertexAttribArray(vertexAttribLocation);
		GL20.glDisableVertexAttribArray(textureAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		shader.unbind();
		GL11.glLineWidth(1f);
	}
	
	@Override
	public void update() {}
	
	@Override
	public void destroy() {
		super.destroy();
		GL15.glDeleteBuffers(textureVBO);
	}
}
