package fr.axicer.furryattack.gui.elements;

import java.nio.FloatBuffer;
import java.nio.charset.Charset;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.shader.TextShader;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.font.CharInfo;
import fr.axicer.furryattack.util.font.FontType;

public class GUIText extends GUIComponent{

	public String text;
	public Color color;
	public FontType type;
	
	public float scale;
	public TextShader shader;
	public int VERTICES_VBO;
	public int TEXCOORD_VBO;
	public Matrix4f modelMatrix;
	
	public GUIText(String text, FontType type, Color color) {
		super();
		this.text = text;
		this.type = type;
		this.color = color;
		this.scale = 1f;
		init();
		initRender();
	}
	
	public GUIText(String text, Vector3f pos, float rot, FontType type, Color color, float scale) {
		super(pos, rot);
		this.text = text;
		this.type = type;
		this.color = color;
		this.scale = scale;
		init();
		initRender();
	}
	
	private void init() {
		modelMatrix = new Matrix4f().translate(pos).rotateZ(rot);
		shader = new TextShader();
	}
	
	private void initRender(){
		byte[] chars = text.getBytes(Charset.forName(Constants.ENCODING));
	    
	    FloatBuffer vertices = BufferUtils.createFloatBuffer(chars.length*6*3);
	    FloatBuffer textures = BufferUtils.createFloatBuffer(chars.length*6*2);
	    
	    float startx = 0;
	    
	    float textWidth = 0;
	    for(int i=0; i<chars.length; i++) {
	    	byte currChar = chars[i];
	        CharInfo charInfo = type.getCharMap().get((char)currChar);
	        textWidth+= charInfo.width;
	    }
	    
	    for(int i=0; i<chars.length; i++) {
	        byte currChar = chars[i];
	        CharInfo charInfo = type.getCharMap().get((char)currChar);
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
	    
	    shader.bind();
	    shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
	    shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
	    shader.setUniformMat4f("modelMatrix", modelMatrix);
	    shader.setUniformvec3f("textColor", new Vector3f(color.x, color.y, color.z));
	    shader.setUniformi("tex", 0);
	    shader.unbind();
	}
	
	@Override
	public void render() {
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
		modelMatrix.identity().translate(pos).rotateZ(rot).scale(scale);
		shader.bind();
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.unbind();
	}
	
	public void destroy() {
		GL15.glDeleteBuffers(TEXCOORD_VBO);
		GL15.glDeleteBuffers(VERTICES_VBO);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		shader.bind();
		shader.setUniformvec3f("textColor", new Vector3f(color.x, color.y, color.z));
		shader.unbind();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		if(VERTICES_VBO != 0 || TEXCOORD_VBO != 0) destroy();
		initRender();
		
	}

	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	public FontType getType() {
		return type;
	}

	public void setType(FontType type) {
		this.type = type;
		if(VERTICES_VBO != 0 || TEXCOORD_VBO != 0) destroy();
		initRender();
	}

}
