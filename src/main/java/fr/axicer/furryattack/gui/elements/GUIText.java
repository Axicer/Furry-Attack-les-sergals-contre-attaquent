package fr.axicer.furryattack.gui.elements;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.shaders.TextShader;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.font.CharInfo;
import fr.axicer.furryattack.util.font.FontType;

/**
 * A text component
 * @author Axicer
 *
 */
public class GUIText extends GUIComponent{

	/**
	 * the text to display
	 */
	private String text;
	/**
	 * the color of the text
	 */
	private Color color;
	/**
	 * the font to display
	 */
	private FontType type;
	/**
	 * the scale of the text
	 */
	public float scale;
	/**
	 * the shader used to render text
	 */
	public TextShader shader;
	/**
	 * vertex buffer id of the component
	 */
	public int VERTICES_VBO;
	/**
	 * texture coordinate id of the component
	 */
	public int TEXCOORD_VBO;
	/**
	 * model matrix of the text
	 */
	public Matrix4f modelMatrix;
	/**
	 * whether the component is ready to be rendered or not
	 */
	private boolean canRender = false;
	/**
	 * the component's width
	 */
	private float textWidth = 0;
	
	/**
	 * A {@link GUIText} constructor
	 * @param text {@link String} to render
	 * @param type {@link FontType} font to use
	 * @param color {@link Color} color of the text
	 * @param alignement {@link GUIAlignement} component alignement
	 * @param guialignement {@link GUIAlignement} gui alignement
	 */
	public GUIText(String text, FontType type, Color color, GUIAlignement alignement, GUIAlignement guialignement) {
		super();
		this.text = text;
		this.type = type;
		this.color = color;
		this.scale = 1f;
		this.alignement = alignement;
		this.guialignement = guialignement;
		initRender();
	}
	
	/**
	 * A {@link GUIText} constructor
	 * @param text {@link String} to render
	 * @param pos {@link Vector3f} position of the component
	 * @param rot float rotation of the component
	 * @param type {@link FontType} font to use
	 * @param color {@link Color} color of the text
	 * @param scale float scale of the text
	 * @param alignement {@link GUIAlignement} component alignement
	 * @param guialignement {@link GUIAlignement} gui alignement
	 */
	public GUIText(String text, Vector3f pos, float rot, FontType type, Color color, float scale, GUIAlignement alignement, GUIAlignement guialignement) {
		super(pos, rot);
		this.text = text;
		this.type = type;
		this.color = color;
		this.scale = scale;
		this.alignement = alignement;
		this.guialignement = guialignement;
		initRender();
	}
	
	/**
	 * Init the component render elements
	 */
	private void initRender(){
	    float startx = 0;
	    textWidth = 0;
	    int removedChar = 0;
	    for(char c : text.toCharArray()) {
	    	CharInfo charInfo = type.getCharMap().get((int)c);
	    	if(charInfo != null)textWidth+= charInfo.width;
	    	else{
	    		removedChar++;
	    	}
	    }
	    int vbufferSize = (text.length()-removedChar)*6*3;
	    int tbufferSize = (text.length()-removedChar)*6*2;
	    if(vbufferSize == 0 || tbufferSize == 0) {
	    	canRender = false;
	    	return;
	    }
	    FloatBuffer vertices = BufferUtils.createFloatBuffer(vbufferSize);
	    FloatBuffer textures = BufferUtils.createFloatBuffer(tbufferSize);
	    
	    for(char c : text.toCharArray()) {
	        CharInfo charInfo = type.getCharMap().get((int)c);
	        if(charInfo == null)continue;
	        // Build a character tile composed by two triangles

	        // Left Top vertex
	        vertices.put(new float[] {startx-textWidth/2, (float)-type.getHeight()/2, -0.2f});
	        textures.put(new float[] {(float)charInfo.startX/(float)type.getWidth(), 1.0f });

	        // Right Top vertex
	        vertices.put(new float[] {startx+charInfo.width-textWidth/2, (float)-type.getHeight()/2, -0.2f});
	        textures.put(new float[] {((float)charInfo.startX+(float)charInfo.width)/(float)type.getWidth(), 1.0f});

	        // Right Bottom vertex
	        vertices.put(new float[] {startx+charInfo.width-textWidth/2, (float)type.getHeight()/2, -0.2f});
	        textures.put(new float[] {((float)charInfo.startX+(float)charInfo.width)/(float)type.getWidth(), 0.0f});

	        
	        // Right Bottom vertex
	        vertices.put(new float[] {startx+charInfo.width-textWidth/2, (float)type.getHeight()/2, -0.2f});
	        textures.put(new float[] {((float)charInfo.startX+(float)charInfo.width)/(float)type.getWidth(), 0.0f});
	        
	        // Left Bottom vertex
	        vertices.put(new float[] {startx-textWidth/2, (float)type.getHeight()/2, -0.2f});
	        textures.put(new float[] {(float)charInfo.startX/(float)type.getWidth(), 0.0f });
	        
	        // Left Top vertex
	        vertices.put(new float[] {startx-textWidth/2, (float)-type.getHeight()/2, -0.2f});
	        textures.put(new float[] {(float)charInfo.startX/(float)type.getWidth(), 1.0f });
	        
	        startx += charInfo.width;
	    }
	    vertices.flip();
	    textures.flip();
	    
	    VERTICES_VBO = GL15.glGenBuffers();
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VERTICES_VBO);
	    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
	    TEXCOORD_VBO = GL15.glGenBuffers();
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, TEXCOORD_VBO);
	    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textures, GL15.GL_STATIC_DRAW);
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	    
	    modelMatrix = new Matrix4f()
				.translate(
						new Vector3f(
								pos.x*(float)Constants.WIDTH+alignement.getOffsetXfromCenter(textWidth*scale)+guialignement.getFrameOffsetX(Constants.WIDTH),
								pos.y*(float)Constants.HEIGHT+alignement.getOffsetYfromCenter(type.getHeight()*scale)+guialignement.getFrameOffsetY(Constants.HEIGHT),
								pos.z
						)
				)
				.rotateZ(rot)
				.scale(scale);
		shader = new TextShader();
		
	    shader.bind();
	    shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
	    shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
	    shader.setUniformMat4f("modelMatrix", modelMatrix);
	    shader.setUniformvec3f("textColor", new Vector3f(color.x, color.y, color.z));
	    shader.setUniformi("tex", 0);
	    shader.unbind();
	    
	    canRender = true;
	}
	
	@Override
	public void render() {
		if(!canRender)return;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		type.getTexture().bind(0);
		shader.bind();
		
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "position");
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VERTICES_VBO);
		GL20.glVertexAttribPointer(vertexAttribLocation, 3, GL11.GL_FLOAT, false, 0, 0);
		
		int texcoordAttribLocation = GL20.glGetAttribLocation(shader.program, "texcoord");
		GL20.glEnableVertexAttribArray(texcoordAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, TEXCOORD_VBO);
		GL20.glVertexAttribPointer(texcoordAttribLocation, 2, GL11.GL_FLOAT, false, 0, 0);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.length()*6);
		
		GL20.glDisableVertexAttribArray(vertexAttribLocation);
		GL20.glDisableVertexAttribArray(texcoordAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		shader.unbind();
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void update() {
		
		modelMatrix.identity()
				.translate(
						new Vector3f(
								pos.x*(float)Constants.WIDTH+alignement.getOffsetXfromCenter(textWidth*scale)+guialignement.getFrameOffsetX(Constants.WIDTH),
								pos.y*(float)Constants.HEIGHT+alignement.getOffsetYfromCenter(type.getHeight()*scale)+guialignement.getFrameOffsetY(Constants.HEIGHT),
								pos.z)
						)
				.rotateZ(rot)
				.scale(scale);
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.unbind();
	}
	
	@Override
	public void destroy() {
		canRender = false;
		GL15.glDeleteBuffers(TEXCOORD_VBO);
		GL15.glDeleteBuffers(VERTICES_VBO);
	}

	/**
	 * @return float component scale
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * set component scale
	 * @param scale float scale to use
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	/**
	 * @return {@link Color} text color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * set the text's color
	 * @param color {@link Color} to use
	 */
	public void setColor(Color color) {
		this.color = color;
		shader.bind();
		shader.setUniformvec3f("textColor", new Vector3f(color.x, color.y, color.z));
		shader.unbind();
	}

	/**
	 * @return {@link String} text
	 */
	public String getText() {
		return text;
	}

	/**
	 * define the text
	 * @param text {@link String} text to draw
	 */
	public void setText(String text) {
		this.text = text;
		if(VERTICES_VBO != 0 || TEXCOORD_VBO != 0) destroy();
		initRender();
	}

	/**
	 * @return {@link Matrix4f} model matrix
	 */
	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	/**
	 * @return {@link FontType} font used to render
	 */
	public FontType getType() {
		return type;
	}

	/**
	 * define the font to render
	 * @param type {@link FontType} to use
	 */
	public void setType(FontType type) {
		this.type = type;
		if(VERTICES_VBO != 0 || TEXCOORD_VBO != 0) destroy();
		initRender();
	}

	/**
	 * Whether the component can be drawn
	 * @param val boolean can be drawn
	 */
	public void allowRender(boolean val) {
		this.canRender = val;
	}
	
	@Override
	public void recreate(int width, int height) {}
}
