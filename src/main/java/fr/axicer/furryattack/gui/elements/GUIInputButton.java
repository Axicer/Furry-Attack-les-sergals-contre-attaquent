package fr.axicer.furryattack.gui.elements;

import java.awt.Font;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.UUID;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
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
import fr.axicer.furryattack.util.Util;
import fr.axicer.furryattack.util.collision.CollisionBoxM;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.control.MouseHandler;
import fr.axicer.furryattack.util.control.events.KeyTypedEvent;
import fr.axicer.furryattack.util.control.events.MousePressedEvent;
import fr.axicer.furryattack.util.control.events.MouseReleasedEvent;
import fr.axicer.furryattack.util.events.EventListener;
import fr.axicer.furryattack.util.events.EventManager;
import fr.axicer.furryattack.util.font.FontType;

/**
 * A input button component
 * @author Axicer
 *
 */
public class GUIInputButton extends GUIComponent implements EventListener{

	/**
	 * default key if not defined
	 */
	private static final int DEFAULT_KEY = 0;
	/**
	 * component width and height
	 */
	private float width, height;
	/**
	 * component scale
	 */
	private float scale;
	/**
	 * Colision box used
	 */
	private CollisionBoxM box;
	/**
	 * whether the input button is hovered
	 */
	private boolean hover;
	/**
	 * whether the input button is clicked
	 */
	private boolean clicked;
	/**
	 * whether the button is clickable
	 */
	private boolean clickable = true;
	/**
	 * thread to execute when clicked
	 */
	private Thread actionThread;
	/**
	 * event system listener id given by {@link EventManager} when registering this as an event
	 */
	private UUID listenerId;
	/**
	 * whether this input button is listening for a key
	 */
	private boolean listening;
	/**
	 * the text component inside
	 */
	private GUIText textG;
	/**
	 * base texture
	 */
	private Texture tex;
	/**
	 * texture when hovered
	 */
	private Texture hover_tex;
	/**
	 * texture when clicked
	 */
	private Texture click_tex;
	/**
	 * input button's model matrix
	 */
	private Matrix4f modelMatrix;
	/**
	 * shader used to render this input button
	 */
	private ButtonShader shader;
	/**
	 * vertex buffer id
	 */
	private int VBO_ID;
	/**
	 * Configuration to modify
	 */
	private Configuration config;
	/**
	 * path to the key storage
	 */
	private String path;
	/**
	 * file used to save the configuration
	 */
	private File configFile;
	
	/**
	 * Constructor of an input button
	 * @param gui the parent gui
	 * @param config configuration to modify
	 * @param configFile file used to save the config
	 * @param path path to the key storage
	 * @param textMul text scale
	 * @param type {@link Font} used to render
	 * @param width int button width
	 * @param height int button height
	 * @param scale button's scale
	 * @param color {@link Color} of the text
	 * @param pos {@link Vector3f} position of the text
	 * @param rot float rotation
	 * @param texturePath base texture path
	 * @param textureHoverPath path for hovered texture
	 * @param textureClickPath path for clicked texture
	 * @param alignement {@link GUIAlignement} component alignement
	 * @param guialignement {@link GUIAlignement} gui alignement
	 */
	public GUIInputButton(GUI gui, Configuration config, File configFile, String path, float textMul, FontType type, float width, float height, float scale, Color color, Vector3f pos, float rot, String texturePath, String textureHoverPath, String textureClickPath, GUIAlignement alignement, GUIAlignement guialignement) {
		this.tex = Texture.loadTexture(texturePath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
		this.hover_tex = Texture.loadTexture(textureHoverPath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
		this.click_tex = Texture.loadTexture(textureClickPath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);

		this.width = width;
		this.height = height;
		this.pos = pos;
		this.rot = rot;
		this.hover = false;
		this.scale = scale;
		this.gui = gui;
		this.alignement = alignement;
		this.guialignement = guialignement;
		
		this.textG = new GUIText(Util.getKeyRepresentation(config.getInt(path, DEFAULT_KEY)), new Vector3f(
				pos.x+alignement.getOffsetXfromCenter(width),
				pos.y+alignement.getOffsetYfromCenter(height),
				pos.z
		), rot, type, color, textMul, GUIAlignement.CENTER, guialignement);
		this.shader = new ButtonShader();
		this.box = new CollisionBoxM();
		
		
		this.modelMatrix = new Matrix4f().translate(
				new Vector3f(
						(pos.x+alignement.getOffsetXfromCenter(width)+guialignement.getFrameOffsetX(1))*(float)Constants.WIDTH,
						(pos.y+alignement.getOffsetYfromCenter(height)+guialignement.getFrameOffsetY(1))*(float)Constants.HEIGHT,
						pos.z
				)
		).rotateZ(rot).scale(scale);
		
		listenerId = FurryAttack.getInstance().getEventManager().addListener(this);
		
		this.actionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				getTextGUI().setText("<entrez une valeur>");
				listening = true;
			}
		});
		this.config = config;
		this.path = path;
		this.configFile = configFile;

		init();
	}
	
	/**
	 * init rendering values
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
	    shader.setUniformf("width", width*Constants.WIDTH);
	    shader.setUniformf("height", height*Constants.HEIGHT);
		shader.unbind();
	}
	
	/**
	 * when click
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
		modelMatrix.identity()
			.translate(
					new Vector3f(
							(pos.x+alignement.getOffsetXfromCenter(width)+guialignement.getFrameOffsetX(1))*(float)Constants.WIDTH,
							(pos.y+alignement.getOffsetYfromCenter(height)+guialignement.getFrameOffsetY(1))*(float)Constants.HEIGHT,
							pos.z
					)
			)
			.rotateZ(rot)
			.scale(scale);
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformf("width", width*Constants.WIDTH);
	    shader.setUniformf("height", height*Constants.HEIGHT);
		shader.unbind();
		
		Vector3f topL = new Vector3f(-width*Constants.WIDTH/2, -height*Constants.HEIGHT/2, 1.0f);
		modelMatrix.transformPosition(topL, topL);
		Vector3f topR = new Vector3f(width*Constants.WIDTH/2, -height*Constants.HEIGHT/2, 1.0f);
		modelMatrix.transformPosition(topR, topR);
		Vector3f bottomR = new Vector3f(width*Constants.WIDTH/2, height*Constants.HEIGHT/2, 1.0f);
		modelMatrix.transformPosition(bottomR, bottomR);
		Vector3f bottomL = new Vector3f(-width*Constants.WIDTH/2, height*Constants.HEIGHT/2, 1.0f);
		modelMatrix.transformPosition(bottomL, bottomL);
		
		box.updatePos(new Vector2f(topL.x, topL.y), new Vector2f(topR.x, topR.y), new Vector2f(bottomR.x, bottomR.y), new Vector2f(bottomL.x, bottomL.y));
		
		hover = box.isInside((float)MouseHandler.getPosX()-Constants.WIDTH/2f, -((float)MouseHandler.getPosY()-Constants.HEIGHT/2f));
		
		textG.setPosition(new Vector3f(
				pos.x+alignement.getOffsetXfromCenter(width),
				pos.y+alignement.getOffsetYfromCenter(height),
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
	
	/**
	 * Set the button value
	 * @param val int key value
	 */
	public void setValue(int val) {
		if(val <= 0) {
			getTextGUI().setText("[unbind]");
		}else {
			getTextGUI().setText(Util.getKeyRepresentation(val));
		}
		config.setInt(path, val, true);
	}

	// EVENTS LISTENING //
	
	/**
	 * When a key is pressed
	 */
	public void onKeyPressed(MousePressedEvent ev) {
		//if the gui is shown and hover
		if(FurryAttack.getInstance().getRenderer().getGUIRenderer().getCurrentGUI().getGUI().equals(gui) && hover) {
			clicked = true;
		}
	}
	
	/**
	 * When a key is released
	 */
	public void onKeyReleased(MouseReleasedEvent ev) {
		if(hover && clickable && FurryAttack.getInstance().getRenderer().getGUIRenderer().getCurrentGUI().getGUI().equals(gui))onClick();
		clicked = false;
	}
	
	/**
	 * When a key is typed
	 */
	public void onKeyTyped(KeyTypedEvent ev) {
		if(!listening)return;
		if(ev.getKey() != GLFW.GLFW_KEY_ESCAPE) {
			setValue(ev.getKey());
		}else{
			setValue(DEFAULT_KEY);
		}
		config.save(configFile);
		listening = false;
	}
	
	// GETTERS AND SETTERS //
	
	/**
	 * get the base texture
	 * @return {@link Texture} base texture
	 */
	public Texture getTexture() {
		return tex;
	}
	/**
	 * set the base texture
	 * @param tex {@link Texture} base texture to use
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
	 * @param hover_tex {@link Texture} hovered texture to use
	 */
	public void setHoverTexture(Texture hover_tex) {
		this.hover_tex = hover_tex;
	}
	/**
	 * get the clicked texture
	 * @return {@link Texture} clicked texture
	 */
	public Texture getClickedTexture() {
		return click_tex;
	}
	/**
	 * set the clicked texture
	 * @param click_tex {@link Texture} click texture to use
	 */
	public void setClickTexture(Texture click_tex) {
		this.click_tex = click_tex;
	}
	
	/**
	 * get the input button's width
	 * @return int width
	 */
	public float getButtonWidth() {
		return width;
	}
	/**
	 * get the input button's height
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
	 * get the text gui component used to render the text
	 * @return {@link GUIText} text component
	 */
	public GUIText getTextGUI() {
		return textG;
	}
	/**
	 * whether the inout button is hovered
	 * @return boolean hovered
	 */
	public boolean isHover() {
		return hover;
	}
	/**
	 * get this input button's model matrix
	 * @return {@link Matrix4f} model matrix
	 */
	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}
	/**
	 * get this input button's scale
	 * @return float scale
	 */
	public float getScale() {
		return scale;
	}
	/**
	 * set this input button's scale
	 * @param scale float scale to used
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	/**
	 * whether the input button is clickable
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
	
	@Override
	public void recreate(int width, int height) {}
}
