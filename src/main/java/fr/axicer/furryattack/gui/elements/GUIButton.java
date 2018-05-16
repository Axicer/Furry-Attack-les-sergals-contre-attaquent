package fr.axicer.furryattack.gui.elements;

import java.nio.FloatBuffer;
import java.util.UUID;

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
import fr.axicer.furryattack.util.events.EventManager;
import fr.axicer.furryattack.util.font.FontType;

/**
 * A simple button component
 * @author Axicer
 *
 */
public class GUIButton extends GUIComponent implements EventListener{

	/**
	 * button width and height
	 */
	private int width, height;
	/**
	 * float scale
	 */
	private float scale;
	/**
	 * collision box used
	 */
	private CollisionBoxM box;
	/**
	 * whether the button is hovered
	 */
	private boolean hover;
	/**
	 * whether the button is clicked
	 */
	private boolean clicked;
	/**
	 * whether the button is clickable
	 */
	private boolean clickable = true;
	/**
	 * the action thread when clicked
	 */
	private Thread actionThread;
	/**
	 * event system listener id given by {@link EventManager}
	 */
	protected UUID listenerId;
	/**
	 * text component used to render text
	 */
	private GUIText textG;
	/**
	 * base texture
	 */
	private Texture tex;
	/**
	 * hovered texture
	 */
	private Texture hover_tex;
	/**
	 * clicked texture
	 */
	private Texture click_tex;
	/**
	 * button's model matrix
	 */
	private Matrix4f modelMatrix;
	/**
	 * shader used to render
	 */
	private ButtonShader shader;
	/**
	 * vertex buffer id
	 */
	private int VBO_ID;
	
	/**
	 * A {@link GUIButton} constructor
	 * @param gui {@link GUI} parent gui
	 * @param text {@link String} text to render
	 * @param textMul float text scale
	 * @param width int button's width
	 * @param height int button's height
	 * @param texturePath {@link String} base texture path
	 * @param hoverTexturePath {@link String} hovered texture path
	 * @param clickTexturePath {@link String} clicked texture path
	 * @param scale float scale of the button
	 * @param type {@link FontType} to used
	 * @param textColor {@link Color} color to use
	 * @param pos {@link Vector3f} position of the button
	 * @param rot float rotation of the button
	 * @param alignement {@link GUIAlignement} component alignement
	 * @param guialignement {@link GUIAlignement} gui alignement
	 * @param action {@link Runnable} action to do
	 */
	public GUIButton(GUI gui, String text, float textMul ,int width, int height, String texturePath, String hoverTexturePath, String clickTexturePath, float scale, FontType type, Color textColor, Vector3f pos, float rot, GUIAlignement alignement, GUIAlignement guialignement, Runnable action) {
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
		this.alignement = alignement;
		this.guialignement = guialignement;
		this.textG = new GUIText(text, new Vector3f(
				pos.x+alignement.getOffsetXfromCenter(width)*scale,
				pos.y+alignement.getOffsetYfromCenter(height)*scale,
				pos.z
		), rot, type, textColor, textMul, GUIAlignement.CENTER, guialignement);
		this.shader = new ButtonShader();
		this.box = new CollisionBoxM();
		this.modelMatrix = new Matrix4f().translate(
				new Vector3f(
						pos.x+alignement.getOffsetXfromCenter(width)*scale+guialignement.getReverseOffsetXfromCenter(Constants.WIDTH),
						pos.y+alignement.getOffsetYfromCenter(height)*scale+guialignement.getReverseOffsetYfromCenter(Constants.HEIGHT),
						pos.z
				)
		).rotateZ(rot).scale(scale);
		
		//register this button to the event System
		this.listenerId = FurryAttack.getInstance().getEventManager().addListener(this);
		
		init();
	}
	
	/**
	 * Init rendering values
	 */
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
	
	/**
	 * when the button is clicked
	 */
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
		modelMatrix.identity().translate(
				new Vector3f(
						pos.x+alignement.getOffsetXfromCenter(width)*scale+guialignement.getReverseOffsetXfromCenter(Constants.WIDTH),
						pos.y+alignement.getOffsetYfromCenter(height)*scale+guialignement.getReverseOffsetYfromCenter(Constants.HEIGHT),
						pos.z
				)
		).rotateZ(rot).scale(scale);
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
		
		textG.setPosition(new Vector3f(
				pos.x+alignement.getOffsetXfromCenter(width)*scale,
				pos.y+alignement.getOffsetYfromCenter(height)*scale,
				pos.z
		));
		textG.setRotation(rot);
		textG.update();
	}
	
	@Override
	public void destroy() {
		FurryAttack.getInstance().getEventManager().removeListener(listenerId);
		GL15.glDeleteBuffers(VBO_ID);
		tex.delete();
		hover_tex.delete();
		box.destroy();
		textG.destroy();
	}

	// EVENTS LISTENING //
	
	/**
	 * when a key is pressed
	 */
	public void onKeyPressed(MousePressedEvent ev) {
		//if the gui is shown and hover
		if(FurryAttack.getInstance().getRenderer().getGUIRenderer().getCurrentGUI().getGUI().equals(gui) && hover) {
			clicked = true;
		}
	}
	
	/**
	 * when a key is released
	 */
	public void onKeyReleased(MouseReleasedEvent ev) {
		if(hover && clickable && FurryAttack.getInstance().getRenderer().getGUIRenderer().getCurrentGUI().getGUI().equals(gui))onClick();
		clicked = false;
	}
	
	// GETTERS AND SETTERS //
	
	/**
	 * set the action to do
	 * @param action {@link Runnable} action to do
	 */
	public void setAction(Runnable action) {
		this.actionThread = new Thread(action);
	}
	/**
	 * get the base texture
	 * @return {@link Texture} base texture
	 */
	public Texture getTexture() {
		return tex;
	}
	/**
	 * set the base texture
	 * @param tex {@link Texture} base texture to used
	 */
	public void setTexture(Texture tex) {
		this.tex = tex;
	}
	/**
	 * get the hovered texture
	 * @return {@link Texture} hovered texture
	 */
	public Texture getHoverTexture() {
		return hover_tex;
	}
	/**
	 * set the hovered texture
	 * @param hover_tex {@link Texture} hovered texture
	 */
	public void setHoverTexture(Texture hover_tex) {
		this.hover_tex = hover_tex;
	}
	/**
	 * get the clicked texture
	 * @return {@link Texture} clicked texture
	 */
	public Texture getClickTexture() {
		return click_tex;
	}
	/**
	 * set the click texture
	 * @param click_tex {@link Texture} click texture
	 */
	public void setClickTexture(Texture click_tex) {
		this.click_tex = click_tex;
	}
	/**
	 * get the button's width
	 * @return int width
	 */
	public float getButtonWidth() {
		return width;
	}
	/**
	 * get the button's height
	 * @return int height
	 */
	public float getButtonHeight() {
		return height;
	}
	/**
	 * get the collision box
	 * @return {@link CollisionBoxM} collision box
	 */
	public CollisionBoxM getColisionBox() {
		return box;
	}
	/**
	 * get text component
	 * @return {@link GUIText} text component
	 */
	public GUIText getTextGUI() {
		return textG;
	}
	/**
	 * whether the button is hovered
	 * @return boolean hovered
	 */
	public boolean isHover() {
		return hover;
	}
	/**
	 * get the button's model matrix
	 * @return {@link Matrix4f} mdoel matrix
	 */
	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}
	/**
	 * get the button's scale
	 * @return float scale
	 */
	public float getScale() {
		return scale;
	}
	/**
	 * set the button's scale
	 * @param scale float scale to used
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	/**
	 * get whether the button is clickable
	 * @return boolean clickable
	 */
	public boolean isClickable() {
		return this.clickable;
	}
	/**
	 * set whether the button is clickable or not
	 * @param clickable boolean clickable
	 */
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

}
