package fr.axicer.furryattack.entity.gun.bullet;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.entity.gun.Gun;
import fr.axicer.furryattack.entity.gun.GunType;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.render.shaders.StandardGeomShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Constants;

public class Bullet implements Updateable,Renderable{

	private GunType gunType;
	private Texture texture;
	private StandardGeomShader shader;
	private Matrix4f modelMatrix;
	public Vector2f pos;
	private float rot;
	private int VBO_ID;
	
	public Bullet(Gun instance, GunType type) {
		this.gunType = type;
		this.rot = instance.getRot();
		this.pos = new Vector2f(instance.getPos());
		this.texture = Texture.loadTexture("/img/bullets/"+type.getBulletType().toString().toLowerCase()+".png", GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
		this.modelMatrix = new Matrix4f().translate(pos.x*Constants.WIDTH, pos.y*Constants.HEIGHT, -1f).rotateZ(rot);
		this.shader = new StandardGeomShader();
		
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformi("tex", 0);
	    shader.setUniformf("width", type.getBulletType().getBulletsWidth()*Constants.WIDTH);
	    shader.setUniformf("height", type.getBulletType().getBulletsHeight()*Constants.HEIGHT);
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
	public void render() {
		//enable blending
		GL11.glEnable(GL11.GL_BLEND);
		//defines blend functions
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//bind the animation texture
		texture.bind(0);
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
	public void update() {
		//update the model matrix
		modelMatrix.identity().translate(pos.x*Constants.WIDTH, pos.y*Constants.HEIGHT, 0f).rotateZ(rot);
		//bind the shader
		shader.bind();
		//push matrices
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		//unbind the shader
		shader.unbind();
	}
	
	public BulletType getBulletType() {
		return this.gunType.getBulletType();
	}
	
	/**
	 * Get if the bullet is actually colliding with any entity on the map
	 * @return {@link Boolean} true if an entity is touched, false otherwise
	 */
	public boolean checkEntityIsTouched() {
		return false;
	}
}
