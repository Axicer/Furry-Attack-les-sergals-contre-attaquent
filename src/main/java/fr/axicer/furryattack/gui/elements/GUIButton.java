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
import fr.axicer.furryattack.gui.guis.GUI;
import fr.axicer.furryattack.render.shader.ButtonShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.collision.CollisionBoxM;
import fr.axicer.furryattack.util.control.MouseHandler;
import fr.axicer.furryattack.util.control.events.MousePressedEvent;
import fr.axicer.furryattack.util.control.events.MouseReleasedEvent;
import fr.axicer.furryattack.util.events.EventListener;
import fr.axicer.furryattack.util.font.FontType;

public class GUIButton extends GUIComponent implements EventListener{

	private Vector3f pos;
	private float rot;
	private float width, height;
	private float scale;
	private GUI gui;
	
	private CollisionBoxM box;
	private boolean hover;
	private boolean clicked;
	private boolean clickable = true;
	
	private Thread actionThread;
	
	private GUIText textG;
	
	private Texture tex;
	private Texture hover_tex;
	private Texture click_tex;
	
	private Matrix4f modelMatrix;
	private ButtonShader shader;
	private int VBO_ID;
	
	public GUIButton(GUI gui, String text, float textMul ,float width, float height, String texturePath, String hoverTexturePath, String clickTexturePath, float scale, FontType type, Color textColor, Vector3f pos, float rot, Runnable action) {
		this.tex = Texture.loadTexture(texturePath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
		this.hover_tex = Texture.loadTexture(hoverTexturePath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
		this.click_tex = Texture.loadTexture(clickTexturePath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);

		this.actionThread = new Thread(action);

		this.width = width;
		this.height = height;
		this.pos = pos;
		this.rot = rot;
		this.hover = false;
		this.scale = scale;
		this.gui = gui;

		this.textG = new GUIText(text, pos, rot, type, textColor, textMul);
		this.shader = new ButtonShader();
		this.box = new CollisionBoxM();
		this.modelMatrix = new Matrix4f().translate(pos).rotateZ(rot).scale(scale);
		
		//register this button to the event System
		FurryAttack.getInstance().getEventManager().addListener(this);
		
		init();
	}
	
	private void init(){
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
	
	public void onClick() {
		if(!actionThread.isAlive() && clickable)actionThread.run();
	}
	
	@Override
	public void render() {
		shader.bind();
		if(clicked) {
			click_tex.bind(0);
		}else if(hover) {
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
		textG.render();
		//box.render();
	}

	@Override
	public void update() {
		modelMatrix.identity().translate(pos).rotateZ(rot).scale(scale);
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.unbind();
		
		Vector3f topL = new Vector3f(-width/2, -height/2, 1.0f);
		modelMatrix.transformPosition(topL, topL);
		Vector3f topR = new Vector3f(width/2, -height/2, 1.0f);
		modelMatrix.transformPosition(topR, topR);
		Vector3f bottomR = new Vector3f(width/2, height/2, 1.0f);
		modelMatrix.transformPosition(bottomR, bottomR);
		Vector3f bottomL = new Vector3f(-width/2, height/2, 1.0f);
		modelMatrix.transformPosition(bottomL, bottomL);
		
		box.updatePos(new Vector2f(topL.x, topL.y), new Vector2f(topR.x, topR.y), new Vector2f(bottomR.x, bottomR.y), new Vector2f(bottomL.x, bottomL.y));
		
		/*System.out.println(topL);
		System.out.println(topR);
		System.out.println(bottomR);
		System.out.println(bottomL);
		System.out.println((float)MouseHandler.getPosX()-Constants.WIDTH/2f+","+-((float)MouseHandler.getPosY()-Constants.HEIGHT/2f));*/

		hover = box.isInside((float)MouseHandler.getPosX()-Constants.WIDTH/2f, -((float)MouseHandler.getPosY()-Constants.HEIGHT/2f));
		//if(hover && clicked && clickable)onClick();
		
		textG.setPosition(pos);
		textG.setRotation(rot);
		textG.update();
	}
	
	@Override
	public void destroy() {
		FurryAttack.getInstance().getEventManager().removeListener(this);
		GL15.glDeleteBuffers(VBO_ID);
		tex.delete();
		hover_tex.delete();
		box.destroy();
		textG.destroy();
	}

	// EVENTS LISTENING //
	
	public void onKeyPressed(MousePressedEvent ev) {
		//if the gui is shown and hover
		if(FurryAttack.getInstance().getRenderer().getGUIRenderer().getCurrentGUI().getGUI().equals(gui) && hover) {
			clicked = true;
		}
	}
	
	public void onKeyReleased(MouseReleasedEvent ev) {
		if(hover && clickable && FurryAttack.getInstance().getRenderer().getGUIRenderer().getCurrentGUI().getGUI().equals(gui))onClick();
		clicked = false;
	}
	
	// GETTERS AND SETTERS //
	
	public Vector3f getPosition() {
		return pos;
	}

	public void setPosition(Vector3f pos) {
		this.pos = pos;
	}

	public float getRotation() {
		return rot;
	}

	public void setRotation(float rot) {
		this.rot = rot;
	}
	
	public void setAction(Runnable action) {
		this.actionThread = new Thread(action);
	}

	public Texture getTexture() {
		return tex;
	}

	public void setTexture(Texture tex) {
		this.tex = tex;
	}

	public Texture getHoverTexture() {
		return hover_tex;
	}

	public void setHoverTexture(Texture hover_tex) {
		this.hover_tex = hover_tex;
	}

	public float getButtonWidth() {
		return width;
	}

	public float getButtonHeight() {
		return height;
	}

	public CollisionBoxM getColisionBox() {
		return box;
	}

	public GUIText getTextGUI() {
		return textG;
	}

	public boolean isHover() {
		return hover;
	}

	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public boolean isClickable() {
		return this.clickable;
	}
	
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}
}
