package fr.axicer.furryattack.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.render.shader.BackgroundShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.KeyboardHandler;
import fr.axicer.furryattack.util.math.Vector3F;

public class Background implements Renderable,Updateable{
	
	private Texture tex;
	private int vbo;
	private int vbo_texture;
	private BackgroundShader shader;
	
	Camera3D cam = new Camera3D(new Vector3F(0, 0, 0), new Vector3F(0, 0, 0), 0.1f, 1000.0f, 70.0f);
	
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
		vertices.put(new float[] {0f, 0f, 10f,
								  2f,0f, 10f,
								  2f,2f,10f,
								  0f, 2f,10f});
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
		tex.bind(0);
		shader.bind();
		shader.setUniformi("tex", 0);
		//shader.setUniformMat4f("projectionMatrix", new Matrix4f().ortho(-Constants.WIDTH/2, Constants.WIDTH/2, -Constants.HEIGHT/2, Constants.HEIGHT/2, 0.1f, 1000.0f));
		shader.setUniformMat4f("projectionMatrix", cam.getProjectionMatrix());
		shader.setUniformMat4f("viewMatrix", cam.getViewMatrix());
		shader.setUniformMat4f("modelMatrix", new Matrix4f().identity());
		
		/*float[] mat = new float[16];
		GL20.glGetUniformfv(shader.program, GL20.glGetUniformLocation(shader.program, "projectionMatrix"), mat);
		for(int i = 0 ; i < 4 ; i++) {
			for(int j = 0 ; j < 4 ; j++) {
				int pos = i*4+j;
				System.out.print(mat[pos]+"\t");
			}
			System.out.println();
		}*/
		
		int verticesAttrib = GL20.glGetAttribLocation(shader.program, "vertices");
		GL20.glEnableVertexAttribArray(verticesAttrib);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL20.glVertexAttribPointer(verticesAttrib, 3, GL11.GL_FLOAT, false, 0, 0);

		int textureAttrib = GL20.glGetAttribLocation(shader.program, "texture_coordinate");
		GL20.glEnableVertexAttribArray(textureAttrib);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_texture);
		GL20.glVertexAttribPointer(textureAttrib, 2, GL11.GL_FLOAT, false, 0, 0);
		
		GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(verticesAttrib);
		GL20.glDisableVertexAttribArray(textureAttrib);
		
		shader.unbind();
	}

	@Override
	public void update() {
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_UP))cam.rotate(0, 0, -1);
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_LEFT))cam.rotate(0, 1, 0);
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_RIGHT))cam.rotate(0, -1, 0);
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_DOWN))cam.rotate(0, 0, 1);
		System.out.println(cam.getRotation());
	}
}
