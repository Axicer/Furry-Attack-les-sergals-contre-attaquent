package fr.axicer.furryattack.gui.elements;

import java.io.File;
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
import fr.axicer.furryattack.render.shaders.StandardGeomShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.collision.CollisionBoxM;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.control.MouseHandler;
import fr.axicer.furryattack.util.control.events.MouseReleasedEvent;
import fr.axicer.furryattack.util.events.EventListener;

/**
 * A checkbox component
 * @author Axicer
 *
 */
public class GUICheckBox extends GUIComponent implements EventListener{
	
	/**
	 * default status is unchecked
	 */
	public static final boolean DEFAULT_VALUE = false;
	/**
	 * config where the value is modified
	 */
	private Configuration config;
	/**
	 * path to the value
	 */
	private String path;
	/**
	 * configuration path
	 */
	private File configFile;
	/**
	 * component width and height
	 */
	private int width, height;
	/**
	 * checkbox scale
	 */
	private float scale;
	/**
	 * parent gui
	 */
	private GUI gui;
	/**
	 * base texture
	 */
	private Texture tex;
	/**
	 * checked texture
	 */
	private Texture tex_checked;
	/**
	 * whether the checkbox is hovered
	 */
	private boolean hover;
	/**
	 * whether the checkbox is clickable
	 */
	private boolean clickable = true;
	/**
	 * event system listener id used to get this listener
	 */
	private UUID listenerId;
	/**
	 * collision box of the checkbox
	 */
	private CollisionBoxM box;
	/**
	 * checkbox's model matrix
	 */
	private Matrix4f modelMatrix;
	/**
	 * shader used to render
	 */
	private StandardGeomShader shader;
	/**
	 * vertex buffer id
	 */
	private int VBO_ID;
	
	/**
	 * A {@link GUICheckBox} component
	 * @param gui {@link GUI} parent gui
	 * @param config {@link Configuration} used
	 * @param configFile {@link File} configuration file
	 * @param path {@link String} path to the value
	 * @param width int checkbox's width
	 * @param height int checkbox's height
	 * @param scale float checkbox's scale
	 * @param pos {@link Vector3f} checkbox's position
	 * @param rot float checkbox's rotation
	 * @param texturePath {@link String} base texture path
	 * @param textureCheckedPath {@link String} checked texture path
	 * @param alignement {@link GUIAlignement} component alignement
	 * @param guialignement {@link GUIAlignement} gui alignement
	 */
	public GUICheckBox(GUI gui, Configuration config, File configFile, String path, int width, int height, float scale, Vector3f pos, float rot, String texturePath, String textureCheckedPath, GUIAlignement alignement, GUIAlignement guialignement) {
		this.config = config;
		this.configFile = configFile;
		this.path = path;
		
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.gui = gui;
		this.alignement = alignement;
		this.guialignement = guialignement;

		this.pos = pos;
		this.rot = rot;
		this.tex = Texture.loadTexture(texturePath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
		this.tex_checked = Texture.loadTexture(textureCheckedPath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
		
		this.modelMatrix = new Matrix4f().identity().translate(
				new Vector3f(
						pos.x*Constants.WIDTH+alignement.getOffsetXfromCenter(width)*scale+guialignement.getFrameOffsetX(Constants.WIDTH),
						pos.y*Constants.HEIGHT+alignement.getOffsetYfromCenter(height)*scale+guialignement.getFrameOffsetY(Constants.HEIGHT),
						pos.z
				)
		).rotateZ(rot).scale(scale);
		this.box = new CollisionBoxM();
		this.shader = new StandardGeomShader();
		this.listenerId = FurryAttack.getInstance().getEventManager().addListener(this);

		FloatBuffer vertices = BufferUtils.createFloatBuffer(3);
		vertices.put(new float[] {0f,0f,0f});
		vertices.flip();
		VBO_ID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO_ID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
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
	
	@Override
	public void render() {
		shader.bind();
		if(config.getBoolean(path, DEFAULT_VALUE)) {
			tex_checked.bind(0);
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
		modelMatrix.identity().translate(
				new Vector3f(
						pos.x*Constants.WIDTH+alignement.getOffsetXfromCenter(width)*scale+guialignement.getFrameOffsetX(Constants.WIDTH),
						pos.y*Constants.HEIGHT+alignement.getOffsetYfromCenter(height)*scale+guialignement.getFrameOffsetY(Constants.HEIGHT),
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
		hover = box.isInside((float)MouseHandler.getPosX()-Constants.WIDTH/2f, -((float)MouseHandler.getPosY()-Constants.HEIGHT/2f));
	}

	@Override
	public void destroy() {
		FurryAttack.getInstance().getEventManager().removeListener(listenerId);
		GL15.glDeleteBuffers(VBO_ID);
		tex.delete();
		tex_checked.delete();
		box.destroy();
	}
	
	// EVENTS LISTENING //
	
	/**
	 * When a key is released
	 */
	public void onKeyReleased(MouseReleasedEvent ev) {
		if(hover && clickable && FurryAttack.getInstance().getRenderer().getGUIRenderer().getCurrentGUI().getGUI().equals(gui)) {
			config.setBoolean(path, !config.getBoolean(path, DEFAULT_VALUE), true);
			config.save(configFile);
		}
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
	 * @param tex {@link Texture} base texture
	 */
	public void setTexture(Texture tex) {
		this.tex = tex;
	}
	/**
	 * get the checked texture
	 * @return {@link Texture} checked texture
	 */
	public Texture getCheckedTexture() {
		return tex_checked;
	}
	/**
	 * get the checkbox's width
	 * @return int width
	 */
	public float getButtonWidth() {
		return width;
	}
	/**
	 * get the checkbox's height
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
	 * get whether the checkbox is hovered
	 * @return boolean hovered
	 */
	public boolean isHover() {
		return hover;
	}
	/**
	 * get the checkbox's model matrix
	 * @return {@link Matrix4f} model matrix
	 */
	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}
	/**
	 * get the checkbox's scale
	 * @return float scale
	 */
	public float getScale() {
		return scale;
	}
	/**
	 * set the checkbox's scale
	 * @param scale float scale to use
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	/**
	 * get whether the checkbox is clickable
	 * @return boolean clickable
	 */
	public boolean isClickable() {
		return this.clickable;
	}
	/**
	 * set whether the checkbox is clickable
	 * @param clickable boolean clickable
	 */
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}
	
	@Override
	public void recreate(int width, int height) {
		
	}
}
