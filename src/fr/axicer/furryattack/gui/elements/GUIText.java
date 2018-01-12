package fr.axicer.furryattack.gui.elements;

import java.nio.FloatBuffer;
import java.nio.charset.Charset;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.shader.TextShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.font.FontType;

public class GUIText extends GUIComponent{

	public String text;
	public FontType type;
	public Color color;
	
	public Texture tex;
	public TextShader shader;
	public int VERTICES_VBO;
	public int TEXCOORD_VBO;
	public Matrix4f modelMatrix;
	
	public GUIText(String text, FontType type, Color color) {
		super();
		this.text = text;
		this.type = type;
		this.color = color;
		init();
	}
	
	public GUIText(String text, Vector3f pos, float rot, FontType type, Color color) {
		super(pos, rot);
		this.text = text;
		this.type = type;
		this.color = color;
		init();
	}
	
	public void init(){
		modelMatrix = new Matrix4f().translate(pos).rotateZ(rot);
		shader = new TextShader();
		tex = Texture.loadTexture(type.getTexturePath(), GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
		
		byte[] chars = text.getBytes(Charset.forName("UTF-8"));
		float numCols = (float)type.getColumns();
		float numRows = (float)type.getRows();
		
		float tileWidth = (float)tex.width /numCols;
	    float tileHeight = (float)tex.height /numRows;
	    
	    FloatBuffer vertices = BufferUtils.createFloatBuffer(chars.length*6*3);
	    FloatBuffer textures = BufferUtils.createFloatBuffer(chars.length*6*2);
	    
	    float totalWidth = chars.length*tileWidth;
	    
	    for(int i=0; i<chars.length; i++) {
	        byte currChar = chars[i];
	        float col = (float)currChar % numCols;
	        int row = (int) ((float)currChar / numRows);
	        
	        // Build a character tile composed by two triangles

	        // Left Top vertex
	        vertices.put(new float[] {(float)i*tileWidth-totalWidth/2, -tileHeight/2, -0.2f});
	        textures.put(new float[] {col / numCols, (row+1f) / numRows });

	        // Right Top vertex
	        vertices.put(new float[] {(float)(i+1f)*tileWidth-totalWidth/2, -tileHeight/2, -0.2f});
	        textures.put(new float[] {(col+1f) / numCols, (row+1f) / numRows });

	        // Right Bottom vertex
	        vertices.put(new float[] {(float)(i+1f)*tileWidth-totalWidth/2, tileHeight/2, -0.2f});
	        textures.put(new float[] {(col+1f) / numCols, row / numRows });

	        
	        // Right Bottom vertex
	        vertices.put(new float[] {(float)(i+1f)*tileWidth-totalWidth/2, tileHeight/2, -0.2f});
	        textures.put(new float[] {(col+1f)/numCols,	row/numRows });
	        
	        // Left Bottom vertex
	        vertices.put(new float[] {(float)i*tileWidth-totalWidth/2, tileHeight/2, -0.2f});
	        textures.put(new float[] {col/numCols,	row/numRows });
	        
	        // Left Top vertex
	        vertices.put(new float[] {(float)i*tileWidth-totalWidth/2, -tileHeight/2, -0.2f});
	        textures.put(new float[] {col/numCols,	(row+1f)/numRows });
	        
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
		tex.bind(0);
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
		rot += (float)Math.PI/32f;
		modelMatrix.identity().translate(pos).rotateZ(rot);
		shader.bind();
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.unbind();
	}
	
	public void destroy() {
		GL15.glDeleteBuffers(TEXCOORD_VBO);
		GL15.glDeleteBuffers(VERTICES_VBO);
	}

}
