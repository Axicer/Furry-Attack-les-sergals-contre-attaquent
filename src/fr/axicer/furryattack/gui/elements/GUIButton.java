package fr.axicer.furryattack.gui.elements;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.shader.ButtonShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.CollisionBox;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.FPoint;

public class GUIButton extends GUIComponent{

	private Vector3f pos;
	private float rot;
	private float width, height;
	private CollisionBox box;
	private double[] posX, posY;
	
	private Runnable action;
	
	boolean hover;
	private Texture tex;
	private Texture hover_tex;
	private Matrix4f modelMatrix;
	private ButtonShader shader;
	private int VBO_ID;
	
	public GUIButton(String text,float width, float height, String texturePath, String hoverTexturePath, Runnable action) {
		this.action = action;
		this.tex = Texture.loadTexture(texturePath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
		this.hover_tex = Texture.loadTexture(hoverTexturePath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
		this.width = width;
		this.height = height;
		init();
	}
	
	public void init(){
		posX = new double[1];
		posY = new double[1];
		shader = new ButtonShader();
		pos = new Vector3f();
		rot = 0f;
		hover = false;
		box = new CollisionBox();
		modelMatrix = new Matrix4f().translate(pos).rotateZ(rot);
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		buffer.put(new float[] {0,0,0});
		buffer.flip();
		
		VBO_ID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO_ID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
	    shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
	    shader.setUniformMat4f("modelMatrix", modelMatrix);
	    shader.setUniformi("tex", 0);
	    shader.setUniformf("width", width);
	    shader.setUniformf("height", height);
		shader.unbind();
	}
	
	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public float getRot() {
		return rot;
	}

	public void setRot(float rot) {
		this.rot = rot;
	}

	public void onClick() {
		action.run();
	}
	
	@Override
	public void render() {
		shader.bind();
		if(hover) {
			hover_tex.bind(0);
		}else {
			tex.bind(0);
		}
		
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "position");
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO_ID);
		GL20.glVertexAttribPointer(vertexAttribLocation, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
		
		GL20.glDisableVertexAttribArray(vertexAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		shader.unbind();
		//box.render();
	}

	@Override
	public void update() {
		modelMatrix.identity().translate(pos).rotateZ(rot);
		shader.bind();
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.unbind();
		
		Vector3f topL = new Vector3f(pos.x-width/2, pos.y-height/2, 1.0f);
		Vector3f topR = new Vector3f(pos.x+width/2, pos.y-height/2, 1.0f);
		Vector3f bottomR = new Vector3f(pos.x+width/2, pos.y+height/2, 1.0f);
		Vector3f bottomL = new Vector3f(pos.x-width/2, pos.y+height/2, 1.0f);
		topL = modelMatrix.transformPosition(topL);
		topR = modelMatrix.transformPosition(topR);
		bottomR = modelMatrix.transformPosition(bottomR);
		bottomL = modelMatrix.transformPosition(bottomL);
		box.updatePos(new FPoint(topL.x, topL.y), new FPoint(topR.x, topR.y), new FPoint(bottomR.x, bottomR.y), new FPoint(bottomL.x, bottomL.y));
		
		GLFW.glfwGetCursorPos(FurryAttack.getInstance().window, posX, posY);
		
		hover = box.isInside((float)posX[0]-Constants.WIDTH/2, -((float)posY[0]-Constants.HEIGHT/2));
	}
	
	public void destroy() {
		GL15.glDeleteBuffers(VBO_ID);
		tex.delete();
		hover_tex.delete();
		box.destroy();
	}

}
