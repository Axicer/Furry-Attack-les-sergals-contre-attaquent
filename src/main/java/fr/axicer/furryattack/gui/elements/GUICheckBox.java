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
import fr.axicer.furryattack.render.shader.CheckBoxShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.collision.CollisionBoxM;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.control.MouseHandler;
import fr.axicer.furryattack.util.control.events.MouseReleasedEvent;
import fr.axicer.furryattack.util.events.EventListener;

public class GUICheckBox extends GUIComponent implements EventListener{

	public static final boolean DEFAULT_VALUE = false;
	
	private Configuration config;
	private String path;
	private File configFile;
	
	private float width, height;
	private float scale;
	private GUI gui;
	private Texture tex;
	private Texture tex_checked;
	private boolean hover;
	private boolean clickable = true;
	private UUID listenerId;
	
	private CollisionBoxM box;
	private Matrix4f modelMatrix;
	private CheckBoxShader shader;
	private int VBO_ID;
	
	public GUICheckBox(GUI gui, Configuration config, File configFile, String path, int width, int height, float scale, Vector3f pos, float rot, String texturePath, String textureCheckedPath) {
		this.config = config;
		this.configFile = configFile;
		this.path = path;
		
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.gui = gui;

		this.pos = pos;
		this.rot = rot;
		this.tex = Texture.loadTexture(texturePath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
		this.tex_checked = Texture.loadTexture(textureCheckedPath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
		
		this.modelMatrix = new Matrix4f().identity().translate(pos).rotateZ(rot).scale(scale);
		this.box = new CollisionBoxM();
		this.shader = new CheckBoxShader();
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
	
	public void onKeyReleased(MouseReleasedEvent ev) {
		if(hover && clickable && FurryAttack.getInstance().getRenderer().getGUIRenderer().getCurrentGUI().getGUI().equals(gui)) {
			config.setBoolean(path, !config.getBoolean(path, DEFAULT_VALUE), true);
			config.save(configFile);
		}
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
	
	public Texture getTexture() {
		return tex;
	}

	public void setTexture(Texture tex) {
		this.tex = tex;
	}

	public Texture getCheckedTexture() {
		return tex_checked;
	}

	public void setHoverTexture(Texture texChecked) {
		this.tex_checked = texChecked;
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
