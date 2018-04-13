package fr.axicer.furryattack.util.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.axicer.furryattack.generator.config.FontConfigGenerator;
import fr.axicer.furryattack.generator.images.FontImageGenerator;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.config.FileManager;

public class FontType {
	public static FontType CAPTAIN;
	
	private static List<FontType> fonts;
	
	private String name;
	private Font f;
	private Texture tex;
	private int width, height;
	private Map<Integer, CharInfo> charMap;
	private Configuration config;
	
	public static void initalize(boolean recreate) {
		fonts = new ArrayList<>();
		CAPTAIN = new FontType("captain", "captain.ttf", Font.PLAIN, 100, recreate);
		fonts.add(CAPTAIN);
	}
	
	private FontType(String name, String fileName, int style, int size, boolean recreate) {
		try {
			Font realFont = Font.createFont(Font.TRUETYPE_FONT, FontType.class.getResourceAsStream("/font/"+fileName));
			this.f = realFont.deriveFont(style, size);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(f);
			
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		this.charMap = new HashMap<>();
		try {
			buildTexture(recreate);
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
	
	private void buildTexture(boolean recreate) throws Exception {
		//TODO redo this
		File charTextureFile = new File(FileManager.IMAGE_FOLDER_FILE, "charMap.png");
		File charTextureConfigFile = new File(FileManager.CONFIG_FOLDER_FILE, "charTexture.json");
		//if config is not existing or it's asked to recreate
		String allChars = getAllAvailableChars(Constants.ENCODING);
		if(!charTextureConfigFile.exists() || recreate) {
			//generating the config
			
			// Get the font metrics for each character for the selected font by using image
			BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2D = img.createGraphics();
			g2D.setFont(f);
			g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			FontMetrics fontMetrics = g2D.getFontMetrics();
			
			//generating characters configuration
			config = new FontConfigGenerator(allChars, fontMetrics).generate();
			g2D.dispose();
			config.save(charTextureConfigFile);
		}else {
			//loading from config
	    	config = new Configuration(charTextureConfigFile); 
		}
		//loading informations
    	width = config.getInt("global.width",0);
    	height = config.getInt("global.height",0);
    	JSONObject object = config.getJSONObject("chars",new JSONObject());
    	for(Object key : object.keySet()) {
    		if(key instanceof String) {
    			String skey = (String)key;
    			int startX = config.getInt("chars."+skey+".startX",0);
    			int width = config.getInt("chars."+skey+".width",0);
    			CharInfo info = new CharInfo(startX, width);
    			charMap.put(Integer.parseInt(skey), info);
    		}else {
    			System.out.println(key.getClass());
    		}
    	}
		//if charTexture file doesn't exists or it's asked to recreate
		if(!charTextureFile.exists() || recreate) {
		    // Create the image associated to the charset
		    BufferedImage img = new FontImageGenerator(allChars, width, height, Color.WHITE, charMap, f).generate();
		    // Dump image to a byte buffer
		    ImageIO.write(img, "PNG", charTextureFile);
	    }
	    InputStream is = new FileInputStream(charTextureFile);
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

	public Map<Integer, CharInfo> getCharMap() {
		return charMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Configuration getConfig() {
		return this.config;
	}
	
	public static FontType getFontType(String name) {
		for(FontType typ : fonts) {
			if(typ.name.equalsIgnoreCase(name))return typ;
		}
		return null;
	}
}
