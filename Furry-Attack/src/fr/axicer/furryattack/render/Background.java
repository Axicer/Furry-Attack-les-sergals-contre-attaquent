package fr.axicer.furryattack.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.shader.BackgroundShader;
import fr.axicer.furryattack.render.textures.Texture;

public class Background implements Renderable{
	
	private Texture tex;
	private int vbo;
	private int vbo_texture;
	private BackgroundShader shader;
	
	public Background() {
		shader = new BackgroundShader();
		BufferedImage img = null;
		try {
			img = ImageIO.read(Texture.class.getResourceAsStream("/img/intro.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		tex = Texture.loadTexture(img);
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(4*3);
		vertices.put(new float[] {1000f, -1f, 1000f,
								  -1000f,  -1f, 1000f,
								  -1000f,   1f, -1000f,
								  1000f,  1f, -1000f});
		vertices.flip();
		FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(4*2);
		textureBuffer.put(new float[] {0f,0f,
									   1f,0f,
									   1f,1f,
									   0f,1f});
		textureBuffer.flip();
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		vbo_texture = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_texture);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void render() {
		//tex.bind(0);
		shader.bind();
		//shader.setUniformi("tex", 0);
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().entity.cam.getProjectionMatrix());
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().entity.cam.getViewMatrix());
		shader.setUniformMat4f("modelMatrix", new Matrix4f().identity());
		
		System.out.println(GL11.glGetError());
		
		int verticesAttrib = GL20.glGetAttribLocation(shader.program, "vertices");
		GL20.glEnableVertexAttribArray(verticesAttrib);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL20.glVertexAttribPointer(verticesAttrib, 3, GL11.GL_FLOAT, false, 0, 0);

		/*int textureAttrib = GL20.glGetAttribLocation(shader.program, "texture_coordinate");
		GL20.glEnableVertexAttribArray(textureAttrib);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_texture);
		GL20.glVertexAttribPointer(textureAttrib, 2, GL11.GL_FLOAT, false, 0, 0);*/
		
		GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(verticesAttrib);
		//GL20.glDisableVertexAttribArray(textureAttrib);
		
		shader.unbind();
	}
}
