package fr.axicer.furryattack.gui.elements;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.shader.BackgroundShader;
import fr.axicer.furryattack.render.textures.Texture;

public class GUIImage extends GUIComponent{

	private Texture tex;
	private int vbo;
	private BackgroundShader shader;
	private Matrix4f modelMatrix;
	private float scale;
	
	public GUIImage(String imgPath, Vector3f pos) {
		this(imgPath, 128, 128, pos);
	}
	
	public GUIImage(String imgPath, float width, float height, Vector3f pos) {
		this(imgPath, height, width, pos, 0f, 1f);
	}
	
	public GUIImage(String imgPath, float width, float height, Vector3f pos, float rot) {
		this(imgPath, height, width, pos, rot, 1f);
	}
	
	public GUIImage(String imgPath, float width, float height, Vector3f pos, float rot, float scale) {
		this(imgPath, false, new Vector2f(), height, width, pos, rot, scale);
	}
	
	public GUIImage(String imgPath, boolean blur, Vector2f blurDirection, float width, float height, Vector3f pos, float rot, float scale) {
		shader = new BackgroundShader();
		tex = Texture.loadTexture(imgPath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
		this.pos = pos;
		this.rot = rot;
		this.scale = scale;
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(3);
		vertices.put(new float[] {0f,0f,0f});
		vertices.flip();
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		modelMatrix = new Matrix4f().identity().translate(pos).rotateZ(rot).scale(scale);
		
		shader.bind();
		shader.setUniformi("tex", 0);
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformf("screenWidth", (float)width);
		shader.setUniformf("screenHeight", (float)height);
		shader.setUniformi("blur", blur ? 1 : 0);
		shader.setUniformvec2f("blurDir", blurDirection);
		shader.unbind();
	}
	
	/**
	 * Create a GUIImage from a given textureId
	 * @param textureId
	 * @param width
	 * @param height
	 * @param pos
	 * @param rot
	 * @param scale
	 */
	public GUIImage(int textureId, int width, int height, Vector3f pos, float rot, float scale) {
		this.tex = new Texture(textureId, width, height, GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
		shader = new BackgroundShader();
		this.pos = pos;
		this.rot = rot;
		this.scale = scale;
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(3);
		vertices.put(new float[] {0f,0f,0f});
		vertices.flip();
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		modelMatrix = new Matrix4f().identity().translate(pos).rotateZ(rot).scale(scale);
		
		shader.bind();
		shader.setUniformi("tex", 0);
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformf("screenWidth", (float)width);
		shader.setUniformf("screenHeight", (float)height);
		shader.setUniformi("blur", 0);
		shader.setUniformvec2f("blurDir", new Vector2f());
		shader.unbind();
	}
	
	@Override
	public void render() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tex.bind(0);
		shader.bind();
		
		int verticesAttrib = GL20.glGetAttribLocation(shader.program, "vertices");
		GL20.glEnableVertexAttribArray(verticesAttrib);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL20.glVertexAttribPointer(verticesAttrib, 3, GL11.GL_FLOAT, false, 0, 0);

		GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(verticesAttrib);
		
		shader.unbind();
		Texture.unbind();
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	@Override
	public void update() {
		modelMatrix.identity().translate(pos).rotateZ(rot).scale(scale);
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.unbind();
	}

	@Override
	public void destroy() {
		GL15.glDeleteBuffers(vbo);
		tex.delete();
		shader.destroy();
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}
	public Vector3f getPos() {
		return this.pos;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	public float getScale() {
		return this.scale;
	}
}
