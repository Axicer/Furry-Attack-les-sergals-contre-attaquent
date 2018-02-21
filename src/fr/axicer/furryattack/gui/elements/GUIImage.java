package fr.axicer.furryattack.gui.elements;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.shader.BackgroundShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Constants;

public class GUIImage extends GUIComponent{

	private Texture tex;
	private int vbo;
	private BackgroundShader shader;
	
	public GUIImage(String imgPath) {
		this(imgPath, false, new Vector2f(0.5f, 0.5f));
	}
	
	public GUIImage(String imgPath, boolean blur, Vector2f blurDirection) {
		shader = new BackgroundShader();
		tex = Texture.loadTexture(imgPath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(3);
		vertices.put(new float[] {0f,0f,0f});
		vertices.flip();
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		shader.bind();
		shader.setUniformi("tex", 0);
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", new Matrix4f().identity());
		shader.setUniformf("screenWidth", (float)Constants.WIDTH);
		shader.setUniformf("screenHeight", (float)Constants.HEIGHT);
		shader.setUniformi("blur", blur ? 1 : 0);
		shader.setUniformvec2f("blurDir", blurDirection);
		shader.unbind();
	}
	
	@Override
	public void render() {
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
	}

	@Override
	public void update() {}

	@Override
	public void destroy() {
		GL15.glDeleteBuffers(vbo);
		tex.delete();
		shader.destroy();
	}

}
