package fr.axicer.furryattack.util.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Constants;

public enum FontType {
	DK_KITSUNE_TAIL("dkkt.ttf", Font.PLAIN, 100);
	
	private Font f;
	private Texture tex;
	private int width, height;
	private Map<Character, CharInfo> charMap;
	
	private FontType(String name, int style, int size) {
		try {
			Font realFont = Font.createFont(Font.TRUETYPE_FONT, FontType.class.getResourceAsStream("/font/"+name));
			this.f = realFont.deriveFont(style, size);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(f);
			
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		this.charMap = new HashMap<>();
		try {
			buildTexture();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	    g2D.setFont(f);
	    g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    FontMetrics fontMetrics = g2D.getFontMetrics();

	    String allChars = getAllAvailableChars(Constants.ENCODING);
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
	    g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2D.setFont(f);
	    fontMetrics = g2D.getFontMetrics();
	    g2D.setColor(java.awt.Color.WHITE);
	    for(char c : allChars.toCharArray()) {
	    	g2D.drawString(String.valueOf(c), charMap.get(c).startX, fontMetrics.getAscent());
	    }
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

	public Font getFont() {
		return f;
	}

	public Texture getTexture() {
		return tex;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Map<Character, CharInfo> getCharMap() {
		return charMap;
	}
}
