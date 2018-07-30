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
import fr.axicer.furryattack.render.shaders.BackgroundShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Constants;

/**
 * A image component
 * @author Axicer
 *
 */
public class GUIImage extends GUIComponent{

	/**
	 * image texture
	 */
	private Texture tex;
	/**
	 * vertex buffer id
	 */
	private int vbo;
	/**
	 * shader used to render
	 */
	private BackgroundShader shader;
	/**
	 * component's model matrix
	 */
	private Matrix4f modelMatrix;
	/**
	 * component's scale
	 */
	private float scale;
	/**
	 * image width and height
	 */
	private float width,height;
	
	/**
	 * A {@link GUIImage} constructor
	 * @param imgPath {@link String} texture path
	 * @param blur boolean whether to blur the image
	 * @param blurDirection {@link Vector2f} blur direction (ignored if blur = false)
	 * @param width int image width
	 * @param height int image height
	 * @param pos {@link Vector3f} position of the image
	 * @param rot float rotation of the image
	 * @param scale float scale of the image
	 * @param alignement {@link GUIAlignement} component alignement
	 * @param guialignement {@link GUIAlignement} gui alignement
	 */
	public GUIImage(String imgPath, boolean blur, Vector2f blurDirection, float width, float height, Vector3f pos, float rot, float scale, GUIAlignement alignement, GUIAlignement guialignement) {
		shader = new BackgroundShader();
		tex = Texture.loadTexture(imgPath, GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
		this.pos = pos;
		this.rot = rot;
		this.scale = scale;
		this.width = width;
		this.height = height;
		this.alignement = alignement;
		this.guialignement = guialignement;
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(3);
		vertices.put(new float[] {0f,0f,0f});
		vertices.flip();
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		modelMatrix = new Matrix4f().identity().translate(
				new Vector3f(
						(pos.x+alignement.getOffsetXfromCenter(width)+guialignement.getFrameOffsetX(1))*Constants.WIDTH,
						(pos.y+alignement.getOffsetYfromCenter(height)+guialignement.getFrameOffsetY(1))*Constants.HEIGHT,
						pos.z
				)
		).rotateZ(rot).scale(scale);
		
		shader.bind();
		shader.setUniformi("tex", 0);
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformf("screenWidth", width*(float)Constants.WIDTH);
		shader.setUniformf("screenHeight", height*(float)Constants.HEIGHT);
		shader.setUniformi("blur", blur ? 1 : 0);
		shader.setUniformvec2f("blurDir", blurDirection);
		shader.unbind();
	}
	
	/**
	 * Create a GUIImage from a given textureId
	 * @param textureId
	 * @param width
	 * @param height
	 * @param pos
	 * @param rot
	 * @param scale
	 */
	public GUIImage(int textureId, int width, int height, Vector3f pos, float rot, float scale) {
		this.tex = new Texture(textureId, width, height, GL12.GL_CLAMP_TO_EDGE, GL11.GL_LINEAR);
		shader = new BackgroundShader();
		this.pos = pos;
		this.rot = rot;
		this.scale = scale;
		this.width = width;
		this.height = height;
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(3);
		vertices.put(new float[] {0f,0f,0f});
		vertices.flip();
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		modelMatrix = new Matrix4f().identity().translate(
				new Vector3f(
						(pos.x+alignement.getOffsetXfromCenter(width)+guialignement.getFrameOffsetX(1))*Constants.WIDTH,
						(pos.y+alignement.getOffsetYfromCenter(height)+guialignement.getFrameOffsetY(1))*Constants.HEIGHT,
						pos.z
				)
		).rotateZ(rot).scale(scale);
		
		shader.bind();
		shader.setUniformi("tex", 0);
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformf("screenWidth", width*(float)Constants.WIDTH);
		shader.setUniformf("screenHeight", height*(float)Constants.HEIGHT);
		shader.setUniformi("blur", 0);
		shader.setUniformvec2f("blurDir", new Vector2f());
		shader.unbind();
	}
	
	@Override
	public void render() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		tex.bind(0);
		shader.bind();
		
		int verticesAttrib = GL20.glGetAttribLocation(shader.program, "vertices");
		GL20.glEnableVertexAttribArray(verticesAttrib);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL20.glVertexAttribPointer(verticesAttrib, 3, GL11.GL_FLOAT, false, 0, 0);

		GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(verticesAttrib);
		
		shader.unbind();
		Texture.unbind();
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	@Override
	public void update() {
		modelMatrix.identity().translate(
				new Vector3f(
						(pos.x+alignement.getOffsetXfromCenter(width)+guialignement.getFrameOffsetX(1))*Constants.WIDTH,
						(pos.y+alignement.getOffsetYfromCenter(height)+guialignement.getFrameOffsetY(1))*Constants.HEIGHT,
						pos.z
				)
		).rotateZ(rot).scale(scale);
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformf("screenWidth", width*(float)Constants.WIDTH);
		shader.setUniformf("screenHeight", height*(float)Constants.HEIGHT);
		shader.unbind();
	}

	@Override
	public void destroy() {
		GL15.glDeleteBuffers(vbo);
		tex.delete();
		shader.destroy();
	}
	
	/**
	 * set the image's scale
	 * @param scale float scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	/**
	 * get the image's scale
	 * @return float scale
	 */
	public float getScale() {
		return this.scale;
	}
	
	@Override
	public void recreate(int width, int height) {}

}
