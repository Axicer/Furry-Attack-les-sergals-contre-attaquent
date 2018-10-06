package fr.axicer.furryattack.entity.character.old;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.entity.Species;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.render.shaders.CharacterPartShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.collision.CollisionBoxM;

public class CharacterPart implements Updateable, Renderable{
	
	public Vector2f pos;
	private float rot;
	private Texture texture;
	private CharacterPartShader shader;
	private Matrix4f modelMatrix;
	private int VERTEX_VBO_ID;
	
	private CollisionBoxM collisionBox;
	private CollisionBoxM originBox;
	private CharacterPartsType type;
	
	public CharacterPart(CharacterPartsType type, Species race, Vector2f pos) {
		this.texture = Texture.loadTexture("/img/species/"+race.toString().toLowerCase()+"/"+type.toString().toLowerCase()+".png", GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
		this.pos = pos;
		this.shader= new CharacterPartShader();
		this.modelMatrix = new Matrix4f().identity().translate(pos.x, pos.y, 0f).rotateZ(rot);
		this.type = type;
		//creating an hitbox
		this.originBox = new CollisionBoxM(0f								,0f,
											  type.getWidth()*Constants.WIDTH	,0f,
											  type.getWidth()*Constants.WIDTH	,type.getHeight()*Constants.HEIGHT,
											  0f								,type.getHeight()*Constants.HEIGHT);
		this.collisionBox = originBox.clone();
		
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3);
		vertexBuffer.put(new float[] {0f,0f,0f});
		vertexBuffer.flip();
		VERTEX_VBO_ID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VERTEX_VBO_ID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		shader.bind();
		shader.fillShader();
	    shader.setUniformMat4f("modelMatrix", modelMatrix);
	    shader.setUniformf("width", type.getWidth()*Constants.WIDTH);
	    shader.setUniformf("height", type.getHeight()*Constants.HEIGHT);
		shader.unbind();
	}

	@Override
	public void render() {
		//enable blending
		GL11.glEnable(GL11.GL_BLEND);
		//defines blend functions
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//bind the part texture at location 0 (default for this shader)
		texture.bind(0);
		//bind the shader
		shader.bind();
		//get the vertex attrib location ID from the shader
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "vertices");
		//bind the vertex attrib location for the shader
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		//bind the buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VERTEX_VBO_ID);
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
		this.collisionBox.render();
	}

	@Override
	public void update() {
		//move the collision box
		this.collisionBox = originBox.clone();
		this.collisionBox.rotateFrom(type.getWidth()/2f, type.getHeight()/2f, rot);
		this.collisionBox.move(pos.x,  pos.y);
		//update the model matrix
		modelMatrix.identity().translate(pos.x, pos.y, 0f).rotateZ(rot);
		//bind the shader
		shader.bind();
		//push matrices
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		//unbind the shader
		shader.unbind();
	}
}
