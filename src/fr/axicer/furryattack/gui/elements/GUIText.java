package fr.axicer.furryattack.gui.elements;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

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

public class GUIText extends GUIComponent{

	public String text;
	public Font font;
	public Color color;
	public Map<Character, CharInfo> charMap;
	private int width, height;
	
	public Texture tex;
	public TextShader shader;
	public int VERTICES_VBO;
	public int TEXCOORD_VBO;
	public Matrix4f modelMatrix;
	
	public GUIText(String text, Font font, Color color) {
		super();
		this.text = text;
		this.font = font;
		this.color = color;
		init();
	}
	
	public GUIText(String text, Vector3f pos, float rot, Font font, Color color) {
		super(pos, rot);
		this.text = text;
		this.font = font;
		this.color = color;
		init();
	}
	
	private String getAllAvailableChars(String charsetName) {
	    CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
	    StringBuilder result = new StringBuilder();
	    for (char c = 0; c < Character.MAX_VALUE; c++) {
	        if (ce.canEncode(c)) {
	            result.append(c);
	        }
	    }
	    return result.toString();
	}
	
	private void buildTexture() throws Exception {
		// Get the font metrics for each character for the selected font by using image
	    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2D = img.createGraphics();
	    g2D.setFont(font);
	    FontMetrics fontMetrics = g2D.getFontMetrics();

	    String allChars = getAllAvailableChars("ISO-8859-1");
	    this.width = 0;
	    this.height = 0;
	    for (char c : allChars.toCharArray()) {
	        // Get the size for each character and update global image size
	        CharInfo charInfo = new CharInfo(width, fontMetrics.charWidth(c));
	        charMap.put(c, charInfo);
	        width += charInfo.width;
	        height = Math.max(height, fontMetrics.getHeight());
	    }
	    g2D.dispose();
	    // Create the image associated to the charset
	    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    g2D = img.createGraphics();
	    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2D.setFont(font);
	    fontMetrics = g2D.getFontMetrics();
	    g2D.setColor(java.awt.Color.WHITE);
	    g2D.drawString(allChars, 0, fontMetrics.getAscent());
	    g2D.dispose();
	    // Dump image to a byte buffer
	    InputStream is;
	    try (
	        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	        ImageIO.write(img, "PNG", out);
	        out.flush();
	        is = new ByteArrayInputStream(out.toByteArray());
	    }
	    
	    tex = Texture.loadTexture(is, GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
	}
	
	public void init(){
		charMap = new HashMap<>();
		modelMatrix = new Matrix4f().translate(pos).rotateZ(rot);
		shader = new TextShader();
		try {
			buildTexture();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		byte[] chars = text.getBytes(Charset.forName("ISO-8859-1"));
	    
	    FloatBuffer vertices = BufferUtils.createFloatBuffer(chars.length*6*3);
	    FloatBuffer textures = BufferUtils.createFloatBuffer(chars.length*6*2);
	    
	    float startx = 0;
	    
	    float textWidth = 0;
	    for(int i=0; i<chars.length; i++) {
	    	byte currChar = chars[i];
	        CharInfo charInfo = charMap.get((char)currChar);
	        textWidth+= charInfo.width;
	    }
	    
	    for(int i=0; i<chars.length; i++) {
	        byte currChar = chars[i];
	        CharInfo charInfo = charMap.get((char)currChar);
	        // Build a character tile composed by two triangles

	        // Left Top vertex
	        vertices.put(new float[] {startx-textWidth/2, (float)-height/2, -0.2f});
	        textures.put(new float[] {(float)charInfo.startX/(float)width, 1.0f });

	        // Right Top vertex
	        vertices.put(new float[] {startx+charInfo.width-textWidth/2, (float)-height/2, -0.2f});
	        textures.put(new float[] {((float)charInfo.startX+(float)charInfo.width)/(float)width, 1.0f});

	        // Right Bottom vertex
	        vertices.put(new float[] {startx+charInfo.width-textWidth/2, (float)height/2, -0.2f});
	        textures.put(new float[] {((float)charInfo.startX+(float)charInfo.width)/(float)width, 0.0f});

	        
	        // Right Bottom vertex
	        vertices.put(new float[] {startx+charInfo.width-textWidth/2, (float)height/2, -0.2f});
	        textures.put(new float[] {((float)charInfo.startX+(float)charInfo.width)/(float)width, 0.0f});
	        
	        // Left Bottom vertex
	        vertices.put(new float[] {startx-textWidth/2, (float)height/2, -0.2f});
	        textures.put(new float[] {(float)charInfo.startX/(float)width, 0.0f });
	        
	        // Left Top vertex
	        vertices.put(new float[] {startx-textWidth/2, (float)-height/2, -0.2f});
	        textures.put(new float[] {(float)charInfo.startX/(float)width, 1.0f });
	        
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
		rot += (float)Math.PI/64f;
		modelMatrix.identity().translate(pos).rotateZ(rot);
		shader.bind();
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.unbind();
	}
	
	public void destroy() {
		GL15.glDeleteBuffers(TEXCOORD_VBO);
		GL15.glDeleteBuffers(VERTICES_VBO);
	}

	
	 public static class CharInfo {

        private final int startX;
        private final int width;

        public CharInfo(int startX, int width) {
            this.startX = startX;
            this.width = width;
        }
    }
}
