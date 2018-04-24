package fr.axicer.furryattack.render;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.shader.GUIFrameDrawerShader;
import fr.axicer.furryattack.render.textures.Texture;

public class GUIFrameDrawer implements Renderable,Updateable,Destroyable{
	
	private int vboId;
	private Matrix4f modelMatrix;
	private GUIFrameDrawerShader shader;
	
	private Vector3f pos;
	private Texture tex;
	
	public GUIFrameDrawer(int textureId, int width, int height) {
		this.tex = new Texture(textureId, width, height, GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
		this.pos = new Vector3f(0f,0f,-1f);
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(3);
		vertices.put(new float[] {0f,0f,0f});
		vertices.flip();
		vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		modelMatrix = new Matrix4f().identity().translate(pos);
		
		shader = new GUIFrameDrawerShader();
		shader.bind();
		shader.setUniformi("tex", 0);
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformf("screenWidth", (float)width);
		shader.setUniformf("screenHeight", (float)height);
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
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
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
		modelMatrix.identity().translate(pos);
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.unbind();
	}

	@Override
	public void destroy() {
		GL15.glDeleteBuffers(vboId);
		tex.delete();
		shader.destroy();
	}
}
