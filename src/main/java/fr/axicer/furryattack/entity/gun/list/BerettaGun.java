package fr.axicer.furryattack.entity.gun.list;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.entity.gun.AbstractGun;
import fr.axicer.furryattack.entity.gun.GunType;
import fr.axicer.furryattack.render.shaders.GunShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Constants;

public class BerettaGun extends AbstractGun{

	private Texture gunTexture;
	private GunShader shader;
	private Matrix4f modelMatrix;
	
	private int VBO_ID;
	
	public BerettaGun(int bulletAmount) {
		super(GunType.BERETTA, bulletAmount);
		this.gunTexture = Texture.loadTexture(type.getImgPath(), GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
		this.shader = new GunShader();
		this.modelMatrix = new Matrix4f().identity()
				.translate(pos.x, pos.y, 0f)
				.rotateZ(rot);
		
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformi("tex", 0);
	    shader.setUniformf("width", getGunWidth()*Constants.WIDTH);
	    shader.setUniformf("height", getGunHeight()*Constants.HEIGHT);
	    shader.setUniformi("revert", revert ? 1 : 0);
		shader.unbind();
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3);
		vertexBuffer.put(new float[] {0f,0f,0f});
		vertexBuffer.flip();
		
		VBO_ID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO_ID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void update() {
		
		this.modelMatrix.identity()
			.translate(pos.x, pos.y, 0f)
			.rotateZ(rot);
		shader.bind();
		shader.setUniformMat4f("modelMatrix", modelMatrix);
	    shader.setUniformf("width", getGunWidth()*Constants.WIDTH);
	    shader.setUniformf("height", getGunHeight()*Constants.HEIGHT);
	    shader.setUniformi("revert", revert ? 1 : 0);
		shader.unbind();
	}

	@Override
	public void render() {
		//enable blending
		GL11.glEnable(GL11.GL_BLEND);
		//defines blend functions
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		gunTexture.bind(0);
		//bind the shader
		shader.bind();
		//get the vertex attrib location ID from the shader
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "vertices");
		//bind the vertex attrib location for the shader
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		//bind the buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO_ID);
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
	}

	@Override
	public void destroy() {
		GL15.glDeleteBuffers(VBO_ID);
		this.gunTexture.delete();
		this.shader.destroy();
	}

	@Override
	public float getGunWidth() {
		return 0.05859f/1.5f;
	}

	@Override
	public float getGunHeight() {
		return 0.06806f/1.5f;
	}
	
}
